package usp.kanban.client.Model;

import java.util.Hashtable;

/*

 * Classe que define o formato da mensagem que será enviada ao Servidor.
    
 * Recebe duas HashTables, uma para o cabeçalho da mensagem e outra para 
 * o corpo, e uma String que será o método executado. 

 * Formato da mensagem:
   LENGTH:180
   HEADER
   key1:value1
   key2:value2

   METHOD:MethodName

   BODY
   key1:value1
   key2:value2

*/


public class Message{
    private Hashtable<String, String> header;
    private String method;
    private Hashtable<String, String> body;

    public Message(){
        this.header = new Hashtable<>();
        this.body = new Hashtable<>();
        this.method = "";
    }

    public Message(Hashtable<String, String> header, String method, Hashtable<String, String> body){
        this.header = header == null ? new Hashtable<String, String>() : header;
        this.body = body == null ? new Hashtable<String, String>() : body;
        this.method = method;
    }

    public Message(String content) throws Exception{
        String[] lines = content.split("\n");

        Hashtable<String, String> messageHeader = new Hashtable<String, String>();
        Hashtable<String, String> messageBody = new Hashtable<String, String>();
        String messageMethod = "";

        if(lines[1] != "HEADER") throw new Exception("formato da mensagem inválido: HEADER");
        
        
        int i = 2;
        while(i < lines.length && lines[i] != ""){
            String[] dic = lines[i].split(":");
            messageHeader.put(dic[0], dic[1]);
            i++;
        }
        i++;

        if(!lines[i].contains("METHOD:")) throw new Exception("formato da mensagem inválido: METHOD");
        else{
            messageMethod = lines[i].split(":")[1];
        }
        i++;

        if(lines[i] != "BODY") throw new Exception("formato da mensagem inválido: BODY");

        i++;
        while(i < lines.length && lines[i] != ""){
            String[] dic = lines[i].split(":");
            messageBody.put(dic[0], dic[1]);
            i++;
        }

        this.method = messageMethod;
        this.header = messageHeader;
        this.body = messageBody;
    }

    public String toString(){
        String message = "";

        message += "HEADER\n";
        for(String key : header.keySet()){
            message += key + ":" + header.get(key) + "\n";
        }
        message += "\n";
        message += "METHOD:" + method + "\n";

        message += "\n";
        message += "BODY\n";
        for(String key : body.keySet()){
            message += key + ":" + body.get(key) + "\n";
        }

        int length = message.length();
        message = "LENGTH:" + length + "\n" + message;
        return message;
    }
}