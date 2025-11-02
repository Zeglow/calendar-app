package org.example;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;
import java.time.LocalTime;

public class CalendarExporterTest {

  @Test
  void testExportEmptyCalendar() {
    Calendar calendar = new Calendar("My Calendar");
    CalendarExporter exporter = new CalendarExporter();

    String csv = exporter.exportToGoogleCalendarCsv(calendar);

    // Should have header row only
    String[] lines = csv.split("\n");
    assertEquals(1, lines.length);
    assertTrue(lines[0].contains("Subject"));
  }

  @Test
  void testExportSingleEvent() {
    Calendar calendar = new Calendar("My Calendar");

    Event event = new Event("Team Meeting",
        LocalDate.of(2024, 11, 1),
        LocalDate.of(2024, 11, 1),
        LocalTime.of(9, 0),
        LocalTime.of(10, 0),
        "public",
        "Discuss Q4 goals",
        "Conference Room A");

    calendar.addEvent(event);

    CalendarExporter exporter = new CalendarExporter();
    String csv = exporter.exportToGoogleCalendarCsv(calendar);

    // Should have header + 1 data row
    String[] lines = csv.split("\n");
    assertEquals(2, lines.length);

    // Check data row contains event info
    String dataRow = lines[1];
    assertTrue(dataRow.contains("Team Meeting"));
    assertTrue(dataRow.contains("11/01/2024"));
    assertTrue(dataRow.contains("09:00 AM"));
    assertTrue(dataRow.contains("10:00 AM"));
    assertTrue(dataRow.contains("Conference Room A"));
  }

  @Test
  void testExportAllDayEvent() {
    Calendar calendar = new Calendar("My Calendar");

    Event allDay = new Event("Birthday Party",
        LocalDate.of(2024, 11, 15),
        LocalDate.of(2024, 11, 15));

    calendar.addEvent(allDay);

    CalendarExporter exporter = new CalendarExporter();
    String csv = exporter.exportToGoogleCalendarCsv(calendar);

    String[] lines = csv.split("\n");
    String dataRow = lines[1];

    // All-day event should have "True" in All Day Event column
    assertTrue(dataRow.contains("Birthday Party"));
    assertTrue(dataRow.contains("True"));
  }

  @Test
  void testExportMultipleEvents() {
    Calendar calendar = new Calendar("My Calendar");

    Event event1 = new Event("Meeting 1",
        LocalDate.of(2024, 11, 1),
        LocalDate.of(2024, 11, 1),
        LocalTime.of(9, 0),
        LocalTime.of(10, 0));

    Event event2 = new Event("Meeting 2",
        LocalDate.of(2024, 11, 2),
        LocalDate.of(2024, 11, 2),
        LocalTime.of(14, 0),
        LocalTime.of(15, 0));

    calendar.addEvent(event1);
    calendar.addEvent(event2);

    CalendarExporter exporter = new CalendarExporter();
    String csv = exporter.exportToGoogleCalendarCsv(calendar);

    String[] lines = csv.split("\n");
    assertEquals(3, lines.length);  // Header + 2 data rows
  }

  @Test
  void testExportPrivateEvent() {
    Calendar calendar = new Calendar("My Calendar");

    Event privateEvent = new Event("Private Meeting",
        LocalDate.of(2024, 11, 1),
        LocalDate.of(2024, 11, 1),
        LocalTime.of(9, 0),
        LocalTime.of(10, 0),
        "private",
        "Confidential",
        "Room 101");

    calendar.addEvent(privateEvent);

    CalendarExporter exporter = new CalendarExporter();
    String csv = exporter.exportToGoogleCalendarCsv(calendar);

    String[] lines = csv.split("\n");
    String dataRow = lines[1];

    // Private event should have "True" in Private column
    assertTrue(dataRow.contains("True"));
  }
}