# AndroidElasticSearchDataManager

AndroidElasticSearchDataManager is a simple to use library for Android for doing [CRUD](https://en.wikipedia.org/wiki/Create,_read,_update_and_delete) operations on [Elastic Search](https://www.elastic.co). Additionally, it supports features like offline persistent queuing and caching as well.

The ideal way to interface with Elastic Search through this library is via the QueuedDataManager that offers all the above mentioned functionalities. The QueuedDataManager internally composes of other DataManager implementations. These other implementations, like LocalDataManager and HttpDataManager, can be used for perfoming local file IO or HTTP IO with an ElasticSearch-like API, respectively, as well.

### Building & Testing

This is a Gradle project build in Android Studio. Open the project in the IDE for building andtesting.

### Usage & Documentation

A compiled .aar is kept in the /bin directory for direct importing. The usage documentation is present in the project as Javadocs in the /javadocs directory. A sample trading app using this library as an imported .aar is located [here](https://github.com/CMPUT301F15T03/301p).

### Credits

The offline persistent queuing functionality has been implemented through the [android-priority-jobqueue](https://github.com/yigit/android-priority-jobqueue) project.

### License

[Apache v2.0](https://github.com/udeyrishi/AndroidElasticSearchDataManager/blob/master/LICENSE)