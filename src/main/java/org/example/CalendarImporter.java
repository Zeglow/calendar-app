package org.example;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

/**
 * Imports calendar data from CSV format compatible with Google Calendar.
 */
public class CalendarImporter {

  private static final DateTimeFormatter DATE_FORMATTER =
      DateTimeFormatter.ofPattern("MM/dd/yyyy", Locale.US);
  private static final DateTimeFormatter TIME_FORMATTER =
      DateTimeFormatter.ofPattern("hh:mm a", Locale.US);

  /**
   * Imports events from Google Calendar CSV format into the specified calendar. Format: Subject,
   * Start Date, Start Time, End Date, End Time, All Day Event, Description, Location, Private
   *
   * @param calendar the calendar to import into
   * @param csvData  the CSV string to import
   * @throws IOException              if there's an error reading the CSV
   * @throws IllegalArgumentException if the CSV format is invalid
   */
  public void importFromGoogleCalendarCsv(Calendar calendar, String csvData) throws IOException {
    BufferedReader reader = new BufferedReader(new StringReader(csvData));

    // Skip header line
    String header = reader.readLine();
    if (header == null) {
      throw new IllegalArgumentException("CSV is empty");
    }

    String line;
    int lineNumber = 1;
    while ((line = reader.readLine()) != null) {
      lineNumber++;
      if (line.trim().isEmpty()) {
        continue;
      }

      try {
        Event event = parseCsvLine(line);
        calendar.addEvent(event);
      } catch (Exception e) {
        throw new IllegalArgumentException(
            "Error parsing line " + lineNumber + ": " + e.getMessage(), e);
      }
    }
  }

  /**
   * Parses a single CSV line into an Event.
   *
   * @param line the CSV line
   * @return the parsed Event
   */
  private Event parseCsvLine(String line) {
    String[] fields = parseCsvFields(line);

    if (fields.length < 9) {
      throw new IllegalArgumentException(
          "Invalid CSV format: expected 9 fields, got " + fields.length);
    }

    String subject = fields[0];
    LocalDate startDate = LocalDate.parse(fields[1], DATE_FORMATTER);
    String startTimeStr = fields[2];
    LocalDate endDate = LocalDate.parse(fields[3], DATE_FORMATTER);
    String endTimeStr = fields[4];
    boolean isAllDay = "True".equalsIgnoreCase(fields[5]);
    String description = fields[6];
    String location = fields[7];
    boolean isPrivate = "True".equalsIgnoreCase(fields[8]);

    LocalTime startTime = null;
    LocalTime endTime = null;

    if (!isAllDay && !startTimeStr.isEmpty() && !endTimeStr.isEmpty()) {
      startTime = LocalTime.parse(startTimeStr, TIME_FORMATTER);
      endTime = LocalTime.parse(endTimeStr, TIME_FORMATTER);
    }

    String visibility = isPrivate ? "private" : "public";

    return new Event(
        subject,
        startDate,
        endDate,
        startTime,
        endTime,
        visibility,
        description.isEmpty() ? null : description,
        location.isEmpty() ? null : location
    );
  }

  /**
   * Parses CSV fields, handling quoted fields with commas.
   *
   * @param line the CSV line
   * @return array of field values
   */
  private String[] parseCsvFields(String line) {
    String[] fields = new String[9];
    int fieldIndex = 0;
    StringBuilder currentField = new StringBuilder();
    boolean inQuotes = false;

    for (int i = 0; i < line.length(); i++) {
      char c = line.charAt(i);

      if (c == '"') {
        if (inQuotes && i + 1 < line.length() && line.charAt(i + 1) == '"') {
          // Escaped quote
          currentField.append('"');
          i++; // Skip next quote
        } else {
          // Toggle quote state
          inQuotes = !inQuotes;
        }
      } else if (c == ',' && !inQuotes) {
        // Field delimiter
        fields[fieldIndex++] = currentField.toString();
        currentField = new StringBuilder();
      } else {
        currentField.append(c);
      }
    }

    // Add last field
    if (fieldIndex < 9) {
      fields[fieldIndex] = currentField.toString();
    }

    return fields;
  }
}