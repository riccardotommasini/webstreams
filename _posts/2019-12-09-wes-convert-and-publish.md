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
    * A Wikimedia Event Stream, e.g., recent changes [link](https://stream.wikimedia.org/?doc#/Streams)
    * A Text Editor 
    * An RML Mapping Engine
    * A Domain Ontology for the Stream Content
    * Knowledge About OWL and RDF
    * [Optional] an RSP engine to calculate some descriptive statistics
  directions_markdown: |-
    1. Select one among the Wikimedia Event Streams, e.g., the recent change
    2. Identify the publication case ( see [Figure 1](#fig:quadrant))
    3. Find the schema that is used for the messages
    4. Design an ontology to be used for conversion into RDF Stream
    4. Use the Text Editor to map the schema into a common ontology
    5. Using an mapping engine, apply the mapping on the fly
---

![fig:wes1](/images/wes1.png "Figure 1"){:id="fig:wes1"}*Figure 1*

[Figure 1](fig:wes1) shows the three situations a practitioner might find when she/he wants to publish Web Streams. The lower-right quadrant identifies our ultimate goal, i.e., Streaming Linked Data. The other quadrants presents possible starting points, i.e., (upper-left) Web Data published in batches; (upper-right) Linked Data published in batches; and (lower-left) Web Data published as streams. 

The case of Wikimedia Event Stream is the one identified by the lower-left quadrant, i.e., a Web Stream that is not linked yet.

To proceed creating a Linked Data Stream we follow the publication pipeline included in the following [Figure](fig:wes2).

![fig:wes2](/images/lifecycleragab.jpg)

We collected the information about the streams schemas on GitHub[^1].
We also assume to have an OWL 2 ontology that capture the semantics of the WESs domain Notably, WESs are designed around the notion of
event, therefore, reasonable vocabularies to annotate the data existing, e.g., Event Ontology[^2]. 

Data items are timestamped individually,
and we used this timestamp to name the graph containing all the event
data. Regarding the *recentchanges* stream, we emphasizes the modeling
of the events types in our ontology, i.e., \"edit\", \"new\", \"log\",
\"categorize\", or \"external\". Similarly, we take into account what
could be represented as external resources like Wikidata.
The following listing shows an example of WES recentchange data item.

```json
{
  "event": "message",
  "id": [
    {
      "topic": "eqiad.mediawiki.recentchange",
      "partition": 0,
      "timestamp": 1576599002001
    },
    {
      "topic": "codfw.mediawiki.recentchange",
      "partition": 0,
      "offset": -1
    }
  ],
  "data": {
    "$schema": "/mediawiki/recentchange/1.0.0",
    "meta": {
      "uri": "https://www.wikidata.org/wiki/Q78902990",
      "request_id": "Xfj92gpAAK4AAG77O64AAABP",
      "id": "a99cd5d4-981a-42af-9947-f065d1ee28bb",
      "dt": "2019-12-17T16:10:02Z",
      "domain": "www.wikidata.org",
      "stream": "mediawiki.recentchange",
      "topic": "eqiad.mediawiki.recentchange",
      "partition": 0,
      "offset": 2039616376
    },
    "type": "log",
    "namespace": 0,
    "title": "Q78902990",
    "comment": "",
    "timestamp": 1576599002,
    "user": "Alicia Fagerving (WMSE)",
    "bot": false,
    "log_id": 0,
    "log_type": "abusefilter",
    "log_action": "hit",
    "log_params": {
      "action": "edit",
      "filter": 64,
      "actions": "",
      "log": 10906339
    },
    "log_action_comment": "Alicia Fagerving (WMSE) triggered 
    [[Special:AbuseFilter/64|filter 64]], performing the action \"edit\" on 
    [[Q78902990]]. Actions taken: none ([[Special:AbuseLog/10906339|details]])",
    "server_url": "https://www.wikidata.org",
    "server_name": "www.wikidata.org",
    "server_script_path": "/w",
    "wiki": "wikidatawiki",
    "parsedcomment": ""
  }
}
```

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

[^1]: https://github.com/wikimedia/mediawiki-event-schemas/tree/master/jsonschema/mediawiki/recentchange
[^2]: event ontology