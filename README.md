# ProjetWeek2
## ProjetWeek2 - *NYT_week2*

**NYTimes** is an android app that allows a user to search for articles on web using simple filters. The app utilizes [New York Times Search API](http://developer.nytimes.com/docs/read/article_search_api_v2).

Time spent: **25+** hours spent in total

## User Stories

The following **required** functionality is completed:

* [x] User can enter a search query that will display a grid of news articles using the thumbnail and headline from the New York Times Search API. (3 points)
* [x] User can click on "filter" icon which allows selection of advanced search options to filter articles. (3 points)
  * An example of a query with filters (begin_date, sort, and news_desk) applied can be found here. Full details of the API can be found on this article search README.
* [x] User can configure advanced search filters such as: (points included above)
  * [x] Begin Date (using a date picker)
  * [x] Sort order (oldest or newest) using a spinner dropdown
  * [x] News desk values (Arts, Fashion & Style, Sports) using checkboxes
* [x] Subsequent searches will have any filters applied to the search results. (1 point)
* [x] User can tap on any article in results to view the contents in an embedded browser. (2 points)
* [x] User can scroll down "infinitely" to continue loading more news articles. The maximum number of articles is limited by the API search. (1 point)

The following **optional** features are implemented:

* [x] Implements robust error handling, [check if internet is available](http://guides.codepath.com/android/Sending-and-Managing-Network-Requests#checking-for-network-connectivity), handle error cases, network failures
* [x] Used the **ActionBar SearchView** or custom layout as the query box instead of an EditText
* [x] User can **share an article link** to their friends or email it to themselves
* [x] Replaced Filter Settings Activity with a lightweight modal overlay
* [x] Improved the user interface and experiment with image assets and/or styling and coloring

The following **bonus** features are implemented:

* [x] Use the [RecyclerView](http://guides.codepath.com/android/Using-the-RecyclerView) with the `StaggeredGridLayoutManager` to display improve the grid of image results
* [x] For different news articles that only have text or only have images, use [Heterogenous Layouts](http://guides.codepath.com/android/Heterogenous-Layouts-inside-RecyclerView) with RecyclerView
* [ ] Use Parcelable instead of Serializable using the popular [Parceler library](http://guides.codepath.com/android/Using-Parceler).
* [ ] Leverages the [data binding support module](http://guides.codepath.com/android/Applying-Data-Binding-for-Views) to bind data into layout templates.
* [ ] Replace all icon drawables and other static image assets with [vector drawables](http://guides.codepath.com/android/Drawables#vector-drawables) where appropriate.
* [x] Replace Picasso with [Glide](http://inthecheesefactory.com/blog/get-to-know-glide-recommended-by-google/en) for more efficient image rendering.
* [ ] Uses [retrolambda expressions](http://guides.codepath.com/android/Lambda-Expressions) to cleanup event handling blocks.
* [x] Leverages the popular [GSON library](http://guides.codepath.com/android/Using-Android-Async-Http-Client#decoding-with-gson-library) to streamline the parsing of JSON data.
* [ ] Leverages the [Retrofit networking library](http://guides.codepath.com/android/Consuming-APIs-with-Retrofit) to access the New York Times API.
* [ ] Replace the embedded `WebView` with [Chrome Custom Tabs](http://guides.codepath.com/android/Chrome-Custom-Tabs) using a custom action button for sharing. (_**2 points**_)



## Video Walkthrough

Here's a walkthrough of implemented user stories:

<img src='assets/NYT.gif' title='Video Walkthrough' width='350' alt='Video Walkthrough'/>



<img src='http://imgur.com/u0PdA6j.png'  title='Video Walkthrough' width='250' alt='Video Walkthrough'/>....<img src='http://imgur.com/2eA9wUl.png' title='pic' width='250' alt='pic' />....<img src='http://imgur.com/sxtVNZM.png' title='pic' width='250' alt='pic'/>


GIF created with [LiceCap](http://www.cockos.com/licecap/).

## Notes

Good job

## Open-source libraries used

- [Android Async HTTP](https://github.com/loopj/android-async-http) - Simple asynchronous HTTP requests with JSON parsing
- [Picasso](http://square.github.io/picasso/) - Image loading and caching library for Android
- [Glide](https://github.com/bumptech/glide) - Image loading library
- [Retrofit](https://github.com/square/retrofit) - Http Client
- [Gson](https://github.com/google/gson) - Java serialization/deserialization library
- [Parceler](https://parceler.org/) - Android Parcelable code generator

## License

    Copyright [Modele] [Dominique Charles]

    
