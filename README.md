
# Wallpaper Changing Application 

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

![App Screenshot](https://github.com/gunishjain/WallpaperApp/blob/main/screenshots/1.png)

![App Screenshot](https://github.com/gunishjain/WallpaperApp/blob/main/screenshots/2.png)


## Tech Stack

**Frontend:** Android-Kotlin

**API:** https://www.pexels.com/api/
