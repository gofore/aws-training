#!/bin/bash

# This script does a live update to existing servers. This is different
# from the idea of having immutable servers. It uses Ansible's dynamic
# inventory script to form a list of servers to update based on their
# EC2 tags.
#
# Example usage:
# ./hot_update_servers.sh myusername ../mykeyfile.pem fetcher ubuntu


HELP="Usage: $0 YOUR_USER_NAME PATH_TO_SSH_KEYFILE APP_TO_UPDATE SSH_USER_NAME\nExample: ./hot_update_servers.sh myusername ../mykeyfile.pem fetcher ubuntu\nAPP_TO_UPDATE is one of ui, loader, fetcher."

if [ $# -ne 4 ]
  then
    echo -e "Error: wrong number of arguments\n$HELP"
    exit 1
fi

USER_NAME="$1"
KEY_PATH="$2"
APP="$3"
SSH_USER="$4"

PREFIX="aws-workshop-"
PLAYBOOK="hot_update_servers.yml"

read -r -p "Warning: Are you sure you want to update '$PREFIX$USER_NAME-$APP' with the latest jar from S3? [y/N] " response
response=${response,,} # to lower-case

if [[ $response =~ ^(yes|y)$ ]]; then

  ansible-playbook -e "app_name=$PREFIX$APP user_name=$USER_NAME" --limit=tag_Name_$PREFIX$USER_NAME-$APP -v -u $SSH_USER --private-key=$KEY_PATH -i ansible-dynamic-inventory/ec2.py $PLAYBOOK

else
  echo "Aborting."
fi
