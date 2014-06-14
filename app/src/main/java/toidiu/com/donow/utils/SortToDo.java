package toidiu.com.donow.utils;


import java.util.ArrayList;

import toidiu.com.donow.structs.ToDoItem;

public class SortToDo {

    private static ArrayList<ToDoItem> sortedList = null;
    private static ToDoItem tempItem = null;

    public static ArrayList<ToDoItem> sort(ArrayList<ToDoItem> list){
        sortedList = list;

        for(int i=0, j=0; i<sortedList.size(); i++){
            tempItem = sortedList.get(j);

            if(tempItem.getStatus() == true) {
                sortedList.remove(j);
                sortedList.add(tempItem);
            }else{
                j++;
            }
        }

        return sortedList;
    }
}
