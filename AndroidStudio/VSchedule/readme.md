 ![alt text](https://github.com/MaorAssayag/Additional-Apps-Projects/blob/master/AndroidStudio/VSchedule/screenshots/v_logo.png)
# VSchedule - Task Manager

## What is VSchedule?
This application allows users to keep track of tasks. These tasks include a description and the desire time to the notification. Every task can be deleted. 

This app is my first introduction to AndroidStudio. the end goal is to get experience with the implemention of such idea, which will be develop in the future to aid-App for people with disabilities that need such help with remembering those daily tasks.

![Output sample](https://github.com/MaorAssayag/Additional-Apps-Projects/blob/master/AndroidStudio/VSchedule/screenshots/open_gif.gif)![Output sample](https://github.com/MaorAssayag/Additional-Apps-Projects/blob/master/AndroidStudio/VSchedule/screenshots/clock_gif.gif)


## APIs and Architectures Used:

* This project is greatly inspired by Clean Architecture Principles. 
Model View Presenter (Passive View). Keep Views dumb and simple.

* Clean Architecture. To complex to summarize in a few sentences; but the basic idea is having three layers of an Enterprise App (Presentation, Domain (Use Case), and Data (Service) layers). This seperation does increase the number of Classes, but the advantage is in simplicity, testability, and modularity to name a few.

* System Services Like AlarmManager


