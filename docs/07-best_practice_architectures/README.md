
## Best practice architectures
### AWS Training Module 6

---

## Agenda

- Loose coupling
- Ephemeral and automated instances
- Horizontally scalable stateless applications
- Reference architecture

--

## Designing a cloud-friendly application

- Split the application into small, **stateless**, horizontally scalable services
- **Loosely couple** services with queues, load balancers and service discovery
- **Automate** infrastructure setup and application deployment
- [12factor.net](http://12factor.net/) provides some design principles for cloud-friendly apps

---

## Abstract resources

- Abstract hardware out of everywhere
- All you see is APIs and resource endpoint URIs
- Use managed services instead of DIY solutions
-

--

## Clusters of resources

- Not a single resource, but n resources of same type
- Access clusters via load balancers and auto scaling groups
- Bind to

--

## Glue with async messaging

- Parts of system are glued together with async messaging
- SQS and SNS provide

---

## Ephemeral instances

- Instances are short lived and hold no storage
-

--

## Automate everything

- Infrastructure as code, automatic provisioning and automatic deployment
- Prerequisites for proper autoscaling
- Provides repeatable and versioned infrastructure

---

## Cloud compatible software architecture

--

## Scale out

- Scaling out offers better resource and cost optimization
-

---

## Reference architecture

![Web hosting reference architecture](/images/aws_reference_architecture_web_hosting.png)

[aws.amazon.com/architecture](http://aws.amazon.com/architecture/)
