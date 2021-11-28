package Actors

import java.io.File
import FileMonitoring.{FileAdapter, FileEvent, FileWatcher}
import akka.{Done, NotUsed}
import akka.actor.{Actor, ActorLogging, ActorRef, ActorSystem, Props}
import com.amazonaws.regions.Regions
import com.amazonaws.services.config.model.Source
import com.amazonaws.services.s3.model.ObjectMetadata
import com.amazonaws.services.s3.{AmazonS3, AmazonS3ClientBuilder}
import com.typesafe.config.ConfigFactory




object logFileWatcher{
  def props(pullLogs: ActorRef, path:String): Props = Props(new logFileWatcher(pullLogs,path))
}


class logFileWatcher(pullLogs: ActorRef, path:String) extends Actor with ActorLogging{

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

    //val s3: AmazonS3 = AmazonS3ClientBuilder.standard.withRegion(Regions.US_EAST_1).build
    //val metadata: ObjectMetadata = s3.getObjectMetadata(bucket_name, key_name)





  }


}


object Main extends App{
  val config = ConfigFactory.load("application")
  val path:String = config.getString("s3.folder_path")

  val system=ActorSystem("logFileWatcherSystem")
  val pullLogs=system.actorOf(Props[logFileExtraction], name="pullLogs")
  val filewatcher=system.actorOf(logFileWatcher.props(pullLogs,path), name="filewatcher")
  filewatcher ! "monior"



}
