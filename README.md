Recommended Configurations:
- <span style="background-color: yellow;">Android Studio latest (Koala)</span>
- Java 17

Project Description

This project builds a mobile application using the GitHub API to search for repositories, explore details, and navigate to contributors' tagged repositories. It adheres to modern Android development best practices, leveraging MVVM architecture, Jetpack Compose for a declarative UI, Room for offline data storage, Coroutines for asynchronous operations, Retrofit for network communication, and LiveData for reactive data updates.

Key Features

Search Functionality:
Employs the GitHub API to fetch repository results.
Integrates a search bar for user input.
Limits search results to 10 per page.


Home Screen:
Utilizes Jetpack Compose to construct a visually appealing interface.
Implements a RecyclerView with CardViews to display search results.
Enables saving the first 15 search results for offline browsing.
Provides pagination to load additional results incrementally.

<img src="https://github.com/kiran18995/Github_Search/assets/48232762/297e25a0-ca3f-4d54-a817-f9b3b3863abe" alt="My Image" width="200" height="400">

Repository Details Screen:
Navigates to a dedicated screen upon selecting a repository from the search results.
Displays detailed repository information, including:
Image (if available)
Name
Project Link (opens in a WebView)
Description
Contributors (clickable)
Lists contributors with the option to view their tagged repositories.

<img src="https://github.com/kiran18995/Github_Search/assets/48232762/7509d72c-7c60-434f-b003-e82357867bd4" alt="My Image" width="200" height="400">

Technical Stack

Android Studio as the integrated development environment (IDE) Latest Version Koala only
Kotlin as the primary programming language
Jetpack Compose for a declarative UI
Room for local data persistence
Coroutines and Flow for asynchronous operations and network requests
Retrofit for interacting with the GitHub API
LiveData for data observation and updates
MVVM Architecture for separation of concerns
GitHub API for repository search, details, and contributors' information

Root Directory

app/ (application code)
src/
main/
java/ (Kotlin code)
com.kiran.githubsearch/
data/ (data models, repositories, etc.)
di/ (dependency injection modules)
network/ (Retrofit services)
ui/ (screens, composables)
util/ (utility classes)
res/ (resources)
build.gradle (project and app build configurations)
gradle.properties (local project properties)
Offline Data Storage

The Room library facilitates offline data storage.
An appropriate entity class (e.g., RepositoryEntity) would be defined to represent stored data.
Room would then be used to create a local database and interact with it.
