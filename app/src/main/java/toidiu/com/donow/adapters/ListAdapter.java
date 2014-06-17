package toidiu.com.donow.adapters;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;

import toidiu.com.donow.R;
import toidiu.com.donow.interfaces.AddDiagFragListener;
import toidiu.com.donow.structs.ToDoItem;
import toidiu.com.donow.utils.SortToDo;

public class ListAdapter extends ArrayAdapter<ToDoItem> implements AddDiagFragListener {

    private ArrayList<ToDoItem> list = new ArrayList();
    private HashMap<String, Integer> mIdMap = new HashMap();
    private ListView listView;
    private Context Ctx;
    private ViewHolder holder;
    private static LayoutInflater inflater = null;
    ToDoItem temp = null;

    public ListAdapter(Context ctx, int textViewResourceId, final ArrayList<ToDoItem> list, ListView listView) {
        super(ctx, textViewResourceId, list);
//        for (int i = 0; i < obj.size(); ++i) {
//            mIdMap.put(obj.get(i), i);
//        }

        this.Ctx = ctx;
        this.list = list;
        this.listView = listView;
        this.inflater = (LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        setItemListener();
    }

    public View getView(int position, View convertView, ViewGroup parent) {
//        return super.getView(position, convertView, parent);

        View vi = convertView;

        if (convertView == null) {
            vi = inflater.inflate(R.layout.todo_row, null);

            holder = new ViewHolder();
            holder.t = (TextView) vi.findViewById(R.id.task);
            holder.i = (ImageView) vi.findViewById(R.id.image);
            holder.l = (LinearLayout) vi.findViewById(R.id.imgWrap);

            vi.setTag(holder);

        } else {
            holder = (ViewHolder) vi.getTag();
        }

        if(list.size() <= 0){
            holder.t.setText("no task");
            holder.i.setImageResource(R.drawable.ic_launcher);

        }else{
            temp = null;
            temp = (ToDoItem) this.list.get(position);

            holder.t.setText(temp.getTask());
//            holder.t.setText( "test1" );
            if(temp.getStatus() == true){
                ((TextView) vi.findViewById(R.id.task)).setTextColor(Color.LTGRAY);
                GradientDrawable d = (GradientDrawable) vi.findViewById(R.id.imgWrap).getBackground();
                d.setColor(Ctx.getResources().getColor(R.color.silver));
            }else{
                ((TextView) vi.findViewById(R.id.task)).setTextColor(Color.BLACK);
                GradientDrawable d = (GradientDrawable) vi.findViewById(R.id.imgWrap).getBackground();
                d.setColor(Ctx.getResources().getColor(R.color.LightBlue));
            }
            holder.i.setImageResource(R.drawable.ic_launcher);

        }
            return vi;
    }

    //set listener for list item click
    private void setItemListener() {
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            int i = 0;

            @Override
            public void onItemClick(AdapterView<?> parent, final View view,
                                    int position, long id) {
                i++;
                Handler handler = new Handler();
                final ToDoItem item = (ToDoItem) parent.getItemAtPosition(position);

                Runnable r = new Runnable() {
                    @Override
                    public void run() {
                        if(i==1){
                            Log.d("TEST LIST", "1");
                            item.toggleStatus();
                            SortToDo.sort(list);
                            notifyDataSetChanged();
                            i = 0;
                        } else{
                            Log.d("TEST LIST","2");
                            list.remove(item);
                            notifyDataSetChanged();
                            i = 0;
                        }
                    }
                };

                if(i==1){
                    handler.postDelayed(r, 400);
                }

            }

        });


        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {

            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view,
                                           int position, long id) {
                int i = position - 1 ;
                final ToDoItem item = (ToDoItem) parent.getItemAtPosition(position);

                String s = item.getTask();
                ClipboardManager clipboardManager;
                clipboardManager = (ClipboardManager) Ctx.getSystemService(Context.CLIPBOARD_SERVICE);

                ClipData myClip;
                myClip = ClipData.newPlainText("text", s);
                clipboardManager.setPrimaryClip(myClip);

                Toast.makeText(Ctx, "Copied to clipboard", Toast.LENGTH_LONG).show();

                return true;
            }
        });
    }

    @Override
    public void doPositiveClick(EditText editText) {
        String add = editText.getText().toString();

        if(add != null && !add.isEmpty()){
            ToDoItem toDoItem = new ToDoItem(add);

            list.add(0, toDoItem);
            notifyDataSetChanged();
        }
    }

    @Override
    public void doNegativeClick() {

    }

    public static class ViewHolder{
        public TextView t;
        public ImageView i;
        public LinearLayout l;
    }

}

