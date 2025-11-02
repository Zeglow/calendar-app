package org.example;

import java.time.LocalDate;
import java.time.LocalTime;

public class Main {

  public static void main(String[] args) {
    Event allDayEvent = new Event("Birthday",
        LocalDate.of(2024, 11, 1),
        LocalDate.of(2024, 11, 1));

    System.out.println("Event: " + allDayEvent.getSubject());
    System.out.println("Is all day: " + allDayEvent.isAllDay());

    Event timedEvent = new Event("Meeting",
        LocalDate.of(2024, 11, 1),
        LocalDate.of(2024, 11, 1),
        LocalTime.of(9, 0),
        LocalTime.of(10, 0),
        "public",
        "Team sync",
        "Room 101");

    System.out.println("\nEvent: " + timedEvent.getSubject());
    System.out.println("Time: " + timedEvent.getStartTime() + " - " + timedEvent.getEndTime());
    System.out.println("Location: " + timedEvent.getLocation());

    try {
      Event invalid = new Event("Bad Event",
          LocalDate.of(2024, 11, 2),
          LocalDate.of(2024, 11, 1));
    } catch (IllegalArgumentException e) {
      System.out.println("\nCaught expected error: " + e.getMessage());
    }
  }
}