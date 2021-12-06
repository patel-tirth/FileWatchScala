package Actors

import java.util.{Calendar, Date}
import FileMonitoring.{FileAdapter, FileEvent, FileWatcher}
import akka.actor.{Actor, ActorLogging, ActorRef, ActorSystem, Props}
import com.amazonaws.regions.Regions
import scala.annotation.tailrec
import com.amazonaws.services.s3.{AmazonS3, AmazonS3ClientBuilder}
import com.typesafe.config.ConfigFactory
import org.slf4j.{Logger, LoggerFactory}

// companion object
object LogFileWatcher{
  def props(pullLogs: ActorRef): Props = Props(new LogFileWatcher(pullLogs))
}

class LogFileWatcher(pullLogs: ActorRef) extends Actor with ActorLogging{
  val logger: Logger = LoggerFactory.getLogger(this.getClass)
  log.debug("file watcher started running!")

  val config = ConfigFactory.load("application")
  val bucket_name: String = config.getString("s3.bucket")


  override def receive: Receive = {
    case "monitor" =>
      monitor(pullLogs)
  }
  // tail recursive function to continuously watch s3 bucket for new log files
  @tailrec
  final def watch( logfileextractor: ActorRef, newTime:Date): Unit ={
    val s3: AmazonS3 = AmazonS3ClientBuilder.standard.withRegion(Regions.US_EAST_1).build
    val listing = s3.listObjects(bucket_name)
    val summaries = listing.getObjectSummaries
    var nTime = Calendar.getInstance().getTime
    summaries.forEach(os => {
      if (os.getLastModified.after(newTime)) {
        logger.info("inside if modified")
        logger.info(os.getKey)
        nTime = os.getLastModified
        logfileextractor ! os.getKey
      }

    })
    watch(logfileextractor,nTime)
  }

  def monitor(pullLogs: ActorRef): Unit = {
    logger.info("Calling Watch Function..")
    watch(pullLogs,Calendar.getInstance().getTime)
  }
}

object Entry{

  def main(args: Array[String]): Unit = {
    val system = ActorSystem("logFileWatcherSystem")

    val pullLogs = system.actorOf(LogFileExtraction.props(), name = "pulllogs")
    val filewatcher =system.actorOf(LogFileWatcher.props(pullLogs), "filewatcher")

    filewatcher ! "monitor"

  }
}
