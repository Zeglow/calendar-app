package org.example;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Handles saving and restoring multiple calendars to/from persistent storage.
 */
public class CalendarStorage {

  private static final String DEFAULT_STORAGE_FILE = "calendars.dat";
  private final String storageFilePath;

  /**
   * Creates a CalendarStorage with default file path.
   */
  public CalendarStorage() {
    this(DEFAULT_STORAGE_FILE);
  }

  /**
   * Creates a CalendarStorage with specified file path.
   *
   * @param storageFilePath the path to the storage file
   */
  public CalendarStorage(String storageFilePath) {
    this.storageFilePath = storageFilePath;
  }

  /**
   * Saves all calendars to the storage file.
   *
   * @param calendars the list of calendars to save
   * @throws IOException if there's an error writing to the file
   */
  public void saveAllCalendars(List<Calendar> calendars) throws IOException {
    try (PrintWriter writer = new PrintWriter(new FileWriter(storageFilePath))) {
      writer.println("CALENDARS_V1"); // Version marker

      for (Calendar calendar : calendars) {
        saveCalendar(writer, calendar);
      }
    }
  }

  /**
   * Restores all calendars from the storage file.
   *
   * @return list of restored calendars
   * @throws IOException if there's an error reading from the file
   */
  public List<Calendar> restoreAllCalendars() throws IOException {
    File file = new File(storageFilePath);
    if (!file.exists()) {
      return new ArrayList<>();
    }

    List<Calendar> calendars = new ArrayList<>();

    try (BufferedReader reader = new BufferedReader(new FileReader(storageFilePath))) {
      String version = reader.readLine();
      if (!"CALENDARS_V1".equals(version)) {
        throw new IOException("Unsupported storage file version");
      }

      String line;
      while ((line = reader.readLine()) != null) {
        if (line.startsWith("CALENDAR:")) {
          Calendar calendar = restoreCalendar(reader, line);
          calendars.add(calendar);
        }
      }
    }

    return calendars;
  }

  /**
   * Saves a single calendar to the writer.
   *
   * @param writer   the PrintWriter to write to
   * @param calendar the calendar to save
   */
  private void saveCalendar(PrintWriter writer, Calendar calendar) {
    writer.println("CALENDAR:" + escape(calendar.getTitle()));

    for (Event event : calendar.getEvents()) {
      saveEvent(writer, event);
    }

    writer.println("END_CALENDAR");
  }

  /**
   * Saves a single event to the writer.
   *
   * @param writer the PrintWriter to write to
   * @param event  the event to save
   */
  private void saveEvent(PrintWriter writer, Event event) {
    StringBuilder line = new StringBuilder("EVENT:");
    line.append(escape(event.getId())).append("|");
    line.append(escape(event.getSeriesId())).append("|");
    line.append(escape(event.getSubject())).append("|");
    line.append(event.getStartDate()).append("|");
    line.append(event.getEndDate()).append("|");
    line.append(event.getStartTime() != null ? event.getStartTime() : "").append("|");
    line.append(event.getEndTime() != null ? event.getEndTime() : "").append("|");
    line.append(escape(event.getVisibility())).append("|");
    line.append(escape(event.getDescription())).append("|");
    line.append(escape(event.getLocation()));

    writer.println(line);
  }

  /**
   * Restores a single calendar from the reader.
   *
   * @param reader the BufferedReader to read from
   * @param header the header line containing calendar title
   * @return the restored calendar
   * @throws IOException if there's an error reading
   */
  private Calendar restoreCalendar(BufferedReader reader, String header) throws IOException {
    String title = unescape(header.substring("CALENDAR:".length()));
    Calendar calendar = new Calendar(title);

    String line;
    while ((line = reader.readLine()) != null) {
      if (line.equals("END_CALENDAR")) {
        break;
      }

      if (line.startsWith("EVENT:")) {
        Event event = restoreEvent(line);
        // Use a special method to add event without validation (preserving IDs)
        addEventDirectly(calendar, event);
      }
    }

    return calendar;
  }

  /**
   * Restores a single event from a line.
   *
   * @param line the line containing event data
   * @return the restored event
   */
  private Event restoreEvent(String line) {
    String data = line.substring("EVENT:".length());
    String[] parts = data.split("\\|", -1);

    if (parts.length < 10) {
      throw new IllegalArgumentException("Invalid event data format");
    }

    String id = unescape(parts[0]);
    String seriesId = unescape(parts[1]);
    String subject = unescape(parts[2]);
    LocalDate startDate = LocalDate.parse(parts[3]);
    LocalDate endDate = LocalDate.parse(parts[4]);
    LocalTime startTime = parts[5].isEmpty() ? null : LocalTime.parse(parts[5]);
    LocalTime endTime = parts[6].isEmpty() ? null : LocalTime.parse(parts[6]);
    String visibility = unescape(parts[7]);
    String description = unescape(parts[8]);
    String location = unescape(parts[9]);

    // Create event using reflection or a special constructor to preserve ID
    return createEventWithId(id, seriesId, subject, startDate, endDate,
        startTime, endTime, visibility, description, location);
  }

  /**
   * Creates an event with a specific ID (uses reflection to access private constructor).
   *
   * @param id          the event ID
   * @param seriesId    the series ID
   * @param subject     the subject
   * @param startDate   the start date
   * @param endDate     the end date
   * @param startTime   the start time
   * @param endTime     the end time
   * @param visibility  the visibility
   * @param description the description
   * @param location    the location
   * @return the created event
   */
  private Event createEventWithId(String id, String seriesId, String subject,
      LocalDate startDate, LocalDate endDate, LocalTime startTime, LocalTime endTime,
      String visibility, String description, String location) {
    try {
      var constructor = Event.class.getDeclaredConstructor(
          String.class, String.class, LocalDate.class, LocalDate.class,
          LocalTime.class, LocalTime.class, String.class, String.class,
          String.class, String.class);
      constructor.setAccessible(true);
      return constructor.newInstance(id, subject, startDate, endDate,
          startTime, endTime, visibility, description, location, seriesId);
    } catch (Exception e) {
      throw new RuntimeException("Failed to create event with ID", e);
    }
  }

  /**
   * Adds an event directly to the calendar without validation (used during restoration).
   *
   * @param calendar the calendar
   * @param event    the event to add
   */
  private void addEventDirectly(Calendar calendar, Event event) {
    calendar.addEventDirect(event);
  }

  /**
   * Escapes special characters in strings for storage.
   *
   * @param str the string to escape
   * @return the escaped string
   */
  private String escape(String str) {
    if (str == null) {
      return "";
    }
    return str.replace("\\", "\\\\")
        .replace("|", "\\|")
        .replace("\n", "\\n")
        .replace("\r", "\\r");
  }

  /**
   * Unescapes special characters from stored strings.
   *
   * @param str the string to unescape
   * @return the unescaped string, or null if empty
   */
  private String unescape(String str) {
    if (str == null || str.isEmpty()) {
      return null;
    }
    return str.replace("\\r", "\r")
        .replace("\\n", "\n")
        .replace("\\|", "|")
        .replace("\\\\", "\\");
  }
}