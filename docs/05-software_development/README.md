
## Software development with AWS
### AWS Training Module 5

---

## Agenda

- API and CLI
- SDK with exercises
- Deployment pipelines

--

## What you will need

- Laptop with Internet connectivity
- [Vagrant](https://www.vagrantup.com/) (and [Virtualbox](https://www.virtualbox.org/)) is recommended
- Optionally Java 8 SDK, Maven 3+ and an IDE
- Material is available at [github.com/gofore/aws-training](https://github.com/gofore/aws-training)

---

## AWS API

- REST API for infrastructure and resource management
- Authorized by access key or instance role

--

## AWS CLI

- [aws-cli](https://github.com/aws/aws-cli) and numerous community created tools
- Alternative to web console
- Automate tasks and create helper scripts
- More lightweight approach than full SDK

---

## SDKs and tools for everyone

- Java SDK: [aws.amazon.com/sdk-for-java](http://aws.amazon.com/sdk-for-java/)
- Ansible: [docs.ansible.com/list_of_cloud_modules.html](http://docs.ansible.com/list_of_cloud_modules.html)
- Boto for Python: [github.com/boto/boto](https://github.com/boto/boto)
- Other SDKs and command-line tools: [aws.amazon.com/tools](http://aws.amazon.com/tools/)

--

## Java SDK

- Collection of libraries to operate AWS resources in Java
  - since version 1.9 the libraries are split in submodules for more fine grained dependency management
  - available in maven central
- New object oriented resource APIs are [under development](https://github.com/awslabs/aws-sdk-java-resources)
- For complex resource management with the SDK see [Netflix Asgard](https://github.com/Netflix/asgard) (...but it's coded in Groovy and Grails)

--

## Using IAM credentials with SDK

SDKs support credentials provider chain, including [Java SDK](https://github.com/gofore/aws-training/blob/master/workshop/complete/aws-workshop-common/src/main/java/com/gofore/aws/workshop/common/di/AwsModule.java#L58-64).

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

## Programming exercises

- [Initial](https://github.com/gofore/aws-training/tree/master/workshop/initial) project contains programming tasks utilizing the Java SDK

---

## Simple deployment pipeline

![Deployment pipeline](/images/deployment_pipeline.png)

--

## Building with legos

![Build process](/images/netflix_build_legos.png)

[Netflix tech blog: Building with Legos](http://techblog.netflix.com/2011/08/building-with-legos.html)

--

## AMI models

![AMI models](/images/ami-models.png)

[Cloud-Powered CI and deployment](http://www.slideshare.net/AmazonWebServices/cloudpowered-continuous-integration-and-deployment-architectures-jinesh-varia)
