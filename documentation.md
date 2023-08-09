# Documentation
I have chosen a research project from the journal club to implement. Initially, I selected several intriguing research papers: [FaceTouch](https://dl.acm.org/doi/10.1145/2984511.2984576)[1], [Lip Interaction](https://dl.acm.org/doi/10.1145/3242587.3242599)[2], and [Screenmatcher](https://dl.acm.org/doi/10.1145/3473856.3474014)[3]. I ruled out FaceTouch due to its reliance on VR HMD equipment, capacitive screens, and buttons for connecting the capacitive screen. I also decided against the lip-controlled mobile app as it requires extensive machine learning training to improve lip recognition accuracy. Eventually, I settled on Screenmatcher, not only because I quickly grasped its implementation process, but also due to its practicality.

### Implementation Phase
I synchronized Android and Python programming aspects. I divided the entire Screenmatcher process into smaller chunks: capturing photos with an Android device and sending the photo data to a computer, where the computer matches the image with the screen, then sends the processed image back to the Android side. I initially focused on setting up a basic receiving program on the computer. Then, I began working on the Android part. My initial goal was to enable photo capture on the Android device. After attempting to create an in-app camera, I opted for the simplest approach: utilizing the built-in camera app. Next, I aimed to display the captured photo after taking it, and once that was achieved, I proceeded to enable saving the photo to the gallery. With this basic framework in place, I moved on to implementing the functionality to send messages to and from the computer. I envisioned a process where the Android device sends image data to the computer, which processes the data and sends back the manipulated image. Along the way, I encountered two challenges:

1. Issue with incomplete base64 string reception on the PC side.
   I discovered that Android sends base64 strings in wrapped form via the Socket, causing the Python side to stop receiving after a single line.
   
2. Android device's inability to receive messages from the computer.
   The solution involved uploading the image data that Python intended to send to the Android device to an HTTP server. The Android side would then initiate the download task for the photo data sent by the computer through the HTTP server. This approach achieved the desired result of displaying the processed photo on the Android device.

For image matching, I directly utilized OpenCV's **[Template Matching](https://docs.opencv.org/3.4/d4/dc6/tutorial_py_template_matching.html)**[4] method. After testing six comparison methods, I settled on using the **`cv.TM_CCOEFF_NORMED`** method. Regarding the matched screenshot's area, due to image compression applied during Android image transmission, the matched area ended up smaller. Thus, I expanded the matching area by two times and used it as the basis for cropping the screenshot. This cropped image was uploaded to the HTTP server for the Android side to download.

### Challenges and Further Enhancements
1. I implemented the Android device's return to the main screen using a somewhat indirect method involving **PendingIntent** and **AlarmManager**. This approach restarts the application when users choose to return or save after the screenshot match is complete.
2. The computer's server doesn't continuously listen for messages from the Android device, leading to the need to restart `app.py` each time the screenshot match functionality is needed.
3. Improving the accuracy of the matching process remains a future goal.

### Application Scenarios
A video is provided to demonstrate the potential application scenarios.

### Reference
[1] Gugenheimer, J., Dobbelstein, D., Winkler, C., Haas, G., & Rukzio, E. (2016, October). Facetouch: Enabling touch interaction in display fixed uis for mobile virtual reality. In Proceedings of the 29th Annual Symposium on User Interface Software and Technology (pp. 49-60).
[2] Sun, K., Yu, C., Shi, W., Liu, L., & Shi, Y. (2018, October). Lip-interact: Improving mobile device interaction with silent speech commands. In Proceedings of the 31st Annual ACM Symposium on User Interface Software and Technology (pp. 581-593).
[3] Schmid, A., Fischer, T., Weichart, A., Hartmann, A., & Wimmer, R. (2021). ScreenshotMatcher: Taking Smartphone Photos to Capture Screenshots. In Proceedings of Mensch und Computer 2021 (pp. 44-48).
[4] Template Matching: https://docs.opencv.org/3.4/d4/dc6/tutorial_py_template_matching.html
