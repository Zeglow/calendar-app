package org.example;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public class Controller {

  public static void main(String[] args) {

    Calendar calendar = new Calendar("Test");

    calendar.addEvent(new Event("Meeting",
        LocalDate.now(), LocalDate.now(),
        LocalTime.of(10, 0), LocalTime.of(11, 0)));

    // restore calendar
    CalendarStorage storage = new CalendarStorage("calendars.dat");
    List<Calendar> calendars;

    try {
      calendars = storage.restoreAllCalendars();
    } catch (Exception e) {
      calendars = List.of();
    }

    // choose a calendar
    Calendar cal;
    if (calendars.isEmpty()) {
      cal = new Calendar("My Calendar");
    } else {
      cal = calendars.get(0);
    }

    // Make sure there is an event
    if (cal.getEvents().isEmpty()) {
      cal.addEvent(new Event("Meeting",
          LocalDate.now(), LocalDate.now(),
          LocalTime.of(10, 0), LocalTime.of(11, 0)));
    }

    new CreateEventView(cal).setVisible(true);
    new EventDetailView(cal, cal.getEvents().get(0)).setVisible(true);
  }
}
