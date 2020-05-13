---
date: 2018-10-09
title: Global Dataset of Event, Language and Tone
categories:
  - RDF Stream
  - Web Stream
  - Event Stream
featured_image: https://blog.gdeltproject.org/wp-content/uploads/2019-top-people-of-2019-20191206-header-1064x410.png
stream: true
---

Global Database of Events, Language and Tone (GDELT) {#sec:geldt}
----------------------------------------------------

The `GDELT` is the largest open-access spatio-temporal archive for human
society. Its Global Knowledge Graph spans more than 215 years and
connects people, organizations, locations, all over the world. GDELT
captures themes, images, and emotions into a single holistic global
network.

GDELT data comes from a multitude of news sources. The extraction
process follows techniques from Natural Language Processing and makes
use of two ontologies, i.e., Conflict and Mediation Event Observations
(CAMEO) [@gerner2002conflict], and Global Content Analysis Measures
(GCAM). GDELT provisions multiple streams, in the following we discuss
three of them, i.e., Events, Mentions and Global Knowledge Graphs (GKG).
Consequently, GDELT information is naturally multidimensional and
interconnected due to the usage of complex background knowledge.

GDELT provisions streams of TSV data every 15 minutes. As anticipated,
it includes three streams, i.e., Events, Mentions, and Global 
Knowledge Graphs (GKG). 

The *Event Stream* makes use of the dyadic CAMEO format. The
most important information regards the two actors participating in each
event, and the action they perform. 

The *Mention Stream* records every
mention of an event over time, along with the timestamp the article was
published. The mention stream is linked to the Event stream by the
Global Event ID field.


The *GKG Stream* connects people, organizations,
locations, themes, news source, and event across the planet into a
single massive network. GKG stream is rich and multidimensional. Fields
like the GCAM, Themes, or Persons include more than one entry, using a
CSV or similar formats.


The following Listing presents an RSP-QL query that mixes all GDELT
streams. The query, which is inspired to an GDELT analyses available on
the GDELT blog, counts the number of mentions in events that are
happening in a particular area of IRAQ, and whose theme is `gcam:kill`.

{% highlight ttl linenos %}
REGISTER RSTREAM <complexstrm> AS
SELECT (COUNT(?newsa) AS ?tot)
FROM NAMED WINDOW <e>  ON gdelt-bq:gdeltv2.events    [RANGE 30MPT 15MPT]
FROM NAMED WINDOW <m>  ON gdelt-bq:gdeltv2.mentions  [RANGE 30MPT 15MPT]
FROM NAMED WINDOW <g>  ON gdelt-bq:gdeltv2.gkg       [RANGE 30MPT 15MPT]
WHERE {
 WINDOW <e> { ?event gdelt:quadClass cameo:4 ; actionGeo_cc ‘IZ’. }
 WINDOW <m> { ?event gdelt:mentions ?newsa. }
 WINDOW <g> { ?newsa gdelt:theme gcam:kill ; gdelt:location geo:iraq. } }
GROUP BY ?newsa
{% endhighlight %}

[^1]: <http://bit.ly/GDELT-event-mentions>

[^2]: <http://bit.ly/GDELT-GKG>

[^3]: <https://cloud.google.com/bigquery/>

[^4]: <http://bit.ly/2CYqnAI>[\[ftn:ontologies\]]{#ftn:ontologies
    label="ftn:ontologies"}

[^5]: <http://bit.ly/2FOwgB3>
