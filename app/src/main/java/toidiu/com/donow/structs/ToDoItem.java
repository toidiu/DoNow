package toidiu.com.donow.structs;

public class ToDoItem{
    private String task = "";
    private Boolean doneTask = false;

    //Constructor
    public ToDoItem(String task){
        this.task = task;
        this.doneTask = false;
    }

    public ToDoItem(String task, Boolean status){
        this.task = task;
        this.doneTask = status;
    }


    //task
    public String getTask() {
        return task;
    }

    public void setTask(String task){
        this.task = task;
    }


    //status
    public Boolean getStatus(){
        return doneTask;
    }
    public void setUndone(){
        doneTask = false;
    }
    public void setDone(){
        doneTask = true;
    };
    public void toggleStatus() {
        doneTask = doneTask ^ true;
    }

}
