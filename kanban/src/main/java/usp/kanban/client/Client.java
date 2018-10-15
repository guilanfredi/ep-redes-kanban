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
 * Classe principal do processo cliente
 */
public class Client {

    static String serverIP = "localhost";
    
    static Socket socket;
    static BufferedReader input;
    static PrintWriter output;
    
    public static void main(String[] args) throws Exception {
        GetIPAddress();
        
        // Inicia uma nova conexão por socket na porta 9090
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
        
        while(true){
            int command = Form.PrintMenu();
            boolean exit = false;

            switch(command){
                case 1:
                String newTask = Form.CreateTask();
                SendNewTask(newTask);
                ShowTasks();
                break;

                case 2:
                Hashtable<String,String> body = Form.UpdateTask();
                SendUpdateTask(body);
                ShowTasks();
                break;
                
                case 3:
                ShowTasks();
                break;
                
                case 0:
                exit = Form.ConfirmExit();
                Cookie.removeSessionCookie();
                break;

                default:
                break;

            }
            if(exit) break;
        }
        System.exit(0);
    }

    private static void SendUpdateTask(Hashtable<String, String> body) {
        Hashtable<String,String> header = new Hashtable<>();
        header.put("SessionID", Cookie.readCookie("SessionID"));

        Message updateTaskMessage = new Message(header, "UpdateTask", body);

        SendMessage(updateTaskMessage);
        Message message = ReceiveMessage();
    }

    private static void SendNewTask(String newTask) {
        Hashtable<String,String> header = new Hashtable<>();
        header.put("SessionID", Cookie.readCookie("SessionID"));

        Hashtable<String,String> body = new Hashtable<>();
        body.put("newTask", newTask);

        Message newTaskMessage = new Message(header, "NewTask", body);
        SendMessage(newTaskMessage);
        Message message = ReceiveMessage();
        
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
            
            int id = Integer.parseInt(attrs[0].split(",")[1]);
            int userId = Integer.parseInt(attrs[1].split(",")[1]);
            String title = attrs[2].split(",")[1];
            String status = attrs[3].split(",")[1];

            Task task = new Task(id, userId, title, status);
            tasks.add(task);
        }
        Form.PrintTasks(tasks);
        
    }

    /*
     * Método de autenticação no servidor, se existir algum cookie local usa ele,
     * caso contrário exibe um formulário para o usuário informar um usuário e
     * senha e envia a mensagem com esses dados ao servidor, se a resposta for
     * positiva salva um cookie com o novo ID da Sessão.
    */
    private static void Login() throws Exception {
        if(Cookie.readCookie("SessionID") == null){
            Hashtable<String, String> loginInformation = Form.LoginForm();
            Message loginMessage = new Message(null, "LoginOrRegister", loginInformation);
            
            try{
                
                SendMessage(loginMessage);

                Message session = ReceiveMessage();
                if(session == null){
                    return;
                }
                
                Cookie.addSessionCookie(session.getBody().get("guid"));            
            }
            catch(Exception e){
                log(e.getMessage());
            }
        }
    }

    /*
     * Aguarda o recebimento de uma mensagem, quando receber, desserializa ela.
     * A desserialização é feita lendo a primeira linha da mensagem que contem
     * o tamanho em caracteres do resto da mensagem, depois isso lê essa quanti-
     * dade de caracteres.
     * 
     * Também trata alguns tipos de erro, se a sessão estiver expirada ou a senha
     * for inválida, remove o cookie e executa o login novamente.
    */

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
                    log("Sessao expirada.");
                    Cookie.removeSessionCookie();
                    Login();
                    break;

                    case "Invalid password":
                    log("Senha invalida.");
                    Cookie.removeSessionCookie();
                    Login();
                    break;
                    
                    case "Couldn't update event":
                    log("Esse evento nao existe.");
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

    /**
     * Procura o endereço de ip do servidor no arquivo cookies.json
     * se achar, usa esse ip, caso contrário abre um form para que
     * o usuário insira um.
     */
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