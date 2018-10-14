package usp.kanban.client;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import java.util.*;
import javax.swing.*;

import usp.kanban.client.Model.Message;
import usp.kanban.client.Model.Task;
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
    public static void main(String[] args) throws Exception {
        GetIPAddress();
                
        socket = new Socket(serverIP, 9090);
        input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        output = new PrintWriter(socket.getOutputStream(), true);

        Message greet = ReceiveMessage();
        if(!greet.getMethod().equals("HI")){
            log("Mensagem de conexão inválida." + greet.toString());
            return;
        }

        Login();
        
        ShowTasks();
        
    }

    private static void ShowTasks() {
        Hashtable<String,String> header = new Hashtable<>();
        header.put("SessionID", Cookie.readCookie("SessionID"));
        Message getTasksMessage = new Message(header, "GetTasks", null);
        SendMessage(getTasksMessage);
        Message tasksMessage = ReceiveMessage();
        
        if(tasksMessage == null){
            ShowTasks();
            return;
        }
        ArrayList<Task> tasks = new ArrayList<Task>();
        for (String value : tasksMessage.getBody().values()) {
            String[] attrs = value.split(";");
            
            int id = Integer.parseInt(attrs[0].split(":")[1]);
            int userId = Integer.parseInt(attrs[1].split(":")[1]);
            String title = attrs[2].split(":")[1];
            String status = attrs[3].split(":")[1];

            Task task = new Task(id, userId, title, status);
            tasks.add(task);
        }
        Form.PrintTasks(tasks);
        Form.PrintMenu();
    }

    private static void Login() throws Exception {
        if(Cookie.readCookie("SessionID") == null){
            Hashtable<String, String> loginInformation = Form.LoginForm();
            Message loginMessage = new Message(null, "LoginOrRegister", loginInformation);
            
            try{
                
                SendMessage(loginMessage);

                Message session = ReceiveMessage();

                if(session.getMethod().equals("ERROR")){
                    log(session.getBody().get("message"));
                    return;
                }
                
                Cookie.addSessionCookie(session.getBody().get("guid"));            
            }
            catch(Exception e){
                log(e.getMessage());
            }
        }
    }


    private static Message ReceiveMessage(){
        try{
            String response = input.readLine();
            if(response == null || !response.contains("LENGTH:")) return null;
            
            response = response.replace("LENGTH:", "");

            char[] buffer = new char[Integer.parseInt(response)];
            input.read(buffer, 0, Integer.parseInt(response));
            
            Message message = new Message(new String(buffer));
            if(message.getMethod().equals("ERROR")){
                switch(message.getBody().get("message")){
                    case "Expired Session":
                    Cookie.removeSessionCookie();
                    Login();
                    break;

                }
                return null;
            }
            else{
                return message;
            }
        }
        catch(Exception ex){
            ex.printStackTrace();
            return null;
        }
    }

    private static void SendMessage(Message message){
        output.write(message.toString());
        output.flush();
    }

    private static void GetIPAddress()throws Exception{
        String ip = Cookie.readCookie("ServerIP");
        if(ip != null){
            serverIP = ip;
        }
        else{
            serverIP = JOptionPane.showInputDialog(
                "Insira o endereco de IP do computador \n" +
                "executando o servidor na porta 9090: \n" +
                "(ENTER para localhost)");

            if(serverIP == "") serverIP = "localhost";
        }
    }

    private static void log(String text){
        System.out.println("CLIENT: " + text);
    }
}