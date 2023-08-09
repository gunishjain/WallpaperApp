
# Wallpaper Changing Application 

Play Store Link: https://play.google.com/store/apps/details?id=com.gunishjain.wallpaperapp

Android Application which allows users to set wallpapers for Homescreen and Lockscreen.

It is utilising:

1. Dagger-Hilt : Dependency Injection to inject room and retrofit instance to activities and fragments.

2. Retrofit : Making HTTP connection with the rest API and convert Photo json file to Kotlin
object.

3. Room : Save Wallpaper data in local database.

4. MVVM & LiveData : Seperate logic code from views and save the state in case the screen configuration changes.

5. Coroutines : do some code in the background.

6. View binding : instead of inflating views manually view binding will take care of that.

7. Glide : Catch images and load them in ImageView.




## Screenshots

<p align="center">
<img src="https://github.com/gunishjain/WallpaperApp/blob/main/screenshots/1.png" width="200" height="400">
<img src="https://github.com/gunishjain/WallpaperApp/blob/main/screenshots/2.png" width="200" height="400">
</p>

## Tech Stack

**Frontend:** Android-Kotlin
**UI:** XML
**API:** https://www.pexels.com/api/

