#!/bin/bash

# Install prerequisite packages
sudo add-apt-repository -y ppa:webupd8team/java
echo oracle-java8-installer shared/accepted-oracle-license-v1-1 select true | sudo /usr/bin/debconf-set-selections

sudo apt-get update
sudo apt-get install -y python-dev python-pip git oracle-java8-installer
sudo pip install boto ansible

wget http://ppa.launchpad.net/natecarlson/maven3/ubuntu/pool/main/m/maven3/maven3_3.2.1-0~ppa1_all.deb
sudo dpkg -i maven3_3.2.1-0~ppa1_all.deb
sudo ln -s /usr/bin/mvn3 /usr/bin/mvn

# Export AWS credentials from CSV file
if [ "$USER" == "vagrant" ]
  then
    echo ". /vagrant/workshop/export_env_variables.sh" >> ~/.bashrc
fi
