# Imoji Android SDK

The Imoji Android SDK is a set of easy to integrate Java classes for integrating Imoji stickers into your application. Much of the codebase is built using standard Android libraries with the exception of the following libraries:

* [GSON](https://github.com/google/gson) - For faster processing of JSON results from the Imoji Rest API
* [Support Annotations](http://tools.android.com/tech-docs/support-annotations) - To enfore null/nonnull checks throughout the codebase
* AppCompat - For backward compatability for older Android builds

### Prerequisites

You'll need to grab developer keys prior to integration. Sign up for a free developer account at [https://developer.imoji.io](https://developer.imoji.io) to get your keys.

### Setup

Integrating the libraries can be done in multiple fashions:

* [Download the latest](http://search.maven.org/#search%7Cgav%7C1%7Cg%3A%22io.imoji.sdk%22%20AND%20a%3A%22imoji-sdk%22) build from Maven Central manually
* Add the following to your Gradle build file:
```
dependencies {
        compile ('io.imoji.sdk:imoji-sdk:+@aar') {
            transitive=true
        }
}
```

### Authentication

Initiate the client id and api token for ImojiSDK whe your application launches:

```java
public class MyApplication extends Application {
 @Override
    public void onCreate() {
        super.onCreate();
        ImojiSDK.getInstance()
                .setCredentials(UUID.fromString("YOUR_CLIENT_ID_HERE"), "YOUR_CLIENT_SECRET_HERE");
    }
}
```

### API Examples

Call **createSession** in **ImojiSDK** to get a reference to the main class responsible for making API requests. Every API call with **Session** returns an **ApiTask** object that can be executed as an **AsyncTask** or dispatched to an ExecutorService if you wish.

In most cases, using the AsyncTask route is the simplest:

```java
ImojiSDK.getInstance()
        .createSession(getApplicationContext())
	.getFeaturedImojis()
	.executeAsyncTask(new ApiTask.WrappedAsyncTask<ImojisResponse>() {
	    @Override
	    protected void onPostExecute(ImojisResponse imojisResponse) {
	        //Bind the results to an adapter of sorts
	    }
	});
```

You can optionally take the same API call and pass it to an ExecutorService. This will call **FutureTask.get** and block the current thread.

```java
ExecutorService executorService = Executors.newSingleThreadExecutor();
try {
        ImojisResponse imojisResponse = ImojiSDK.getInstance()
                .createSession(getContext())
                .getFeaturedImojis()
                .executeImmediately(executorService);

        List<Imoji> imojis = imojisResponse.getImojis();

} catch (ExecutionException | InterruptedException e) {
        // handle error
}
```

####Cancelling requests

ApiTask.executeAsyncTask returns the AsyncTask itself that can easily be cancelled:

```java
AsyncTask<Future<ImojisResponse>, Void, ImojisResponse> task = 
ImojiSDK.getInstance()
        .createSession(getContext())
        .getFeaturedImojis()
        .executeAsyncTask(new ApiTask.WrappedAsyncTask<ImojisResponse>() {
            @Override
            protected void onPostExecute(ImojisResponse imojisResponse) {

            }
        });
task.cancel(true);
        
```

### Downloading Imojis

The SDK returns full URL's with size metadata for each imoji image but does not provide built in classes for downloading the contents. This can be easily libraries such as [Picasso](http://square.github.io/picasso/) as seen here:

```java
Picasso.with(getContext()).load(imoji.getStandardThumbnailUri());
```
### Java Docs

For future reference, check out the full Java docs here:
[http://www.javadoc.io/doc/io.imoji.sdk/imoji-sdk/](http://www.javadoc.io/doc/io.imoji.sdk/imoji-sdk/2.0.1)
