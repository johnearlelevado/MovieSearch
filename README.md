## MovieSearch

### Architecture Pattern
  - The architecture used is MVVM, I chose this over MVP/MVC because it has the separation of concerns like MVP but doesn't require the instance of the view. It also makes unit testing easier. On the MVVM, the views subscribe to events from the ViewModel via livedata or any observable instances. On top of that, Google even created a native ViewModel class that is lifecycle aware and will help restore data when configuration changes. All these things helps minimizes the risk of memory leaks and state loss. The only disadvantage of using MVVM is has a big overhead for beginners. Aside from learning the MVVM architecture, you will also need some knowledge on Reactive Programming (RxJava/RxAndroid/RxKotlin).

### Navigation
  - It made more sense to just use two the Activities `MainActivity` and `DetailActivity` instead of Jetpack Navigation component for simplicity since the app doesn't have too many UIs to navigate.

### Tech stack
  - Retrofit2
  - Dagger Hilt
  - View Binding
  - ViewModel
  - LiveData

### Unit Test
  - ViewModel Unit Test

### Download APK
  - https://drive.google.com/file/d/1TbOwnl-0Qix_0XXv9P0ggypNYIE7Z-7A/view?usp=sharing
