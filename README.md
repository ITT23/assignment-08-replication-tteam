##README

# ScreenshotMatcher

![Ttransfer Logo](https://github.com/ITT23/assignment-08-replication-tteam/blob/master/android/app/src/main/res/mipmap-hdpi/frame_01.png)

This project is inspired by the work of Schmid, A., Fischer, T., Weichart, A., Hartmann, A., & Wimmer, R. (2021), focusing on creating an application that allows smartphones to capture screenshots of computer screens. Currently, the application is in its preliminary stage and requires further work in terms of refactoring, bug fixing, and more. For screenshot matching, I'm utilizing the template matching method from OpenCV. More details about the method can be found in the [OpenCV documentation](https://docs.opencv.org/3.4/d4/dc6/tutorial_py_template_matching.html).

## Installation and Usage

### Mobile App (Android)
Currently, the mobile app only supports Android devices. You can download the latest `ttransfer.apk` from the [release section](https://github.com/ITT23/assignment-08-replication-tteam/releases/tag/v1.0.0). Make sure to grant the app permissions to access the camera and device storage. You can manually enter the host address and port number in mobile app.

### Desktop
The desktop component requires your computer to run the `app.py` file found in the project folder. Before running, check your computer's IPv4 address and adjust the `HOST` variable in the `app.py` file accordingly. Also, ensure that you have Python installed on your computer, as the application is developed using Python. If you haven't installed Python, you can download it from the official [Python](https://www.python.org/downloads/) website. You can also customize the port numbers in app.py according to your needs. Please ensure that the port number you set on both the mobile app and the app.py file on your computer are the same.

Additionally, make sure that your smartphone and computer are connected to the same Wi-Fi network. After these configurations, you can run the `app.py` file to start the server. The mobile app will connect automatically when `app.py` is running. 

The crucial part is to keep the port consistent between the mobile app and the computer. Once the host and port are configured, you can use the mobile app to capture screenshots. The captured screenshots will be displayed on the mobile after taking the photo. You can save the photo to your phone's gallery by clicking the "Save" button.

Please note that this project is a work in progress, and contributions are welcome.

### App Preview
![Start page](https://github.com/ITT23/assignment-08-replication-tteam/blob/master/readme%20photo/start.jpg)
![Configure Host and Port](https://github.com/ITT23/assignment-08-replication-tteam/blob/master/readme%20photo/connect.jpg)
![Screenshot match result](https://github.com/ITT23/assignment-08-replication-tteam/blob/master/readme%20photo/result.jpg)

**Sources for Mobile Icon and Startup Page Background:**
- [Icon & Background Source](https://giphy.com/gifs/nadrient-90s-80s-computer-l41lMAzNZfYAiyR0s)

## Acknowledgments
This project is based on the research published in the paper [ScreenshotMatcher - Taking Smartphone Photos to Capture Screenshots (2021)](https://hci.ur.de/publications/screenshotmatcher_-_taking_smartphone_photos_to_capture_screenshots_2021).