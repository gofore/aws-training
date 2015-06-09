
## Software development with AWS
### AWS Training Module 5

---

## Agenda

- API
- SDK
- Deployment pipelines

---

--

## What you will need

- Laptop with Internet connectivity
- [Vagrant](https://www.vagrantup.com/) (and [Virtualbox](https://www.virtualbox.org/)) is recommended
- Optionally Java 8 SDK, Maven 3+ and an IDE
- Material is available at [github.com/gofore/aws-training](https://github.com/gofore/aws-training)

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

![Build process](/images/netflix_build_legos.png)

[Netflix tech blog: Building with Legos](http://techblog.netflix.com/2011/08/building-with-legos.html)

--

![AMI models](/images/ami-models.png)

[Cloud-Powered CI and deployment](http://www.slideshare.net/AmazonWebServices/cloudpowered-continuous-integration-and-deployment-architectures-jinesh-varia)
