package usp.kanban.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import java.util.*;
import java.awt.*;
import javax.swing.*;

import usp.kanban.client.Model.Message;
import usp.kanban.client.View.Form;

/**
 * Trivial client for the date server.
 */
public class Client {

    static String serverIP = "localhost";
    
    static Socket socket;
    static BufferedReader input;
    static PrintWriter output;
    /**
     * Runs the client as an application.  First it displays a dialog
     * box asking for the IP address or hostname of a host running
     * the date server, then connects to it and displays the date that
     * it serves.
     */
    public static void main(String[] args) throws IOException {

        GetIPAddress();

        Login();
        try{
            while(true){
                String answer = input.readLine();
                
                log("Resposta recebida\n");
                log(answer);
            }
        }
        catch(Exception ex){
            log(ex.getMessage());
        }
    }

    private static void Login(){
        Hashtable<String, String> loginInformation = Form.LoginForm();
        Message message = new Message(null, "LoginOrRegister", loginInformation);
        
        try{
            socket = new Socket(serverIP, 9090);
            
            input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            output = new PrintWriter(socket.getOutputStream(), true);

            String response = ReceiveMessage();
            
            // if(!lines[0].contains("LENGTH:")) throw new Exception("formato da mensagem inválido: LENGTH");

            // Message response = new Message();

            // log("Enviando mensagem de login:\n" + loginMessage.toString() + "\n");
            // out.write(loginMessage.toString());
            // log("Mensagem enviada");
        }
        catch(Exception e){}
    }


    private static String ReceiveMessage() throws Exception{
        String response;
        while(true){
            response = input.readLine();
            if(response != null && response.contains("LENGTH:")) break;
        }
        response = response.replace("LENGTH:", "");

        char[] buffer = new char[Integer.parseInt(response)];
        input.read(buffer, 0, Integer.parseInt(response));

        return buffer.toString();
    }

    private static void GetIPAddress(){
        serverIP = JOptionPane.showInputDialog(
            "Insira o endereco de IP do computador \n" +
            "executando o servidor na porta 9090: \n" +
            "(ENTER para localhost)");

        if(serverIP == "") serverIP = "localhost";
    }

    private static void log(String text){
        System.out.println("CLIENT: " + text);
    }
}