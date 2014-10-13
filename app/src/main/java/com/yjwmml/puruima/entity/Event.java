package com.yjwmml.puruima.entity;

/**
 * Created by yjwm on 14-10-11.
 */
public class Event {
    private String title;
    private String content;
    private boolean isCompleted;
    private boolean isClicked;

    public Event(String content) {
        if (content.length() > 9) {
            this.title = content.substring(0, 8) + "······";
        }
        else {
            this.title = content;
        }
        this.content = content;
        this.isCompleted = false;
        this.isClicked = false;
    }

    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public boolean isCompleted() {
        return isCompleted;
    }

    public void setCompleted(boolean isCompleted) {
        this.isCompleted = isCompleted;
    }

    public boolean isClicked() {
        return isClicked;
    }

    public void setClicked(boolean isClicked) {
        this.isClicked = isClicked;
    }

}
