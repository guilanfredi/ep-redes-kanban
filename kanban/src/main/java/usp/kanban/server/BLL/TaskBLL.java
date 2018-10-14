package usp.kanban.server.BLL;

import java.util.ArrayList;

import usp.kanban.server.DAL.TaskDAL;
import usp.kanban.server.Model.Message;
import usp.kanban.server.Model.Task;

public class TaskBLL{

    public ArrayList<Task> GetTasks(Message tasksMessage){
        int UserId = new CredentialBLL().GetIdBySession(tasksMessage.getHeader().get("SessionID"));
        ArrayList<Task> result = TaskDAL.getTasksByUser(UserId);
        return result;
    }

	public boolean InsertTask(Message receivedMessage) {
        String title = receivedMessage.getBody().get("newTask");
        int UserId = new CredentialBLL().GetIdBySession(receivedMessage.getHeader().get("SessionID"));

        Task task = new Task();
        task.setTitle(title);
        task.setStatus("To do");
        task.setUserId(UserId);

        return TaskDAL.insertTask(task);
	}
}