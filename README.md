# Imoji Android SDK


## Android Studio Setup

1. In your ```builds.gradle``` file, add the following dependencies:
        
        compile ('com.imoji.sdk:imoji-sdk:+@aar') {
            transitive=true
        }

### Simple Integration

- Initialize the Api in your Application's onCreate() method.

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

- Make an api call:

```java
public class MyActivity extends Activity {
    @Override
    public void onCreate() {
        ImojiSDK.getInstance()
                .createSession(getApplicationContext())
                .getFeaturedImojis()
                .executeAsyncTask(new ApiTask.WrappedAsyncTask<ImojisResponse>() {
                    @Override
                    protected void onPostExecute(ImojisResponse imojisResponse) {
                        //Bind the results to an adapter of sorts
                    }
                });
    }
  }  
```


