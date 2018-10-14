package usp.kanban.server.Model;

public class Task{
    private int id;
    private int userId;

    public int getUserId() {
        return this.userId;
    }
    private String title;
    private String status;

    public String toString(){
        return "id:" + id + ";userId:" + userId + ";title:" + title + ";status:" + status;
    }

}