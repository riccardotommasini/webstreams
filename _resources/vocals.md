---
date: 2019-12-1
title: VoCaLS
type: resource
categories:
  - RDF Stream Publishing
featured_image: https://i.imgflip.com/3izngg.jpg
public: true
---

The  **Vo**cabulary for **Ca**taloging and **L**inking **S**treams (VoCaLS) is a OWL 2 ontology that aims at enabling Web Stream selection and discovery.


The vocabulary is organized in three modules:
VoCaLS Core, which describes the core elements of the vocabulary,
VoCaLS Service
Description, which describes RDF stream service descriptions, and
VoCaLS Provenance, focused on streaming data transformation and manipulation.
We will introduce each module separately.

![fig:vcore](/images/vcore.png)
*Figure 1: VoCaLS Core module*

**VoCALS Core Vocabulary** contains the concepts that are based on an extension
of [dcat](https://www.w3.org/TR/vocab-dcat-2/) to represent streams on the Web. As depicted in [Figure 1](#fig:vcore) the model introduces the basic abstractions to represent streams.

A *vocals:StreamDescriptor* is a document accessible via HTTP that holds
metadata about the stream and its contents. A *vocals:Stream* represents a Web stream, i.e., an unbounded sequence of time-varying data elements that might be findable and accessible on the Web, and which can be consumed via a *vocals:StreamEndpoint*. Finally, a *vocals:FiniteStreamPartition* is a portion of the stream available for regular Linked Data services to access and process its content.

![fig:vsd](/images/vsd.png) 
*Figure 2: VoCaLS Service Description module*. 

**VoCaLS Service Description** focuses on metadata
related to streaming services and their capabilities, enabling consumers
to discover and select services suitable to their needs as shown in [Figure 2](#fig:vsd) 
*vsd:StreamingService* is an abstraction to represent a service that deals
data streams of any type. Continuous query engines, stream reasoners,
and RDF stream publishers are valid examples. Three classes of RDF
streaming services were identified, although others could be added if
needed:

* *vsd:CatalogService*, a service that may provide metadata about streams,
    their content, query endpoints and more.
* *vsd:PublishingService*, which represents a service that publishes RDF
    streams, possibly following a Linked Data compliant scheme, and
* *vsd:ProcessingService*, which models a stream processing service that
    performs any kind of transformation on streaming data, e.g. querying,
    reasoning, filtering.

![fig:vprov](/images/vprov.png) 
*Figure 3: VoCaLS Provenance  module*. 

**VoCaLS Stream Provenance**  focuses on tracking the
provenance of stream processing services, i.e., tracing the consequences
of operations performed over the streams. The module defines four main
classes also shown in [Figure 3](#fig:vprov).

- *vprov:R2ROperator* refers to operators that produce RDF mappings
(relations) from other RDF mappings.
- *vprov:R2SOperator* represents operators that produce a stream from a
relation.
- *vprov:S2ROperator* refers to operators that produce relations from
streams, e.g., windowing. Finally,
- *vprov:S2SOperator* allows describing operators that produce a stream from
another stream.


More details about the ontology can be found in the [documentation](https://w3id.org/rsp/vocals) or in the [paper](https://link.springer.com/chapter/10.1007/978-3-030-00668-6_16).