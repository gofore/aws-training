
# AWS for Developers
### *crash course to cloud app development*

---

## Agenda

1. **Introduction:** Main services and exercise description
2. **Loose coupling**: Separating services with queues
3. **Persistence:** Object storage and database
4. **Elasticity:** Scaling resources based on demand

--

## What you will need

- Laptop with Internet connectivity
- [Vagrant](https://www.vagrantup.com/) (and [Virtualbox](https://www.virtualbox.org/)) is recommended
- Optionally Java 8 SDK, Maven 3+ and an IDE
- Material is available at [github.com/gofore/aws-training](https://github.com/gofore/aws-training)

---

# Introduction to AWS

--

## Amazon Web Services (AWS)

- Extensive set of cloud services available in the web
- On-demand, elastic resources
- Pay-per-use with no up-front costs (with optional commitment)
- Self-serviced and programmable

--

![List of AWS Services](/images/aws_list_of_services.png)

[AWS Services](http://aws.amazon.com/products/)

--

### We will focus on the following

- [Elastic Compute Cloud (EC2)](http://aws.amazon.com/ec2/) and its subservices, [Elastic Load Balancing (ELB)](http://aws.amazon.com/elasticloadbalancing/) and [Auto Scaling](http://aws.amazon.com/autoscaling/)
- [CloudWatch](http://aws.amazon.com/cloudwatch/) monitoring service and [Simple Notification Service (SNS)](http://aws.amazon.com/sns/)
- [Identity and Access Management (IAM)](http://aws.amazon.com/iam/)
- [Simple Queue Service (SQS)](http://aws.amazon.com/sqs/)
- [Simple Storage Service (S3)](http://aws.amazon.com/s3/) and [SimpleDB](http://aws.amazon.com/simpledb/)
- [CloudFormation](http://aws.amazon.com/cloudformation/) infrastructure as code

---

# Elastic Compute Cloud

--

## [Elastic Compute Cloud (EC2)](http://aws.amazon.com/ec2/)

- One of the core services of AWS
- Virtual machines (or *instances*) as a service
- ~20 different *instance types* that vary in performance and cost
- Instance is created from an *Amazon Machine Image (AMI)*, which in turn can be created again from instances

Notes: Usage is billed per *instance-hour* for running instances. Prices vary based on region, instance type, and operating system. Purchasing options include *On-Demand Instances*, *Reserved Instances*, *Spot Instances*

--

![AWS Region map](/images/aws_map_regions.png)

Notes: Regions: Frankfurt, Ireland, US East (N. Virginia), US West (N. California), US West (Oregon), South America (Sao Paulo), Tokyo, Singapore, Sydney. Special regions are **GovCloud** and **Beijing**.

--

![AWS EU Region map](/images/aws_map_regions_eu.png)

[Regions, Availability Zones (AZ)](http://docs.aws.amazon.com/AWSEC2/latest/UserGuide/using-regions-availability-zones.html) and CDN [Edge Locations](http://aws.amazon.com/about-aws/global-infrastructure/)

Notes: We will only use Ireland (eu-west-1) region in this workshop. See also [A Rare Peek Into The Massive Scale of AWS](http://www.enterprisetech.com/2014/11/14/rare-peek-massive-scale-aws/).

--

## Networking in AWS

- Regions and availability zones
- *Security groups* provide firewalling
- More detailed IP subnetting with [Virtual Private Cloud (VPC)](http://aws.amazon.com/vpc/)

--

## [Identity and Access Management (IAM)](http://aws.amazon.com/iam/)

- User-specific *access keys* for API access
- Fine-grained access policies to services and resources
- IAM *roles* to automatically allow API access within AWS.

Notes: Always use roles, do not store credentials inside the instances, or [something bad](http://www.browserstack.com/attack-and-downtime-on-9-November) might happen.

--

## Exercise: Launch an instance

1. Log-in to [gofore-crew.signin.aws.amazon.com/console](https://gofore-crew.signin.aws.amazon.com/console)
2. Go to EC2 dashboard and switch to Ireland region
3. Pass a shell script as [*User Data*](http://docs.aws.amazon.com/AWSEC2/latest/UserGuide/user-data.html) (configure instance -> advanced)
4. `ssh -i your_key.pem ubuntu@instance_hostname`
5. `curl http://169.254.169.254/latest/user-data/`
6. `curl http://169.254.169.254/latest/meta-data/`

Notes: You will have to reduce keyfile permissions `chmod og-xrw mykeyfile.pem`. If you are on Windows and use Putty, you will have to convert the .pem key to .ppk key using [puttygen](http://www.chiark.greenend.org.uk/~sgtatham/putty/download.html)

---

# Objective of today

--

## Super crappy image search

- Application that mimics Google image search
- Micro service architecture:
  - user interface (web application)
  - page loader (does the initial image search)
  - image fetcher (loads all the image data to AWS hosted storages)

--

![Workshop application architecture](/images/aws_workshop_arch_2_no_alarms.png)

--

![Web hosting reference architecture](/images/aws_reference_architecture_web_hosting.png)

[aws.amazon.com/architecture](http://aws.amazon.com/architecture/)

--

## Designing a cloud-friendly application

- Split the application into small, **stateless**, horizontally scalable services
- **Loosely couple** services with queues, load balancers, service discovery
- **Automate** infrastructure setup and application deployment
- [12factor.net](http://12factor.net/) provides some design principles for cloud-friendly apps

---

# Programmability

--

## SDKs and tools for everyone

Java SDK: [aws.amazon.com/sdk-for-java](http://aws.amazon.com/sdk-for-java/)

Ansible: [docs.ansible.com/list_of_cloud_modules.html](http://docs.ansible.com/list_of_cloud_modules.html)

Other SDKs and command-line tools: [aws.amazon.com/tools](http://aws.amazon.com/tools/)

Boto for Python: [github.com/boto/boto](https://github.com/boto/boto)

--

## Java SDK

- Collection of libraries to operate AWS resources in Java
  - since version 1.9 the libraries are split in submodules for more fine grained dependency management
  - available in maven central
- New object oriented resource APIs are [under development](https://github.com/awslabs/aws-sdk-java-resources)
- For complex resource management with the SDK see [Netflix Asgard](https://github.com/Netflix/asgard) (...but it's coded in Groovy and Grails)

--

## Using IAM credentials with the SDKs

SDKs support credentials provider chain, including [Java SDK](https://github.com/gofore/aws-training/blob/master/workshop/initial/aws-workshop-common/src/main/java/com/gofore/aws/workshop/common/di/AwsModule.java#L52-58).

<pre><code data-trim="" class="java">
public AWSCredentialsProvider credentialsProvider(ApplicationProperties properties) {
    return new AWSCredentialsProviderChain(
            new StaticCredentialsProvider(new PropertyLoaderCredentials(properties)),
            new ProfileCredentialsProvider(),
            new InstanceProfileCredentialsProvider()
    );
}
</code></pre>

--

## [CloudFormation](http://aws.amazon.com/cloudformation/)

Create a *stack* of resources from a JSON *template*.

Reusable, versionable infrastructure description that can be commited to source control.

[Template anatomy](http://docs.aws.amazon.com/AWSCloudFormation/latest/UserGuide/template-anatomy.html) | [Simple template](http://docs.aws.amazon.com/AWSCloudFormation/latest/UserGuide/example-templates-ec2-with-security-groups.html) | [Example snippets](http://docs.aws.amazon.com/AWSCloudFormation/latest/UserGuide/CHAP_TemplateQuickRef.html)

--

## Structure of a template

<pre><code data-trim="" class="json">
{
  "AWSTemplateFormatVersion": "2010-09-09",
  "Description": "This template constructs a single web server",

  "Parameters": {
    "MySSHKeyName": {"Type": "String", "MinLength": "1", "MaxLength": "255"}
  }, 
  "Resources": {
    "MyWebServer": {"Type": "AWS::EC2::Instance", "Properties": { ... }}
  },
  "Outputs": {
    "MyDomainName": {"Value": {"Fn::GetAtt": ["MyWebServer", "PublicDnsName"]}}
  },

  "Mappings": { ...Optional set of helper mappings... },
  "Conditions": { ...Optional set of conditions... }
}
</code></pre>

--

## Creating a stack with Ansible

<pre><code data-trim="" class="ruby">
- hosts: localhost
  tasks:
  - name: "Create CloudFormation stack"
    cloudformation:
      stack_name="my-precious-stack"
      template="my-cloudformation-template.json"
      region="eu-west-1"
      state=present
    args:
      template_parameters:
        KeyName: "my-key-in-ec2"
        AmiId: "ami-828334f5"
        InstanceType: "t2.micro"
      tags:
        Name: "name-for-all-the-resources"
        Project: "aws-workshop-project"
</code></pre>


---

# Loose coupling with Queues

--

## [Simple Queue Service (SQS)](http://aws.amazon.com/sqs/)

- Message queue with simple REST API
- Scalable, reliable and persistent
- Does not rely on any existing standard (JMS, AMQP)
- Lots of gotchas and need to knows

--

## SQS usage

- Messages must be polled from the REST API
  - supports long polling but by default releases immediately if there are no messages
- Successful message receive triggers invisibility period of a message
  - no transactions, automatic "rollback" if the message is not deleted within the invisibility period
- Messages are sent to the REST API
  - 256kB payload limit, should use handle to S3 stored data for bigger payloads
  - maximum message retention period is 14 days

--

## Exercise: Create a stack

<pre><code data-trim="" class="ruby">
# Run the following command in workshop/initial/deploy
# ansible-playbook -e "user_name=FOO" -i localhost, create_queues_and_database.yml

- hosts: all
  connection: local
  tasks:
  - name: "Create a stack of SQS queues and SimpleDB domain"
    cloudformation:
      stack_name="aws-workshop-{{ user_name }}"
      template="cloudformation-templates/infrastructure-queues-and-sdb.template"
      region="eu-west-1"
      state=present
    args:
      template_parameters:
        UserName: "{{ user_name }}"
      tags:
        Name: "aws-workshop-{{ user_name }}"
</code></pre>

Verify that you can find your queues from the management console

--

## Exercise: Programming with SQS

1. Compile and run the `aws-workshop-ui` locally
2. Complete [programming task #1](https://github.com/gofore/aws-training/tree/master/workshop/initial#task-1-sqs-request)  
   See `com.gofore.aws.workshop.ui.rest.QueueAttributesResource`
3. Complete [programming task #2](https://github.com/gofore/aws-training/tree/master/workshop/initial#task-2-sqs-message-send)  
   See `com.gofore.aws.workshop.ui.rest.QueriesResource`
4. View your messages in the queues from the management console
5. Run the `aws-workshop-loader` locally

Notes: If you use Vagrant, you can access the web app at http://10.10.10.10:9001 on your host machine.

--

## Handling SQS messages

[com.gofore.aws.workshop.common.sqs.SqsService.java](https://github.com/gofore/aws-training/blob/master/workshop/initial/aws-workshop-common/src/main/java/com/gofore/aws/workshop/common/sqs/SqsService.java)

<pre><code data-trim="" class="java">
public class SqsService extends Service {
    public SqsService(SqsClient sqsClient, String queueUrl) {}
    public SqsService addMessageHandler(Function&lt;Message, CompletableFuture&lt;Message&gt;&gt; messageHandler) {}
    public SqsService setCompleteHandler(BiConsumer&lt;? super Message, ? super Throwable&gt; completeHandler) {}
    public synchronized void start() throws Exception {}
    public synchronized void stop() throws Exception {}
    protected CompletableFuture&lt;Message&gt; handleMessage(Message message) {}
    protected CompletableFuture&lt;Message&gt; completeMessage(CompletableFuture&lt;Message&gt; message) {}
    protected CompletableFuture&lt;Message&gt; deleteMessage(CompletableFuture&lt;Message&gt; message) {}
}
</code></pre>

--

![Workshop application architecture](/images/aws_workshop_arch_2_no_alarms.png)

---

# Object Storage

--

## [Simple Storage Service (S3)](http://aws.amazon.com/s3/)

- Store blob objects into *buckets* over an RESTful interface
- Supports object [versioning](http://docs.aws.amazon.com/AmazonS3/latest/dev/Versioning.html) and easy archiving to [Glacier](http://aws.amazon.com/glacier/)
- Several fine-grained access control mechanisms (IAM policies, bucket policies, Access Control Lists (ACLs) and query string authentication)
- Supports static hosting of web resources

--

## Exercise: Put objects to S3

1. Complete [programming task #3](https://github.com/gofore/aws-training/tree/master/workshop/initial#task-3-put-object-to-s3)  
   See `com.gofore.aws.workshop.fetcher.images.ThumbnailUploader`

Notes: Content length must be known when putting files to S3. This might become an issue when streaming content straight to S3.

---

# Databases

--

## Plenty of database services

- [Relational Database Service (RDS)](http://aws.amazon.com/rds/)
- Non-relational [DynamoDB](http://aws.amazon.com/dynamodb/)
- [Redshift](http://aws.amazon.com/redshift/) petabyte-scale data warehouse
- [ElastiCache](http://aws.amazon.com/elasticache/) in-memory caching
- ...Oh, and [SimpleDB](http://aws.amazon.com/simpledb/)

--

## Yes, it is **Simple**

- Performant NoSQL database to store flat attributes within an item
  - all attributes are indexed automatically
- Supports **only string** data type!
  - sorting, numbers, dates, etc. are not trivial
- Hard limits for storage
  - 256 total attribute name-value pairs per item
  - one billion attributes per domain
  - 10 GB of total user data storage per domain
- Very limited SQL-like query syntax

--

## Exercise: Put attributes to SimpleDB

1. Complete [programming task #4](https://github.com/gofore/aws-training/tree/master/workshop/initial#task-4-put-attributes-to-simpledb)  
   See `com.gofore.aws.workshop.fetcher.images.MetadataRepository`

--

## Public HTTP access for S3 objects

1. Submit a query and watch how it gets processed within the application
2. Find your files in S3, try to access their URL
3. In your code, add an Access Control List (ACL) to fix the object permissions if you haven't already (see hint in task 3)
4. Try to access your files again

Notes: A [Canned ACL](http://docs.aws.amazon.com/AmazonS3/latest/dev/acl-overview.html#CannedACL) is a predefined ACL, but it is possible to create a fully customized ACL.

--

### Let's take a look into SimpleDB

- sdbtool (Firefox plugin) [code.google.com/p/sdbtool/](https://code.google.com/p/sdbtool/)
- SdbNavigator (Chrome plugin) [www.kingsquare.nl/sdbnavigator](http://www.kingsquare.nl/sdbnavigator)

--

## Exercise: SimpleDB query

1. Complete [programming task #5](https://github.com/gofore/aws-training/tree/master/workshop/complete#task-5-simpledb-query)  
   See `com.gofore.aws.workshop.ui.rest.SearchResource`

---

# Really simple deployment pipeline

--

![Deployment pipeline](/images/deployment_pipeline.png)

--

## Exercise: Deploy your .jar files to S3

1. `mvn deploy`
2. Verify that you can find your files from the management console

---

# [Auto Scaling](http://docs.aws.amazon.com/AutoScaling/latest/DeveloperGuide/WhatIsAutoScaling.html)

--

## Auto Scaling instances

- *Auto Scaling Group* contains instances whose lifecycles are automatically managed by CloudWatch alarms or schedule
- A *scaling policy* describes how the group scales in or out. You should always have policies for both directions. [*Policy cooldowns*](http://docs.aws.amazon.com/AutoScaling/latest/DeveloperGuide/Cooldown.html) control the rate in which scaling happens.
- A [*launch configuration*](http://docs.aws.amazon.com/AutoScaling/latest/DeveloperGuide/LaunchConfiguration.html) describes the configuration of the instance. Having a good AMI and bootstrapping is crucial.

--

![Auto Scaling Group Lifecycle](http://docs.aws.amazon.com/AutoScaling/latest/DeveloperGuide/images/as-lifecycle-basic-diagram.png)

[Auto Scaling Group Lifecycle](http://docs.aws.amazon.com/AutoScaling/latest/DeveloperGuide/AutoScalingGroupLifecycle.html)

--

## [Elastic Load Balancer](http://aws.amazon.com/elasticloadbalancing/)

- Route traffic to an Auto Scaling Group (ASG)
- Runs health checks to instances to decide whether to route traffic to them
- Spread instances over multiple AZs for higher availability
- ELB scales itself. Never use ELB IP address. Pre-warm before flash traffic.

--

![ELB Architecture](http://awsmedia.s3.amazonaws.com/2012-02-24-techdoc-elb-arch.png)

[Best practices in ELB](https://aws.amazon.com/articles/1636185810492479)

--

![Autoscaling with alarms](/images/aws_workshop_arch_3_alarms.png)

--

## Complete CloudFormation template

workshop/initial/deploy/cloudformation-templates/

[infrastructure-complete.template](https://github.com/gofore/aws-training/blob/master/workshop/initial/deploy/cloudformation-templates/infrastructure-complete.template)

--

## Exercise: Deploy complete stack

1. Create a stack with `create_complete_infrastructure.yml`
2. Look at CloudFormation -> Stack -> Events until complete
3. Look at EC2 -> Load Balancers -> Instances until InService
4. Check your e-mail and subscribe to the notifications
5. Try out the application
6. Try to make the aws-workshop-fetchers scale out
7. Terminate instances from an auto scaling group and see what happens

--

## Exercise: Monkey time!

![Chaos Monkey](/images/netflix-chaos-monkey.jpg)

Be a [Chaos Monkey](https://github.com/Netflix/SimianArmy/wiki/Chaos-Monkey): terminate all of your `aws-workshop-ui` instances!

---

# Summary

--

![Workshop application architecture](/images/aws_workshop_arch_4.png)

--

## Things that we did right

- Using IAM user accounts instead of sharing the root account
- Using IAM roles on the instances
- Load-balancing, auto-scaling and elasticity
- Loose coupling with SQS and ELB
- CloudFormation and bootstrapping with User Data and Ansible

--

## Things that we *should* have done

- Set up a VPC and subnetting, proper security groups. No private IPs and no public access, no public SSH.
- Distribute credentials via secure path, least privileges in IAM
- Break CloudFormation template into smaller pieces
- Versioned jar files or golden images: Launch configuration should be static

Notes: Currently new ui-instances that are born with autoscale pull the latest jar from S3. This means that the "launch configuration" is not static, and every autoscaled instance might be different.

--

![Build process](/images/netflix_build_legos.png)

[Netflix tech blog: Building with Legos](http://techblog.netflix.com/2011/08/building-with-legos.html)

--

![AMI models](/images/ami-models.png)

[Cloud-Powered CI and deployment](http://www.slideshare.net/AmazonWebServices/cloudpowered-continuous-integration-and-deployment-architectures-jinesh-varia)

--

## Things that we *could* have done

- Use proper microservice architecture, no shared usage of resources
- Distribute static content from [CloudFront CDN](http://aws.amazon.com/cloudfront/)
- Use a more supported(?) database than SimpleDB
- [AWS::CloudFormation::Init](http://docs.aws.amazon.com/AWSCloudFormation/latest/UserGuide/aws-resource-init.html)
- Build golden image / [Docker](https://www.docker.com/) container to provide fully immutable instances

--

## What next?

- Reference architectures [aws.amazon.com/architecture](http://aws.amazon.com/architecture/)
- Whitepapers [aws.amazon.com/whitepapers](aws.amazon.com/whitepapers)

- Register to [Amazon Partner Network Portal](https://www.apn-portal.com/home/home.jsp) and take the AWS Technical Professional or AWS Business Professional course
