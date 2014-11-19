# Workshop Demo Application and Infrastructure as Code

The workshop demo consists of a Java application that is a super crappy version of google image search utilizing
microservice architecture and CloudFormation powered infrastructure templates.

See the [Getting Started](/docs/getting_started.md) guide on how to set up your tools and environment.

## Application architecture

The Java application includes three modules (micro services):
- **UI** (aws-workshop-ui)
  The UI is AngularJS powered frontend service that provides the minimal UI that is required to see the all the features
  of the application in work.
- **Loader** (aws-workshop-loader)
  The loader service is responsible for taking in the initial google image search queries and fire up the real
  image crawlers that will populate the metadata and thumbnail storages.
- **Fetcher** (aws-workshop-fetcher)
  The fetcher service handles all the hard lifting from parsing the images to storing the thumbnails to S3 and image
  metadata to simpledb.

![Workshop application architecture](/images/aws_workshop_arch.png)
