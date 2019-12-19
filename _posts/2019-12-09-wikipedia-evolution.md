---
date: 2019-11-07
title: Analyzing Wikipedia Evolution using DBPedia Live Updates
recipe: true
categories:
  - Continuous Query
  - Processing
featured_image: https://source.unsplash.com/qJ0zGkrE1Zg/1560x940
recipe:
  servings: 12 cupcakes
  prep: 5 minutes
  cook: 25 minutes
  ingredients_markdown: |-
    * Access to DBPedia Live Update as RDF Stream
    * An RSP Engine
    * Knowledge About OWL and RDF
  directions_markdown: |-

    1. Preheat Oven 350 degree
    2. In a bowl combine flour, cocoa baking powder, baking soda and salt.
    3. In a food processor combine butter and sugar and process until smooth. Add the eggs, 4 oz. of chocolate pieces and vanilla. Add half of the flour mixture and ½ of the milk. Process and add the other half of the flour and the remainder of the milk. Slowly, add the hot water.
    4. Grease and fill muffin tins to top.
    5. Bake 20 25 minutes or until toothpick test comes out clean.
    6. Let cool.

 
---


DBL was successfully used to analyze DBPedia evolution. In particular,
it was used to satisfy information needs like *How many entities are
updated in last 5 minutes?*. DBPedia live statistics is a Daily updated
Web page that shows analyses like top-k entity changes.

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