
name := "Log-Data-Pipeline"

version := "0.1"

scalaVersion := "2.13.7"

val logbackVersion = "1.3.0-alpha10"
val sfl4sVersion = "2.0.0-alpha5"
val typesafeConfigVersion = "1.4.1"
val apacheCommonIOVersion = "2.11.0"
val scalacticVersion = "3.2.9"
val generexVersion = "1.0.2"
val AkkaVersion = "2.6.17"
val kafkaVersion = "2.8.0"
val awsjavaVersion = "1.12.90"
val awsjavas3Version = "1.12.98"
val akkaHttpVersion = "10.2.7"

resolvers += "Typesafe Repository" at "https://repo.typesafe.com/typesafe/releases/"


libraryDependencies ++= Seq(
  "com.amazonaws" % "aws-java-sdk" % awsjavaVersion,
  "com.amazonaws" % "aws-java-sdk-s3" % awsjavas3Version,
  "ch.qos.logback" % "logback-core" % logbackVersion,
  "ch.qos.logback" % "logback-classic" % logbackVersion,
  "org.slf4j" % "slf4j-api" % sfl4sVersion,
  "com.typesafe" % "config" % typesafeConfigVersion,
  "commons-io" % "commons-io" % apacheCommonIOVersion,
  "org.scalactic" %% "scalactic" % scalacticVersion,
  "org.scalatest" %% "scalatest" % scalacticVersion % Test,
  "org.scalatest" %% "scalatest-featurespec" % scalacticVersion % Test,
  "com.typesafe" % "config" % typesafeConfigVersion,
  "com.github.mifmif" % "generex" % generexVersion,
  "com.typesafe.akka" %% "akka-actor" % AkkaVersion,
  "org.apache.kafka" % "kafka-clients" % kafkaVersion,
  "com.typesafe.akka" %% "akka-stream" % AkkaVersion,
  "com.typesafe.akka" %% "akka-http" % akkaHttpVersion,
  "com.lightbend.akka" %% "akka-stream-alpakka-cassandra" % "3.0.3", // Scala 2.12/2.13
  "com.lightbend.akka" %% "akka-stream-alpakka-couchbase" % "3.0.3", // Scala 2.12/2.13
  "com.lightbend.akka" %% "akka-stream-alpakka-csv"       % "3.0.3", // Scala 2.12/2.13
  "com.typesafe.akka" %% "akka-http-spray-json" % akkaHttpVersion,
  "com.typesafe.akka" %% "akka-actor-typed" % AkkaVersion,
  "com.typesafe.akka"  %% "akka-stream-kafka" % "2.1.1",
  "com.lightbend.akka" %% "akka-stream-alpakka-s3" % "3.0.3",
  "com.typesafe.akka" %% "akka-stream" % AkkaVersion,
  "com.typesafe.akka" %% "akka-http" % akkaHttpVersion,
  "com.typesafe.akka" %% "akka-http-xml" % akkaHttpVersion,
  "org.apache.kafka" % "kafka-clients" % "2.8.0",
  "org.apache.kafka" % "kafka-streams" % "2.8.0",
  "org.apache.kafka" %% "kafka-streams-scala" % "2.8.0",
  "io.circe" %% "circe-core" % "0.14.1",
  "io.circe" %% "circe-generic" % "0.14.1",
  "io.circe" %% "circe-parser" % "0.14.1"


)