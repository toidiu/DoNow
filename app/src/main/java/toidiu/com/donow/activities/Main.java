package toidiu.com.donow.activities;


import android.app.DialogFragment;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;

import com.google.gson.reflect.TypeToken;

import java.io.File;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;

import de.timroes.android.listview.EnhancedListView;
import roboguice.activity.RoboActivity;
import roboguice.inject.ContentView;
import roboguice.inject.InjectResource;
import roboguice.inject.InjectView;
import toidiu.com.donow.R;
import toidiu.com.donow.adapters.ListAdapter;
import toidiu.com.donow.fragments.DiagFrag;
import toidiu.com.donow.structs.ToDoItem;
import toidiu.com.donow.utils.SaveLoadHandler;
import toidiu.com.donow.utils.SortToDo;

@ContentView(R.layout.activity_main)
public class Main extends RoboActivity {

    @InjectView(R.id.add)   Button addButton;
    @InjectView (R.id.listview) EnhancedListView listview;
    @InjectResource(R.color.win8_orange) int winOrange;
    @InjectResource(R.color.IndianRed) int indRed;


    public ArrayList<ToDoItem> list = new ArrayList();
    private final static String SAVE_FILE = "instance.json";
//    private Button addButton;
    private SaveLoadHandler<ArrayList<ToDoItem>> slh;
    public File FILE;

    private static SharedPreferences sharedPref;
    public static final String PREFS_NAME = "PrefsFile";
    public static final String FIRST_START = "FirstStart";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //init save/load handler
        FILE = new File(this.getFilesDir() + File.separator + SAVE_FILE);
        Type type = new TypeToken<ArrayList<ToDoItem>>(){}.getType();
        this.slh = new SaveLoadHandler(type, FILE);

        //Prefs for detecting first time starting
        sharedPref = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        Boolean firstStart = sharedPref.getBoolean(FIRST_START, false);

        if(firstStart) {
            //load data and sort
            ArrayList<ToDoItem> listTemp = this.slh.loadData();
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
        final ListAdapter adapter = new ListAdapter(this, R.layout.todo_row,
                list, listview);
        listview.setAdapter(adapter);

        //init touch listerners
        initButtonListener(Main.this, adapter);
    }

    @Override
    protected void onPause() {
        super.onPause();
        slh.saveData(list);
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
                        addButton.setBackgroundColor(winOrange);
                        break;
                    }
                    case MotionEvent.ACTION_UP: {
                        addButton.setBackgroundColor(indRed);
                        break;
                    }
                }
                return false;
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
