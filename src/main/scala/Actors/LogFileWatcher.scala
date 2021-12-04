package Actors

import java.io.File
import java.util.{Calendar, Date}
import FileMonitoring.{FileAdapter, FileEvent, FileWatcher}
import akka.{Done, NotUsed}
import akka.actor.{Actor, ActorLogging, ActorRef, ActorSystem, Props}
import akka.util.ByteString
import com.amazonaws.regions.Regions
import org.joda.time.DateTime
//import com.amazonaws.services.config.model.Source
import com.amazonaws.services.s3.model.ObjectMetadata
import com.amazonaws.services.s3.{AmazonS3, AmazonS3ClientBuilder}
import com.typesafe.config.ConfigFactory

import org.slf4j.{Logger, LoggerFactory}
import com.amazonaws.services.s3.model.S3ObjectSummary
//import scala.collection.JavaConversions._
//import akka.stream.alpakka.s3.scaladsl.{S3, S3ClientIntegrationSpec, S3WireMockBase}
import akka.stream.scaladsl.{Sink}
import com.amazonaws.services.s3.model.ObjectListing
import software.amazon.awssdk.regions.Region
import software.amazon.awssdk.regions.providers._
import com.amazonaws.services.s3.model.S3ObjectSummary
import org.apache.commons.io.IOUtils
import scala.io.Source
// companion object
object LogFileWatcher{
  def props(pullLogs: ActorRef, file:String): Props = Props(new LogFileWatcher(pullLogs,file))
}


class LogFileWatcher(pullLogs: ActorRef, file:String) extends Actor with ActorLogging{
  val logger: Logger = LoggerFactory.getLogger(this.getClass)
  log.debug("file watcher started running!")

  val config = ConfigFactory.load()
  val bucket_name: String = config.getString("s3.bucket")
  val file_path: String = config.getString("s3.file_path")
  val key_name: String = config.getString("s3.key")

  override def receive: Receive = {
//    case _ => log.info("Provide valid Input!")
    case "monitor" =>
      monitor(pullLogs)
  }

  def monitor(pullLogs: ActorRef): Unit = {
    val s3: AmazonS3 = AmazonS3ClientBuilder.standard.withRegion(Regions.US_EAST_1).build


    val obj = s3.getObject(bucket_name, file)

    val data = Source.fromInputStream(obj.getObjectContent).mkString(sys.props("line.separator"))
    logger.info(data)
//    print(myData)
//    val appended_data = myData + "06:55:06.035 [scala-execution-context-global-64] DEBUG  HelperUtils.Parameters$ - x2oBSI0/\\%CdfV2%ChSsnZ7vJo=2qJqZ%.\"kb"
//    s3.putObject(bucket_name,"log.log",appended_data)
//    while(true){
//      val summaries = listing.getObjectSummaries
//      print(summaries)
//      summaries.forEach(os => {
//        if (os.getLastModified.after(lastUpdatedTime)) {
//          print("modified")
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

object Main extends App{

  val config = ConfigFactory.load("application")
  val path:String = config.getString("s3.folder_path")

  val getFile = args(0)
  val system=ActorSystem("logFileWatcherSystem")
  private val pullLogs= createActorExtractor()
  private val filewatcher = createActorWatcher()
  filewatcher ! "monitor"

  // top level actor
  protected def createActorWatcher(): ActorRef =
    system.actorOf(LogFileWatcher.props(pullLogs, getFile) , "filewatcher")

  protected def createActorExtractor(): ActorRef =
    system.actorOf(LogFileExtraction.props(),name="pulllogs")


}
