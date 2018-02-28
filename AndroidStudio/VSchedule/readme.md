What is VSchedule?




[![Demo CountPages alpha](https://gifs.com/gif/ZVgGJJ)]

APIs and Architectures Used:

This project is greatly inspired by Clean Architecture Principles. Now that I'm more comfortable with RxJava and Dagger 2, I'm confident that I'll be able to execute a modular and highly testable Application on the Android Platform. This project is an attempt to make that idea into an Open Source App.

Architecture patterns:

    Model View Presenter (Passive View). Keep Views dumb and simple, so that they don't need to be tested much, if at all (I may write some Espresso Tests, but it isn't high on my priority list).

    Clean Architecture. To complex to summarize in a few sentences; but the basic idea is having three layers of an Enterprise App (Presentation, Domain (Use Case), and Data (Service) layers). This seperation does increase the number of Classes, but the advantage is in simplicity, testability, and modularity to name a few.

    Dependency Injection Layer. This Layer satisfies creation of Dependencies (such as ReminderService.java) so that each part of the App has what it needs, when it needs it. This also decouples Object creation from Classes which shouldn't really be creating Objects in the first place (Seperation of Concerns).

    Dagger 2. D.I. Framework which I use to Inject Presenters, as well as to Inject Objects into my Service Layer.

    RxJava/RxAndroid 2 for concurrency. Rx allows me to create Data Streams which change over time, based on what happens in each Layer.

    Mockito to help with Unit Testing

    Realm for storing Reminders

    Plenty of System Services Like Vibrator, AlarmManager, MediaPlayer, PowerManager.WakeLock


