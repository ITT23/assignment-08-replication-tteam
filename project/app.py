# https://docs.opencv.org/3.4/d4/dc6/tutorial_py_template_matching.html
# https://stackoverflow.com/questions/4383571/importing-files-from-different-folder

from server.server import Server
from screenshot import TakeScreenshot
import base64

HOST = '192.168.43.236'  # Replace with the desired host
PORT = 7800  # Replace with the desired port

def start_server():
    base64_server = Server(HOST, PORT)
    received_base64_data = base64_server.receive_base64_data()
    # print("Received Base64 Data:", received_base64_data)
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
    width_size = 300
    height_size = 400

    screenshot_obj = TakeScreenshot(left_pos, top_pos, width_size, height_size)
    base64_string = screenshot_obj.capture_and_convert()
    # print("Sended Base64 String:", base64_string)

if __name__ == "__main__":
    received_base64_data = start_server()
    if base64_validation(received_base64_data):
        take_screenshot()