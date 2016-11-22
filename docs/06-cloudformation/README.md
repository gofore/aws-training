
## Infrastructure as code
### AWS Training Module 6

---

## Agenda

- Management & deployment tools
- CloudFormation

---

# Management

![Application management services](/images/aws_application_management.png)

---

# CloudFormation

--

## [CloudFormation](http://aws.amazon.com/cloudformation/)

- Create a [*stack*](http://docs.aws.amazon.com/AWSCloudFormation/latest/UserGuide/cfn-whatis-concepts.html) of resources from a [*template*](http://docs.aws.amazon.com/AWSCloudFormation/latest/UserGuide/cfn-whatis-concepts.html)
- Templates are written in JSON or YAML
- Template can define [*parameters*](http://docs.aws.amazon.com/AWSCloudFormation/latest/UserGuide/parameters-section-structure.html) that must be given during stack creation
- Reusable, versionable infrastructure description that can be commited to source control

Notes: [Template anatomy](http://docs.aws.amazon.com/AWSCloudFormation/latest/UserGuide/template-anatomy.html) | [Simple template](http://docs.aws.amazon.com/AWSCloudFormation/latest/UserGuide/example-templates-ec2-with-security-groups.html) | [Example snippets](http://docs.aws.amazon.com/AWSCloudFormation/latest/UserGuide/CHAP_TemplateQuickRef.html)

--

## Structure of a template

<pre><code data-trim="" class="json">
{
  "AWSTemplateFormatVersion": "2010-09-09",
  "Description": "This template constructs a single web server",

  "Parameters": {
    "MySSHKeyName": {"Type": "String", "MinLength": "1", "MaxLength": "255"}
  },
  "Resources": {
    "MyWebServer": {"Type": "AWS::EC2::Instance", "Properties": { ... }}
  },
  "Outputs": {
    "MyDomainName": {"Value": {"Fn::GetAtt": ["MyWebServer", "PublicDnsName"]}}
  },

  "Mappings": { ...Optional set of helper mappings... },
  "Conditions": { ...Optional set of conditions... }
}
</code></pre>

--

## Exercise: Create a stack

- Unique stack name
- Sample template: **Multi-AZ WordPress blog**
- Stack parameters: DBPassword at least 8 chars, KeyName, WebServerCapacity 2
- Tags: Add Name tag

--

## Advanced stack settings

- Notifications
- Timeout for stack creation
- Rollback on failure
- [Stack policy](https://docs.aws.amazon.com/AWSCloudFormation/latest/UserGuide/protect-stack-resources.html)

--

### Update, rollback, cancel and delete

- Update a stack: [*Update requires: Replacement*](http://docs.aws.amazon.com/AWSCloudFormation/latest/UserGuide/aws-properties-ec2-instance.html#cfn-ec2-instance-blockdevicemappings) *"Oops!"*
- You can cancel a stack update [*simply by clicking cancel*](https://docs.aws.amazon.com/AWSCloudFormation/latest/UserGuide/using-cfn-cancel-stack-update.html). *"Errr..."*
- Delete an All-in-one template: *"Oops, there goes the database"*

--

## Creating a stack with Ansible

<pre><code data-trim="" class="ruby">
- hosts: localhost
  tasks:
  - name: "Create CloudFormation stack"
    cloudformation:
      stack_name="my-precious-stack"
      template="my-cloudformation-template.json"
      region="eu-west-1"
      state=present
    args:
      template_parameters:
        KeyName: "my-key-in-ec2"
        AmiId: "ami-828334f5"
        InstanceType: "t2.micro"
      tags:
        Name: "name-for-all-the-resources"
        Project: "aws-workshop-project"
</code></pre>

--

## Exercise: Create a stack

<pre><code data-trim="" class="ruby">
# Run the following command in workshop/initial/deploy
# ansible-playbook -e "stack_name=FOO" -i localhost, create_queues_and_database.yml

- hosts: all
  connection: local
  tasks:
  - name: "Create a stack of SQS queues and SimpleDB domain"
    cloudformation:
      stack_name="aws-workshop-{{ stack_name }}"
      template="cloudformation-templates/infrastructure-queues-and-sdb.template"
      region="eu-west-1"
      state=present
    args:
      template_parameters:
        UserName: "{{ stack_name }}"
      tags:
        Name: "aws-workshop-{{ stack_name }}"
</code></pre>

Verify that you can find your queues from the management console
