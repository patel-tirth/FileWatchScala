package Actors


import com.typesafe.config.{Config, ConfigFactory}
import akka.actor.{Actor, ActorLogging, ActorRef, ActorSystem, Props}
import com.amazonaws.regions.Regions
import com.amazonaws.services.s3.{AmazonS3, AmazonS3ClientBuilder}
import akka.Done
import akka.kafka.ProducerSettings
import akka.kafka.scaladsl.Producer
import akka.stream.{ActorMaterializer, Materializer}
import akka.stream.scaladsl.Source
import org.apache.kafka.clients.producer.{KafkaProducer, ProducerConfig, ProducerRecord}
import org.apache.kafka.common.serialization.StringSerializer
import org.slf4j.{Logger, LoggerFactory}

import java.io.{BufferedReader, InputStreamReader}
import java.util.Properties
import scala.concurrent.{ExecutionContextExecutor, Future}
import scala.util.matching.Regex

object LogFileExtraction {
  def props(): Props = Props(new LogFileExtraction())
}

class LogFileExtraction extends Actor with ActorLogging{
  val config: Config = ConfigFactory.load("application")
  val logger: Logger = LoggerFactory.getLogger(this.getClass)
  val bucket_name: String = config.getString("s3.bucket")

  val properties: Properties = new Properties()
  properties.setProperty(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, config.getString("kafka.bootstrapserver"))
  properties.setProperty(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, classOf[StringSerializer].getName)
  properties.setProperty(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, classOf[StringSerializer].getName)


  val s3: AmazonS3 = AmazonS3ClientBuilder.standard.withRegion(Regions.US_EAST_1).build

  val pattern: Regex = "(WARN|ERROR)".r
  val checkLogMessage : PartialFunction[String,String] = {
    case s: String if pattern.findFirstIn(s) != None => s
  }

  override def receive: Receive = {
    case file: String =>
      val obj = s3.getObject(bucket_name,file)
      logger.info("received s3 object key..")

      val reader = new BufferedReader(new InputStreamReader(obj.getObjectContent))
      val data =  Stream.continually(reader.readLine()).takeWhile(_ != null).collect( checkLogMessage ).toList
      logger.info("sending data to kafka..")
      runKafka(data)

  }
  implicit val system: ActorSystem = ActorSystem("producer-sys")
  implicit val mat: Materializer = ActorMaterializer()
  implicit val ec: ExecutionContextExecutor = system.dispatcher

//  val config = ConfigFactory.load()
  val producerConfig = config.getConfig("akka.kafka.producer")
  val producerSettings = ProducerSettings(producerConfig, new StringSerializer, new StringSerializer)

  //  produce onComplete  {
  //    case Success(_) => println("Done"); system.terminate()
  //    case Failure(err) => println(err.toString); system.terminate()
  //  }

  def runKafka(data:List[String]): Unit ={
    val produce: Future[Done] = {
      Source(data)
        .map(value => new ProducerRecord[String, String]("LogDataTopic", value.toString))
        .runWith(Producer.plainSink(producerSettings))
    }
  }

}
