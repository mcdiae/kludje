#!/bin/bash

rm -Rf web/artefacts/*

mkdir -p web/artefacts/sample
cp ../kludje-testkit/src/main/java/uk/kludje/test/sample/* web/artefacts/sample/

mkdir -p web/artefacts/api/0.1
cd web/artefacts/api/0.1
wget http://search.maven.org/remotecontent?filepath=uk/kludje/kludje-core/0.1/kludje-core-0.1-javadoc.jar -O docs.jar
jar -xvf  docs.jar
rm docs.jar
cd ../../../..

mkdir -p web/artefacts/api/0.2
cd web/artefacts/api/0.2
wget http://search.maven.org/remotecontent?filepath=uk/kludje/kludje-core/0.2/kludje-core-0.2-javadoc.jar -O docs.jar
jar -xvf  docs.jar
rm docs.jar

mkdir -p web/artefacts/api/0.3
cd web/artefacts/api/0.3
wget http://search.maven.org/remotecontent?filepath=uk/kludje/kludje-core/0.3/kludje-core-0.3-javadoc.jar -O docs.jar
jar -xvf  docs.jar
rm docs.jar
