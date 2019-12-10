---
date: 2019-11-05
title: The Most Important People Of The Last 15 minutes in GKG
categories:
  - Publishing
recipe: true
featured_image: https://blog.gdeltproject.org/wp-content/uploads/2019-top-people-of-2019-20191206-header-1064x410.png
recipe:
  servings: 1 query
  prep: 20 minutes
  cook: 15 minutes
  ingredients_markdown: |-
    * GDELT Global Knowledge Graph Stream
    * An R2RML Mapping
    * An RML Mapping Engine
    * An RDF Stream Processor, e.g., YASPER
    * Data Visualization Technique
  directions_markdown: |-
    1. Access the Web Stream 
    2. Convert the stream into RDF to simplify data integration using the R2RML mapping and the mapping engine 
    3. Design your query on paper simple SPARQL syntax
    4. Extend the SPARQL query to RSP-QL by adding the prescribed window operator
    5. Verify if the syntax is correct registering the query to YASPER
    6. Deploy and wait for the results (every 15 minutes)
---

Several studies have been running using GDELT. In particular, data
visualization techniques that take into account the spatio-temporal
metadata of GDELT extracted events. GDELT offers different APIs to run
analysis and a `Google Big Query`[^3] access to the database. GDELT
exposes examples of pre-configures analyses via the analysis service.
For instance, the Event `TimeMapper` visualizes events matching a given
search over time.


{% highlight ttl linenos %}
:events a vocals:StreamDescriptor ; dcat:dataset :eventstream  .
:eventstream a vocals:Stream ;
 dcat:title "GDELT Event Stream"^^xsd:string ;
 dcat:publisher <http://www.streamreasoning.org> ;
 dcat:description "GDELT Events Stream"^^xsd:string ;
 vocals:windowType vocals:logicalTumbling ;
 vocals:windowSize "PT15M"^^xsd:duration ;
 vocals:hasEndpoint [
   a vocals:StreamEndpoint ;
   dcat:license <https://creativecommons.org/licenses/by-nc/4.0/> ;
   dcat:format frmt:JSON-LD;
   dcat:accessURL "ws://examples:8080/events" ] .
{% endhighlight %}

We collected all the information regarding the streams schemas into an
OWL 2 Ontology. Moreover, we started an knowledge engineering task that
involves both `CAMEO` and `GCAM`[^4].

The `CAMEO` ontology is a coding scheme designed for the study of
third-party mediation in international disputes. It contains
hierarchical coding scheme for dealing with sub-state actors, event
types, and an extensive taxonomy for religious groups and ethnic groups.
For CAMEO, we focus on event types and actors, creating a comprehensive
hierarchy for the stream.

`GCAM` is a pipeline of 18 content analysis tools. Each news article
monitored by GDELT goes through GCAM pipeline that captures over 2230
dimensions, reporting density and value scores for each. Using GCAM, you
can assess the density of "*Anxiety*" speech via Linguistic Inquiry and
Word Count (LIWC), or "*Smugness*" via WordNet Affect. The GCAM Master
Codebook lists of all of the dimensions available[^5]. We converted the
Codebook in RDF and we refined it manually to link it to existing
vocabularies such as DBPedia and WordNET.

Listing [\[lst:gdelt1\]](#lst:gdelt1){reference-type="ref"
reference="lst:gdelt1"} shows a VoCaLS description for the GDELT Event
Stream. GDELT does not use a licence format, thus we include a licence
that is compliant with the terms of use. We also linked ontologies and
mappings using `rdfs:seeAlso`. Since the stream is published regularly
as 15 minutes batch, we include metadata about the rate, i.e.,
`vocals:windowType` and `vocals:windowSize` at line 6 and 7.

Due to the lack of space, we do not include a `vocals:StreamEndpoint`
for the original source. However, since the complexity of the knowledge
engineering task is high, we are particularly interested in making the
mapping process transparent to collect feedback and improve data quality.

GDELT content is not natively RDF. Therefore, similarly to what we did
for WES, we need to set up a conversion mechanism. Similarly to what we
did for Wikimedia Event, Data conversion follows the RML approach, where
CSV data are converted using Mapping.

The following Listings show a sample of the GDELT event mapping. We
used `rr:class` to assign the `gdelt:Event` type to the data at line 4.
Moreover, we also assign the CAMEO type using `rdf:type` at line 7. To
model the actors and its type hierarchy, we include a separate triple
map at line 12.

{% highlight ttl linenos %}
<GEM> a rr:TriplesMap ; rml:logicalSource  <source> ;
  rr:subjectMap [ 
    rr:template "http://gdelt.com/instance/{GLOBALEVENTID}";
    rr:class gdelt:Event;
    rr:graphMap [ rr:template  "http://gdelt.com/time/{DATEADDED}"]];
  rr:predicateObjectMap [
    rr:predicate rdf:type;
    rr:objectMap [rr:template "http://gdelt.com/cameo/{EventCode}"; ]];
  rr:predicateObjectMap [
    rr:predicate gdelt:actor;
    rr:objectMap [ rr:parentTriplesMap <Atr1TM> ]]; [...] .
<Atr1TM> rml:logicalSource <source> ;
    rr:subjectMap [ rr:template "http://gdelt.com/cameo/{Actor1Name}" ];
     rr:predicateObjectMap [
         rr:predicate gdelt:actorCode;
         rr:objectMap [ rml:reference "Actor1Code" ; ] ]; [...] .
{% endhighlight %}

CARML allows us to extrapolate multidimensional entries of the GKG
stream like Themes or GCAM dimensions using custom functions. We develop
functions that can split the entry content and treat it accordingly.
Moreover, we enriched the Persons entry by retrieving relevant DBPedia
entities using DBPedia Spotlight and GeoNames APIs for locations.

We publish the GDELT stream using TripleWave approach. We shared the
VoCALS description files like the first Listing as S-GRAPH via REST APIs. Since the concept of event is first-class, we opted for a graph-based stream data model that maintains the granularity of information.