package usp.kanban.client.Model;

public class Task{
    private int id;
    private int userId;
    private String title;
    private String status;

    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserId() {
        return this.userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getTitle() {
        return this.title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getStatus() {
        return this.status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    
    public Task(){

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