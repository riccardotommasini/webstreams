---
date: 2018-10-09
title: Wikimedia Event Streams
categories:
  - JSON Stream
  - Web Stream
  - Event Stream
featured_image: https://source.unsplash.com/NAN22eh754c/1560x940
stream: true
---

Wikimedia EventStreams {#sec:wikimedia}
----------------------

Wikimedia Foundation is an American non-profit organization that hosts,
among the other, open-knowledge projects like Wikipedia. In particular,
Wikimedia invests financial and technical resources for the maintenance
of projects that foster free and open knowledge.

Wikimedia EventStream (WES) was created for internal data analysis and
maintenance and open-sourced in 2018. WES is a web service that exposes
streams of structured data following the Server-Sent Events (SSE)
protocol, i.e., data is pushed to the interested clients. Schema of the
data is versioned and available on GitHub. WES API currently generates
eight streams:

- recentchanges
- revision-create
- page-create
- page-property-change
- page-links-change
- page-move
- page-delete
- page-undelete.

All the streams are shared as structured data, i.e., JSON.

In the following, we will focus on *recentchanges* stream that, among
the others, is the one with the most complex content.
Listing [\[lst:wes1\]](#lst:wes1){reference-type="ref"
reference="lst:wes1"} shows an example of *rechentchange* stream data
which is timestamped (line 1). On the *recentchange* stream, four kinds
of events are possible: \"edit\" (like in
Listing [\[lst:wes1\]](#lst:wes1){reference-type="ref"
reference="lst:wes1"}) for existing page modification; \"new\", for new
page creation, \"log\" for log action, \"external\" for external
changes, and \"categorize\" for category membership change. Moreover,
the \"title\" indicates the page title in a Wiki of the Wikimedia
foundation. This practically links the event to an external entity.
Listing [\[lst:wes1\]](#lst:wes1){reference-type="ref"
reference="lst:wes1"} points to the Wikidata Entity at
<https://www.wikidata.org/enity/Q31218558.ttl> that we can retrive to
enrich the stream[^1].



[^1]: We excluded metadata about the Kafka Topic from the current
    modeling and from the examples.

[^2]: <http://bit.ly/2FS0mDE>

[^3]: <http://motools.sourceforge.net/event/event.122.html>
