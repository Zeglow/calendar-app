package org.example;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public class Controller {

  public static void main(String[] args) {

    // restore calendar
    CalendarStorage storage = new CalendarStorage("calendars.dat");
    List<Calendar> calendars;

    try {
      calendars = storage.restoreAllCalendars();
      System.out.println("Restored " + calendars.size() + " calendar(s)");
    } catch (Exception e) {
      System.out.println("No saved calendars");
      calendars = List.of();
    }

    // choose a calendar
    Calendar cal;
    if (calendars.isEmpty()) {
      cal = new Calendar("My Calendar");
      System.out.println("Created new calendar");
    } else {
      cal = calendars.get(0);
      System.out.println("Selected: " + cal.getTitle());
    }

    // Make sure there is an event
    if (cal.getEvents().isEmpty()) {
      cal.addEvent(new Event("Meeting",
          LocalDate.now(), LocalDate.now(),
          LocalTime.of(10, 0), LocalTime.of(11, 0)));
      System.out.println("Added sample event");
    }

    System.out.println("\nEvents in calendar: " + cal.getEvents().size());
    for (Event e : cal.getEvents()) {
      System.out.println("  - " + e.getSubject());
    }

    new CreateEventView(cal).setVisible(true);
    new EventDetailView(cal, cal.getEvents().get(0)).setVisible(true);

    // Save when exit
    final Calendar finalCal = cal;
    Runtime.getRuntime().addShutdownHook(new Thread(() -> {
      try {
        storage.saveAllCalendars(List.of(finalCal));
        System.out.println("Calendar saved!");
      } catch (Exception e) {
        e.printStackTrace();
      }
    }));
  }
}
