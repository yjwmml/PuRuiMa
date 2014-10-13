package com.yjwmml.puruima.utility;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.yjwmml.puruima.activity.EventListActivity;
import com.yjwmml.puruima.entity.DayEventsList;
import com.yjwmml.puruima.entity.Event;

public class FileUtility {
	
	public static int saveData(DayEventsList dayEventsList) {
		Gson gson = new Gson();
		Type writeType = new TypeToken<ArrayList<Event>>(){}.getType();
		String writeString = gson.toJson(dayEventsList.getDayEventList(), writeType);
		FileOutputStream out = null;
		BufferedWriter writer = null;
		try {
			out = MyApplication.getContext().openFileOutput(dayEventsList.getDate(), Context.MODE_PRIVATE);
			writer = new BufferedWriter(new OutputStreamWriter(out));
			writer.write(writeString);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return 0;
		} finally {
			if (writer != null) {
				try {
					writer.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		return 1;
	}
	
	public static List<Event> loadData(String date) {
		Gson gson = new Gson();
		Type readType = new TypeToken<ArrayList<Event>>(){}.getType();
		FileInputStream in;
		BufferedReader reader = null;
		StringBuilder readContent = new StringBuilder();
		try {
			in = MyApplication.getContext().openFileInput(date);
			reader = new BufferedReader(new InputStreamReader(in));
			String line = "";
			while ((line = reader.readLine()) != null) {
				readContent.append(line);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			return null;
		} finally {
			if (reader != null) {
				try {
					reader.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		return gson.fromJson(readContent.toString(), readType);
	}

    public static int writeDate(String date) {
        Gson gson = new Gson();
        if (EventListActivity.stack.isEmpty() || !date.equals(EventListActivity.stack.get(0))) {
            EventListActivity.stack.add(0, date);
        }
        Type writeType = new TypeToken<ArrayList<String>>(){}.getType();
        String writeString = gson.toJson(EventListActivity.stack, writeType);
        FileOutputStream out;
        BufferedWriter writer = null;
        try {
            out = MyApplication.getContext().openFileOutput("DateList", Context.MODE_PRIVATE);
            writer = new BufferedWriter(new OutputStreamWriter(out));
            writer.write(writeString);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return 0;
        } finally {
            if (writer != null) {
                try {
                    writer.close();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }
        return 1;
    }

    public static void loadDate() {
        Gson gson = new Gson();
        Type readType = new TypeToken<ArrayList<String>>(){}.getType();
        FileInputStream in;
        BufferedReader reader = null;
        StringBuilder readContent = new StringBuilder();
        try {
            in = MyApplication.getContext().openFileInput("DateList");
            reader = new BufferedReader(new InputStreamReader(in));
            String line = "";
            while ((line = reader.readLine()) != null) {
                readContent.append(line);
            }
        } catch (IOException e) {
            // TODO Auto-generated catch block
            return;
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }
        EventListActivity.stack = gson.fromJson(readContent.toString(), readType);
    }
}
