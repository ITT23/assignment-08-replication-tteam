# https://stackoverflow.com/questions/42458475/sending-image-over-sockets-only-in-python-image-can-not-be-open

import socket

class Receiver:
    
    def __init__(self, host, port, buffer_size=1024):
        self.host = host
        self.port = port
        self.buffer_size = buffer_size
        self.server_socket = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
        self.server_socket.bind((self.host, self.port))
        self.server_socket.listen(3)
        print("Waiting for connection...")
        self.conn, self.addr = self.server_socket.accept()
        print("Connected to:", self.addr)

    def receive_base64_data(self):
        base64_data = b''

        while True:
            data = self.conn.recv(self.buffer_size)
            if not data:
                break
            base64_data += data

        # conn.close()
        # server_socket.close()
        return base64_data
   
    def send_data(self):
        self.conn.sendall(b'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')

if __name__ == "__main__":
    HOST = '192.168.43.236'
    PORT = 7800

    base64_server = Receiver(HOST, PORT)
    received_base64_data = base64_server.receive_base64_data()
