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

object Main extends App{

}


object logFileWatcher{
  def props(pullLogs: ActorRef): Props = Props(new logFileWatcher(pullLogs))
}


class logFileWatcher(pullLogs: ActorRef) extends Actor with ActorLogging{

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
