#!/bin/sh

# This script deletes the cloudformation stack from AWS with the given name.
# This is a helpful wrapper around Ansible so that you do not need to learn
# the Ansible parameter passing.

HELP="Usage: $0 YOUR_USER_NAME"

if [ $# -ne 1 ]
  then
    echo "Error: wrong number of arguments\n$HELP"
    exit 1
fi

USER_NAME="$1"

ansible-playbook -e "{\"user_name\":\"$USER_NAME\"}" -i ansible-inventory-localhost delete_infrastructure.yml
