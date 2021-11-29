package Actors

import java.io.File
import FileMonitoring.{FileAdapter, FileEvent, FileWatcher}
import akka.{Done, NotUsed}
import akka.actor.{Actor, ActorLogging, ActorRef, ActorSystem, Props}
import akka.util.ByteString
import com.amazonaws.regions.Regions
import com.amazonaws.services.config.model.Source
import com.amazonaws.services.s3.model.ObjectMetadata
import com.amazonaws.services.s3.{AmazonS3, AmazonS3ClientBuilder}
import com.typesafe.config.ConfigFactory
import akka.http.scaladsl.model.headers.ByteRange
import akka.http.scaladsl.model.{ContentType, ContentTypes, HttpEntity, HttpResponse, IllegalUriException}
import akka.stream.Attributes
import akka.stream.alpakka.s3.BucketAccess.{AccessDenied, AccessGranted, NotExists}
import akka.stream.alpakka.s3._
import akka.stream.alpakka.s3.headers.ServerSideEncryption
//import akka.stream.alpakka.s3.scaladsl.{S3, S3ClientIntegrationSpec, S3WireMockBase}
import akka.stream.scaladsl.{Sink, Source}

import software.amazon.awssdk.regions.Region
import software.amazon.awssdk.regions.providers._

import org.apache.commons.io.IOUtils

// companion object
object LogFileWatcher{
  def props(pullLogs: ActorRef, path:String): Props = Props(new LogFileWatcher(pullLogs,path))
}


class LogFileWatcher(pullLogs: ActorRef, path:String) extends Actor with ActorLogging{

  log.debug("file watcher started running!")

  val config = ConfigFactory.load()
  val bucket_name: String = config.getString("s3.bucket")
  val file_path: String = config.getString("s3.file_path")
  val key_name: String = config.getString("s3.key")

  override def receive: Receive = {
    case _ => log.info("Provide valid Input!")
    case "monitor logs" =>
      monitor(pullLogs)

  }

  def monitor(pullLogs: ActorRef): Unit = {

//    val s3: AmazonS3 = AmazonS3ClientBuilder.standard.withRegion(Regions.US_EAST_1).build
//    //val metadata: ObjectMetadata = s3.getObjectMetadata(bucket_name, key_name)
//    val obj = s3.getObject(bucket_name,s3File )
//    val bytes = IOUtils.toByteArray(obj.getObjectContent())
//    val file = new FileOutputStream(filepath)
//    file.write(bytes)

    val folderpath=new File(path)
    val filewatcher = new FileWatcher(folderpath)

    filewatcher.addListener(new FileAdapter() {
      override def onModified(event: FileEvent): Unit = {
        pullLogs ! event.getFile
      }
    }).watch()
  }


}


object Main extends App{

  val config = ConfigFactory.load("application")
  val path:String = config.getString("s3.folder_path")

  val system=ActorSystem("logFileWatcherSystem")

  // top level actor
  protected def createActorWatcher(): ActorRef =
    system.actorOf(LogFileWatcher.props(pullLogs, path) , "filewatcher")

  protected def createActorExtractor(): ActorRef =
    system.actorOf(LogFileExtraction.props(),name="pulllogs")

  private val filewatcher = createActorWatcher()

  private val pullLogs= createActorExtractor()

  filewatcher ! "monitor"


}
