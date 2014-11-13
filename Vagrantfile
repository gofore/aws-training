# -*- mode: ruby -*-
# vi: set ft=ruby :
#

VAGRANTFILE_API_VERSION = "2"
Vagrant.configure(VAGRANTFILE_API_VERSION) do |config|

  config.vm.define "aws-workshop" do |aws|
    aws.vm.box = "ubuntu/trusty64"
    aws.vm.box_url = "https://vagrantcloud.com/ubuntu/boxes/trusty64"
    aws.vm.network :private_network, ip: "10.10.10.10"
    aws.vm.provision "shell", path: "install_prerequisites.sh"
  end
end
