# https://stackoverflow.com/questions/42458475/sending-image-over-sockets-only-in-python-image-can-not-be-open

import socket

class Server:

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

if __name__ == "__main__":
    HOST = '192.168.43.236'
    PORT = 7800

    base64_server = Server(HOST, PORT)
    received_base64_data = base64_server.receive_base64_data()
