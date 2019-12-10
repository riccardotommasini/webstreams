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
    **Cupcakes**

    1. Preheat Oven 350 degree
    2. In a bowl combine flour, cocoa baking powder, baking soda and salt.
    3. In a food processor combine butter and sugar and process until smooth. Add the eggs, 4 oz. of chocolate pieces and vanilla. Add half of the flour mixture and ½ of the milk. Process and add the other half of the flour and the remainder of the milk. Slowly, add the hot water.
    4. Grease and fill muffin tins to top.
    5. Bake 20 25 minutes or until toothpick test comes out clean.
    6. Let cool.

 
---


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