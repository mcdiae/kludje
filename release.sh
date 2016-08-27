#!/bin/bash
set -e

which gradle || (echo 'gradle not found' && exit 1)

THERE="$(pwd)"
HERE=$( dirname "${BASH_SOURCE[0]}" )
cd $HERE
BLDVER=="$(grep -F -m 1 'version = ' build.gradle)"; BLDVER="${BLDVER#*\'}"; BLDVER="${BLDVER%\'*}"
DOCDIR="$HERE/docs/artefacts/api/$BLDVER"

echo "CODE VERSION = $BLDVER"

gradle -version > $HERE/lastbuild_info.txt

gradle -Prelease clean build check jacoco uploadArchives

cd $THERE
