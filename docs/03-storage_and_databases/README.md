
## Storage and databases
### AWS Training Module 3

---

## Agenda

- Databases
- Object Storage

---

# Databases

--

## Plenty of database services

- [Relational Database Service (RDS)](http://aws.amazon.com/rds/)
- Non-relational [DynamoDB](http://aws.amazon.com/dynamodb/)
- [Redshift](http://aws.amazon.com/redshift/) petabyte-scale data warehouse
- [ElastiCache](http://aws.amazon.com/elasticache/) in-memory caching
- ...Oh, and [SimpleDB](http://aws.amazon.com/simpledb/)

--

## [Relational Database Service (RDS)](http://aws.amazon.com/rds/)

- MySQL, PostgreSQL, Oracle, Microsoft SQL Server, AWS Aurora
- Auto-upgrade, Auto-backup (with maintenance window of your choice)
- Optional Multi-AZ deployment with automatic failover
- [Cost](http://aws.amazon.com/rds/pricing/) consists of instance hour, provisioned storage and IOPS, Multi-AZ, backups and data transfer

--

## Demo: Database under 5 minutes

---

# Object Storage

--

## [Simple Storage Service (S3)](http://aws.amazon.com/s3/)

- Store key-value blob objects into [*buckets*](http://docs.aws.amazon.com/AmazonS3/latest/dev/UsingBucket.html) over an RESTful interface with eventual consistency
- Fine-grained [access control mechanisms](http://docs.aws.amazon.com/AmazonS3/latest/dev/access-control-overview.html) (IAM [user policies](http://docs.aws.amazon.com/AmazonS3/latest/dev/example-policies-s3.html), [bucket policies](http://docs.aws.amazon.com/AmazonS3/latest/dev/example-bucket-policies.html), [Access Control Lists (ACLs)](http://docs.aws.amazon.com/AmazonS3/latest/dev/S3_ACLs_UsingACLs.html) and query string authentication)
- Optional server-side encryption
- Supports static hosting of web resources
- Object lifecycle management with [versioning](http://docs.aws.amazon.com/AmazonS3/latest/dev/Versioning.html) and archiving to [Glacier](http://aws.amazon.com/glacier/)

--

## S3 [pricing](http://aws.amazon.com/s3/pricing/) and SLA

- 99.99% availability, 99.999999999% durability
- Pricing consists of data stored and requests made
- Standard storage, [*Reduced Redundancy*](http://aws.amazon.com/s3/details/) (99.99%) and Glacier affect pricing

--

## Exercise: Host files from S3

1. Create an S3 bucket and upload a file into it
2. View the file in S3 and try to access its URL
3. Add an ACL that makes the file publicly accessible


---

# Recap

### M2 and M3

--

![How would you improve the following architecture?](/images/improvable_architecture.png)

How would you improve the following architecture?
