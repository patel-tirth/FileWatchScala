package Actors

import java.io.{File, FileWriter}
import java.util.{Calendar, Date}
import FileMonitoring.{FileAdapter, FileEvent, FileWatcher}
import akka.{Done, NotUsed}
import akka.actor.{Actor, ActorLogging, ActorRef, ActorSystem, Props}
import akka.util.ByteString
import com.amazonaws.regions.Regions
import org.joda.time.DateTime

import scala.annotation.tailrec
//import com.amazonaws.services.config.model.Source
import com.amazonaws.services.s3.model.ObjectMetadata
import com.amazonaws.services.s3.{AmazonS3, AmazonS3ClientBuilder}
import com.typesafe.config.ConfigFactory

import org.slf4j.{Logger, LoggerFactory}
import com.amazonaws.services.s3.model.S3ObjectSummary
//import scala.collection.JavaConversions._
//import akka.stream.alpakka.s3.scaladsl.{S3, S3ClientIntegrationSpec, S3WireMockBase}
//import akka.stream.scaladsl.{Sink}
import com.amazonaws.services.s3.model.ObjectListing
//import software.amazon.awssdk.regions.Region
//import software.amazon.awssdk.regions.providers._
import com.amazonaws.services.s3.model.S3ObjectSummary
import org.apache.commons.io.IOUtils
import scala.io.Source
// companion object
object LogFileWatcher{
  def props(pullLogs: ActorRef): Props = Props(new LogFileWatcher(pullLogs))
}


class LogFileWatcher(pullLogs: ActorRef) extends Actor with ActorLogging{
  val logger: Logger = LoggerFactory.getLogger(this.getClass)
  log.debug("file watcher started running!")

  val config = ConfigFactory.load("application")
  val bucket_name: String = config.getString("s3.bucket")
//  val file_path: String = config.getString("s3.file_path")
//  val key_name: String = config.getString("s3.key")

  override def receive: Receive = {
//    case _ => log.info("Provide valid Input!")
    case "monitor" =>
      monitor(pullLogs)
  }

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
    watch(logfileextractor, nTime)
  }

  def monitor(pullLogs: ActorRef): Unit = {
    watch(pullLogs,Calendar.getInstance().getTime)

//    val s3: AmazonS3 = AmazonS3ClientBuilder.standard.withRegion(Regions.US_EAST_1).build
//
////    val obj = s3.getObject(bucket_name, file)
//    val listing = s3.listObjects(bucket_name)
////    val data = Source.fromInputStream(obj.getObjectContent)
//    val format = new java.text.SimpleDateFormat("HH:mm:ss.SSS")
//    var lastUpdatedTime : Date= Calendar.getInstance().getTime
//    print(lastUpdatedTime)

//    logger.info(data)
//    val folder = new File("src/test")
//    val testfile = new File(folder + "/test.txt")
//    val writer = new FileWriter(testfile)
//    writer.write(data)
//    print(myData)
//    val appended_data = myData + "06:55:06.035 [scala-execution-context-global-64] DEBUG  HelperUtils.Parameters$ - x2oBSI0/\\%CdfV2%ChSsnZ7vJo=2qJqZ%.\"kb"
//    s3.putObject(bucket_name,"log.log",appended_data)
//    while(true){
////      logger.info("inside while")
//      val summaries = listing.getObjectSummaries
////      print(summaries)
//      summaries.forEach(os => {
////          print(os.getLastModified)
//        if (os.getLastModified.after(lastUpdatedTime)) {
//          logger.info("inside if modified")
//          logger.info(os.getKey)
////          print("modified")
//          lastUpdatedTime = Calendar.getInstance().getTime
//        }
//      })
//
//    }
//    summaries.forEach(os => {
//      if (os.getLastModified.after(lastUpdatedTime)) System.out.println("###### " + os.getKey + " ########" + os.getLastModified)
//    })
//    for(os <- summaries) {
//      if (os.getLastModified.after(lastUpdatedTime)) System.out.println("###### " + os.getKey + " ########" + os.getLastModified)
//    }
    //    val bytes = IOUtils.toByteArray(obj.getObjectContent())
    //    val file = new FileOutputStream(filepath)
    //    file.write(bytes)
    //    val myData = Source.fromInputStream(obj.getObjectContent())
//        val folderpath=new File("https://loggeneratorbucket-t.s3.amazonaws.com")
//        val filewatcher = new FileWatcher(folderpath)
//        logger.info("inside monitor")
//        filewatcher.addListener(new FileAdapter() {
//          override def onModified(event: FileEvent): Unit = {
//            logger.info("inside onmodified")
//    //        pullLogs ! event.getFile
//            print("modified")
//          }
//        }).watch()

  }
}

object Entry{
//

//  private val pullLogs = createActorExtractor()
//  private val filewatcher = createActorWatcher()
//  protected def createActorWatcher(): ActorRef =
//    system.actorOf(LogFileWatcher.props(pullLogs, getFile), "filewatcher")

//  protected def createActorExtractor(): ActorRef =
//    system.actorOf(LogFileExtraction.props(), name = "pulllogs")

  def main(args: Array[String]): Unit = {
    val system = ActorSystem("logFileWatcherSystem")
    val config = ConfigFactory.load("application")
////    val path: String = config.getString("s3.folder_path")
////    val getFile = args(0)
////
////    print(getFile)
    val pullLogs = system.actorOf(LogFileExtraction.props(), name = "pulllogs")
    val filewatcher =system.actorOf(LogFileWatcher.props(pullLogs), "filewatcher")
    filewatcher ! "monitor"

  }
}
