
# Amazon Web Services
## Workshop (mainly) for developers

---

## Agenda

- **Introduction to AWS:** Launching resources manually
- **Programmability:** API and SDKs
- Your first basic Java App
- **Elasticity:** Load balancer and auto-scaling
- **Loose coupling:** Connecting services with SQS queues

--

## What you will need

- Laptop with an IDE and JDK

---

# Amazon Web Services

--

![List of AWS Services](/img/aws_list_of_services.png)

AWS Services

---

# Elastic Compute Cloud
## ![EC2 icon](/img/aws_icon_ec2.svg)

--

## Elastic Compute Cloud (EC2)

- Core service
- Virtual machines (or *instances*) as a service
- ~20 different *instance types* that vary in computing power and memory

--

![AWS Region map](/img/aws_map_regions.png)

Regions

Notes: Regions: Frankfurt, Ireland, US East (N. Virginia), US West (N. California), US West (Oregon), South America (Sao Paulo), Tokyo, Singapore, Sydney.

Special regions are **GovCloud** and **Beijing**.

--

## Regions

Each geographical *Region* is split into several *Availability Zones*. CDN content is distributed from *Edge Locations*.

European regions include Ireland (eu-west-1) and Frankfurt (eu-central-1).

--

## Security groups

Each instance must belong to a *security group*.

--

## Demo: Launching an instance

![AWS Management console](/img/aws_management_console.png)

--

## Pricing

Usage is billed per *instance-hour* for running instances.

Prices vary based on region, instance type, and operating system.

Purchasing options include *On-Demand Instances*, *Reserved Instances*, *Spot Instances*

---

# Programmability

--

## SDKs and tools

SDKs for Java, Python, Node.js, JavaScript etc.

Command-line tools

[aws.amazon.com/tools](http://aws.amazon.com/tools/)

--

## Boto for Python

<pre><code data-trim="" class="shell">
sudo apt-get install python-pip
sudo pip install boto
</code></pre>

<pre><code data-trim="" class="python">
#!/usr/bin/env python

import boto.cloudformation
cf = boto.cloudformation.connect_to_region("eu-west-1",
                                           aws_access_key_id="FOO",
                                           aws_secret_access_key="BAR")
cf.create_stack("Stack Name",
                template_url="https://s3-eu-west-1.amazonaws.com/../Sample.template",
                parameters=[("KeyName", "my-ssh-key"), ("InstanceType", "t2.micro")],
                tags={"project":"FooBar Project"},
                timeout_in_minutes=5)
</code></pre>

[github.com/boto/boto](https://github.com/boto/boto) | [docs.pythonboto.org](http://docs.pythonboto.org/)

--

## Java SDK

[aws.amazon.com/sdk-for-java](http://aws.amazon.com/sdk-for-java/)

---

# AWS-centric Application

--

![Web hosting reference architecture](/img/aws_reference_architecture_web_hosting.png)

[aws.amazon.com/architecture](http://aws.amazon.com/architecture/)
