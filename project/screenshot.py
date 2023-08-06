import base64
import io
from PIL import ImageGrab

class TakeScreenshot:
    def __init__(self, left, top, width, height):
        self.left = left
        self.top = top
        self.width = width
        self.height = height

    def take_screenshot(self):
        screenshot = ImageGrab.grab(bbox=(self.left, self.top, self.left + self.width, self.top + self.height))
        return screenshot

    def convert_image_to_base64(self, image):
        buffer = io.BytesIO()
        image.save(buffer, format="PNG")
        base64_str = base64.b64encode(buffer.getvalue()).decode()
        return base64_str

    def capture_and_convert(self):
        screenshot_img = self.take_screenshot()

        base64_str = self.convert_image_to_base64(screenshot_img)
        return base64_str

if __name__ == "__main__":
    left_pos = 0
    top_pos = 0
    width_size = 20
    height_size = 20

    screenshot_obj = TakeScreenshot(left_pos, top_pos, width_size, height_size)
    base64_string = screenshot_obj.capture_and_convert()
