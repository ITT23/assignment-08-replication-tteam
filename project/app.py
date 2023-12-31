# https://docs.opencv.org/3.4/d4/dc6/tutorial_py_template_matching.html
# https://stackoverflow.com/questions/4383571/importing-files-from-different-folder
# https://janakiev.com/blog/python-shell-commands/
# https://cloud.tencent.com/developer/article/1831561
# https://www.codespeedy.com/convert-base64-string-to-image-in-python/

import base64
import os
import mss
from PIL import Image, ImageGrab
from server.server import Server
import match_screenshot


HOST = '192.168.43.236'  # Replace with the desired host
PORT = 7800  # Replace with the desired port
HTTP_PORT = 8857
base64_server = Server(HOST, PORT)

def start_server():
    received_base64_data = base64_server.receive_base64_data()
    # print("Received Base64 Data:", received_base64_data)
    return received_base64_data

def base64_validation(base64_data):
    try:
        decoded_data = base64.b64decode(base64_data)
        return True
    except base64_data.binascii.Error:
        return False

def take_screenshot(left_pos, top_pos, width, height):
    folder_name = "screenshot"
    file_name = "screenshot.png"

    if not os.path.exists(folder_name):
        os.makedirs(folder_name)
    file_path = os.path.join(folder_name, file_name)

    with mss.mss() as sct:
        region = {"left": left_pos, "top": top_pos, "width": width, "height": height}
        screenshot = sct.grab(region)

        img = Image.frombytes("RGB", screenshot.size, screenshot.bgra, "raw", "BGRX")
        img.save(file_path)
        print(f"Screenshot saved to {file_path}")
    # base64_string = screenshot_obj.capture_and_convert()
    # print("Sended Base64 String:", base64_string)

def take_main_image_screenshot():
    screenshot = ImageGrab.grab()
    screenshot_folder = "./screenshot_matcher"
    screenshot_filename = "main.png"
    file_path = f"{screenshot_folder}/{screenshot_filename}"
    screenshot.save(file_path)

def start_http_server():
    try:
        cmd_command = f"start python -m http.server {HTTP_PORT}"
        # for Linux: cmd_command = f"nohup python -m http.server {PORT}"
        os.system(cmd_command)
    except OSError as error:
        print("cmd error:", error)

def save_received_base64_as_image(received_data):
    decoded_data = base64.b64decode((received_data))
    img_folder = "./screenshot_matcher"
    if not os.path.exists(img_folder):
        os.makedirs(img_folder)
    img_filename = "template.png"
    image_path = os.path.join(img_folder, img_filename)
    with open(image_path, 'wb') as img_file:
        img_file.write(decoded_data)

if __name__ == "__main__":
    received_base64_data = start_server()
    take_main_image_screenshot()
    if base64_validation(received_base64_data):
        save_received_base64_as_image(received_base64_data)
        left_pos, top_pos, width, height = match_screenshot.match_screenshot()
        take_screenshot(left_pos, top_pos, width, height)
        start_http_server()

