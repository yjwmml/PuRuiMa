package com.yjwmml.puruima.entity;

import com.yjwmml.puruima.utility.ConstantVariable;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by yjwm on 14-10-11.
 */
public class DayEventsList {

    private List<Event> dayEventsList;
    private Date curDate;


    public DayEventsList() {
        dayEventsList = new ArrayList<Event>();
        curDate = Calendar.getInstance().getTime();
    }

    public List<Event> getDayEventList() {
        return dayEventsList;
    }

    public void setDayEventsList(List<Event> dayEventsList) {
        this.dayEventsList = dayEventsList;
    }

    public void addEvent(Event event) {
        dayEventsList.add(event);
    }

    public String getDate() {
        SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd");
        return f.format(curDate);
    }


}
