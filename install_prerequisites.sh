#!/bin/sh

sudo add-apt-repository -y ppa:webupd8team/java
echo oracle-java8-installer shared/accepted-oracle-license-v1-1 select true | sudo /usr/bin/debconf-set-selections

sudo apt-get update
sudo apt-get install -y python-dev python-pip git oracle-java8-installer
sudo pip install boto ansible
