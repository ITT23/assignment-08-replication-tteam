# https://docs.opencv.org/3.4/d4/dc6/tutorial_py_template_matching.html
''''''
import cv2 as cv
import numpy as np
import os
import matplotlib.pyplot as plt

margin = 200

script_directory = os.path.dirname(os.path.abspath(__file__))
image_folder_relative_path = '../screenshot_matcher'
image_folder_path = os.path.normpath(os.path.join(script_directory, image_folder_relative_path))
image_relative_path = 'main.png'
image_path = os.path.join(image_folder_path, image_relative_path)
if cv.imread(image_path) is not None:
    img = cv.imread(image_path, cv.IMREAD_GRAYSCALE)
    img2 = img.copy()
template_image_relative_path = 'template.png'
template_path = os.path.join(image_folder_path,template_image_relative_path)
if cv.imread(template_path) is not None:
    template = cv.imread(template_path, cv.IMREAD_GRAYSCALE)


w, h = template.shape[::-1]

methods = ['cv.TM_CCOEFF', 'cv.TM_CCOEFF_NORMED', 'cv.TM_CCORR_NORMED', 'cv.TM_SQDIFF_NORMED']
for meth in methods:
    img = img2.copy()
    method = eval(meth)
    # Apply template Matching
    res = cv.matchTemplate(img,template,method)
    min_val, max_val, min_loc, max_loc = cv.minMaxLoc(res)
    # If the method is TM_SQDIFF, take minimum
    if method in [cv.TM_SQDIFF]:
        top_left = min_loc
    else:
        top_left = max_loc
    #top_left = (top_left[0] - margin, top_left[1] - margin)
    #bottom_right = (top_left[0] + w + margin, top_left[1] + h + margin)
    bottom_right = (top_left[0] + w, top_left[1] + h)
    cv.rectangle(img,top_left, bottom_right, 255, 2)
    plt.subplot(121),plt.imshow(res,cmap = 'gray')
    plt.title('Matching Result'), plt.xticks([]), plt.yticks([])
    plt.subplot(122),plt.imshow(img,cmap = 'gray')
    plt.title('Detected Point'), plt.xticks([]), plt.yticks([])
    plt.suptitle(meth)
    plt.show()

'''
import cv2
import os
import numpy as np

def open_image(image_name):
    script_directory = os.path.dirname(os.path.abspath(__file__))
    image_folder_relative_path = '../screenshot_matcher'
    image_folder_path = os.path.normpath(os.path.join(script_directory, image_folder_relative_path))
    image_relative_path = image_name + '.png'
    image_path = os.path.join(image_folder_path, image_relative_path)
    if cv2.imread(image_path) is not None:
        return cv2.imread(image_path)

main_image = open_image("main")
main_image_gray = cv2.cvtColor(main_image, cv2.COLOR_BGR2GRAY)
template_image = open_image("template")

# Perform template matching using cv2.matchTemplate()
result = cv2.matchTemplate(main_image, template_image, cv2.TM_CCOEFF_NORMED)

# Find the location of the best match
min_val, max_val, min_loc, max_loc = cv2.minMaxLoc(result)
top_left = max_loc
bottom_right = (top_left[0] + template_image.shape[1], top_left[1] + template_image.shape[0])

# Draw a rectangle around the matched region on the main image
cv2.rectangle(main_image, top_left, bottom_right, (0, 255, 0), 2)

# Display the main image with the bounding box
cv2.imshow('Matched Image', main_image)
cv2.waitKey(0)
cv2.destroyAllWindows()
'''