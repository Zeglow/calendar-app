package org.example;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;

/**
 * Exports calendar data to CSV format compatible with Google Calendar.
 */
public class CalendarExporter {

  private static final DateTimeFormatter DATE_FORMATTER =
      DateTimeFormatter.ofPattern("MM/dd/yyyy", Locale.US);
  private static final DateTimeFormatter TIME_FORMATTER =
      DateTimeFormatter.ofPattern("hh:mm a", Locale.US);

  /**
   * Exports a calendar to Google Calendar CSV format. Format: Subject, Start Date, Start Time, End
   * Date, End Time, All Day Event, Description, Location, Private
   *
   * @param calendar the calendar to export
   * @return CSV string
   */
  public String exportToGoogleCalendarCsv(Calendar calendar) {
    StringBuilder csv = new StringBuilder();

    csv.append("Subject,Start Date,Start Time,End Date,End Time,");
    csv.append("All Day Event,Description,Location,Private\n");

    List<Event> events = calendar.getEvents();
    for (Event event : events) {
      csv.append(eventToCsvRow(event)).append("\n");
    }

    return csv.toString().trim();
  }

  /**
   * Converts a single event to a CSV row.
   *
   * @param event the event
   * @return CSV row string
   */
  private String eventToCsvRow(Event event) {
    StringBuilder row = new StringBuilder();

    row.append(escapeCsvField(event.getSubject())).append(",");

    row.append(event.getStartDate().format(DATE_FORMATTER)).append(",");

    if (event.getStartTime() != null) {
      row.append(formatTime(event.getStartTime()));
    }
    row.append(",");

    row.append(event.getEndDate().format(DATE_FORMATTER)).append(",");

    if (event.getEndTime() != null) {
      row.append(formatTime(event.getEndTime()));
    }
    row.append(",");

    row.append(event.isAllDay() ? "True" : "False").append(",");

    row.append(escapeCsvField(event.getDescription())).append(",");

    row.append(escapeCsvField(event.getLocation())).append(",");

    boolean isPrivate = "private".equalsIgnoreCase(event.getVisibility());
    row.append(isPrivate ? "True" : "False");

    return row.toString();
  }

  /**
   * Formats a time in 12-hour format with AM/PM.
   *
   * @param time the time
   * @return formatted time string
   */
  private String formatTime(LocalTime time) {
    return time.format(TIME_FORMATTER);
  }

  /**
   * Escapes a CSV field, handling quotes and commas.
   *
   * @param field the field value
   * @return escaped field
   */
  private String escapeCsvField(String field) {
    if (field == null || field.isEmpty()) {
      return "";
    }

    if (field.contains(",") || field.contains("\"") || field.contains("\n")) {
      String escaped = field.replace("\"", "\"\"");
      return "\"" + escaped + "\"";
    }

    return field;
  }
}