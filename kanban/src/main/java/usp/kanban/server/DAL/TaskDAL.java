package usp.kanban.server.DAL;

import java.util.ArrayList;
import java.util.stream.Collectors;

import usp.kanban.server.Model.Task;

public class TaskDAL{
    public static ArrayList<Task> dbList = new ArrayList<Task>();

    public static ArrayList<Task> getTasksByUser(int UserId){
        ArrayList<Task> result = new ArrayList<Task>();
        result.addAll(dbList.stream().filter(x -> x.getUserId() == UserId).collect(Collectors.toList()));
        return result;
    }

}