# -*- mode: ruby -*-
# vi: set ft=ruby :
#

VAGRANTFILE_API_VERSION = "2"
Vagrant.configure(VAGRANTFILE_API_VERSION) do |config|

  config.vm.define "aws-workshop" do |aws|
    aws.vm.box = "ubuntu/trusty64"
    aws.vm.box_url = "https://vagrantcloud.com/ubuntu/boxes/trusty64"
    aws.vm.network :private_network, ip: "10.10.10.10"

    case RUBY_PLATFORM
    when /mswin|msys|mingw|cygwin|bccwin|wince|emc/
      # Fix Windows file rights, otherwise Ansible tries to execute files
      aws.vm.synced_folder "./", "/vagrant", :mount_options => ["dmode=777","fmode=666"]
    else
      # Basic VM synced folder mount
      aws.vm.synced_folder "", "/vagrant"
    end

    aws.vm.provision "shell", path: "workshop/install_prerequisites.sh"
  end
end
