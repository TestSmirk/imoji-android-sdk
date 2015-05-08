# Imoji Android SDK


## Android Studio Setup
1. Put the sdk aar file into ```libs``` folder

2. In your ```builds.gradle``` file, add the following dependencies:

        
        compile 'com.squareup.retrofit:retrofit:1.9.0'
        compile 'com.squareup.picasso:picasso:2.5.2'
        compile 'com.google.code.gson:gson:2.3.1'
        compile 'com.imojiapp.imoji:imoji-sdk:0.1.1@aar' //Note: adjust the version to match the version you dropped into the libs folder
        

### Simple Integration
- Initialize the Api in your Application's onCreate() method.
```java
public class MyApplication extends Application {
 @Override
    public void onCreate() {
        super.onCreate();
        ImojiApi.init(this, "YOUR_CLIENT_ID_HERE", "YOUR_CLIENT_SECRET_HERE");
    }
}
```

- Make an api call:
```java
public class MyActivity extends Activity {
    @Override
    public void onCreate() {
        ImojiApi.with(this).getFeatured(new Callback<List<Imoji>>() {
            @Override
            public void onSuccess(List<Imoji> result) {
                //Bind the results to an adapter of sorts
            }
  
            @Override
            public void onFailure() {
                Log.d(LOG_TAG, "failure");
            }
        });
    }
  }  
```
or for a more proper production ready example:
```java
public class MyActivity extends Activity {

    @Override
    public void onCreate() {
        ImojiApi.with(this).getFeatured(new FeatureCallback(this));
    }
    
    public static class FeatureCallback extends Callback<List<Imoji>>{
        
        private WeakReference<MyActivity> mMyActivity;
        
        public FeatureCallback(MyActivity activity){
            mMyActivity = new WeakReference(activity);
        }
        
        @Override
        public void onSuccess(List<Imoji> result) {
            MyActivity a = mMyActivity.get();
            if(a != null){
                //update the ui, bind to adapter, etc
            }
        }

        @Override
        public void onFailure() {
            Log.d(LOG_TAG, "failure");
        }
    }
  }  
```

### Advanced Integration
- You can configure the ImojiApi instance before initialization to set defaults or modify its behavior:
```java
public class MyApplication extends Application {
 @Override
    public void onCreate() {
        super.onCreate();
        ImojiApi apiInstance = new ImojiApi.Builder().defaultResultCount(60).build();
        ImojiApi.init(this, "YOUR_CLIENT_ID_HERE", "YOUR_CLIENT_SECRET_HERE", apiInstance);
    }
}
```

