package usp.kanban.client.Model;

public class Task{
    private int id;

    
    private int userId;
    private String title;
    private String status;

    public int getId() {
        return this.id;
    }
    
    public String getTitle() {
        return this.title;
    }

    public String getStatus() {
        return this.status;
    }

    public int getUserId() {
        return this.userId;
    }

    public Task(int id, int userId, String title, String status){
        this.id = id;
        this.userId = userId;
        this.title = title;
        this.status = status;
    }

    public String toString(){
        return "id:" + id + ";userId:" + userId + ";title:" + title + ";status:" + status;
    }
}