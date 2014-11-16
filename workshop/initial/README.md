Complete version of the demo application. This will be constructed piece by piece during the workshop.

### Task 1: SQS request

The UI polls for SQS queue attributes so that the user can see what's happening in the queues. The implementation can be found at `com.gofore.aws.workshop.ui.rest.QueueAttributesResource` and your task is to create the request and return the `GetQueueAttributesResult` for the UI.

Hint: See `com.amazonaws.services.sqs.AmazonSQS#getQueueAttributes()` javadoc for help.


### Task 2: SQS message send

The UI can be used to publish new queries to the crawler engine. The resource `com.gofore.aws.workshop.ui.rest.QueriesResource` is responsible for receiving the queries. Your task is to send the SQS message and return the `SendMessageResult` for the UI.

Hint: See `com.amazonaws.services.sqs.AmazonSQS#sendMessage()` javadoc for help.


### Task 3: Put object to S3

The crawler engine reads image content from web page and stores the thumbnail image to S3 so it will be visible for the UI. Thumbnail upload to S3 is implemented in `com.gofore.aws.workshop.fetcher.images.ThumbnailUploader`. Your task is to fill the gaps and create object metadata and the put request from the original HTTP image entity. You should return `CompletableFuture` of `PutObjectResult` to allow the image processing tasks to run in parallel.

Hint: See `com.amazonaws.services.s3.AmazonS3#putObject()` javadoc for help and remember the object permissions.


### Task 4: Put attributes to SimpleDB

The image metadata is stored to SimpleDB. It consists of thumbnail url (S3 url) `thumbnailUrl`, original image url `imageUrl`, description `description` and array of words `term` parsed from description to allow reasonable search. Your task is to map the attributes to the item and create the put request. You should return `CompletableFuture` of `Void` to allow the image processing tasks to run in parallel.

Hint: See `com.amazonaws.services.simpledb.AmazonSimpleDB#putAttributes()` and `com.amazonaws.services.simpledb.model.ReplaceableAttribute` javadoc for help. You should use `com.gofore.aws.workshop.fetcher.utils.TermsParser` to create the words from `description`.


### Task 5: SimpleDB query

Implement SimpleDB search by the simplified full text word mapping of original image description. All relevant code can be found at `com.gofore.aws.workshop.ui.rest.SearchResource`. Words should have been stored in the image metadata domain under the attribute `term`. Creating the main select clause (`SELECT_TEMPLATE`) and term where condition (`TERM_WHERE_TEMPLATE`) templates should be enough to solve the task. See the implementation for hints how to build the templates. You can always roll your own search implementation if you want to.

Hint: SimpleDB query syntax is quite close to SQL.
