package com.yjwmml.puruima.activity;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.fortysevendeg.swipelistview.BaseSwipeListViewListener;
import com.fortysevendeg.swipelistview.SwipeListView;
import com.yjwmml.puruima.R;
import com.yjwmml.puruima.dialog.EventDialog;
import com.yjwmml.puruima.entity.DayEventsList;
import com.yjwmml.puruima.entity.Event;
import com.yjwmml.puruima.utility.FileUtility;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;


public class EventListActivity extends FragmentActivity implements EventDialog.AddEventDialogListener {

    private SwipeListView event_listview;
    private DayEventsList cur_dayEvents;
    private EventListAdapter adapter;
    public static ArrayList<String> stack;
    public static List<Event>[] data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Date curDate = Calendar.getInstance().getTime();
        SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd");
        setTitle(f.format(curDate));
        setContentView(R.layout.activity_event_list);
        getElements();
        getDayEvents();
        stack = new ArrayList<String>();
        adapter = new EventListAdapter(EventListActivity.this);
        event_listview.setAdapter(adapter);
        event_listview.setSwipeListViewListener(new BaseSwipeListViewListener() {
            @Override
            public void onClickFrontView(int position) {
                if (cur_dayEvents.getDayEventList().get(position).isClicked()) {
                    cur_dayEvents.getDayEventList().get(position).setClicked(false);
                } else {
                    cur_dayEvents.getDayEventList().get(position).setClicked(true);
                }
                adapter.notifyDataSetChanged();
            }
        });
        new Thread(loadData).start();
    }

    @Override
    protected void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
        FileUtility.saveData(cur_dayEvents);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.event_list, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        if (id == R.id.action_add_event) {
            addEvent();
        }
        if (id == R.id.action_history) {
            HistoryActivity.startThisActivity(EventListActivity.this);
        }
        return super.onOptionsItemSelected(item);
    }

    /*载入历史数据线程*/
    private Runnable loadData = new Runnable() {
        @Override
        public void run() {
            FileUtility.loadDate();
            FileUtility.writeDate(cur_dayEvents.getDate());
            if (stack.size() - 1 <= 7) {
                data = new List[stack.size() - 1];
            }
            else {
                data = new List[7];
            }
            if (stack.size() > 1) {
                for (int i = 1; i < stack.size(); i++) {
                    if (i > 7) {
                        break;
                    }
                    else {
                        data[i - 1] = FileUtility.loadData(stack.get(i));
                    }
                }
            }
        }
    };

    /*得到控件*/
    public void getElements() {
        event_listview = (SwipeListView) findViewById(R.id.example_lv_list);
    }

    /*得到当日任务列表*/
    public void getDayEvents() {
        cur_dayEvents = new DayEventsList();
        List<Event> result = FileUtility.loadData(cur_dayEvents.getDate());
        if (result != null) {
            cur_dayEvents.setDayEventsList(result);
        }
    }

    /*添加任务*/
    public void addEvent() {
        EventDialog dialog = new EventDialog(cur_dayEvents.getDayEventList(), 1);
        dialog.show(getSupportFragmentManager(), "add_event");
    }

    /*删除任务*/
    public void deleteEvent(int position) {
        cur_dayEvents.getDayEventList().remove(position);
        adapter.notifyDataSetChanged();
    }

    /*更新任务*/
    public void updateEvent(int position) {
        EventDialog dialog = new EventDialog(cur_dayEvents.getDayEventList(), 2, position);
        dialog.show(getSupportFragmentManager(), "update_event");
    }

    /*完成任务*/
    public void completeEvent(int position) {
        if (cur_dayEvents.getDayEventList().get(position).isCompleted()) {
            Toast.makeText(EventListActivity.this, "您已经完成过该任务了！", Toast.LENGTH_LONG).show();
            return;
        }
        if (position != 0) {
            Toast.makeText(EventListActivity.this, "您得先完成您的第一项任务哦！", Toast.LENGTH_LONG).show();
            return;
        }
        Event e = cur_dayEvents.getDayEventList().get(0);
        e.setCompleted(true);
        cur_dayEvents.getDayEventList().remove(0);
        cur_dayEvents.addEvent(e);
        adapter.notifyDataSetChanged();
    }

    /*dialog添加任务的接口实现*/
    @Override
    public void onDialogPositiveClick(DialogFragment dialog, String title, int index) {
        // TODO Auto-generated method stub
        Event event = new Event(title);
        cur_dayEvents.getDayEventList().add(index, event);
        adapter.notifyDataSetChanged();
    }

    /*dialog更新任务的接口实现*/
    @Override
    public void updateClick(DialogFragment dialog, String title, int index, int position) {
        // TODO Auto-generated method stub
        Event event = new Event(title);
        cur_dayEvents.getDayEventList().add(index, event);
        if (position <= index) {
            cur_dayEvents.getDayEventList().remove(position);
        } else {
            cur_dayEvents.getDayEventList().remove(position + 1);
        }
        adapter.notifyDataSetChanged();
    }

    /*SwipListView的适配器*/
    class EventListAdapter extends BaseAdapter {

        private LayoutInflater mLayoutInflater;

        public EventListAdapter(Context context) {
            mLayoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        @Override
        public int getCount() {
            return cur_dayEvents.getDayEventList().size();
        }

        @Override
        public Event getItem(int i) {
            return cur_dayEvents.getDayEventList().get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        public View getView(final int position, View convertView, ViewGroup parent) {
            Event event = getItem(position);
            final ViewHolder viewHolder;
            if (convertView == null) {
                convertView = mLayoutInflater.inflate(R.layout.event_list, parent, false);
                viewHolder = new ViewHolder();
                viewHolder.eventTitle = (TextView) convertView.findViewById(R.id.event_list);
                viewHolder.isCompleted = (TextView) convertView.findViewById(R.id.event_iscompleted);
                viewHolder.bt_delete = (Button) convertView.findViewById(R.id.bt_delete);
                viewHolder.bt_update = (Button) convertView.findViewById(R.id.bt_update);
                viewHolder.bt_complete = (Button) convertView.findViewById(R.id.bt_complete);
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }
            if (event.isClicked()) {
                viewHolder.eventTitle.setText(event.getContent());
            } else {
                viewHolder.eventTitle.setText(event.getTitle());
            }
            if (event.isCompleted()) {
                viewHolder.isCompleted.setText("已完成");
                viewHolder.isCompleted.setTextColor(Color.GREEN);
            } else {
                viewHolder.isCompleted.setText("未完成");
                viewHolder.isCompleted.setTextColor(Color.RED);
            }
            viewHolder.bt_delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    deleteEvent(position);
                    event_listview.closeOpenedItems();
                }
            });
            viewHolder.bt_update.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    updateEvent(position);
                    event_listview.closeOpenedItems();
                }
            });
            viewHolder.bt_complete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    completeEvent(position);
                    event_listview.closeOpenedItems();
                }
            });
            return convertView;
        }

    }

    static class ViewHolder {
        TextView eventTitle;
        TextView isCompleted;
        Button bt_delete;
        Button bt_update;
        Button bt_complete;
    }
}
