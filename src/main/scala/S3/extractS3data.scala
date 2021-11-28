package S3


import akka.http.scaladsl.Http
import akka.actor.ClassicActorSystemProvider
import akka.http.scaladsl.model._
import akka.http.scaladsl.model.headers.ByteRange
import akka.stream.Attributes
//import akka.stream.alpakka.s3.headers.{CannedAcl, ServerSideEncryption}
//import akka.stream.alpakka.s3.impl._
//import akka.stream.scaladsl.{RunnableGraph, Sink, Source}
import akka.util.ByteString
import akka.{Done, NotUsed}

import com.amazonaws.AmazonServiceException
import com.amazonaws.services.s3.{AmazonS3, AmazonS3ClientBuilder}
import com.amazonaws.regions.Regions
import com.typesafe.config.ConfigFactory

import scala.collection.immutable
import scala.concurrent.Future





class extractS3data {
  val config = ConfigFactory.load("application")
  val bucket_name: String = config.getString("s3.bucket")
  val file_path: String = config.getString("s3.file_path")
  val key_name: String = config.getString("s3.key")



}
