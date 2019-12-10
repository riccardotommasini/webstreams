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




[^1]: <http://dbpedia-live.openlinksw.com/live/>
