package com.yjwmml.puruima.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.TextView;

import com.yjwmml.puruima.R;
import com.yjwmml.puruima.entity.Event;
import com.yjwmml.puruima.utility.ConstantVariable;
import com.yjwmml.puruima.utility.FileUtility;

import java.util.ArrayList;
import java.util.List;

public class HistoryActivity extends Activity {

    private ExpandableListView mListView;
    private List<String> days;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        days = new ArrayList<String>(EventListActivity.stack);
        days.remove(0);
        mListView = (ExpandableListView) findViewById(R.id.history_list_view);
        mListView.setAdapter(mAdapter);
    }

    /*用去其他Activity启动该Activity*/
    public static void startThisActivity(Context context) {
        Intent intent = new Intent(context, HistoryActivity.class);
        context.startActivity(intent);
    }

    ExpandableListAdapter mAdapter = new BaseExpandableListAdapter() {


        private TextView text_date;
        private TextView text_isCompleted;
        private TextView text_task;
        private TextView text_task_isCompleted;

        @Override
        public int getGroupCount() {
            return days.size();
        }

        @Override
        public int getChildrenCount(int i) {
            return EventListActivity.data[i].size();
        }

        @Override
        public String getGroup(int i) {
            return days.get(i);
        }

        @Override
        public Event getChild(int i, int i2) {
            return EventListActivity.data[i].get(i2);
        }

        @Override
        public long getGroupId(int i) {
            return 0;
        }

        @Override
        public long getChildId(int i, int i2) {
            return 0;
        }

        @Override
        public boolean hasStableIds() {
            return false;
        }

        @Override
        public View getGroupView(int i, boolean b, View view, ViewGroup viewGroup) {
            if (view == null) {
                view = ((LayoutInflater)HistoryActivity.this.getSystemService(LAYOUT_INFLATER_SERVICE)).inflate(R.layout.date_list_parent, viewGroup, false);
                text_date = (TextView) view.findViewById(R.id.text_date);
                text_isCompleted = (TextView) view.findViewById(R.id.text_isCompleted);
            }
            text_date.setText(getGroup(i));
            if (getGroup(i) == null || getChild(i, 0) == null || getChild(i, 0).isCompleted()) {
                text_isCompleted.setText("太棒了！全部完成了！");
                text_isCompleted.setTextColor(Color.GREEN);
            }
            else {
                text_isCompleted.setText("您还有任务没有完成！");
                text_isCompleted.setTextColor(Color.RED);
            }
            return view;
        }

        @Override
        public View getChildView(int i, int i2, boolean b, View view, ViewGroup viewGroup) {
            if (view == null) {
                view = ((LayoutInflater)HistoryActivity.this.getSystemService(LAYOUT_INFLATER_SERVICE)).inflate(R.layout.date_list_child, viewGroup, false);
                text_task = (TextView) view.findViewById(R.id.text_task);
                text_task_isCompleted = (TextView) view.findViewById(R.id.text_task_isCompleted);
            }
            if (getChild(i, i2) != null) {
                text_task.setText(getChild(i, i2).getTitle());
                if (getChild(i, i2).isCompleted()) {
                    text_task_isCompleted.setText("已完成");
                    text_task_isCompleted.setTextColor(Color.GREEN);
                }
                else {
                    text_task_isCompleted.setText("未完成");
                    text_task_isCompleted.setTextColor(Color.RED);
                }
            }
            return view;
        }

        @Override
        public boolean isChildSelectable(int i, int i2) {
            return false;
        }

        @Override
        public void notifyDataSetChanged() {
            super.notifyDataSetChanged();
        }
    };

}
