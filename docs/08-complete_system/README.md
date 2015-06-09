
## Complete system
### AWS Training Module 8

---

## Agenda

1. **Introduction:** Main services and exercise description
2. **Loose coupling**: Separating services with queues
3. **Persistence:** Object storage and database
4. **Elasticity:** Scaling resources based on demand

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


---

# Simple deployment pipeline

--

![Deployment pipeline](/images/deployment_pipeline.png)

--

## Exercise: Deploy your .jar files to S3

1. `mvn deploy`
2. Verify that you can find your files from the management console

---



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

![Workshop application architecture](/images/aws_workshop_arch_4.png)

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

# Summary


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

## Things that we *could* have done

- Use proper microservice architecture, no shared usage of resources
- Distribute static content from [CloudFront CDN](http://aws.amazon.com/cloudfront/)
- Use a more supported(?) database than SimpleDB
- [AWS::CloudFormation::Init](http://docs.aws.amazon.com/AWSCloudFormation/latest/UserGuide/aws-resource-init.html)
- Build golden image / [Docker](https://www.docker.com/) container to provide fully immutable instances
