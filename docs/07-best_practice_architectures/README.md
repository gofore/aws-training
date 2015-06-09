
## Best practice architectures
### AWS Training Module 6

---

## Agenda

- Loose coupling
- Ephemeral and automated instances
- Horizontally scalable stateless applications
- Reference architecture

--

## Designing cloud-friendly systems

- Split the system into small, **stateless**, horizontally scalable services
- **Loosely couple** services with queues, load balancers and service discovery
- **Automate** infrastructure setup and application deployment

---

## Abstract resources

- Abstract hardware out of everywhere
- All you see is APIs and resource endpoint URIs
- Use managed services instead of DIY solutions

--

## Clusters of resources

- Not a single resource, but n resources of same type
- Access clusters via load balancers and auto scaling groups
- Attach to named clusters instead of specific nodes
- Offers resiliency

--

## Glue with async messaging

- *'Everything fails all the time'* -Werner Wogels
- Parts of the system are glued together with async messaging
- SQS and SNS provide key resources for loose coupling

---

## Ephemeral instances

- Instances are short lived and hold no storage
- Pets vs Cattle
- Any instance can be replaced or removed at any time
- [Simian Army](http://techblog.netflix.com/2011/07/netflix-simian-army.html)

--

## Automate everything

- Infrastructure as code, automatic provisioning and automatic deployment
- Prerequisites for proper autoscaling
- Provides repeatable and versioned infrastructure

---

## Cloud compatible software architecture

- [12factor.net](http://12factor.net/) provides some design principles for cloud-friendly apps
- [Cloud Design Patterns](http://download.microsoft.com/download/B/B/6/BB69622C-AB5D-4D5F-9A12-B81B952C1169/CloudDesignPatternsBook-PDF.pdf) offers architecture guidance for cloud applications
- [Reactive Manifesto](http://www.reactivemanifesto.org/) is the cloud manifesto

--

## Scale in and out

- Scaling in and out offers better resource and cost optimization
- Requires token based authentication and central session storage
- Sticky sessions result inflexible scaling
- Dynamic scaling based on different metrics or schedules

---

## Reference architecture

![Web hosting reference architecture](/images/aws_reference_architecture_web_hosting.png)

[aws.amazon.com/architecture](http://aws.amazon.com/architecture/)

[AWS best practices](http://media.amazonwebservices.com/AWS_Cloud_Best_Practices.pdf)
