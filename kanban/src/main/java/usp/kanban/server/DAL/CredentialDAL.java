package usp.kanban.server.DAL;

import java.util.ArrayList;

import usp.kanban.server.Model.Credential;

public class CredentialDAL{
    public static ArrayList<Credential> dbList = new ArrayList<Credential>();

    public static boolean insert(Credential ett){
        try{
            dbList.add(ett);
            return true;
        }
        catch(Exception ex){
            return false;
        }
    }

    public static int exists(String user, String pass){
        if(dbList.stream().filter(x -> x.getLogin().equals(user)).count() == 0){
            return -1;
        }
        else if(dbList.stream().filter(x -> x.getLogin().equals(user)&& x.getLogin().equals(user)).count() == 0){
            return 0;
        }
        else{
            return 1;
        }
    }

    public static Credential get(String user, String pass){
        Credential cred = dbList.stream().filter(x -> x.getLogin().equals(user) && x.getPassword().equals(pass)).findFirst().get();
        return cred;
    }
}