package com.yjwmml.puruima.dialog;

import java.util.ArrayList;
import java.util.List;

import com.yjwmml.puruima.R;
import com.yjwmml.puruima.entity.Event;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

public class EventDialog extends DialogFragment{
	
	private TextView edit_add_event;
	private Spinner spinner;
	
	
	private List<String> cur_events_title = new ArrayList<String>();
	private List<Event> cur_events_list = new ArrayList<Event>();
	private int index = 0;
	private int operation;
	private int position;


    public EventDialog(List<Event> cur_events_list, int operation) {
		for (int i = 0; i < cur_events_list.size(); i++) {
			this.cur_events_title.add(cur_events_list.get(i).getTitle());
		}
		this.cur_events_title.add("让它最后完成吧");
		this.index = cur_events_list.size();
		this.operation = operation;
	}
	
	public EventDialog(List<Event> cur_events_list, int operation, int position) {
		this.cur_events_list = cur_events_list;
		for (int i = 0; i < cur_events_list.size(); i++) {
			this.cur_events_title.add(cur_events_list.get(i).getTitle());
		}
		this.cur_events_title.add("让它最后完成吧");
		this.index = position;
		this.operation = operation;
		this.position = position;
	}

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		LayoutInflater inflater = getActivity().getLayoutInflater();
		View view = inflater.inflate(R.layout.dialog_event, null);
		edit_add_event = (TextView) view.findViewById(R.id.edit_event_title);
		if (operation == 2) {
			edit_add_event.setText(cur_events_list.get(position).getContent());
		}
		spinner = (Spinner) view.findViewById(R.id.spinner);
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item,
				cur_events_title);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinner.setAdapter(adapter);
		spinner.setSelection(index, true);
		spinner.setOnItemSelectedListener(new OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int arg0, long id) {
				// TODO Auto-generated method stub
				index = arg0;
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
				// TODO Auto-generated method stub			
			}
		});
		builder.setView(view);
		if (operation == 1) {
			builder.setTitle(R.string.title_add_event);
		}
		else {
			builder.setTitle(R.string.title_update_event);
		}
		builder.setPositiveButton(R.string.ok, new OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub				
				String add_event_title = edit_add_event.getText().toString();
				if (operation == 1) {
					mListener.onDialogPositiveClick(EventDialog.this, add_event_title, index);
				}
				else {
					mListener.updateClick(EventDialog.this, add_event_title, index, position);
				}
			}
		})
		.setNegativeButton(R.string.cancel, new OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				EventDialog.this.getDialog().cancel();
			}
		});
		
		return builder.create();
	}
	
	public interface AddEventDialogListener {
		public void onDialogPositiveClick(DialogFragment dialog, String title, int index);
		public void updateClick(DialogFragment dialog, String title, int index, int position);
	}
	
	AddEventDialogListener mListener;

	@Override
	public void onAttach(Activity activity) {
		// TODO Auto-generated method stub
		super.onAttach(activity);
		try {
			mListener = (AddEventDialogListener) activity;
		} catch (ClassCastException e) {
			throw new ClassCastException(activity.toString() + "must implement "
					+ "AddEventDialogListener");
		}
	}

}
