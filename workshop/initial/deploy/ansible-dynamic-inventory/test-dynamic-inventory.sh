#!/bin/sh

echo "Trying to generate a dynamic inventory of instances"
./ec2.py --list

echo "Trying to ping instances"
ansible -i ec2.py -u ubuntu eu-west-1 -m ping
