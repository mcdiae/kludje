#!/bin/bash

rm -Rf web/artefacts/*

mkdir -p web/artefacts/sample
cp ../kludje-testkit/src/main/java/uk/kludje/test/sample/* web/artefacts/sample/

for i in `seq 1 5`;
do
	mkdir -p web/artefacts/api/0.$i
	cd web/artefacts/api/0.$i
	wget http://search.maven.org/remotecontent?filepath=uk/kludje/kludje-core/0.$i/kludje-core-0.$i-javadoc.jar -O docs.jar
	jar -xvf  docs.jar
	rm docs.jar
	cd ../../../..
done
