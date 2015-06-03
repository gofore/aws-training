---

# SimpleDB

--

## Yes, it is **Simple**

- Performant NoSQL database to store flat attributes within an item
  - all attributes are indexed automatically
- Supports **only string** data type!
  - sorting, numbers, dates, etc. are not trivial
- Hard limits for storage
  - 256 total attribute name-value pairs per item
  - one billion attributes per domain
  - 10 GB of total user data storage per domain
- Very limited SQL-like query syntax

--

## Exercise: Put attributes to SimpleDB

1. Complete [programming task #4](https://github.com/gofore/aws-training/tree/master/workshop/initial#task-4-put-attributes-to-simpledb)  
   See `com.gofore.aws.workshop.fetcher.images.MetadataRepository`

--

### Let's take a look into SimpleDB

- sdbtool (Firefox plugin) [code.google.com/p/sdbtool/](https://code.google.com/p/sdbtool/)
- SdbNavigator (Chrome plugin) [www.kingsquare.nl/sdbnavigator](http://www.kingsquare.nl/sdbnavigator)

--

## Exercise: SimpleDB query

1. Complete [programming task #5](https://github.com/gofore/aws-training/tree/master/workshop/complete#task-5-simpledb-query)  
   See `com.gofore.aws.workshop.ui.rest.SearchResource`


---


## Exercise: Put objects to S3

1. Complete [programming task #3](https://github.com/gofore/aws-training/tree/master/workshop/initial#task-3-put-object-to-s3)  
   See `com.gofore.aws.workshop.fetcher.images.ThumbnailUploader`

Notes: Content length must be known when putting files to S3. This might become an issue when streaming content straight to S3.

--

### Public HTTP access for S3 objects

1. Submit a query and watch how it gets processed within the application
2. Find your files in S3, try to access their URL
3. In your code, add an Access Control List (ACL) to fix the object permissions if you haven't already (see hint in task 3)
4. Try to access your files again

Notes: A [Canned ACL](http://docs.aws.amazon.com/AmazonS3/latest/dev/acl-overview.html#CannedACL) is a predefined ACL, but it is possible to create a fully customized ACL.
