package usp.kanban.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Hashtable;

import usp.kanban.server.BLL.CredentialBLL;
import usp.kanban.server.Model.Message;

/**
 * A server program which accepts requests from clients to
 * capitalize strings.  When clients connect, a new thread is
 * started to handle an interactive dialog in which the client
 * sends in a string and the server thread sends back the
 * capitalized version of the string.
 *
 * The program is runs in an infinite loop, so shutdown in platform
 * dependent.  If you ran it from a console window with the "java"
 * interpreter, Ctrl+C generally will shut it down.
 */
public class Server {

    /**
     * Application method to run the server runs in an infinite loop
     * listening on port 9898.  When a connection is requested, it
     * spawns a new thread to do the servicing and immediately returns
     * to listening.  The server keeps a unique client number for each
     * client that connects just to show interesting logging
     * messages.  It is certainly not necessary to do this.
     */
    public static void main(String[] args) throws Exception {
        System.out.println("SERVER: O servidor de kanban esta sendo executado.");
        int clientNumber = 0;
        ServerSocket listener = new ServerSocket(9090);
        try {
            while (true) {
                new Interpreter(listener.accept(), clientNumber++).start();
            }
        } finally {
            listener.close();
        }
    }

    /**
     * A private thread to handle capitalization requests on a particular
     * socket.  The client terminates the dialogue by sending a single line
     * containing only a period.
     */
    private static class Interpreter extends Thread {
        private Socket socket;
        private int clientNumber;
        BufferedReader input;
        PrintWriter out;

        public Interpreter(Socket socket, int clientNumber) throws IOException {
            this.socket = socket;
            this.clientNumber = clientNumber;
            this.input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            this.out = new PrintWriter(socket.getOutputStream(), true);

            log("Nova conexao com o cliente #" + clientNumber + " em " + socket);
        }

        /**
         * Services this thread's client by first sending the
         * client a welcome message then repeatedly reading strings
         * and sending back the capitalized version of the string.
         */
        public void run() {
            try{
                Message greet = new Message(null, "HI", null);
                SendMessage(greet);
                
                Message loginMessage = ReceiveMessage();

                String guid = new CredentialBLL().LoginOrRegister(loginMessage);
                if(guid == null){
                    SendErrorMessage(guid);
                }
                else{
                    Hashtable<String, String> messageBody = new Hashtable<String, String>();
                    messageBody.put("guid", guid);
                    Message sessionMessage = new Message(null, "Login", messageBody);
                    SendMessage(sessionMessage);
                }

            } catch (Exception e) {
                log("Error handling client# " + clientNumber + ": " + e);
            } finally {
                try {
                    socket.close();
                } catch (IOException e) {
                    log("Couldn't close a socket, what's going on?");
                }
                log("Connection with client# " + clientNumber + " closed");
            }
        }

        private Message ReceiveMessage() throws Exception{

            String response = input.readLine();
            if(response == null || !response.contains("LENGTH:")) return null;
            
            response = response.replace("LENGTH:", "");
    
            char[] buffer = new char[Integer.parseInt(response)];
            input.read(buffer, 0, Integer.parseInt(response));
            
            return new Message(new String(buffer));
        }

        private void SendMessage(Message message){
            out.write(message.toString());
            out.flush();
        }

        private void SendErrorMessage(String errorMessage){
            Hashtable<String, String> messageBody = new Hashtable<String, String>();
            messageBody.put("message", errorMessage);
            Message error = new Message(null, "ERROR", messageBody);
            out.write(error.toString());
            out.flush();
        }

        private void log(String message) {
            System.out.println("SERVER: " + message);
        }
    }
}