# Imoji SDK Change Log

##2.1.2
* Avoid processing InterruptedException's in WrappedAsyncTasks like ExecutionException's. They'll occur typically on cancel's which doesn't merit the need for exception handling for the caller.
* Adds support for relatedCategories in ImojisResponse

##2.1.1
* Fix for #14 - For older single processor phones, ensure our ExecutorService in ApiTask doesn't get exhausted waiting for inner tasks to complete
* Fix for #12 - Remove the app_name metadata info from AndroidManifest.xml

##2.1.0
* Adds a method to Session to fetch attribution for one or more Imoji's
* Adds attribution details (URL type, related tags, etc) to the Attribution object
* Adds support for fetching categories relevant to a provided search sentence
* Adds license style's to Imoji's and Categories (either non-commerical or print)

##2.0.3
* Avoid sending null tags arrays when creating new Imoji references
* Avoid calling onError multiple times in WrappedAsyncTask

##2.0.2
* Makes sure BuildConfig is in the new io.imoji.sdk namespace

##2.0.1
* Updated ApiTask to handle errors properly and let callers override onError
* If the client credentials change, invalidate the oauth credentials and get a new token
* Imoji is Parcelable again

##2.0.0
* Revamped Imoji Android SDK to work much like the iOS version!
* Introduces Artist content, gif image url's and non-bordered images
* Callers of Imoji endpoints now receive an ApiTask instance which can be executed as an AsyncTask or with an ExecutorService of your choosing.

##0.7.5
* Exposes 320 and 512 size Imoji image urls. 
* Deprecates getThumbUrl and getUrl in favor of a more explicit getImageUrl method.

##0.7.4
* Simplify upload Imoji image process.
* Set Imoji-SDK-Version to 0.1.0 (the current server version supported)

##0.7.3
* Remove allowBackup=true

##0.7.2
* Set target sdk back to 22

##0.7.1
* Fix issue with broken create flow

## 0.7.0
* Removed external networking library dependencies
* Implemented a networking stack based on Android's HttpUrlConnection

## 0.6.1
* New api call to report abusive imojis

## 0.6.0
* New endpoint to create an imoji from a bitmap

## 0.5.3
* Added a new search method to the API so that you can pass a map of params
* You can now perform a sentance search with the search api

## 0.5.2
* Add 2nd Imoji Package as one able to grant external access

## 0.5.1
* Update fetchImojisById response structure 

## 0.5.0
* Changes for releasing to maven

## 0.4.2
* Update groupId to 'com.imojiapp'  and the artifact id to 'imoji-sdk'
* Added sources and jars
* PGP Signatures for artifacts

## 0.4.1
* Fix broken ImojiCategory as a result of 0.4.0 changes
* Deprecated ImojiCategory.getId() in favor of ImojiCategory.getSearchText()

## 0.4.0
* This is a major update and a few api method calls have changed. At its core, this release removes the rendering burden off the client, so almost all changes are related to that.
* Fixed a bug when handling errors with Retrofit

## 0.3.4
* Add support for GingerBread, MinSdk=10

## 0.3.3
* Prevent initialization of Picasso singleton

## 0.3.2
* Default Picasso instance can be passed into the SDK through ImojiApi.Builder
* Added Getter/Setter for Picasso instance

## 0.3.1
* Catch ActivityNotFoundException when launching Google Play Store intents

## 0.3.0
* Option to use koush/ion (networking & image loading) instead of picasso and retrofit

## 0.2.2
* Launch Activities using `Intent.FLAG_ACTIVITY_NEW_TASK` because the API context is not an `Activity` context
* Fix crash that resulted from intermittent network failure body parsing

## 0.2.1
* `ImojiApi.getImojiCategory` now takes an optional `ImojiCategory.Classification`

## 0.2.0
* Handle user related calls internally such that there's no need to handle that flow; however, the option is still there
* New endpoint to add an imoji to a user's collection

## 0.1.7
* Added ```getImojisById``` method to sdk

## 0.1.6
* Prevent proguard from stripping out `ImojiApi.Builder` and  `ImojiCategory`
* Removed launcher icon from library project
