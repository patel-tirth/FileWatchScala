package S3


import akka.http.scaladsl.Http
import akka.actor.ClassicActorSystemProvider
import akka.http.scaladsl.model._
import akka.http.scaladsl.model.headers.ByteRange
import akka.stream.Attributes
import com.amazonaws.services.s3.model.{GetObjectRequest, ListObjectsRequest}

import java.io.File
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

import scala.collection.immutable.Seq

import akka.NotUsed
import akka.stream.Attributes.LogLevels
import akka.stream.alpakka.csv.scaladsl.{CsvFormatting, CsvQuotingStyle}
import akka.stream.alpakka.s3.scaladsl.S3
import akka.stream.alpakka.s3.{ListBucketResultContents, MultipartUploadResult}
import akka.stream.scaladsl.{Sink, Source}
import akka.stream.{Attributes, Materializer}


import com.amazonaws.regions.Regions
import com.amazonaws.services.config.model.Source
import com.amazonaws.services.s3.model.ObjectMetadata
import com.amazonaws.services.s3.{AmazonS3, AmazonS3ClientBuilder, AmazonS3Client}
import com.typesafe.config.ConfigFactory


class extractS3data {
  val config = ConfigFactory.load("application")
  val bucket_name: String = config.getString("s3.bucket")
  val file_path: String = config.getString("s3.file_path")
  val key_name: String = config.getString("s3.key")

  val bucketfolder_path:String=config.getString("s3.path")


  val s3: AmazonS3 = AmazonS3ClientBuilder.standard.withRegion(Regions.US_EAST_1).build()
  val metadata: ObjectMetadata = s3.getObjectMetadata(bucket_name, key_name)
}
