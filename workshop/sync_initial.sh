#!/bin/bash 

rsync -av --exclude-from=sync_excludes.txt complete/ initial/
