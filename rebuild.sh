#!/bin/bash

which gradle || (echo 'gradle not found' && exit 1)
which grep || (echo 'grep not found' && exit 1)

THERE="$(pwd)"
HERE=$( dirname "${BASH_SOURCE[0]}" )
cd $HERE
BLDVER=="$(grep -F -m 1 'version = ' build.gradle)"; BLDVER="${BLDVER#*\'}"; BLDVER="${BLDVER%\'*}"
DOCDIR="$HERE/docs/artefacts/api/$BLDVER"

echo "CODE VERSION = $BLDVER"

gradle -version > $HERE/lastbuild_info.txt

gradle clean build jacoco

if [[ $BLDVER == *"SNAPSHOT"* ]]; then
  echo "Skipping documentation for SNAPSHOT build"
else
  echo "Documenting latest API in $DOCDIR"
  if [ -d "$DOCDIR" ]; then
    rm -Rf "$DOCDIR"
  fi
  mkdir "$DOCDIR"
  cp -r kludje-core/build/docs/javadoc/* "$DOCDIR"
fi

cd $THERE