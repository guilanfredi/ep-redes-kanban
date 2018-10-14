package usp.kanban.server.DAL;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.stream.Collectors;

import usp.kanban.server.Model.Task;

public class TaskDAL{
    public static ArrayList<Task> dbList = new ArrayList<Task>();

    public static ArrayList<Task> getTasksByUser(int UserId){
        ArrayList<Task> result = new ArrayList<Task>();
        result.addAll(dbList.stream().filter(x -> x.getUserId() == UserId).collect(Collectors.toList()));
        return result;
    }

	public static boolean insertTask(Task task) {
		try{
            int maxId = 0;
            if(dbList.size() > 0){
                maxId = dbList.stream().max(Comparator.comparing(x -> x.getId())).get().getId();
            }
            task.setId(maxId + 1);
            dbList.add(task);
            return true;
        }
        catch(Exception ex){
            return false;
        }
	}

	public static boolean updateTask(int userId, int taskId, String status) {
        if(dbList.stream().filter(x -> x.getUserId() == userId && x.getId() == taskId).count() > 0){
            Task task = dbList.stream().filter(x -> x.getUserId() == userId && x.getId() == taskId).findFirst().get();
            task.setStatus(status);
            dbList.removeIf(x -> x.getUserId() == userId && x.getId() == taskId);
            dbList.add(task);
            return true;
        }
        else{
            return false;
        }
	}

}