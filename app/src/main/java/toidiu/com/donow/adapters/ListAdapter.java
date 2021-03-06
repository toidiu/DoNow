package toidiu.com.donow.adapters;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import de.timroes.android.listview.EnhancedListView;
import toidiu.com.donow.R;
import toidiu.com.donow.interfaces.AddDiagFragListener;
import toidiu.com.donow.structs.ToDoItem;
import toidiu.com.donow.utils.SortToDo;

public class ListAdapter extends ArrayAdapter<ToDoItem> implements AddDiagFragListener {

    private ArrayList<ToDoItem> list = new ArrayList();
    private EnhancedListView listView;
    private Context Ctx;
    private ViewHolder holder;
    private static LayoutInflater inflater = null;
    ToDoItem temp = null;

    public ListAdapter(Context ctx, int textViewResourceId, final ArrayList<ToDoItem> list, EnhancedListView listView) {
        super(ctx, textViewResourceId, list);

        this.Ctx = ctx;
        this.list = list;
        this.listView = listView;
        this.inflater = (LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        setItemListener();
        setSwipeDismiss();
    }

    public View getView(int position, View convertView, ViewGroup parent) {

        View vi = convertView;

        if (convertView == null) {
            vi = inflater.inflate(R.layout.todo_row, null);

            holder = new ViewHolder();
            holder.t = (TextView) vi.findViewById(R.id.task);
            holder.i = (ImageView) vi.findViewById(R.id.image);
            holder.l = (LinearLayout) vi.findViewById(R.id.imgWrap);

            vi.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {

                    int eventaction = event.getAction();
                    switch (eventaction ) {
                        case MotionEvent.ACTION_DOWN: { // touch on the screen event
                            ((TextView) v.findViewById(R.id.task)).setTextColor(Ctx.getResources().getColor(R.color.Goldenrod));
                            break;
                        }
//                        case MotionEvent.ACTION_MOVE: {
//                            ((TextView) v.findViewById(R.id.task)).setTextColor(Ctx.getResources().getColor(R.color.Indigo));
//                            break;
//                        }
//                        case MotionEvent.ACTION_UP: {
//                            ((TextView) v.findViewById(R.id.task)).setTextColor(Ctx.getResources().getColor(R.color.DarkGreen));
//                            break;
//                        }
                    }
//                    GradientDrawable d = (GradientDrawable) v.findViewById(R.id.imgWrap).getBackground();
//                    d.setColor(Ctx.getResources().getColor(R.color.win8_orange));

                    return false;
                }
            });

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
                ((TextView) vi.findViewById(R.id.task)).setTextColor(Ctx.getResources().getColor(R.color.LightGrey));
                GradientDrawable d = (GradientDrawable) vi.findViewById(R.id.imgWrap).getBackground();
                d.setColor(Ctx.getResources().getColor(R.color.silver));
            }else{
                ((TextView) vi.findViewById(R.id.task)).setTextColor(Ctx.getResources().getColor(R.color.black));
                GradientDrawable d = (GradientDrawable) vi.findViewById(R.id.imgWrap).getBackground();
                d.setColor(Ctx.getResources().getColor(R.color.win8_orange));
            }
            holder.i.setImageResource(R.drawable.ic_launcher);

        }
            return vi;
    }

    //set listener for list item click
    private void setItemListener() {
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, final View view,
                                    int pos, long id) {
                final ToDoItem item = (ToDoItem) parent.getItemAtPosition(pos);

                item.toggleStatus();
                SortToDo.sort(list);
                notifyDataSetChanged();

            };
        });


        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view,
                                           int pos, long id) {
                final ToDoItem item = (ToDoItem) parent.getItemAtPosition(pos);

                String s = item.getTask();
                ClipboardManager clipboardManager;
                clipboardManager = (ClipboardManager) Ctx.getSystemService(Context.CLIPBOARD_SERVICE);

                ClipData myClip;
                myClip = ClipData.newPlainText("text", s);
                clipboardManager.setPrimaryClip(myClip);

                Toast.makeText(Ctx, "Task copied", Toast.LENGTH_SHORT).show();

                return false;
            }
        });
    }

    //set swipe to dismiss listener
    private void setSwipeDismiss() {
        listView.setDismissCallback( new EnhancedListView.OnDismissCallback() {
            final EnhancedListView l = listView;
            @Override
            public EnhancedListView.Undoable onDismiss(EnhancedListView enhancedListView, int pos) {
                final ToDoItem item = (ToDoItem) l.getItemAtPosition(pos);
                list.remove(item);
                notifyDataSetChanged();
                return null;
            }
        });

        listView.enableSwipeToDismiss();
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

