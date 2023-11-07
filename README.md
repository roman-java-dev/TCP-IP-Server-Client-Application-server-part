# TCP/IP Server-Client Application

This server-client application allows communication over TCP/IP. The server performs various operations on received data and can execute predefined commands. The client can send commands to the server and receive messages in response.

## Part 1: Server Application

### Functionality

1.1. **Receive and Process Data**: The server can receive information from the client and process it according to the following rules:
- If it's a string, alternate between converting letters to uppercase and lowercase (e.g., "Hello55 World123" becomes "HeLlO55_wOrLd123").
- Replace spaces with underscores "_".
- If the received information is a number, multiply it by 1000 (e.g., 5 becomes 5000).

1.2. **Execute Commands**: The server can receive command words from the client, look up the corresponding command from the "Commands" file, and return the response to the client. For example, if the client sends "command1" or "GetSet5," the server will return "this is command 1" or "this is GetSet5 command".

1.3. **Log Messages**: The server logs received and sent messages in a file named "LogFile."

1.4. **Send Periodic Messages**: Every 10 seconds, the server sends a message to the client in the format "Counter " + N + ", Time " + current datetime, where N is the count number (0, 1, 2, 3...N, counted separately for each client), current datetime (25.05.2020 15:20:35).

### Usage

1. Clone the repository and navigate to the server application folder.

2. Compile and run the server application. Use your preferred development environment, or execute the application via the command line:

3. The server will start listening on the specified port (8080).

4. The server is now ready to accept client connections and process data.
