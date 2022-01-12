# Streaming Log Data Pipeline

## Contribution

<!-- | Contributors | Email | -->
| --- | --- |
<!-- | Ayna Jain | ajain85@uic.edu | -->
| Tirth Patel | tpatel79@uic.edu |

**Akka and Kafka code is explained in this video**
<!-- | Youtube Link | https://youtu.be/ZcWVkD12RNE | -->

**Running Kafka, Spark and sending email**
| Youtube Link | https://youtu.be/XWBTjyXmY7U |

## Explanation

The main aim of this project is to notify the stakeholders via email in real time when more than one ERROR or WARN messages appear within a certain length time window. The data is processed in a pipeline using services such as Akka, Kafka and Spark in order.

``` The second part of the project is linked below```
### Spark Project -  [Spark Streaming Project Link](https://github.com/patel-tirth/KafkaSparkStreaming)
#### Prerequisites to build and run the project:
- SBT installed on your system
- AWS CLI installed and configured on your system

## Project Structure

src/main/resources/
```
application.conf/
contains Kafka bootstrapserver broker strings and kafka topic which are preconfigured while downloading and 
configuring kafka and are used in the program for AWS Apache Kafka processing.
```
src/main/scala/
```
Actors/
LogFileWatcher - LogFileWatcher monitors the S3 bucket for any new files added to the bucket by running the 
project "LogGenerator" on EC2 instance using a tailrecursive function in the code which continuously 
monitors the S3 bucket and notifies the next actor "LogFileExtraction" in loop if a new file is notified 
in the bucket.

LogFileExtraction - LogFileExtraction is called in loop whenever a new file is found in the S3 bucket. 
The code in this file fetches the logs from the S3 object and read them line-by-line. It then extract the 
logs which are of type "WARN" or "ERROR" and pass them to Kafka streams for furrther processing by calling 
the function "runKafka".
```
```
KafkaService/ 
Producer - This Kafka producer conatins all the properties of kafka that are defined for running the kafka server.
It receives the extracted log messages from the LogFileExtraction actor and it publishes those messages to Kafka 
topic "LogDataTopic", which is a Kafka topic that is created while downloading and configuring Kafka. 
```


## Instructions

#### Deploy the Log generator on EC2 to continuously produce logs in S3 bucket. "loggenerator": [LogGenerator](https://github.com/patel-tirth/LogGenerationS3)

#### Instructions to deploy the Log Generator on EC2 and generating the log file on S3:

1. Create S3 bucket.
2. Launch an EC2 instance (Make sure to download the private key file).
3. Create an IAM instance role to grant access to S3 bucket.
   - Open IAM console. 
   - Select Roles and then Click on Create role. 
   - Click on AWS Service and then choose EC2. 
   - Click on Next: Permissions. Filter for the AmazonS3FullAccess managed policy and then select it. 
   - Click next for rest. At this stage you have created a role which allows us for full access to S3 bucket. Now we have to attach this role to EC2 instance to grant access to S3 bucket.
4. Attaching Role to EC2 Instance:
   - From your EC2 console. Select the instance that you launched so that you can grant full access to S3 bucket. 
   - Select the Actions tab from top left menu, select Instance Settings , and then choose Attach/Replace IAM role. Choose the IAM role that you just created, click on Apply , and then choose Close.
   - At this point we created a role and attached to EC2 instance for full access to S3 bucket.
5. Finally, validate access to S3 bucket using AWS CLI.
6. Select the EC2 instance and click "Connect" to ssh into the EC2 instance.
7. Open a terminal. Locate your private key file which is used to launch the instance.
8. Run the command to ensure your key is not publicly viewable.
   ``` 
   chmod 400 yourKeyFileName.pem 
   ```
9. Connect to your instance using its Public DNS:
   ``` 
   ssh -i "yourKeyFileName.pem" ec2-user@ec2-3-82-110-201.compute-1.amazonaws.com
   ```
10. Finally, connected to EC2 instance using SSH.
11. In order to run the LogGenerator project to generate log messages on EC2 we must install the following on EC2:
   - Java
     Command - ``` sudo yum install java-1.8.0-openjdk ``` (when using Amazon Linux 2 AMI)
   - SBT 
     Command - 
      ```
      curl -L https://www.scala-sbt.org/sbt-rpm.repo > sbt-rpm.repo
      sudo mv sbt-rpm.repo /etc/yum.repos.d/
      sudo yum install sbt
      ```
   - Git 
     Command - ``` sudo yum install git -y ``` (when using Amazon Linux 2 AMI)
12. Now, we have everything installed, let us clone our LogGenerator on EC2 using command -
    ``` 
    git clone https://github.com/AynaJain/LogGenerator 
    ```
13. Once we have our LogGenerator on EC2. cd into LogGenerator directory using ``` cd LogGenerator ``` 
    Now enter the follwing command: ``` sbt run ```

14. This code will generate the follwing output on the ssh terminal:
    ```
    [info] running GenerateLogData 
    23:54:01.919 [run-main-0] INFO  GenerateLogData$ - Log data generator started...
    23:54:02.293 [run-main-0] WARN  HelperUtils.Parameters$ - Max count 10 is used to create records instead of timeouts
    23:54:02.687 [scala-execution-context-global-73] WARN  HelperUtils.Parameters$ - s%]s,+2k|D}K7b/XCwG&@7HDPR8z
    23:54:03.192 [scala-execution-context-global-73] INFO  HelperUtils.Parameters$ - ;kNI&V%v<c#eSDK@lPY(
    23:54:03.579 [scala-execution-context-global-73] INFO  HelperUtils.Parameters$ - l9]|92!uHUQ/IVcz~(;.Uz%K*5jTUd08
    23:54:03.834 [scala-execution-context-global-73] INFO  HelperUtils.Parameters$ - G3sw7^U<^q^Cl!aMTDbNz<:$;?e<.0JD_'
    23:54:04.229 [scala-execution-context-global-73] WARN  HelperUtils.Parameters$ - A><YFqpg+~"E1T
    23:54:04.637 [scala-execution-context-global-73] DEBUG HelperUtils.Parameters$ - JrQB;P0"&+6;&Dk-
    23:54:04.950 [scala-execution-context-global-73] INFO  HelperUtils.Parameters$ - OsI1`qAeU5H;\+
    23:54:05.143 [scala-execution-context-global-73] INFO  HelperUtils.Parameters$ - [h!Q9PEY>L(NpKLBO"Gjo:=4kRXQ_tZ!
    23:54:05.307 [scala-execution-context-global-73] INFO  HelperUtils.Parameters$ - B?y&C"C5rsb:2037;f&|vM#x?z|Ny|&<44Z8B&rF1#&M
    23:54:05.656 [scala-execution-context-global-73] WARN  HelperUtils.Parameters$ - x2oBSI0/\%CdfV2%ChSsnZ7vJo=2qJqZ%."kbc!0ne`y&m
    23:54:05.657 [run-main-0] INFO  GenerateLogData$ - Log data generation has completed after generating 10 records.
    Dec 04, 2021 11:54:06 PM com.amazonaws.util.Base64 warn
    WARNING: JAXB is unavailable. Will fallback to SDK implementation which may be less performant.If you are using Java 9+, you will need to include javax.xml.bind:jaxb-api as a dependency.
    [success] Total time: 10 s, completed Dec 4, 2021, 11:54:07 PM
    ```
15. After it is done generating the logs on ssh terminal it will generate a .log file inside the S3 bucket with the same log messages generated on the terminal above.

#### Kafka 

Run the following commands to start Kafka zookeeper and create a Kafka topic:
``` 
bin/zookeper-server-start.sh config/zookeeper.properties 
kafka-server-start.sh config/server.properties
bin/kafka-topics.sh --create --zookeeper 127.0.0.1:2181 replication-factor 2 --partitions 1 --topic LogDataTopic
```

Start the kafka producer:
``` 
kafka-console-producer.sh --broker-list 127.0.0.1:9092 --topic LogDataTopic
```

Start the Kafka consumer:
```
kafka-console-consumer.sh --bootstrap-server 127.0.0.1:9092 --topic LogDataTopic
```

Now, we have started the Kafka Producer and consumer. 
Now, run the [LogGenerator](https://github.com/patel-tirth/LogGenerationS3) program on EC2 using ``` sbt run ```
Also, run this project using ``` sbt run ```

LogGenerator will produce a .log file in the s3 bucket. ``` FileWatchScala ``` project created an actor system to monitor any incoming .log files inside the S3 bucket. As a new .log is found inside the bucket after the last program modified time. This actor ```LogFileWatcher ``` will send the file to another actor with the file time and actor reference. The file will be received to the actor ``` LogFileExtraction ``` . This actor will read the file line-by-line and extract all the ERROR and WARN messages if more than 1 and then finally send it to Kafka stream for further processing. Kafka producer will receive the stream of messages from ``` LogFileExtraction ``` actor which will publish the log messages to Kafka topic ``` LogDataTopic ```. These messages can also be seen inside the kafka consumer that we started above. Although the aim is to run Spark as the consumer for these messages. Therefore, the [Spark project](https://github.com/patel-tirth/KafkaSparkStreaming) when run using ``` sbt run ``` will read the data from the Kafka topic ``` LogDataTopic ``` and send email message to stakeholders using AWS SES service.

The three extracted messages from running the LogGenerator on EC2 can be seen on Kafka consumer as well as Spark are:
```
23:54:02.687 [scala-execution-context-global-73] WARN  HelperUtils.Parameters$ - s%]s,+2k|D}K7b/XCwG&@7HDPR8z
23:54:04.229 [scala-execution-context-global-73] WARN  HelperUtils.Parameters$ - A><YFqpg+~"E1T
23:54:05.656 [scala-execution-context-global-73] WARN  HelperUtils.Parameters$ - x2oBSI0/\%CdfV2%ChSsnZ7vJo=2qJqZ%."kbc!0ne`y&m
```







