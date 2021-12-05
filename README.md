# FileWatchScala

## Contribution

| Contributors | Email |
| --- | --- |
| Ayna Jain | ajain85@uic.edu |
| Tirth Patel | tpatel79@uic.edu |

## Explanation

The main aim of this projeect is to notify the stakeholders via email in real time when more than one ERROR or WARN messages appear within a certain length time window. The data is processed in a pipeline using services such as Akka, Kafka and Spark in order.

#### Prerequisites to build and run the project:
- SBT installed on your system
- AWS CLI installed and configured on your system

## Instructions

#### Deployed the Log generator on EC2 to produce 10 logs at a time in S3 bucket file "loggenerator":
####  [LogGenerator](https://github.com/AynaJain/LogGenerator)

#### Deploying Log Generator on EC2 and generating the log file on S3:

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
    ``` git clone https://github.com/AynaJain/LogGenerator ```


