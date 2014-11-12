#!/bin/sh

HELP="Usage: $0 YOUR_USER_NAME YOUR_EMAIL_ADDRESS KEY_NAME"

if [ $# -ne 3 ]
  then
    echo "Error: wrong number of arguments\n$HELP"
    exit 1
fi

USER_NAME="$1"
EMAIL="$2"
KEY_NAME="$3"

ansible-playbook -e "{\"user_name\":\"$USER_NAME\",\"user_email\":\"$EMAIL\",\"key_name\":\"$KEY_NAME\"}" -i ansible-inventory-localhost create_infrastructure.yml
