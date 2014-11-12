#!/bin/sh

ansible-playbook -v -i ansible-inventory-localhost create_infrastructure.yml
