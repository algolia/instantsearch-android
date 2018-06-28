---
title: InstantSearch from the backend
layout: main.pug
name: guide-custom-backend
category: main
withHeadings: true
navWeight: 5
---
## Who should use this guide


Advanced InstantSearch users may have the need to query Algolia’s servers from their backend instead of the frontend, while still being able to reuse InstantSearch widgets. Possible motivations could be for security restrictions, for SEO purposes or to enrich the data sent by the custom server (i.e. fetch Algolia data and data from their own servers). If this sounds appealing to you, feel free to follow this guide. Keep in mind though that we, at Algolia, recommend doing frontend search for performance and high availability reasons.


By the end of this guide, you will have learned how to leverage InstantSearch with your own backend architecture to query Algolia. Even if you're not using Algolia on your backend and still want to benefit from using InstantSearch, then this guide is also for you.


## A quick overview on how InstantSearch works


InstantSearch, as you probably know, offers reactive UI widgets that automatically update when new search events occur. Internally, it uses a `Searchable` interface that takes care of making network calls to get search results. The most important method of that `Searchable` is a simple `search()` function that takes in a parameter that contains all the search query parameters, and then expects a callback to be called with the search results that you get from your backend. Let's see how this works in action


## Basic implementation of using a custom backend


The most basic implementation of using a custom backend uses the `DefaultSearchClient` and requires you to implement just one method: `search(query:searchResultsHandler:)`. In this function, you use the query passed to you, make a network request to your backend server, transform the response into a `SearchResults` instance, and then finally call the `searchResultsHandler` callback with the searchResults. In case of error, you call the callback with the error. Here is an example using the Alamofire networking library.


``` java
public class DefaultCustomClient extends DefaultSearchClient {

    // 1
    public static class AsyncRequest extends AsyncTask<Query, Boolean, JSONObject> implements Request {
      // Implement your AsyncRequest to make the request to your backend.
    }

    @Override
    public Request search(@Nullable Query query, @Nullable final SearchResultsHandler<JSONObject> completionHandler) {
        // 2
        AsyncRequest request = new AsyncRequest(new SearchResultsHandler<JSONObject>() {
            @Override
            public void requestCompleted(final JSONObject content, final Exception error) {

                Handler h = new Handler(Looper.getMainLooper());
                Runnable r = new Runnable() {
                    @Override
                    public void run() {
                        if (error != null) {
                          // 3
                            completionHandler.requestCompleted(null, new AlgoliaException((error.getMessage())));
                        } else {
                          // 3
                            completionHandler.requestCompleted(content, null);
                        }
                    }
                };
                h.post(r);

            }
        });
        request.execute(query);
        return request;
    }
}
```


This is the simplest example and will work only if on your backend, you're calling Algolia and then just forwarding your result to the mobile app without doing any modification to the json data.


1- Get the query text from the Query parameter and do your request to the backend.


2- Make your request to your backend using the queryText parse in step 1.


3- Call the `requestCompleted` function in order to instruct InstantSearch about the new search event, in this case the arrival of new search results, or an error.


## Advanced implementation of using a custom backend


The above snippet only covers the case of doing a basic search of hits, with conjunctive (contrary to disjunctive) filtering. Here, we'll take a look at improving the structure of our custom backend class, as well as supporting disjunctive faceting.


Let's start with the code snippet:


```java

public class CustomClient extends SearchClient<JSONObject, JSONObject> {
    // 1
    @Override
    public JSONObject map(@NonNull Query query) {
      // map from query to your JSONObject that your backend expects. What is returned here will be used as a parameter when doing the request.
    }

    // 2
    @Override
    public JSONObject map(@NonNull Query query, @NonNull Collection<String> disjunctiveFacets, @NonNull Map<String, ? extends Collection<String>> refinements) {
        // map from query+disjunctiveFacets+refinements to your JSONObject that your backend expects. What is returned here will be used as a parameter when doing the request.
    }

    // 3
    @Override
    public JSONObject map(@NonNull JSONObject customSearchResults) {
        // map from your result from your custom backend to the JSONObject that InstantSearch expects

        if (customSearchResults == null) {
            return null;
        }
        JSONObject hitsContent = customSearchResults.optJSONObject("hits");
        if (hitsContent == null) {
            return null;
        }

        JSONObject obj = new JSONObject();
        try {
            obj.putOpt("hits", hitsContent.optJSONArray("hits")); // Required
            obj.putOpt("nbHits", hitsContent.optInt("total")); // Required
            obj.putOpt("query", "test");
            obj.putOpt("params", "testparams");
            obj.putOpt("processingTimeMS", customSearchResults.optInt("took"));
        } catch (Exception e) {
            return null;
        }
        return obj;
    }

    // 4
    public static class AsyncRequest extends AsyncTask<Query, Boolean, JSONObject> implements Request {
      // Implement your AsyncRequest to make the request to your backend.
    }

    // 5
    @Override
    public Request search(@Nullable JSONObject query, @Nullable final SearchResultsHandler<JSONObject> completionHandler) {
        AsyncRequest request = new AsyncRequest(new SearchResultsHandler<JSONObject>() {
            @Override
            public void requestCompleted(final JSONObject content, final Exception error) {

                Handler h = new Handler(Looper.getMainLooper());
                Runnable r = new Runnable() {
                    @Override
                    public void run() {
                        if (error != null) {
                            // 6
                            completionHandler.requestCompleted(null, new AlgoliaException((error.getMessage())));
                        } else {
                            // 6
                            completionHandler.requestCompleted(content, null);
                        }
                    }
                };
                h.post(r);

            }
        });
        request.execute(query);
        return request;
    }
}
```


1- Implement the basic param mapper function that converts a query to your parameter model. Make sure to take all the fields you need from the query parameter.


2- Implement the advanced param mapper function. It is the same as 3, but with 2 more parameters that you can use for your call: `disjunctiveFacets` and `refinements`.


3- Implement the result mapper function that converts your result model back to an Algolia `JSONObject` that can be understood by InstantSearch.


4- Make your request to your backend using the `JSONObject` provided in step 1 and 2.


5- Implement the search method, same idea as the basic implementation. The only difference is that now it provides your custom parameter model as its parameter.


6- Call the `requestCompleted` function in order to instruct InstantSearch about the new search event, in this case the arrival of new search results, or an error.
