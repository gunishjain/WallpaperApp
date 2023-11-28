# Wallpaper Changing Application

A Wallpaper Changing Application that allows users to fetch latest wallpapers and apply to homescreen and lockscreen, also allows user to save it locally. It is following MVVM Architecture with Jetpack Components.

Download App: https://play.google.com/store/apps/details?id=com.gunishjain.wallpaperapp

## Major Highlights

- MVVM Architecture
- Kotlin
- Dagger Hilt
- Retrofit
- Coroutines
- LiveData
- Viewbinding
- Pagination
- Room


<img src="https://github.com/gunishjain/NewsApp-MVVM-Architecture/blob/main/assets/MVVM-Arch.png">


## Features Implemented

- Fetching Latest Wallpapers
- Fetch Wallpapers Based on Categories
- Instant Searching of Wallpapers
- Saving Wallpapers locally using ROOM DB
- Download and Set Wallpapers
- Inapp Updates for Playstore
  
## Dependency Used:
- Recycler View for listing
```
implementation "androidx.recyclerview:recyclerview:1.3.1"

```
- Glide for image loading
```
implementation 'com.github.bumptech.glide:glide:4.15.1'
```
- Retrofit for networking
```
implementation("com.squareup.retrofit2:retrofit:2.9.0")
implementation 'com.squareup.retrofit2:converter-gson:2.9.0'
implementation("com.squareup.okhttp3:logging-interceptor:4.11.0")
```
- Android Lifecycle aware component 
```
implementation 'androidx.fragment:fragment-ktx:1.6.0'
implementation "androidx.lifecycle:lifecycle-viewmodel-ktx:2.6.1"
implementation "androidx.lifecycle:lifecycle-runtime-ktx:2.6.1"
```
- Dagger Hilt for dependency Injection 
```
implementation "com.google.dagger:hilt-android:2.44"
kapt "com.google.dagger:hilt-compiler:2.44"
```
- For RoomDB 
```
implementation "androidx.room:room-runtime:2.5.2"
kapt("androidx.room:room-compiler:2.5.2")
implementation("androidx.room:room-ktx:2.5.2")
```
- Card View and Size Unit
```
implementation("androidx.cardview:cardview:1.0.0")
implementation 'com.intuit.sdp:sdp-android:1.1.0'
implementation 'com.intuit.ssp:ssp-android:1.1.0'
```
- Paging library 
```
implementation "androidx.paging:paging-runtime:3.1.1"
```
- In App Updates
```
implementation 'com.google.android.play:app-update:2.1.0'
implementation 'com.google.android.play:app-update-ktx:2.1.0'
```

## Complete Project Structure

```
├───data
│   ├───api
│   ├───db
│   ├───models
│   └───repository
├───di
├───paging
├───ui
│   ├───activities
│   ├───adapters
│   ├───fragments
│   └───viewmodels
└───util
MyApp.kt

```

## Screenshots

<p align="center">
<img src="https://github.com/gunishjain/WallpaperApp/blob/main/screenshots/1.png" width="200" height="400">
<img src="https://github.com/gunishjain/WallpaperApp/blob/main/screenshots/3.png" width="200" height="400">
<img src="https://github.com/gunishjain/WallpaperApp/blob/main/screenshots/4.png" width="200" height="400">
<img src="https://github.com/gunishjain/WallpaperApp/blob/main/screenshots/6.png" width="200" height="400">
</p>


> [!IMPORTANT]
> Wallpapers are fetched from Pexels API: https://www.pexels.com/api/

