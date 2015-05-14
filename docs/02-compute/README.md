
## Compute
### AWS Training Module 2

---

## Agenda

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



## Exercise: Monkey time!

![Chaos Monkey](/images/netflix-chaos-monkey.jpg)

Be a [Chaos Monkey](https://github.com/Netflix/SimianArmy/wiki/Chaos-Monkey): terminate all of your `aws-workshop-ui` instances!
