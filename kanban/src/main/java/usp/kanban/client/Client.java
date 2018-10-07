package usp.kanban.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import java.util.*;
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

    }

    private static void Login(){
        Hashtable<String, String> loginInformation = Form.LoginForm();
        Message loginMessage = new Message(null, "LoginOrRegister", loginInformation);
        
        try{
            socket = new Socket(serverIP, 9090);
            
            input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            output = new PrintWriter(socket.getOutputStream(), true);

            Message greet = ReceiveMessage();
            if(greet.getMethod().equals("HI")){
                log("Recebida mensagem de conexao: " + greet.toString());
            }
            else{
                return;
            }

            log("Enviando mensagem de login:\n" + loginMessage.toString());
            output.write(loginMessage.toString());
            output.flush();
        }
        catch(Exception e){
            log(e.getMessage());
        }
    }


    private static Message ReceiveMessage() throws Exception{

        String response = input.readLine();
        if(response == null || !response.contains("LENGTH:")) return null;
        
        response = response.replace("LENGTH:", "");

        char[] buffer = new char[Integer.parseInt(response)];
        input.read(buffer, 0, Integer.parseInt(response));
        
        return new Message(new String(buffer));
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