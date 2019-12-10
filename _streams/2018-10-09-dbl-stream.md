---
date: 2018-10-09
title: DBPedia Live Updates
categories:
  - CSV Stream
  - Web Stream
  - Change Data Capture Stream
featured_image: https://source.unsplash.com/NAN22eh754c/1560x940
stream: true
---

DBPedia Live
------------

DBPedia is a community project to extract structured data from Wikipedia
in the form of an Open Knowledge Graph. DBPedia is served as linked data
using Entity-Pages, REST APIs, or SPARQL endpoints. The latest version
of DBPedia counts more than 470 millions of RDF triples[^1].

DBPedia Live (DBL) a changelog stream continuously published to keep
DBPedia replicas in-synch. A Synchronization Tool designed to consume
the batches and update the local DBPedia copy is
available [\[1\]](https://dblp.uni-trier.de/rec/bibtex/journals/program/MorseyLASH12).

DBL data are obtained by means of the DBpedia Live extraction
framework [\[2\]](https://dblp.uni-trier.de/rec/bibtex/journals/program/MorseyLASH12) that produces extracted
metadata with annotations from Wikipedia infoboxes. DBL is composed by
three sub-streams carrying the entities to add, those to remove, and the
triples to re-add upon propagating the deletion of an entity. Entities
and triples make use of DBO types and properties.

DBL uses a pull-based mechanism. An update of DBL consists of four
compressed NT files. Two main diff files, i.e., *removed*, and *added*,
determine the insertion and deletion stream. Two further streams share
files for clean updates that are optional to execute: *reinserted*,
which corresponds to unchanged triples that can be reinserted, and
*clear*, which prescribe the delete queries that clear all triples for a
resource. Relevant metadata about the DBPedia Live stream corresponds to
license, format and human-readable description.

DBL was successfully used to analyze DBPedia evolution. In particular,
it was used to satisfy information needs like *How many entities are
updated in last 5 minutes?*. DBPedia live statistics is a Daily updated
Web page that shows analyses like top-k entity changes.

DBPedia Live already provides RDF Data that make use of DBPpedia
ontology (DBO), i.e., a cross-domain ontology, manually created from the
most used Wikipedia info-boxes. The latest version of DBO covers 685
classes and 2795 different properties. It is a directed-acyclic graph,
i.e., classes may have multiple super-classes, as required to map it to
`schema.org`. Although edits are often done in bulk, this information is
not present at the level of granularity the data are shared from DBL.
Changes come unordered and, thus, we decided to ingest them using
time-stamped triples similar to what
`C-SPARQL` [\[4\]](https://dblp.uni-trier.de/rec/bibtex/conf/www/BarbieriV10)
`CQELS` [\[5\]](https://dblp.uni-trier.de/rec/bibtex/conf/debs/PhuocDTDH15) engines do.

The following Listing presents the DBPedia Live VoCaLS Stream
Descriptor. It contains basic information about the publisher and the
licence. We linked DBPedia ontology and other relevant datasets (lines
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


DBPedia Live already provides RDF Data, thus, we do not have to apply
any conversion mechanism.

We decided to use a RDF Stream Processor to publish DBL as a linked
stream. In particular, we make use of
`YASPER` [https://dblp.uni-trier.de/rec/bibtex/conf/semweb/0001CDB0VA16] to compute the aforementioned
statistics. We feed data to YASPER directly by means of an adapter that
time-stamps each triple independently. We shared the VoCaLS file of
Listing [\[lst:dbpedia1\]](#lst:dbpedia1){reference-type="ref"
reference="lst:dbpedia1"} using the RSP engine. Last but not least, we
compute simple analytics in the VoCaLS description using RSP-QL. An
RSP-QL query example that counts the top-20 most edited entities in the
last minute is presented in the following Listings.

{% highlight sparql linenos %}
PREFIX dbl: <http://live.dbpedia.org/changesets/>
REGISTER RSTREAM <top20MosteditedEntities> AS
SELECT (COUNT (?entity) AS ?count) ?entity
FROM NAMED WINDOW <wa> ON dbl:added [RANGE PT1M STEP PT10S]
FROM NAMED WINDOW <wr> ON dbl:removed [RANGE PT1M STEP PT10S]
WHERE { WINDOW ?w { ?entity ?p ?o . } }
GROUP BY ?entity ORDER BY DESC(?count) LIMIT 20

{% endhighlight %}


[^1]: <http://dbpedia-live.openlinksw.com/live/>
