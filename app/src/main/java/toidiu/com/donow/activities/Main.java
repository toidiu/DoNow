package toidiu.com.donow.activities;


import android.app.Activity;
import android.app.DialogFragment;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;

import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;

import de.timroes.android.listview.EnhancedListView;
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

    private static SharedPreferences sharedPref;
    public static final String PREFS_NAME = "PrefsFile";
    public static final String FIRST_START = "FirstStart";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        addButton = (Button) findViewById(R.id.add);

        //init save/load handler
        Type type = new TypeToken<ArrayList<ToDoItem>>(){}.getType();
        this.slh = new SaveLoadHandler(type, SAVE_FILE);

        //Prefs for detecting first time starting
        sharedPref = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        Boolean firstStart = sharedPref.getBoolean(FIRST_START, false);

        if(firstStart) {
            //load data and sort
            ArrayList<ToDoItem> listTemp = this.slh.loadData(this);
            if (listTemp != null) list = listTemp;
            listTemp = SortToDo.sort(list);
            list = listTemp;
        }else{
            ToDoItem[] toDoItems = {
                    new ToDoItem("Do it Now!"),
                    new ToDoItem("Tap to Toggle", true),
                    new ToDoItem("Swipe to Dismiss", true)
            };
            list.addAll(Arrays.asList(toDoItems));  // toDoItems

            //mark as opened first time
            sharedPref.edit().putBoolean(FIRST_START, true).commit();
        }

        //init listview and its adapter
        final EnhancedListView listview = (EnhancedListView) findViewById(R.id.listview);
        final ListAdapter adapter = new ListAdapter(this, R.layout.todo_row,
                list, listview);
        listview.setAdapter(adapter);

        //init touch listerners
        initButtonListener(Main.this, adapter);
    }

    @Override
    protected void onPause() {
        super.onPause();
        slh.saveData(this, list);
    }

    //Listener for UI
    private void initButtonListener (final Context ctx, final ListAdapter adapter){
        //Touch listener
        addButton.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                int eventaction = event.getAction();
                switch (eventaction ) {
                    case MotionEvent.ACTION_DOWN: { // touch on the screen event
                        v.findViewById(R.id.add).setBackgroundColor(ctx.getResources().getColor(R.color.win8_orange));
                        break;
                    }
                    case MotionEvent.ACTION_UP: {
                        v.findViewById(R.id.add).setBackgroundColor(ctx.getResources().getColor(R.color.IndianRed));
                        break;
                    }
                }
                return true;
            }
        });

        //Click button listener
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
