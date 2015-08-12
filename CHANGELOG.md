# Imoji SDK Change Log

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
