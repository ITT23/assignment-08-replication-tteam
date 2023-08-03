#!/usr/bin/env python
# https://stackoverflow.com/questions/42458475/sending-image-over-sockets-only-in-python-image-can-not-be-open

import socket

image = "imm000.jpg"

HOST = '127.0.0.1'
PORT = 6666

sock = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
server_address = (HOST, PORT)
sock.connect(server_address)

try:

    # open image
    with open(image, 'rb') as myfile:
        bytes = myfile.read()
        size = len(bytes)

    # send image size to server
    sock.sendall("SIZE %s" % size.encode())
    answer = sock.recv(4096).decode()

    print(f'answer = {answer}')

    # send image to server
    if answer == 'GOT SIZE':
        sock.sendall(bytes)

        # check what server send
        answer = sock.recv(4096).decode()
        print(f'answer = {answer}')

        if answer == 'GOT IMAGE':
            sock.sendall("BYE BYE ".encode())
            print('Image successfully send to server')

finally:
    sock.close()
    