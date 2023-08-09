# https://docs.opencv.org/3.4/d4/dc6/tutorial_py_template_matching.html

import cv2 as cv
import numpy as np
import os
import matplotlib.pyplot as plt

factor = 2

def match_screenshot():
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

    img = img2.copy()
    method = eval('cv.TM_CCOEFF_NORMED')
    res = cv.matchTemplate(img,template,method)
    min_val, max_val, min_loc, max_loc = cv.minMaxLoc(res)
    top_left = max_loc
    top_left = (top_left[0], top_left[1])
    bottom_right = (top_left[0] + w, top_left[1] + h)

    center_x = (top_left[0] + bottom_right[0]) // 2
    center_y = (top_left[1] + bottom_right[1]) // 2

    # Calculate new top-left and bottom-right for expanded rectangle
    new_top_left = (center_x - w*factor, center_y - h*factor)
    new_bottom_right = (center_x + w*factor, center_y + h*factor)

    rect_width = new_bottom_right[0] - new_top_left[0]
    rect_height = new_bottom_right[1] - new_top_left[1]

    return new_top_left[0], new_top_left[1], rect_width, rect_height
