package org.example;

import java.time.LocalDate;
import java.time.LocalTime;
import javax.swing.JComboBox;
import javax.swing.JTextField;

public class EventDetailView {

  private Calendar calendar;
  private Event event;


  public EventDetailView(Calendar calendar, Event event) {
    this.calendar = calendar;
    this.event = event;
  }

  private void saveChanges(String subject, LocalDate startDate, LocalDate endDate,
      LocalTime startTime, LocalTime endTime,
      String description, String location, String visibility) {
    Event newEvent = new Event(subject, startDate, endDate, startTime, endTime, visibility,
        description, location);
    calendar.updateEvent(event, newEvent);
  }
}