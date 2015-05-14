
## Application Services
### AWS Training Module 4

---

## Agenda


---

# Loose coupling with Queues

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

## Exercise: Programming with SQS

1. Compile and run the `aws-workshop-ui` locally
2. Complete [programming task #1](https://github.com/gofore/aws-training/tree/master/workshop/initial#task-1-sqs-request)  
   See `com.gofore.aws.workshop.ui.rest.QueueAttributesResource`
3. Complete [programming task #2](https://github.com/gofore/aws-training/tree/master/workshop/initial#task-2-sqs-message-send)  
   See `com.gofore.aws.workshop.ui.rest.QueriesResource`
4. View your messages in the queues from the management console
5. Run the `aws-workshop-loader` locally

Notes: If you use Vagrant, you can access the web app at http://10.10.10.10:9001 on your host machine.

--

## Handling SQS messages

[com.gofore.aws.workshop.common.sqs.SqsService.java](https://github.com/gofore/aws-training/blob/master/workshop/initial/aws-workshop-common/src/main/java/com/gofore/aws/workshop/common/sqs/SqsService.java)

<pre><code data-trim="" class="java">
public class SqsService extends Service {
    public SqsService(SqsClient sqsClient, String queueUrl) {}
    public SqsService addMessageHandler(Function&lt;Message, CompletableFuture&lt;Message&gt;&gt; messageHandler) {}
    public SqsService setCompleteHandler(BiConsumer&lt;? super Message, ? super Throwable&gt; completeHandler) {}
    public synchronized void start() throws Exception {}
    public synchronized void stop() throws Exception {}
    protected CompletableFuture&lt;Message&gt; handleMessage(Message message) {}
    protected CompletableFuture&lt;Message&gt; completeMessage(CompletableFuture&lt;Message&gt; message) {}
    protected CompletableFuture&lt;Message&gt; deleteMessage(CompletableFuture&lt;Message&gt; message) {}
}
</code></pre>

