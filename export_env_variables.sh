#!/bin/sh

FILENAME="credentials.csv"

CSV_USER=`tail -n +2 $FILENAME|cut -d , -f 1| tr -d '"'`
CSV_ACCESS_KEY=`tail -n +2 $FILENAME|cut -d , -f 2`
CSV_SECRET_KEY=`tail -n +2 $FILENAME|cut -d , -f 3`

export AWS_WORKSHOP_USER=$CSV_USER
export AWS_ACCESS_KEY=$CSV_ACCESS_KEY
export AWS_ACCESS_KEY_ID=$CSV_ACCESS_KEY
export AWS_SECRET_KEY=$CSV_SECRET_KEY
export AWS_SECRET_ACCESS_KEY=$CSV_SECRET_KEY

echo "The following AWS environment variables have now been set:"
printenv|grep AWS_
