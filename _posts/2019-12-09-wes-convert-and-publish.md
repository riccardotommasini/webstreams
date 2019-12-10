---
date: 2019-11-09
title: Convert and Publish the Wikimedia Event Stream
categories:
  - Publishing
recipe: true
featured_image: https://wikimediablog.files.wordpress.com/2017/10/wikidata-birthday1.jpg
recipe:
  servings: 12 slices
  prep: 15 minutes
  cook: 65 minutes
  ingredients_markdown: |-
    * A Wikimedia Event Stream, e.g., recent changes
    * A Text Editor 
    * An RML Mapping Engine
    * Knowledge About OWL and RDF
    * [Optional] an RSP engine to calculate some descriptive statistics
  directions_markdown: |-
    1. Heat oven to 325° F. Butter and flour a 12-cup Bundt pan. In a medium bowl, whisk together the flour, salt, baking soda, and baking powder.
    2. Using an electric mixer, beat the butter, granulated sugar, and lemon zest on medium-high until light and fluffy, 3 to 4 minutes. Beat in 4 tablespoons of the lemon juice, then the eggs, one at a time, scraping down the sides of the bowl as necessary.
    3. Reduce mixer speed to low. Add half the flour mixture, then the yogurt, and then the remaining flour mixture. Mix just until combined (do not overmix).
    4. Transfer the batter to the prepared pan and bake until a toothpick inserted in the center comes out clean, 65 to 75 minutes. Cool the cake in the pan for 30 minutes, then turn it out onto a wire rack to cool completely.
    5. In a small bowl, whisk together the confectioners’ sugar and 1 of the remaining tablespoons of lemon juice until smooth, adding the remaining lemon juice as necessary to create a thick, but pourable glaze.
---

![Publish](/images/lifecycleragab.jpg)


We collected the information about the streams schemas into an OWL 2
ontology$^{\ref{ftn:ontologies}}$. WES are designed around the notion of
event, therefore, we ported related classes from contextual vocabularies
like the Event Ontology[^3]. Data items are timestamped individually,
and we used this timestamp to name the graph containing all the event
data. Regarding the *recentchanges* stream, we emphasizes the modeling
of the events types in our ontology, i.e., \"edit\", \"new\", \"log\",
\"categorize\", or \"external\". Similarly, we take into account what
could be represented as external resources like Wikidata.

Listing [\[lst:es3\]](#lst:es3){reference-type="ref"
reference="lst:es3"} shows an VoCaLS description for the *recentchanges*
stream. We included a license that is compliant with Wikimedia terms of
use. Using `rdfs:seeAlso`, we linked to our ontology, the mapping file,
and any other relevant metadata. Due to the lack of space, we did not
link to the original sources. However, it would be worth to create a
`vocals:StreamEndpoint` that allows to track the provenance of the
conversion.

As anticipated, WES content is not RDF. Thus, we need to set up a
conversion mechanism.
The following Listings show an example of `RML` mapping with a
`JSON` source that we used for the conversion. At line 7 using
`rr:graphMap` name the RDF graph containg all the triples using the
event timestamp. At line 10 add the event type using `rdf:type` and the
\"type\" field in the JSON.

```ttl
<WMM> a rr:TriplesMap ;
  rml:logicalSource <source> ;
  rr:subjectMap [ rr:template "http://www.wikimedia.org/es/{id}" ;
  rr:graphMap [ rr:template  "http://wiki.time.com/{timestamp}" ] ] ;
  rr:predicateObjectMap [
    rr:predicate rdf:type ;
    rr:objectMap [ rr:template "http://....org/es/voc/{type}"] ] [...] .
```

To apply the mappings we used a modified version of `CARML` that handles
the annotation process incrementally to minimize the translation
latency.

To publish WES RDF Streams, we decided to use `TripleWave` approach. We
included a license compatible with the one from WES, and we made the
VoCaLS description available as S-GRAPH via REST API. We included a
Stream Endpoint that allows to consume the data directly using a
WebSocket. Data are originally shared using a document format with a
rich schema. Therefore, to preserve the level of granularity, we opted
for a graph-base stream data model.

Similarly to DBL, we include an example of statistics analysis:
The following Listings show an example of RSP-QL query calculating
the stream rate every minute.

```SPARQL
REGISTER RSTREAM <outputstream> AS
SELECT (COUNT{*}/60) ?ratesec
FROM NAMED WINDOW <win> ON <http://wikimedia.org/recentchanges/rdf> [RANGE PT60S PT60S]
WHERE { WINDOW <win> { ?s ?p ?o } }
```