#!/bin/bash

# This script does a live update to existing servers. This is different
# from the idea of having immutable servers. It uses Ansible's dynamic
# inventory script to form a list of servers to update based on their
# EC2 tags.
#
# Example usage:
# ./hot_update_servers.sh myusername path/to/mykeyfile.pem fetcher ubuntu


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

STACK_PREFIX="aws-workshop"
PLAYBOOK="hot_update_servers.yml"

read -r -p "Warning: Are you sure you want to update '$STACK_PREFIX-$USER_NAME-$APP' with the latest jar from S3? [y/N] " response
response=${response,,} # to lower-case

if [[ $response =~ ^(yes|y)$ ]]; then

  # -e passes variables to the ansible playbook
  # -i forms an inventory list of all the EC2 instances
  # --limit limits the target machines from the full inventory to only the ones with specific name tag
  # -u is the user which ansible uses to ssh into the target instances
  # --private-key is the ssh key to use on the target instances
  # -v adds verbosity
  ansible-playbook -e "app_name=$STACK_PREFIX-$APP user_name=$USER_NAME" --limit=tag_Name_$STACK_PREFIX-$USER_NAME-$APP -v -u $SSH_USER --private-key=$KEY_PATH -i ansible-dynamic-inventory/ec2.py $PLAYBOOK

else
  echo "Aborting."
fi
