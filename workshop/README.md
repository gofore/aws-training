# Workshop Demo Application and Infrastructure as Code

The workshop demo consists of a Java application that is a super crappy version of google image search utilizing
microservice architecture and CloudFormation powered infrastructure templates.


## Application architecture

The Java application includes three modules (micro services):
- **UI** (aws-workshop-ui)  
  The UI is AngularJS powered frontend service that provides the minimal UI that is required to see the all the features
  of the application in work.
- **Loader** (aws-workshop-loader)  
  The loader service is responsible for taking in the initial google image search queries and fire up the real
  image crawlers that will populate the metadata and thumbnail storages.
- **Fetcher** (aws-workshop-fetcher)  
  The fetcher service handles all the hard lifting from parsing the images to storing the thumbnails to S3 and image
  metadata to simpledb.


## Prerequisites

**Tools:**
- Java 8 SDK
- Maven 3+
- IDE that supports Maven and Java projects (Idea, Eclipse, Netbeans, whatever suits you)
- git
- Python 2
- Ansible 1.7.2+
- boto 2.34.0+

**Knowledge:**
- Basic understanding of dependency injection and REST interfaces ([Guice](https://github.com/google/guice/wiki/GettingStarted) and [Restlet](http://restlet.com/learn/tutorial/2.2/) will be used)
- Hunch of FRP (functional reactive programming) (for [Streams](http://winterbe.com/posts/2014/07/31/java8-stream-tutorial-examples/) and [CompletableFuture](http://www.nurkiewicz.com/2013/05/java-8-definitive-guide-to.html))
- Ability to read HTML and JavaScript (the UI is [AngularJS](https://docs.angularjs.org/tutorial) based)


## Initial setup

Make sure your setup satisfies the defined prerequisites. After the tools are set up we are ready to roll.
1. You should have received an email from your instructors that includes an AWS credentials CSV file. If not, contact
   your instructors and ask one. Copy the `credentials.csv` file to your home directory in `.aws` directory so you will
   end up with file `~/.aws/credentials.csv`.
2. Clone the `aws-training` repository to your local machine with git:
   `git clone https://github.com/gofore/aws-training.git`
3. Change directory to `workshop/complete` where you should see `pom.xml`. In this directory run `mvn clean verify`. You
   should have internet access so that the integration tests can verify the access to AWS. The build should end with
   `BUILD SUCCESS`. If not, you can run `mvn -X clean verify` to get more detail error messages and/or contact your
   instructors for help.
4. Now you should have a working environment and you are ready to get hacking!

A sucessful initial setup should look something like this:
```
$ ls ~/.aws/
credentials.csv

$ git clone https://github.com/gofore/aws-training.git
Cloning into 'aws-training'...
...
Checking connectivity... done.

$ cd aws-training/workshop/complete

$ java -version
java version "1.8.0_25"
Java(TM) SE Runtime Environment (build 1.8.0_25-b17)
Java HotSpot(TM) 64-Bit Server VM (build 25.25-b02, mixed mode)

$ mvn -version
Apache Maven 3.2.3 (33f8c3e1027c3ddde99d3cdebad2656a31e8fdf4; 2014-08-11T23:58:10+03:00)
...
Java version: 1.8.0_25, vendor: Oracle Corporation
...

$ mvn clean verify
[INFO] Scanning for projects...
[INFO] ------------------------------------------------------------------------
[INFO] Reactor Build Order:
[INFO] 
[INFO] aws-workshop
[INFO] aws-workshop-common
[INFO] aws-workshop-loader
[INFO] aws-workshop-fetcher
[INFO] aws-workshop-ui
...
[INFO] ------------------------------------------------------------------------
[INFO] BUILD SUCCESS
[INFO] ------------------------------------------------------------------------
[INFO] Total time: 18.155 s
[INFO] Finished at: 2014-11-13T13:00:28+02:00
[INFO] Final Memory: 29M/477M
[INFO] ------------------------------------------------------------------------
```
