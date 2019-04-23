#
# Makefile for webstream repo
#
# This file is in public domain
#


STREAM=events

clean:
	mvn clean

build:
	mvn clean package
	mv target/webstreams*.jar ./webstreams.jar

run:
	java -jar webstreams.jar