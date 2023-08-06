import socket

def start_client(host, port):
    client_socket = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
    client_socket.connect((host, port))

    # Receive the message from the server
    data = client_socket.recv(1024)
    message = data.decode()

    print("Received from server:", message)

    client_socket.close()

if __name__ == "__main__":
    HOST = '192.168.43.236'
    PORT = 7800

    start_client(HOST, PORT)
