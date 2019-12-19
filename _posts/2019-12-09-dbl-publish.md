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

[Figure 1](#fig:dbl1) shows the three situations a practitioner might find when she/he wants to publish Web Streams. The lower-right quadrant identifies our ultimate goal, i.e., Streaming Linked Data. The other quadrants presents possible starting points, i.e., (upper-left) Web Data published in batches; (upper-right) Linked Data published in batches; and (lower-left) Web Data published as streams. 

The case of DBPedia Live Update Stream is the one identified by the upper-right quadrant, i.e., a Web Stream that is not linked yet.

To proceed creating a Linked Data Stream we follow the publication pipeline included in the following [Figure](#fig:wes2).

![fig:wes2](/images/lifecycleragab.jpg){:id="fig:wes2"}
*Figure 2*

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