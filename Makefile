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
	
package:
	echo ${STREAM}
	docker build -t streamreasoning/webstreams_${STREAM} --build-arg stream=${STREAM} .

package_all:
	docker build -t streamreasoning/webstreams_gdelt_gkg --build-arg stream=gdelt_gkg .
	docker build -t streamreasoning/webstreams_gdelt_events --build-arg stream=gdelt_events .
	docker build -t streamreasoning/webstreams_gdelt_mentions --build-arg stream=gdelt_mentions .
	docker build -t streamreasoning/webstreams_dbl --build-arg stream=dbl .
	docker build -t streamreasoning/webstreams_wes --build-arg stream=wes .

run:
	java -jar webstreams.jar