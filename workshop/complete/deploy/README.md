
We use [Ansible](https://github.com/ansible/ansible) for creating the CloudFormation infrastrucure and deploying the application.

Check each of the .yml playbook files to learn what they do and how they are run.

The basic way to run a playbook is:

    ansible-playbook -i INVENTORY_FILE PLAYBOOK_FILE

We can also pass variables to the playbook with the -e argument:

    ansible-playbook -e "KEY1=VALUE1 KEY2=VALUE2" -i INVENTORY_FILE PLAYBOOK_FILE

An inventory file describes the target hosts to which ansible should run the playbook. When you use ansible to run commands on the local host, there is a hacky way (the extra comma) in ansible to do this without the need of an inventory file:

    ansible-playbook -i localhost, PLAYBOOK_FILE

Thus, a real usage would look like:

    ansible-playbook -e "user_name=johndoe user_email=john.doe@example.com key_name=johns-key" -i localhost, create_infrastructure.yml
