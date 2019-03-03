# GELDT Streaming


## TODO


- set up queries explanation
-- GEDLT CSV/JDBC
- work on keyword search and stream definition
 -- idea on stream inheritance/inclusion
 
 -- DBPedia Changes



## Queries (Ragab)


- get all the articles that are about "Donald Trump" in the last 1 hour:

```
SELECT ?article 
WHERE
{
	?article rdf:type :Article.
	?article :talkabout "Donald Trump".
	?article  :date   ?date. FILTER(?date >= "2502201920000"^^xsd:dateTime  && ?date <= "2502201921500"^^xsd:dateTime )
}
```


```
SELECT ?article, ?title,?url,?urlMobile,?sourceLang,?sourceCountry,?socialImg,?date,?domain,
WHERE
{
	?article rdf:type :Article.
	?article :talkabout "Donald Trump".
	OPTIONAL{?article :hasURL ?url.}
	OPTIONAL{?article dc:title ?title.}
	OPTIONAL{?article :hasMobileURL ?urlMobile.}
	OPTIONAL{?article :hassourceLang ?sourceLang.}
	OPTIONAL{?article :hassourceCountry ?sourceCountry.}
	OPTIONAL{?article :hasSocialImg ?socialImg.}
	OPTIONAL{?article :hasDomain ?domain.}
	?article  :date   ?date. FILTER(?date >= "2502201920000"^^xsd:dateTime  && ?date <= "2502201921500"^^xsd:dateTime )
}
```




- Get all coverage intensity Averages during the last 1 hour of articles that talk about "Trump"?

- Get all Tone Averages during the last 1 hour of articles that talk about "Trump"?

- what is the tone of specific article in the last 1 hour about "trump"?

- Get all the images that are included in the articles about "trump" during the last 1 hour?

```
SELECT ?image 
WHERE
{
	?image rdf:type :Image.
	?image :talkabout "Donald Trump".
	?image  :date   ?date. FILTER(?date >= "2502201920000"^^xsd:dateTime  && ?date <= "2502201921500"^^xsd:dateTime )
}
```

- Get other images in social network websites that are related to the articles about "trump" during the last 1 hour?

- get the articles that exceeds limit of coverage about "Trump" during this time of 1 hour?

- Things that can be inferred:
	
	*Date +Intensity+Location+[Tone] --> Important Events in this Location.
	*Location-1+location-2+location-n Frequency +  keyword ---> Event that relates thes locations.
	*Specific Location+Tone || Intensity  ---> Important Events in this loaction.


### To Analyse for further queries

[ ] https://blog.gdeltproject.org/google-bigquery-gkg-2-0-sample-queries/
[ ] https://gigaom.com/2014/05/29/more-than-250-million-global-events-are-now-in-the-cloud-for-anyone-to-analyze/