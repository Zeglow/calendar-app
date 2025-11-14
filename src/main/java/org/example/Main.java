package org.example;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class Main {

  public static void main(String[] args) throws Exception {

    // Test 1: Storage - Save and Restore
    System.out.println("=== Test 1: Storage ===");

    Calendar work = new Calendar("Work");
    work.addEvent(new Event("Meeting",
        LocalDate.of(2024, 11, 15),
        LocalDate.of(2024, 11, 15),
        LocalTime.of(10, 0),
        LocalTime.of(11, 0)));

    work.addEvent(new Event("Deadline",
        LocalDate.of(2024, 12, 1),
        LocalDate.of(2024, 12, 1)));

    System.out.println("Created calendar: " + work.getTitle());
    System.out.println("Events: " + work.getEvents().size());

    List<Calendar> cals = new ArrayList<>();
    cals.add(work);

    // Save
    CalendarStorage storage = new CalendarStorage("my_calendars.dat");
    storage.saveAllCalendars(cals);
    System.out.println("Saved to file");

    // Restore
    List<Calendar> restored = storage.restoreAllCalendars();
    System.out.println("Restored " + restored.size() + " calendar(s)");
    System.out.println("Events in restored calendar: " + restored.get(0).getEvents().size());

    for (Event e : restored.get(0).getEvents()) {
      System.out.println("  - " + e.getSubject());
    }

    // Test 2: Importer
    System.out.println("\n=== Test 2: Import CSV ===");

    Calendar imported = new Calendar("Imported");

    String csv = """
        Subject,Start Date,Start Time,End Date,End Time,All Day Event,Description,Location,Private
        Birthday,11/20/2024,,11/20/2024,,True,Party,Home,False
        Lunch,11/21/2024,12:00 PM,11/21/2024,01:00 PM,False,,Cafe,False
        """;

    CalendarImporter importer = new CalendarImporter();
    importer.importFromGoogleCalendarCsv(imported, csv);

    System.out.println("Imported " + imported.getEvents().size() + " events:");
    for (Event e : imported.getEvents()) {
      System.out.println("  - " + e.getSubject() + " on " + e.getStartDate());
    }

    // Test 3: Export
    System.out.println("\n=== Test 3: Export CSV ===");

    CalendarExporter exporter = new CalendarExporter();
    String exported = exporter.exportToGoogleCalendarCsv(work);
    System.out.println(exported);

    System.out.println("\n=== All working! ===");
  }
}