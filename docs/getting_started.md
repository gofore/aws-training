
# Getting Started

Before the workshop, you should have received an e-mail with your personal credentials.

If you intend to participate in the small programming exercises, you should do the preliminary tasks listed below, making sure that you have installed the tools successfully.

## Programming prerequisites

### Knowledge

- Basic understanding of dependency injection and REST interfaces ([Guice](https://github.com/google/guice/wiki/GettingStarted) and [Restlet](http://restlet.com/learn/tutorial/2.2/) will be used)
- Hunch of FRP (functional reactive programming) (for [Streams](http://winterbe.com/posts/2014/07/31/java8-stream-tutorial-examples/) and [CompletableFuture](http://www.nurkiewicz.com/2013/05/java-8-definitive-guide-to.html))
- Ability to read HTML and JavaScript (the UI is [AngularJS](https://docs.angularjs.org/tutorial) based)

### Option A: Set up the tools with Vagrant (recommended)

We encourage you to use Vagrant, but you may also install everything on a Linux host if you are brave enough.

**Windows users:** Note that Ansible is NOT available on Windows, so you will have to use Vagrant!

1. Install [Vagrant](https://www.vagrantup.com/) and [VirtualBox](https://www.virtualbox.org/).
2. Clone the git repository to your computer with `git clone https://github.com/gofore/aws-training.git` or by copying the zip file from GitHub.
3. Copy the `credentials.csv` file you received to your working directory under the `workshop` directory.
4. Run `vagrant up` in the main directory of the project, and wait until the provisioning has succeeded.
5. Once the virtual machine is running, SSH into the machine with `vagrant ssh`.
6. Inside the virtual machine, change directory to `cd /vagrant/workshop/complete` and run `mvn clean verify`. After a few minutes, the build should succeed.
7. Import the project into your IDE.

If you want to use an IDE on your host machine, you will then need Java 8 SDK and Maven 3+ also on your host machine. The intended workflow is that you either 1) Program with your IDE on your host machine and use the deployment tools in the virtual machine via the Vagrant shared directory or 2) you completely work inside the Vagrant machine with simple command line tools and code editors.

### Option B: Install tools manually

If you do not want to use Vagrant, you can also install everything manually. You will need the following tools:

- Java 8 SDK
- Maven 3+
- IDE that supports Maven and Java projects (Idea, Eclipse, Netbeans, whatever suits you)
- git
- Python 2
- Ansible 1.7.2+ (`sudo pip install ansible`)
- boto 2.34.0+ (`sudo pip install boto`)

Make sure your setup satisfies the defined prerequisites. After the tools are set up we are ready to roll.

1. Clone the `aws-training` repository to your local machine with git: `git clone https://github.com/gofore/aws-training.git`
2. You should have received an email from your instructors that includes an AWS credentials CSV file. If not, contact your instructors and ask one. Copy the `credentials.csv` file to your home directory in `.aws` directory so you will end up with file `~/.aws/credentials.csv`. You can also place the file under `workshop`.
3. Change directory to `workshop/complete` where you should see `pom.xml`. In this directory run `mvn clean verify`. You should have internet access so that the integration tests can verify the access to AWS. The build should end with `BUILD SUCCESS`. If not, you can run `mvn -X clean verify` to get more detail error messages and/or contact your instructors for help.
4. Now you should have a working environment.
5. Import the project into your IDE.

A successful initial setup should look something like this:
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
