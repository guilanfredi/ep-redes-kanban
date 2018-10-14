package usp.kanban.server.Model;

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

    public String toString(){
        return "id," + id + ";userId," + userId + ";title," + title + ";status," + status;
    }

}