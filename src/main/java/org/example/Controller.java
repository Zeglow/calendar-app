package org.example;

import java.time.LocalDate;
import java.time.LocalTime;

public class Controller {

  public static void main(String[] args) {

    Calendar calendar = new Calendar("Test");

    calendar.addEvent(new Event("Meeting",
        LocalDate.now(), LocalDate.now(),
        LocalTime.of(10, 0), LocalTime.of(11, 0)));

    new CreateEventView(calendar).setVisible(true);
    new EventDetailView(calendar, calendar.getEvents().get(0)).setVisible(true);
  }
}