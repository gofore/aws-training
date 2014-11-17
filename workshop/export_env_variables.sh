#!/bin/bash

# This script exports the AWS user credentials as environment variables
# for tools such as Ansible and boto. You can simply run this script as:
#
# . ./export_env_variables.sh
#
# This requires that the credentials.csv file exists in the same location.
# Optionally you can also pass the path to the file as an argument.

FILE="credentials.csv"

if [ -n "$1" ]
then
  FILE=$1
fi

if [ -f "$FILE" ]
then
  CSV_USER=`tail -n +2 $FILE|cut -d , -f 1| tr -d '"'`
  CSV_ACCESS_KEY=`tail -n +2 $FILE|cut -d , -f 2`
  CSV_SECRET_KEY=`tail -n +2 $FILE|cut -d , -f 3`

  export AWS_WORKSHOP_USER=$CSV_USER
  export AWS_ACCESS_KEY=$CSV_ACCESS_KEY
  export AWS_ACCESS_KEY_ID=$CSV_ACCESS_KEY
  export AWS_SECRET_KEY=$CSV_SECRET_KEY
  export AWS_SECRET_ACCESS_KEY=$CSV_SECRET_KEY

  echo "The following AWS environment variables are now set:"
  printenv|grep AWS_

else
	echo "File $FILE does not exists"
fi
