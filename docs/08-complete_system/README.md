
## Complete system
### AWS Training Module 8

---

## Agenda

- Design cloud compatible system
- Reference system architecture
- Complete stack deployment and inspection
- Summary

---

## Content PowerMonger

- Crawls images and metadata from websites and offers search engine and UI on top of the data
- The UI scales for thousands of concurrent users
- Users can supply queries or websites to crawl
- Queries or websites are transformed into one or more web page crawl tasks
- Web page crawl tasks are handled by scalable crawler nodes to keep content delivery latencies low
- Images and content metadata are extracted from web pages and stored into datastores
- Content is delivered efficiently to users all around the world
- Uses central logging and gathering of interesting system events

--

## Exercise: Design AWS architecture for Content PowerMonger

- In groups of few people make up architecture following AWS best practices

---

## Reference system architecture

- Crawls images from Google image search from user provided queries
- Offers simple search engine on top of image metadata
- Loosely coupled microservices architecture of UI, loader and fetcher services
- S3 based thumbnail store and SimpleDB as content metadata database
- Central logging via SQS

--

![Workshop application architecture](/images/aws_workshop_arch_2_no_alarms.png)

--

![Workshop application architecture](/images/aws_workshop_arch_4.png)

---

## Complete CloudFormation template

[infrastructure-complete.template](https://github.com/gofore/aws-training/blob/master/workshop/complete/deploy/cloudformation-templates/infrastructure-complete.template)

--

## Exercise: Deploy complete stack

1. Create a stack with `create_complete_infrastructure.yml`
2. Look at CloudFormation -> Stack -> Events until complete
3. Look at EC2 -> Load Balancers -> Instances until InService
4. Check your e-mail and subscribe to the notifications
5. Try out the application
6. Try to make the aws-workshop-fetchers and aws-workshop-uis scale out
7. Terminate instances from an auto scaling group and see what happens

---

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

Notes: Currently new ui-instances that are born with autoscale pull the latest jar from S3. This means that the "launch configuration" is not idemponent, and every autoscaled instance might be different.

--

## Things that we *could* have done

- Use proper microservice architecture, no shared usage of resources
- Distribute static content from [CloudFront CDN](http://aws.amazon.com/cloudfront/)
- Use a more supported(?) database than SimpleDB
- [AWS::CloudFormation::Init](http://docs.aws.amazon.com/AWSCloudFormation/latest/UserGuide/aws-resource-init.html)
- Build golden image / [Docker](https://www.docker.com/) container to provide fully immutable instances
