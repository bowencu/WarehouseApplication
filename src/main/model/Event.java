package model;

import java.util.Calendar;
import java.util.Date;

// Represents an alarm system event.

public class Event {

    private static final int HASH_CONSTANT = 13;
    private Date dateLogged;
    private String description;

    // EFFECTS: creates an event with the given description and the current date/time stamp
    public Event(String description) {
        dateLogged = Calendar.getInstance().getTime();
        this.description = description;
    }

    // EFFECTS: returns the date of this event (includes time)
    public Date getDate() {
        return dateLogged;
    }

    // EFFECTS: returns the description of this event
    public String getDescription() {
        return description;
    }

    // EFFECTS: returns true if other has the same date and description has this, false otherwise
    @Override
    public boolean equals(Object other) {
        if (other == null) {
            return false;
        }

        if (other.getClass() != this.getClass()) {
            return false;
        }

        Event otherEvent = (Event) other;

        return (this.dateLogged.equals(otherEvent.dateLogged)
                && this.description.equals(otherEvent.description));
    }

    // EFFECTS: returns hashcode of this
    @Override
    public int hashCode() {
        return (HASH_CONSTANT * dateLogged.hashCode() + description.hashCode());
    }

    // EFFECTS: returns log date and description of this as a string
    @Override
    public String toString() {
        return dateLogged.toString() + "\n" + description;
    }
}
