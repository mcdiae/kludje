#!/bin/bash

which gradle || (echo 'gradle not found' && exit 1)

THERE="$(pwd)"
HERE=$( dirname "${BASH_SOURCE[0]}" )
cd $HERE
gradle -version > $HERE/lastbuild_info.txt

gradle clean build jacoco

cd $THERE
