//package KafkaService
//////import com.amazonaws.services.s3.model.S3Object
////import com.typesafe.config.{Config, ConfigFactory}
////import org.apache.kafka.clients.producer.{KafkaProducer, ProducerRecord}
////import org.apache.kafka.clients.producer.ProducerConfig
////import org.apache.kafka.common.serialization.{StringDeserializer, StringSerializer}
//////import org.apache.spark.streaming.kafka010._
//////import org.apache.spark.streaming.kafka010.LocationStrategies.PreferConsistent
//////import org.apache.spark.streaming.kafka010.ConsumerStrategies.Subscribe
////import org.slf4j.{Logger, LoggerFactory}
////import org.apache.kafka.clients.consumer.ConsumerRecord
//////import org.apache.spark
//////import org.apache.spark._
//////import org.apache.spark.SparkContext._
//////import org.apache.spark.streaming.{Seconds, StreamingContext}
////
////import java.util.Properties
////class Producer {
////  val config: Config = ConfigFactory.load("application")
////  val logger: Logger = LoggerFactory.getLogger(this.getClass)
////  val properties: Properties = new Properties()
////  properties.setProperty(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "127.0.0.1:9092")
////  properties.setProperty(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, classOf[StringSerializer].getName)
////  properties.setProperty(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, classOf[StringSerializer].getName)
//////  properties.setProperty(ProducerConfig.ACKS_CONFIG, "all")
//////    properties.put("bootstrap.servers",config.getString("kafka.bootstrapserver"))
//////     properties.put("key.serializer","org.apache.kafka.common.serialization.StringSerializer")
//////     properties.put("value.serializer","org.apache.kafka.common.serialization.StringSerializer")
//////     properties.put("acks","all")
//////  properties.put("security.protocol",config.getString("kafka.protocol"))
//////  properties.put("ssl.truststore.location",config.getString("kafka.trustStoreLocation"))
////  val producer = new KafkaProducer[String,String](properties)
////
////
////
//////  val kafka: Any = spark
//////    .readStream
//////    .format("kafka")
//////    .option("kafka.bootstrap.servers", config.getString("kafka.bootstrapserver"))
//////    .option("subscribe", "azurefeedback")
//////    .option("startingOffsets", "latest")
//////    .load()
////  def runKafka(message: String): Unit =  {
//////    try {
//////      try {
////        println("Message sent:" + message)
////        logger.info(message)
////        val record = new ProducerRecord[String,String](config.getString("LogDataTopic"), message)
////        producer.send(record)
////        producer.flush()
////         producer.close()
//////      }
//////      catch {
//////        case e: Exception => e.printStackTrace()
//////      }
//////      val producer = new KafkaProducer[String, String](properties)
//////      val record = new ProducerRecord[String, String](config.getString("kafka.topic"), message)
//////      producer.send(record)
//////      logger.info("Sent Message: " + record)
////////      producer.flush()
////////      producer.close()
//////    } catch {
//////      case exception: Exception => exception.printStackTrace()
//////    }
////  }
////
//////  val kafkaParams = Map[String, Object](
//////    "bootstrap.servers" -> config.getString("kafka.bootstrapserver"),
//////    "key.deserializer" -> classOf[StringDeserializer],
//////    "value.deserializer" -> classOf[StringDeserializer],
////////    "group.id" -> "use_a_separate_group_id_for_each_stream",
//////    "auto.offset.reset" -> "latest",
//////    "enable.auto.commit" -> (false: java.lang.Boolean)
//////  )
//////  val topics = Array(config.getString("kafka.topic"))
//////  val stream = KafkaUtils.createDirectStream[String,String](
//////    new StreamingContext(SparkContext.getOrCreate(), Seconds(5)),
//////    PreferConsistent,
//////    ConsumerStrategies.Subscribe[String,String](topics,kafkaParams)
//////  )
//////  stream.map(record => (record.key(), record.value()))
//////  stream.map(record => (record.key, record.value))
////
////
////
////}
//import akka.Done
//import akka.actor.ActorSystem
//import akka.kafka.ProducerSettings
//import akka.kafka.scaladsl.Producer
//import akka.stream.scaladsl.Source
//import akka.stream.{ActorMaterializer, Materializer}
//import com.typesafe.config.ConfigFactory
//import org.apache.kafka.clients.producer.ProducerRecord
//import org.apache.kafka.common.serialization.StringSerializer
//
//import scala.concurrent.{ExecutionContextExecutor, Future}
//import scala.util.{Failure, Success}
//
//class ProducerApp{
//  implicit val system: ActorSystem = ActorSystem("producer-sys")
//  implicit val mat: Materializer = ActorMaterializer()
//  implicit val ec: ExecutionContextExecutor = system.dispatcher
//
//  val config = ConfigFactory.load()
//  val producerConfig = config.getConfig("akka.kafka.producer")
//  val producerSettings = ProducerSettings(producerConfig, new StringSerializer, new StringSerializer)
//
//
//
////  produce onComplete  {
////    case Success(_) => println("Done"); system.terminate()
////    case Failure(err) => println(err.toString); system.terminate()
////  }
//
//  def runKafka(message:String): Unit ={
//    val produce: Future[Done] =
//      Source(message)
//        .map(_ => new ProducerRecord[String, String]("LogDataTopic", message))
//        .runWith(Producer.plainSink(producerSettings))
//  }
//}
