# https://docs.opencv.org/3.4/d4/dc6/tutorial_py_template_matching.html
# https://stackoverflow.com/questions/4383571/importing-files-from-different-folder
# https://janakiev.com/blog/python-shell-commands/
# https://cloud.tencent.com/developer/article/1831561

import base64
import os
import mss
from PIL import Image
from server.server import Server
#from screenshot import TakeScreenshot


HOST = '192.168.43.236'  # Replace with the desired host
PORT = 7800  # Replace with the desired port
base64_server = Server(HOST, PORT)

def start_server():
    #base64_server = Receiver(HOST, PORT)
    received_base64_data = base64_server.receive_base64_data()
    #print("Received Base64 Data:", received_base64_data)
    return received_base64_data

def base64_validation(base64_data):
    try:
        decoded_data = base64.b64decode(base64_data)
        return True
    except base64_data.binascii.Error:
        return False

def take_screenshot():
    left_pos = 100
    top_pos = 200
    width = 300
    height = 400
    folder_name = "screenshot"
    file_name = "screenshot.png"

    if not os.path.exists(folder_name):
        os.makedirs(folder_name)
    file_path = os.path.join(folder_name, file_name)

    #screenshot_obj = TakeScreenshot(left_pos, top_pos, width, height)
    with mss.mss() as sct:
        region = {"left": left_pos, "top": top_pos, "width": width, "height": height}
        screenshot = sct.grab(region)

        img = Image.frombytes("RGB", screenshot.size, screenshot.bgra, "raw", "BGRX")
        img.save(file_path)
        print(f"Screenshot saved to {file_path}")
    # base64_string = screenshot_obj.capture_and_convert()
    # print("Sended Base64 String:", base64_string)

def start_http_server():
    try:
        cmd_command = f"start python -m http.server {PORT+1057}"
        # for Linux: cmd_command = f"nohup python -m http.server {PORT}"
        os.system(cmd_command)
    except OSError as error:
        print("cmd error:", error)

if __name__ == "__main__":
    received_base64_data = start_server()
    if base64_validation(received_base64_data):
        take_screenshot()
        start_http_server()
        # send_message("Hello from Python server!", HOST, PORT)
