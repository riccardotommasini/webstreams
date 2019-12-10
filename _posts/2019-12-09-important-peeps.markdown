---
date: 2019-11-05
title: The Most Important People Of The Last 15 minutes in GKG
categories:
  - Processing
  - Continuous Querying
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