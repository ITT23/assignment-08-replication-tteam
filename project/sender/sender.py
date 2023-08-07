import socket

def send_message(message, host, port):
    client_socket = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
    print("socket")
    client_socket.connect((host, port))

    print("socket connected")
    try:
        client_socket.sendall(message.encode('utf-8'))
        print("Message sent to the client:", message)
    except Exception as e:
        print("Error while sending message:", e)
    finally:
        client_socket.close()
