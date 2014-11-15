Complete version of the demo application. This will be constructed piece by piece during the workshop.

### Task 1: SQS request

The UI polls for SQS queue attributes so that the user can see what's happening in the queues. The implementation can be found at `com.gofore.aws.workshop.ui.rest.QueueAttributesResource` and your task is to create the request and return the `GetQueueAttributesResult` for the UI.

Hint: See `com.amazonaws.services.sqs.AmazonSQS#getQueueAttributes()` javadoc for help.


### Task 2: SQS message send

The UI can be used to publish new queries to the crawler engine. The resource `com.gofore.aws.workshop.ui.rest.QueriesResource` is responsible for receiving the queries. Your task is to send the SQS message and return the `SendMessageResult` for the UI.

Hint: See `com.amazonaws.services.sqs.AmazonSQS#sendMessage()` javadoc for help.


### Task n: SimpleDB query

Implement SimpleDB search by the simplified full text word mapping of original image description. All relevant code can be found at `com.gofore.aws.workshop.ui.rest.SearchResource`. Words should have been stored in the image metadata domain under the attribute `term`. Creating the main select clause (`SELECT_TEMPLATE`) and term where condition (`TERM_WHERE_TEMPLATE`) templates should be enough to solve the task. See the implementation for hints how to build the templates. You can always roll your own search implementation if you want to.

Hint: SimpleDB query syntax is quite close to SQL.
