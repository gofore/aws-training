
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
- Auto-upgrade, Auto-backup, Highly available

--

## Demo: Database under 5 minutes

---

# Object Storage

--

## [Simple Storage Service (S3)](http://aws.amazon.com/s3/)

- Store key-value blob objects into [*buckets*](http://docs.aws.amazon.com/AmazonS3/latest/dev/UsingBucket.html) over an RESTful interface
- Fine-grained access control mechanisms (IAM policies, bucket policies, Access Control Lists (ACLs) and query string authentication)
- Supports static hosting of web resources
- Supports object [versioning](http://docs.aws.amazon.com/AmazonS3/latest/dev/Versioning.html) and easy archiving to [Glacier](http://aws.amazon.com/glacier/)

--

## S3 [pricing](http://aws.amazon.com/s3/pricing/) and SLA

- 99.99% availability, 99.999999999% durability
- Pricing consists of data stored and requests made
- Standard storage, reduced redundancy and glacier affect pricing

--

## Exercise: Host files from S3

1. Create an S3 bucket and upload files into it
2. View the files in S3 and try to access their URL
3. Fix permissions so that the files are publicly accessible
