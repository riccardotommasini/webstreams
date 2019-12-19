---
date: 2019-11-07
title: Publishing DBPedia Live Updates as RDF Streams
recipe: true
categories:
  - Publishing
featured_image: https://source.unsplash.com/qJ0zGkrE1Zg/1560x940
recipe:
  servings: Web Stream
  prep: 20 minutes
  ingredients_markdown: |-
    * Access to DBPedia Live Update
    * A Text Editor 
    * An RML Mapping Engine
    * Knowledge About OWL and RDF
    * [Optional] an RSP engine to calculate some descriptive statistics
  directions_markdown: |-
    1. Get access to DBPedia Live Updates
    2. Identify the publication case ( see [Figure 1](#fig:dbl1))
    3. Find the schema that is used for the messages, if any.
    4. Find/design an ontology to be used for conversion into RDF Stream
    4. Use the Text Editor to map the schema into a common ontology
    5. Using an mapping engine, apply the mapping on the fly
---

![fig:dbl1](/images/dbl1.png "Figure 1"){:id="fig:dbl1"}*Figure 1*

[Figure 1](#fig:dbl1) shows the possible situations practitioners may find when they want to publish streams on the Web. The lower-right quadrant identifies our ultimate goal, i.e., Streaming Linked Data. The other quadrants presents possible starting points, i.e., (upper-left) Web Data published in batches; (upper-right) Linked Data published in batches; and (lower-left) Web Data published as streams. 

The case of DBPedia Live Update (DBL) Stream is the one identified by the upper-right quadrant, i.e., a Web Stream that is not linked yet.

To proceed creating a Linked Data Stream we follow the publication pipeline included in the following [Figure](#fig:wes2).

![fig:wes2](/images/lifecycleragab.jpg){:id="fig:wes2"}
*Figure 2*

DBL data are already in RDF. Thus, it is not necessary to set up any conversion mechanism. Nevertheless, it is relevant to identify which
ontology/vocabulary is used as well as identify the structure of the 
streams.

DBL a *changelog* stream, i.e., the data represent changes to DBPedia and have
the goal of keeping DBPedia replicas in-synch. A [Synchronization Tool](https://github.com/dbpedia/dbpedia-live-mirror/) designed to consume the batches and update the local DBPedia copy is available. 

The Stream, accessible via HTTP [at](http://downloads.dbpedia.org/live/changesets) is organized in four sub-streams:
  - added, i.e., the RDF triples corresponding to added pages.
  - removed, i.e., the RDF triples corresponding to removed pages.
  - reinserted, i.e., RDF triples that need to be left unmodified after the update
  - clear, i.e., only subjects of all added, removed and reInserted triples

The ontology used is [DBPedia's Ontology](http://dbpedia.org/ontology/), which is generated from the manually created specifications in the DBpedia Mappings Wiki.

For the publication, we wrote the stream annotation using [VoCaLS](/resources/vocals) and we released it using [TripleWave](/resource/triplewave).
The following Listing presents the DBPedia Live VoCaLS Stream
Descriptor. It contains basic information about the publisher and the
license. We linked DBPedia ontology and other relevant datasets (lines
5-6). Finally, the VoCaLS file indicates an RSP engine accessible for
query answering using the `vsd:publishedBy` property.

{% highlight ttl linenos %}

:dbl a vocals:StreamDescriptor ; dcat:dataset :dblstream  .
:dblstream a vocals:RDFStream ;
 dcat:title "DPedia Live" ; dcat:publisher <http://www.dbpedia.org> ;
 rdfs:seeAlso <http://downloads.dbpedia.org/2016-10/dbpedia_2016-10.owl>
 rdfs:seeAlso <https://wiki.dbpedia.org/downloads-2016-10#datasets> ;
 dcat:license <https://creativecommons.org/licenses/by-nc/4.0/> ;
 vsd:publishedBy :RSPEngine .
:RSPEngine a vsd:ProcessingService ; 
    vsd:hasLang vsd:RSP-QL;
    vsd:resultFormat frmt:JSON-LD ; 
    vsd:rspEndpoint "http://streamreasoning.org/yasper/queries" .

{% endhighlight %}

