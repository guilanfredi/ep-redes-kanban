package usp.kanban.server.BLL;

import usp.kanban.server.DAL.CredentialDAL;
import usp.kanban.server.Model.Message;
import usp.kanban.server.Model.Credential;;

public class CredentialBLL{

    public String LoginOrRegister(Message loginMessage){
        String login = loginMessage.getBody().get("user");
        String pass = loginMessage.getBody().get("pass");
        
        int result = CredentialDAL.exists(login, pass);
        
        if(result == -1){
            //Não cadastrado
            Credential cred = new Credential(login, pass);
            CredentialDAL.insert(cred);
            return cred.getGuid();
        }
        else if(result == 0){
            //Senha inválida
            return null;
        }
        else{
            //Usuario já cadastrado
            Credential cred = CredentialDAL.get(login, pass);
            return cred.getGuid();
        }

    }
}