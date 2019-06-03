 # Web Streams
 
Data Velocity reached the Web shore. Indeed, the Web is already adding new protocols and APIs (e.g. WebSockets, and EventSource). The Web of Data is also evolving to tame Velocity without neglecting Variety. The RDF Stream Processing (RSP) community is actively addressing these challenges by proposing continuous query languages and working prototypes. The problem of Streaming Linked Data publication is currently under investigation. So far, the RSP community efforts have contributed middleware, engines, and vocabularies that address the scientiﬁc and technical challenges. Nevertheless, a set of guidelines that showcase how to reuse existing resources to publish streams on the Web is still missing. 

This repository contains the code related to the paper *A Cookbook for Publishing Streaming Linked Data*.

The idea of a Cookbook for publishing Streaming Linked Data is based on the publication lifecycle prescribed by W3C. Particularly, (i) We make use of TripleWave, R2RML/RML, VoCaLS, RSP-QL, as well as other resources for RDF Stream publishing and processing. (ii) We bootstrap a catalogue of Web Streams, highlighting the requirements that drove our selection of three examples of wild streams: DBpedia Live changes, Wikimedia EventStreams, and the Global Database of Events, Language and Tone (GDELT). (iii) Last but not least, we open sourced our code, and make it available on for public use at https://w3id.org/webstreams.

 [![DOI](https://zenodo.org/badge/173600663.svg)](https://zenodo.org/badge/latestdoi/173600663)

In the following, we will show how to run this software in order to produce some RDF streams locally on a device, created from data coming from the Web.
The only requirement is to have Java on the machine.

## Download and Run

### Java 

The build file can be downloaded [here](relese)
and run with java 8

```
java -jar webstreams.jar

```

This will start the application with the default settings, which means that the [GDELT Events Stream](https://github.com/riccardotommasini/webstreams/wiki/GDELT---Event-(EXPORT)-Stream) will be provided at http://localhost:8080/events .

To visualize the stream you can use an utility like [wscat](https://www.npmjs.com/package/wscat) or every other method suitable for WebSockets.

Please note that the stream will start in 10 seconds after the launch of the application; then, in the case of GDELT Streams, a check for new data will be made every 5 minutes.

### Manual Configuration

You can use a simple properties file in order to modify the behavior of the application.
Create a text file called "user.properties" and place it in the same folder of the jar file.
You can specify the following values:

| Property name        | Possible values                                             | Description                                      | Default value                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                    |
|----------------------|-------------------------------------------------------------|--------------------------------------------------|--------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| host                 | IP address or "localhost"                                   | The IP on which the web server will be deployed. | localhost                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                        |
| sgraphport           | [1024-65535]                                                | Port to access sgraphs.                          | 8081                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                             |
| streamport           | [1024-65535]                                                | Port to access streams.                          | 8080                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                             |
| streamToCreate       | gdelt_mentions, gdelt_events, gdelt_gkg, wikimedia, dbpedia | This specifies which stream will be deployed.    | gdelt_events                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                     |
| stream_header        | String                                                      | ???                                              | [GDELT Events header](https://github.com/riccardotommasini/webstreams/wiki/Getting-Started/_edit#events)|
| stream_mapping_path  | Path                                                        | Path to load the mapping file from.              | /streams/mapping/geldt_events.ttl                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                |
| stream_sgraph_path   | Path                                                        | Path to load the sgraph file from.               | /streams/sgraphs/events.ttl                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                       |
| geldtLastUpdateUrl   | Url                                                         | URL of data/stream to gather the data from.      | http://data.gdeltproject.org/gdeltv2/lastupdate.txt                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                              |
| wikimediaStreamUrl   | Url                                                         | URL of data/stream to gather the data from.      | https://stream.wikimedia.org/v2/stream/recentchange                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                              |
| DBPediaLastUpdateUrl | Url                                                         | URL of data/stream to gather the data from.      | http://live.dbpedia.org/changesets/lastPublishedFile.txt                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                         |

Default values will be used for fields not specified in user.properties.

Prebuilt Docker images running each of the published streams with default configuration hare available on Docker Hub 

```
docker pull streamreasoning/webstreams_{stream}:latest

```

where stream can be:

- wes for Wikimedia Event Stream 
- dbl for DBPedia Live
- GDELT
    + gdelt_events for EVENT
    + gdelt_mentions for MENTIONS
    + gdelt_gkg for Global Knowledge Graph

For instance

```
docker pull streamreasoning/webstreams_gkg:latest
docker run -p 8080:8080 -p 8081:8081 streamreasoning/webstreams_gkg:latest

```


### Example of user defined properties
For example, if you want to run GDELT GKG Stream, your user.properties should look like this:
```properties
streamToCreate=gdelt_gkg
stream_header=GKGRECORDID\tDATE\tSourceCollectionIdentifier\tSourceCommonName\tDocumentIdentifier\tCounts\tV2Counts\tThemes\tV2Themes\tLocations\tV2Locations\tPersons\tV2Persons\tOrganizations\tV2Organizations\tV2Tone\tDates\tGCAM\tSharingImage\tRelatedImages\tSocialImageEmbeds\tSocialVideoEmbeds\tQuotations\tAllNames\tAmounts\tTranslationInfo\tExtras
stream_mapping_path=/streams/mapping/gdelt_gkg.ttl
stream_sgraph_path=/streams/sgraphs/gkg.ttl
```

## GDELT Header
The following header should be used for GDELT streams:
### Events
```
GLOBALEVENTID\tSQLDATE\tMonthYear\tYear\tFractionDate\tActor1Code\tActor1Name\tActor1CountryCode\tActor1KnownGroupCode\tActor1EthnicCode\tActor1Religion1Code\tActor1Religion2Code\tActor1Type1Code\tActor1Type2Code\tActor1Type3Code\tActor2Code\tActor2Name\tActor2CountryCode\tActor2KnownGroupCode\tActor2EthnicCode\tActor2Religion1Code\tActor2Religion2Code\tActor2Type1Code\tActor2Type2Code\tActor2Type3Code\tIsRootEvent\tEventCode\tEventBaseCode\tEventRootCode\tQuadClass\tGoldsteinScale\tNumMentions\tNumSources\tNumArticles\tAvgTone\tActor1Geo_Type\tActor1Geo_FullName\tActor1Geo_CountryCode\tActor1Geo_ADM1Code\tActor1Geo_ADM2Code\tActor1Geo_Lat\tActor1Geo_Long\tActor1Geo_FeatureID\tActor2Geo_Type\tActor2Geo_FullName\tActor2Geo_CountryCode\tActor2Geo_ADM1Code\tActor2Geo_ADM2Code\tActor2Geo_Lat\tActor2Geo_Long\tActor2Geo_FeatureID\tActionGeo_Type\tActionGeo_FullName\tActionGeo_CountryCode\tActionGeo_ADM1Code\tActionGeo_ADM2Code\tActionGeo_Lat\tActionGeo_Long\tActionGeo_FeatureID\tDATEADDED\tSOURCEURL
```
### Mentions
```
GLOBALEVENTID\tEventTimeDate\tMentionTimeDate\tMentionType\tMentionSourceName\tMentionIdentifier\tSentenceID\tActor1CharOffset\tActor2CharOffset\tActionCharOffset\tInRawText\tConfidence\tMentionDocLen\tMentionDocTone\tMentionDocTranslationInfo\tExtras
```
### GKG
```
GKGRECORDID\tDATE\tSourceCollectionIdentifier\tSourceCommonName\tDocumentIdentifier\tCounts\tV2Counts\tThemes\tV2Themes\tLocations\tV2Locations\tPersons\tV2Persons\tOrganizations\tV2Organizations\tV2Tone\tDates\tGCAM\tSharingImage\tRelatedImages\tSocialImageEmbeds\tSocialVideoEmbeds\tQuotations\tAllNames\tAmounts\tTranslationInfo\tExtras
```
  

## Build 

### MAVEN

The project can be build using maven 

```

maven clean package 

```

or using the included make file

```
make build
```
