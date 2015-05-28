
## Compute
### AWS Training Module 2

---

## Agenda

- EC2
- IAM
- VPC
- ELB, ASG

--

## Prerequisites

- Browser and Internet access
- SSH client (e.g. [Putty](http://www.chiark.greenend.org.uk/~sgtatham/putty/download.html) on Windows)

---

# Elastic Compute Cloud

--

## [Elastic Compute Cloud (EC2)](http://aws.amazon.com/ec2/)

- One of the core services of AWS
- Virtual machines (or *instances*) as a service
- Dozens of *instance types* that vary in performance and cost
- Instance is created from an *Amazon Machine Image (AMI)*, which in turn can be created again from instances

Notes: Usage is billed per *instance-hour* for running instances. Prices vary based on region, instance type, and operating system. Purchasing options include *On-Demand Instances*, *Reserved Instances*, *Spot Instances*

--

![AWS Region map](/images/aws_map_regions.png)

[Regions](http://docs.aws.amazon.com/AWSEC2/latest/UserGuide/using-regions-availability-zones.html) and CDN [Edge Locations](http://aws.amazon.com/about-aws/global-infrastructure/)

Notes: Regions: Frankfurt, Ireland, US East (N. Virginia), US West (N. California), US West (Oregon), South America (Sao Paulo), Tokyo, Singapore, Sydney. Special regions are **GovCloud** and **Beijing**.

--

![AWS EU Region map](/images/aws_map_regions_eu.png)

[Regions and Availability Zones (AZ)](http://docs.aws.amazon.com/AWSEC2/latest/UserGuide/using-regions-availability-zones.html)

Notes: We will only use Ireland (eu-west-1) region in this workshop. See also [A Rare Peek Into The Massive Scale of AWS](http://www.enterprisetech.com/2014/11/14/rare-peek-massive-scale-aws/).

--

## Networking in AWS

- Regions and availability zones
- *Security groups* provide port-level firewalls to instances
- More detailed IP subnetting with [Virtual Private Cloud (VPC)](http://aws.amazon.com/vpc/)

--

## Exercise: Launch an EC2 instance

1. Log-in to [gofore-crew.signin.aws.amazon.com/console](https://gofore-crew.signin.aws.amazon.com/console)
2. Switch to Ireland region and go to EC2 dashboard
3. Launch a new EC2 instance according to instructions
4. During *"Step 3: Configure Instance Details"*, pass a shell script as [*User Data*](http://docs.aws.amazon.com/AWSEC2/latest/UserGuide/user-data.html) under Advanced

--

<pre><code data-trim="" class="shell">
#!/bin/sh
touch /new_empty_file_we_created.txt
echo "It works!" > /it_works.txt
</code></pre>

--

## Exercise: Launch an EC2 instance

SSH into the instance (find the IP address in the EC2 console)

    # Putty users must convert key to .ppk
    ssh -i your_ssh_key.pem ubuntu@instance_ip_address

View instance metadata

    curl http://169.254.169.254/latest/meta-data/

View your [*User Data*](http://docs.aws.amazon.com/AWSEC2/latest/UserGuide/user-data.html) and find the changes your script made

    curl http://169.254.169.254/latest/user-data/
    ls -la /


Notes: You will have to reduce keyfile permissions `chmod og-xrw mykeyfile.pem`. If you are on Windows and use Putty, you will have to convert the .pem key to .ppk key using [puttygen](http://www.chiark.greenend.org.uk/~sgtatham/putty/download.html)

---

# Identity and Access Management

--

## [Identity and Access Management (IAM)](http://aws.amazon.com/iam/)

- Manage AWS user [*credentials*](http://docs.aws.amazon.com/IAM/latest/UserGuide/Using_ManagingLogins.html) for Web console and API access
- Fine-grained access [*policies*](http://docs.aws.amazon.com/IAM/latest/UserGuide/policies.html) to services and resources
- Provide temporary access with [*roles*](http://docs.aws.amazon.com/IAM/latest/UserGuide/roles-toplevel.html)

Notes: Always use roles, do not store credentials inside the instances, or [something bad](http://www.browserstack.com/attack-and-downtime-on-9-November) might happen.

--

## Users on many levels

Imagine running a discussion board in EC2. On how many different levels you might have user accounts?


---

# Virtual Private Cloud

--

## [Virtual Private Cloud (VPC)](http://docs.aws.amazon.com/AmazonVPC/latest/UserGuide/VPC_Introduction.html)

- Heavy-weight virtual IP networking for EC2 and RDS instances. Integral part of AWS, all instances are launched into VPCs
- An AWS root account can have many VPCs, each in a specific region
- Each VPC is divided into [*subnets*](http://docs.aws.amazon.com/AmazonVPC/latest/UserGuide/VPC_Subnets.html). Each subnet is in a specific availability zone
- Each instance connects to a subnet with a Elastic Network Interface

--

![VPC with Public and Private Subnets](http://docs.aws.amazon.com/AmazonVPC/latest/UserGuide/images/nat-instance-diagram.png)

[VPC with Public and Private Subnets](http://docs.aws.amazon.com/AmazonVPC/latest/UserGuide/VPC_Scenario2.html)

--

![VPC with Public and Private Subnets and Hardware VPN Access](http://docs.aws.amazon.com/AmazonVPC/latest/UserGuide/images/Case3_Diagram.png)

[VPC with Public and Private Subnets and Hardware VPN Access](http://docs.aws.amazon.com/AmazonVPC/latest/UserGuide/VPC_Scenario3.html)

--

## Recap

- Instance and Elastic Network Interface
- Region and Availability Zone
- VPC and Subnet
- ACL and Security Group
- Internet Gateway, Virtual Private Gateway, NAT instance


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



## Exercise: Monkey time!

![Chaos Monkey](/images/netflix-chaos-monkey.jpg)

Be a [Chaos Monkey](https://github.com/Netflix/SimianArmy/wiki/Chaos-Monkey): terminate all of your `aws-workshop-ui` instances!
