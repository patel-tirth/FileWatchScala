package KafkaService
import com.amazonaws.services.s3.model.S3Object
import com.typesafe.config.{Config, ConfigFactory}
import org.apache.kafka.clients.producer.{KafkaProducer, ProducerRecord}
import org.apache.kafka.clients.producer.ProducerConfig
import org.apache.kafka.common.serialization.{StringDeserializer, StringSerializer}
import org.apache.spark.streaming.kafka010._
import org.apache.spark.streaming.kafka010.LocationStrategies.PreferConsistent
import org.apache.spark.streaming.kafka010.ConsumerStrategies.Subscribe
import org.slf4j.{Logger, LoggerFactory}

import java.util.Properties
class Producer {
  val config: Config = ConfigFactory.load("application")
  val logger: Logger = LoggerFactory.getLogger(this.getClass)
  val properties: Properties = new Properties()
  properties.setProperty(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, config.getString("kafka.bootstrapserver"))
  properties.setProperty(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, classOf[StringSerializer].getName)
  properties.setProperty(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, classOf[StringSerializer].getName)

  def runKafka(message: String): Unit =  {
    try {
      val producer = new KafkaProducer[String, String](properties)
      val record = new ProducerRecord[String, String](config.getString("kafka.topic"), message)
      producer.send(record)
      logger.info("Sent Message: " + record)
      producer.flush()
      producer.close()
    } catch {
      case exception: Exception => exception.printStackTrace()
    }
  }

  val kafkaParams = Map[String, Object](
    "bootstrap.servers" -> config.getString("kafka.bootstrapserver"),
    "key.deserializer" -> classOf[StringDeserializer],
    "value.deserializer" -> classOf[StringDeserializer],
//    "group.id" -> "use_a_separate_group_id_for_each_stream",
    "auto.offset.reset" -> "latest",
    "enable.auto.commit" -> (false: java.lang.Boolean)
  )

}
