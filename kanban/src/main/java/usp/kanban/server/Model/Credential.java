package usp.kanban.server.Model;

import java.util.UUID;

public class Credential{
    private String login;
    private String password;
    private String guid;
    private int id;

    public String getLogin() {
        return this.login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return this.password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getGuid() {
        return this.guid;
    }

    public int getId() {
        return this.id;
    }
    
    public void setId(int value) {
        this.id = value;
    }

    public Credential(String login, String pass){
        this.login = login;
        this.password = pass;
        this.guid = generateGuid();
    }

    private String generateGuid() {
        return UUID.randomUUID().toString(); 
    }
}