# NYCSchools
Android native app to provide information on NYC High schools.

## Purpose
It shows the use of **clean MVVM architecture** to display NYC High schools list and details fetched from [NYC OpenData](https://data.cityofnewyork.us/Education/DOE-High-School-Directory-2017/s3k6-pzi2).
The fetching mechanism is handled by a repository layer.

## Features
The school list will be fetched using a network call and displayed in a RecyclerView. Each school card shows the school's name, address and phone number that will be downloaded only once and store in memory for any subsequent requests. Additionally, It integrates a pull to refresh functionality so that the user can force refresh the data from the internet. Once the user clicks on a school item it will display the school details information and a SAT chart.

## Android Libraries used

I used some useful android libraries to make the code clean, elegant, maintainable, scalable and make a clear separation of responsibilities.
Here the libraries that has been used :

- **Retrofit** : Handles Network calls. It simplifies the integration of REST based architecture using HTTP based annotations. It makes easy to retrieve data from the internet and using the converters it deserialized the response in Java model in a elegant manner. I also allows to transform the retrofit response into Rx Observable using an adapter.
- **Coroutines** is a light weight threads for asynchronous programming. It also provides a wealth of other possibilities such as concurrency, flows, etc
- **MPAndroidChart** Chart framework. To display the SAT chart bar.
- **Gson** to easily transform POJO <-->JSON. To avoid having the boilerplate of Json serialization/deserialization process. Based on annotation in my Item class, it will transform the Json payload into Java model.
- **Data Binding**  - Using this, helps me to remove common boilerplate view code like findViewById, and directly link data to the UI which make the code cleaner by significantly reducing the lines of boilerplate code written.

## Setup

### Clone the Repository

As usual, get started by cloning the project on your local machine:  
```
$ git clone https://github.com/AudreyAnge/NYCSchoolsApp.git
```
You can also create a new project from Android Studio by importing this project from git using the above git repository url.

### Prerequisites

This project uses JDK 8 and has been built using android SDK 32. It also uses the [androidx.*](https://developer.android.com/jetpack/androidx) package libraries.

## Authors

- **Audrey Maumoin**
Senior Android Developer

## Acknowledgments
I hope that you will have a good time navigating through the project.

**Happy Reading :smiley:**
