package Actors

import java.io.{BufferedReader, File, FileOutputStream, InputStreamReader}
import FileMonitoring.{FileAdapter, FileEvent, FileWatcher}
import akka.{Done, NotUsed}
import akka.actor.{Actor, ActorLogging, ActorRef, ActorSystem, Props}
import com.amazonaws.regions.Regions
import com.amazonaws.services.config.model.Source
import com.amazonaws.services.s3.model.ObjectMetadata
import com.amazonaws.services.s3.{AmazonS3, AmazonS3ClientBuilder}
import com.typesafe.config.{Config, ConfigFactory}
import org.apache.commons.io.IOUtils
import com.amazonaws.auth.BasicAWSCredentials
import com.amazonaws.services.s3.AmazonS3Client
import com.amazonaws.AmazonClientException
import com.amazonaws.AmazonServiceException

import java.io.File
import java.io.BufferedReader
import java.io.InputStreamReader
import java.io.FileOutputStream
import org.apache.commons.io.IOUtils

import java.io.{File, FileOutputStream}
import FileMonitoring.{FileAdapter, FileEvent, FileWatcher}
import akka.{Done, NotUsed}
import akka.actor.{Actor, ActorLogging, ActorRef, ActorSystem, Props}
import akka.util.ByteString
import com.amazonaws.regions.Regions
import com.amazonaws.services.config.model.Source
import com.amazonaws.services.s3.model.ObjectMetadata
import com.amazonaws.services.s3.{AmazonS3, AmazonS3ClientBuilder}

import java.util.StringTokenizer
import scala.io.Source


object LogFileExtraction {
  def props(): Props = Props(new LogFileExtraction())
}

class LogFileExtraction extends Actor with ActorLogging{
  val config: Config = ConfigFactory.load("application")
  val bucket_name: String = config.getString("s3.bucket")
  val file_path: String = config.getString("s3.file_path")
  val key_name: String = config.getString("s3.key")

//  val bucketfolder_path:String=config.getString("s3.path")
  val filepath="/resources/test.log"
  val s3File="log.log"

  val s3: AmazonS3 = AmazonS3ClientBuilder.standard.withRegion(Regions.US_EAST_1).build

  override def receive: Receive = {
    //val s3: AmazonS3 = AmazonS3ClientBuilder.standard.withRegion(Regions.US_EAST_1).build
    case file: File =>
      //val s3: AmazonS3 = AmazonS3ClientBuilder.standard.withRegion(Regions.US_EAST_1).build
      //val metadata: ObjectMetadata = s3.getObjectMetadata(bucket_name, key_name)
      //val obj = s3.getObject(bucket_name,s3File )

//      val obj = s3.getObject(bucket_name, file.getName)
//      val reader = new BufferedReader(new InputStreamReader(obj.getObjectContent()))
//      var line = reader.readLine
//      while (line!=null) {
//        //println(line)
//        if(line.contains("WARN")){
//          println(line)
//          implementKafka(line)
//        }
//        if(line.contains("ERROR")){
//          println(line)
//        }
//        line = reader.readLine
//      }


  }

  def implementKafka(line: String):Unit = {

  }


}
