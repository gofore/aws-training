#!/bin/bash
pip install awscli
cd /tmp/
aws s3 cp --region eu-west-1 s3://aws-workshop-demo/tools/bootstrap/deploy_app.yml deploy_app.yml
aws s3 cp --region eu-west-1 s3://aws-workshop-demo/tools/bootstrap/upstart_service.conf upstart_service.conf
ansible-playbook -i localhost, -e "user_name=testi1 app_name=aws-workshop-loader" deploy_app.yml
