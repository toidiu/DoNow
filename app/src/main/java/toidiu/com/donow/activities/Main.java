package toidiu.com.donow.activities;


import android.app.Activity;
import android.app.DialogFragment;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

import toidiu.com.donow.R;
import toidiu.com.donow.adapters.ListAdapter;
import toidiu.com.donow.fragments.DiagFrag;
import toidiu.com.donow.structs.ToDoItem;
import toidiu.com.donow.utils.SaveLoadHandler;
import toidiu.com.donow.utils.SortToDo;

public class Main extends Activity {

    public ArrayList<ToDoItem> list = new ArrayList();
    private final static String SAVE_FILE = "instance.json";
    private Button addButton;
    private SaveLoadHandler<ArrayList<ToDoItem>> slh;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        addButton = (Button) findViewById(R.id.add);

        //init save/load handler
        Type type = new TypeToken<ArrayList<ToDoItem>>(){}.getType();
        this.slh = new SaveLoadHandler(type, SAVE_FILE);

        //load data and sort
        list = this.slh.loadData(this);
        ArrayList<ToDoItem> listTemp = null;
        listTemp = SortToDo.sort(list);
        list = listTemp;

        //init listview and its adapter
        final ListView listview = (ListView) findViewById(R.id.listview);
        final ListAdapter adapter = new ListAdapter(this, R.layout.todo_row,
                list, listview);
        listview.setAdapter(adapter);

        //init touch listerners
        initListener(Main.this, adapter);
    }

    @Override
    protected void onPause() {
        super.onPause();
        slh.saveData(this, list);
    }

    //Listener for UI
    private void initListener(final Context ctx, final ListAdapter adapter){
        //add text button listener
        addButton.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View view) {
                showDialog(adapter);
            }
        });
    }

    //Get a dialog fragment
    private void showDialog(ListAdapter adapter) {

        DialogFragment addItem = DiagFrag.newInstance(adapter, "ADD ITEM");
        addItem.show(getFragmentManager(), "addDialog");
    }

}
