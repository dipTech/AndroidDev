Grid Image Search
=========

User can enter a search query that will display a grid of image results retreived by the Google Image Search API.

Creator [Dipankar]  Date [18th June, 2014]

> Mandatory Implementation
==========================
* Basic feature to search query using Google Image Search API.
* User can click on "settings" icon (right on Action Bar) which allows selection of advanced search       options to filter results.
* Configurable advanced search filters using spinner, such as:
    Size (small, medium, large, extra-large)
    Color filter (black, blue, brown, gray, green, etc...)
    Type (faces, photo, clip art, line art)
    Site (yahoo.com)
* Search filters are stored in file store and subsequent searches will use the saved filters for future   search, till options again modified by end users.
* User can tap on any image in results to see the image in another activity.
* User can scroll down “infinitely” to continue loading more image results.
---

> Enhancement
=============
* Error handling to check if internet is available - network failure.
* Improve the user interface with styling and coloring.
* User can zoom or pan images displayed in full-screen detail view
-----

Version
----
1.0


Time spent
==
2 days spent in total. 

Reason for unexpected work delay
=====
Somehow my R file is lost and I cannot bring it back. So have to develop a new application again.

User stories:
-----------
1. This application searches images.
2. Just input query and press button to execute your search.
3. Several options are available in menu invoked by top right gear icon button.
4. Available Setting Options mentioned above.
5. Tapping one of the results of your search invokes an enlarged thumbnail.
* N.B: The enlarged image is not perfect and may not satisfy user experience.
* Implemented by SmartImageView, will provide better enlargement in next version.

License
----
CodePath

Demo
---
Demo
---
![Image Search Demo](DipImageSearch.gif)
