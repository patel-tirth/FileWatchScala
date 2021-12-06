import com.typesafe.config.{Config, ConfigFactory}

class LogTest extends AnyFlatSpec with Matchers with PrivateMethodTester {
  val config: Config = ConfigFactory.load("application")

    behavior of "s3"

    it should "get the s3 bukcet name" in {
      val bucket = config.getString("s3.bucket")
      bucket  shouldBe "loggenerator-tirth"
    }

  behavior of "kafka configurations"
  it should "get the kafka bootstrap server of aws msk" in {
    val server = config.getString("kafka.bootstrapserver")
    server  shouldBe "b-2.demo-cluster-1.sqzh4v.c17.kafka.us-east-1.amazonaws.com:9094,b-1.demo-cluster-1.sqzh4v.c17.kafka.us-east-1
  }

  it should "get the kafka trust store location" in {
    val truststore = config.getString("kafka.trustStoreLocation")
    server  shouldBe "/tmp/kafka.client.truststore.jks"
  }

  behavior of "akka kafka producer"
  it should "get the dispatcher" in {
    val dispatcher = config.getString("akka.kafka.producer.use-dispatcher")
    server  shouldBe "akka.kafka.default-dispatcher"
  }

  it should "get the eos commit interval" in {
    val interval = config.getString("akka.kafka.producer.eos-commit-interval")
    server  shouldBe "100ms"
  }

  it should "get the group id of consumer" in {
    val interval = config.getString("akka.consumer.kafka-clients.group.id")
    server  shouldBe "group1"
  }


}