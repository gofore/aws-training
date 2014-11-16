
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

Extensive set of cloud services available in the web.

Mainly IaaS, but in some parts also PaaS.

On-demand, pay-per-use.

Self-serviced and programmable.

Usage is billed per *instance-hour* for running instances.
Prices vary based on region, instance type, and operating system.
Purchasing options include *On-Demand Instances*, *Reserved Instances*, *Spot Instances*

--

![List of AWS Services](/images/aws_list_of_services.png)

[AWS Services](http://aws.amazon.com/products/)

--

### We will focus on the following

- [Elastic Compute Cloud (EC2)](http://aws.amazon.com/ec2/) and its subservices [Elastic Block Store (EBS)](http://aws.amazon.com/ebs/), [Elastic Load Balancing (ELB)](http://aws.amazon.com/elasticloadbalancing/) and [Auto Scaling](http://aws.amazon.com/autoscaling/)
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
- ~20 different *instance types* that vary in computing power and memory
- Instance is created from an *Amazon Machine Image (AMI)*

--

![AWS Region map](/images/aws_map_regions.png)

Regions, Availability Zones (AZ) and CDN Edge Locations

Notes: Regions: Frankfurt, Ireland, US East (N. Virginia), US West (N. California), US West (Oregon), South America (Sao Paulo), Tokyo, Singapore, Sydney. Special regions are **GovCloud** and **Beijing**.

--

## Networking in AWS

- Security groups
- More detailed IP subnetting with [Virtual Private Cloud (VPC)](http://aws.amazon.com/vpc/)


--

## Exercise: Launch an instance

1. Log-in to [gofore-crew.signin.aws.amazon.com/console](https://gofore-crew.signin.aws.amazon.com/console) and go to EC2 to launch an instance
2. Pass a shell script as [*User Data*](http://docs.aws.amazon.com/AWSEC2/latest/UserGuide/user-data.html) (configure instance -> advanced)
3. `ssh -i your_key.pem ubuntu@instance_hostname`
4. `curl http://169.254.169.254/latest/user-data/`
5. `curl http://169.254.169.254/latest/meta-data/`

---

# Objective of today

--

![Workshop application architecture](/images/aws_workshop_arch.png)

--

![Web hosting reference architecture](/images/aws_reference_architecture_web_hosting.png)

[aws.amazon.com/architecture](http://aws.amazon.com/architecture/)

--

## Best practices

- Split the application into small, stateless, horizontally-scalable services
- Loosely couple services with queues and load balancers
- Automate infrastructure setup and application deployment

---

# Programmability

--

## Programmability

SDKs and command-line tools [aws.amazon.com/tools](http://aws.amazon.com/tools/)

Java SDK [aws.amazon.com/sdk-for-java](http://aws.amazon.com/sdk-for-java/)

Ansible: [docs.ansible.com/list_of_cloud_modules.html](http://docs.ansible.com/list_of_cloud_modules.html)

Boto for Python: [github.com/boto/boto](https://github.com/boto/boto) & [docs.pythonboto.org](http://docs.pythonboto.org/)

--

## [Identity and Access Management (IAM)](http://aws.amazon.com/iam/)

For programming, we need IAM credentials. You can state fine-grained access policies.

You should not pass credentials into instances. Use IAM role instead.

--

## [CloudFormation](http://aws.amazon.com/cloudformation/)

Create resources from a template. The collection of created resources is called a *stack*.

Enables versioning of infrastructure.

--

## Structure of a template

<pre><code data-trim="" class="json">
{
  "AWSTemplateFormatVersion": "2010-09-09",
  "Description": "Structure of a cloudformation template",

  "Resources": { ...Set of resources to be created... },

  "Parameters": { ...Set of parameters given when creating the stack... },
  "Outputs": { ...Set of outputs variables... },

  "Mappings": { ...Set of helper mappings... },
  "Conditions": { ...Set of conditions... }
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

## Simple Queue Service (SQS)

--

## Exercise: Create a stack

<pre><code data-trim="" class="ruby">
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
2. Complete programming [tasks #1 and #2](https://github.com/gofore/aws-training/tree/master/workshop/complete)
3. Run the `aws-workshop-loader` locally

--

## Handling SQS messages

[com.gofore.aws.workshop.common.sqs.SqsService.java](https://github.com/gofore/aws-training/blob/master/workshop/complete/aws-workshop-common/src/main/java/com/gofore/aws/workshop/common/sqs/SqsService.java)

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

---

# [Simple Storage Service (S3)](http://aws.amazon.com/s3/)

--

Store blob objects into *buckets* over an RESTful interface.

Supports object [versioning](http://docs.aws.amazon.com/AmazonS3/latest/dev/Versioning.html) and easy archiving to [Glacier](http://aws.amazon.com/glacier/).

--

### Let's take a look at S3 from the management console

--

## Exercise: Put objects to S3

Complete programming [task #3](https://github.com/gofore/aws-training/tree/master/workshop/complete)

---

# SimpleDB

--

### Let's take a look at SimpleDB

- sdbtool (Firefox plugin) [code.google.com/p/sdbtool/](https://code.google.com/p/sdbtool/)
- SdbNavigator (Chrome plugin) [www.kingsquare.nl/sdbnavigator](http://www.kingsquare.nl/sdbnavigator)

--

## Exercise: Put attributes to SimpleDB

Complete programming [task #4](https://github.com/gofore/aws-training/tree/master/workshop/complete)

--

## Exercise: SimpleDB query

Complete programming [task #5](https://github.com/gofore/aws-training/tree/master/workshop/complete)

---

# Auto Scaling

--

Elastic Load balancer, Launch configuration, ASG and Scaling policy

Indirectly uses SNS and CloudWatch

http://docs.aws.amazon.com/AutoScaling/latest/DeveloperGuide/Cooldown.html

--

![Auto Scaling Group Lifecycle](http://docs.aws.amazon.com/AutoScaling/latest/DeveloperGuide/images/as-lifecycle-basic-diagram.png)

[Auto Scaling Group Lifecycle](http://docs.aws.amazon.com/AutoScaling/latest/DeveloperGuide/AutoScalingGroupLifecycle.html)

--

## Exercise: Deploy your .jar files to S3

1. `mvn deploy`
2. Verify that you can find your files from the management console

--

## Complete CloudFormation template

[infrastructure-complete.template](https://github.com/gofore/aws-training/blob/master/workshop/complete/deploy/cloudformation-templates/infrastructure-complete.template)

--

## Exercise: Deploy complete stack

1. Create a stack with `create_complete_infrastructure.yml`
2. Look at CloudFormation -> Stack -> Events until complete
3. Look at EC2 -> Load Balancers -> Instances until InService
4. Check your e-mail and subscribe to the notifications
5. Try out the application
6. Try to make the aws-workshop-fetchers scale out
7. Terminate instances from an auto scaling group and see what happens

---

# Summary

--

![Workshop application architecture](/images/aws_workshop_arch.png)

--

## Things that we did right

- Using IAM user accounts instead of sharing the root account
- Using IAM roles on the instances
- Load-balancing, auto-scaling and elasticity
- Loose coupling with SQS and ELB
- CloudFormation and bootstrapping with userdata and ansible

--

## Things that we *should* have done

- Set up a VPC and subnetting, proper security groups
- Distribute credentials by a secure path

--

## Things that we *could* have done

- Distribute content from CloudFront CDN
- Use a more supported(?) database than SimpleDB
- [AWS::CloudFormation::Init](http://docs.aws.amazon.com/AWSCloudFormation/latest/UserGuide/aws-resource-init.html)

--

## What next?

- Reference architectures [aws.amazon.com/architecture](http://aws.amazon.com/architecture/)
- Whitepapers [aws.amazon.com/whitepapers](aws.amazon.com/whitepapers)

- Register to [Amazon Partner Network Portal](https://www.apn-portal.com/home/home.jsp) and take the AWS Technical Professional or AWS Business Professional course
