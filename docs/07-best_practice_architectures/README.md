
## Best practice architectures
### AWS Training Module 6

---

## Agenda

1. **Introduction:** Main services and exercise description
2. **Loose coupling**: Separating services with queues
3. **Persistence:** Object storage and database
4. **Elasticity:** Scaling resources based on demand

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

