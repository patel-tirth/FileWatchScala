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

class logFileExtraction extends Actor with ActorLogging{


  override def receive: Receive = {

  }

}
