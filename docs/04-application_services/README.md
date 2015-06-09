
## Application Services
### AWS Training Module 4

---

## Agenda

- Administrative tools
- Queues and notifications
- Misc bits

---

# Administrative tools

--

## Administrative tools

- [CloudTrail](http://aws.amazon.com/cloudtrail/)
- [Trusted Advisor](https://aws.amazon.com/premiumsupport/trustedadvisor/)

---

# Queues and notifications

--

## Queues and notifications

- [Simple Queue Service (SQS)](http://aws.amazon.com/sqs/)
- [Simple Notification Service (SNS)](http://aws.amazon.com/sns/)
- [Simple Workflow Service (SWF)](http://aws.amazon.com/swf/)

--

## Loose coupling with Queues

--

## [Simple Queue Service (SQS)](http://aws.amazon.com/sqs/)

- Message queue with simple REST API
- Scalable, reliable and persistent
- Does not rely on any existing standard (JMS, AMQP)
- Lots of gotchas and need to knows

--

## SQS usage

- Messages must be polled from the REST API
  - supports long polling but by default releases immediately if there are no messages
- Successful message receive triggers invisibility period of a message
  - no transactions, automatic "rollback" if the message is not deleted within the invisibility period
- Messages are sent to the REST API
  - 256kB payload limit, should use handle to S3 stored data for bigger payloads
  - maximum message retention period is 14 days

---

# Misc bits

--

- [Simple Email Service (SES)](http://aws.amazon.com/ses/) for sending outbound bulk mail
- [Elastic MapReduce (EMR)](http://aws.amazon.com/elasticmapreduce/) Hadoop and Spark as a service
- [Kinesis](http://aws.amazon.com/kinesis/) real-time processing of data streams
- [Elastic Transcoder](http://aws.amazon.com/elastictranscoder/) media transcoding service
- [CloudSearch](http://aws.amazon.com/cloudsearch/) Solr search engine as a service

--

![List of AWS Services](/images/aws_list_of_services.png)

[AWS Services](http://aws.amazon.com/products/)
