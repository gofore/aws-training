#!/bin/bash

# This script requires that you have installed awscli tools:
#
# sudo pip install awscli
#

HELP="This tool copies files in S3 from one user to another.\nUsage: $0 SOURCE_USER TARGET_USER"

if [ $# -ne 2 ]
  then
    echo -e "Error: Wrong number of arguments\n$HELP"
    exit 1
fi

SOURCE="$1"
TARGET="$2"

read -r -p "Warning: Are you sure you want to sync files in S3 from user '$SOURCE' to '$TARGET'? [y/N] " response
response=${response,,} # to lower-case

if [[ $response =~ ^(yes|y)$ ]]; then

  aws s3 sync s3://aws-workshop-demo/artifacts/$SOURCE s3://aws-workshop-demo/artifacts/$TARGET

else
  echo "Aborting."
fi
