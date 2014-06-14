package toidiu.com.donow.structs;

import java.util.AbstractList;
import java.util.ArrayList;

public abstract class ToDoList extends ArrayList{

    AbstractList<ToDoItem> taskList;

    /*
        ------------TASK
     */
    public String getTask(int i){
        if (taskList.size() > i) {
            return taskList.get(i).getTask();

        }
        return null;
    }

//    public Boolean removeTask(int i){
//        if (taskList.remove(i) != null) {
//            return true;
//        }
//        return false;
//    }
//
//    public void setTask(String task){
////        taskList.add(task);
//    }
//
//    public void setTask(String task, int i){
////        taskList.add(i, task);
//    }

    /*
        -----------STATUS
     */
    public void setDone(int i){
        taskList.get(i).setDone();
    }

}
