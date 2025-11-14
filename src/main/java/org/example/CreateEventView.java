package org.example;

import java.time.LocalDate;
import java.time.LocalTime;

public class CreateEventView {

  private Calendar calendar;

  public CreateEventView(Calendar calendar) {
    this.calendar = calendar;
  }

  private void createEvent(String subject, LocalDate startDate, LocalDate endDate,
      LocalTime startTime, LocalTime endTime, String description, String location,
      String visibility) {
    Event event = new Event(subject, startDate, endDate, startTime, endTime, visibility,
        description, location);
    calendar.addEvent(event);
  }
}