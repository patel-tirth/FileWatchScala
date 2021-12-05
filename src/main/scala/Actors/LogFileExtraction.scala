package Actors

import akka.actor.{Actor, ActorLogging, ActorRef, ActorSystem, Props}
import com.amazonaws.regions.Regions
import com.amazonaws.services.s3.model.{ObjectMetadata, S3Object}
import com.typesafe.config.{Config, ConfigFactory}
import akka.actor.{Actor, ActorLogging, ActorRef, ActorSystem, Props}
import com.amazonaws.regions.Regions
import com.amazonaws.services.s3.{AmazonS3, AmazonS3ClientBuilder}
import KafkaService.Producer
import org.slf4j.{Logger, LoggerFactory}

import scala.io.Source
import java.io.{BufferedReader, InputStreamReader}
import scala.util.matching.Regex

object LogFileExtraction {
  def props(): Props = Props(new LogFileExtraction())
}

class LogFileExtraction extends Actor with ActorLogging{
  val config: Config = ConfigFactory.load("application")
  val logger: Logger = LoggerFactory.getLogger(this.getClass)
  val bucket_name: String = config.getString("s3.bucket")
//  val file_path: String = config.getString("s3.file_path")
//  val key_name: String = config.getString("s3.key")

//  val filepath="/resources/test.log"
//  val s3File="log.log"

  val s3: AmazonS3 = AmazonS3ClientBuilder.standard.withRegion(Regions.US_EAST_1).build

  val pattern: Regex = "(WARN|ERROR)".r
  val checkLogMessage : PartialFunction[String,String] = {
    case s: String if pattern.findFirstIn(s) != None => s
  }
//  def sendToKafka : PartialFunction[String,Unit] = {
//    case s  if s.nonEmpty  => implementKafka(s)
//  }s
  override def receive: Receive = {
    //val s3: AmazonS3 = AmazonS3ClientBuilder.standard.withRegion(Regions.US_EAST_1).build
    case file: String =>

      val obj = s3.getObject(bucket_name,file)

      logger.info("sending s3 object to kafka")

      val reader = new BufferedReader(new InputStreamReader(obj.getObjectContent))

       Stream.continually(reader.readLine()).takeWhile(_ != null).collect( checkLogMessage ).toList.foreach(s => {
         new Producer().runKafka(s)
       })

  }

}
