package org.example;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

/**
 * Main class for demonstrating calendar functionality.
 */
public class Main {

  /**
   * Demonstrates the calendar application features.
   *
   * @param args command line arguments (not used)
   */
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

    Event baseEvent = new Event("Weekly Team Meeting",
        LocalDate.of(2024, 11, 1),
        LocalDate.of(2024, 11, 1),
        LocalTime.of(9, 0),
        LocalTime.of(10, 0),
        "public",
        "Discuss weekly progress",
        "Conference Room A");

    System.out.println("Base Event: " + baseEvent);
    System.out.println();

    System.out.println("Creating recurring event: Every Friday, 3 times");
    RecurringEvent recurring = new RecurringEvent(baseEvent, DayOfWeek.FRIDAY, 3);

    List<Event> instances = recurring.generateInstances();
    System.out.println("Generated " + instances.size() + " instances:");
    for (int i = 0; i < instances.size(); i++) {
      Event e = instances.get(i);
      System.out.println(
          "  " + (i + 1) + ". " + e.getStartDate() + " " + e.getStartTime() + "-" + e.getEndTime()
              + " (" + e.getStartDate().getDayOfWeek() + ")");
    }
    System.out.println();
  }
}