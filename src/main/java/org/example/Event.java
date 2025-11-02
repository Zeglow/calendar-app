package org.example;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Objects;
import java.util.UUID;

/**
 * Represents a calendar event.
 */
public class Event {

  private final String id;            // Unique identifier
  private final String seriesId;      // Series ID for recurring events (null for regular events)
  private final String subject;
  private final LocalDate startDate;
  private final LocalDate endDate;
  private final LocalTime startTime;
  private final LocalTime endTime;
  private final String visibility;
  private final String description;
  private final String location;

  /**
   * Creates a new all-day event with minimal fields.
   *
   * @param subject   the event subject
   * @param startDate the start date
   * @param endDate   the end date
   */
  public Event(String subject, LocalDate startDate, LocalDate endDate) {
    this(subject, startDate, endDate, null, null, null, null, null, null);
  }

  /**
   * Creates a new event with specific times.
   *
   * @param subject   the event subject
   * @param startDate the start date
   * @param endDate   the end date
   * @param startTime the start time
   * @param endTime   the end time
   */
  public Event(String subject, LocalDate startDate, LocalDate endDate,
      LocalTime startTime, LocalTime endTime) {
    this(subject, startDate, endDate, startTime, endTime, null, null, null, null);
  }

  /**
   * Creates a new event with all fields except series ID.
   *
   * @param subject     the event subject
   * @param startDate   the start date
   * @param endDate     the end date
   * @param startTime   the start time (null for all-day event)
   * @param endTime     the end time (null for all-day event)
   * @param visibility  the visibility ("public" or "private")
   * @param description the event description
   * @param location    the event location
   */
  public Event(String subject, LocalDate startDate, LocalDate endDate,
      LocalTime startTime, LocalTime endTime,
      String visibility, String description, String location) {
    this(subject, startDate, endDate, startTime, endTime,
        visibility, description, location, null);
  }

  /**
   * Creates a new event with all fields including series ID.
   *
   * @param subject     the event subject
   * @param startDate   the start date
   * @param endDate     the end date
   * @param startTime   the start time (null for all-day event)
   * @param endTime     the end time (null for all-day event)
   * @param visibility  the visibility ("public" or "private")
   * @param description the event description
   * @param location    the event location
   * @param seriesId    the series ID for recurring events (null for regular events)
   */
  public Event(String subject, LocalDate startDate, LocalDate endDate,
      LocalTime startTime, LocalTime endTime,
      String visibility, String description, String location,
      String seriesId) {
    if (endDate.isBefore(startDate)) {
      throw new IllegalArgumentException(
          "End date cannot be before start date");
    }

    if (startTime == null && endTime != null) {
      throw new IllegalArgumentException(
          "End time requires start time");
    }

    if (startTime != null && endTime == null) {
      throw new IllegalArgumentException(
          "Start time requires end time");
    }

    if (startTime != null && endTime != null && startDate.equals(endDate) && endTime.isBefore(
        startTime)) {
      throw new IllegalArgumentException(
          "End time cannot be before start time on the same day");
    }

    this.id = UUID.randomUUID().toString();
    this.seriesId = seriesId;
    this.subject = subject;
    this.startDate = startDate;
    this.endDate = endDate;
    this.startTime = startTime;
    this.endTime = endTime;
    this.visibility = visibility;
    this.description = description;
    this.location = location;
  }

  /**
   * Gets the unique event ID.
   *
   * @return the event ID
   */
  public String getId() {
    return id;
  }

  /**
   * Gets the series ID (for recurring events).
   *
   * @return the series ID, or null for regular events
   */
  public String getSeriesId() {
    return seriesId;
  }

  /**
   * Gets the event subject.
   *
   * @return the subject
   */
  public String getSubject() {
    return subject;
  }

  /**
   * Gets the start date.
   *
   * @return the start date
   */
  public LocalDate getStartDate() {
    return startDate;
  }

  /**
   * Gets the end date.
   *
   * @return the end date
   */
  public LocalDate getEndDate() {
    return endDate;
  }

  /**
   * Gets the start time.
   *
   * @return the start time, or null for all-day events
   */
  public LocalTime getStartTime() {
    return startTime;
  }

  /**
   * Gets the end time.
   *
   * @return the end time, or null for all-day events
   */
  public LocalTime getEndTime() {
    return endTime;
  }

  /**
   * Gets the visibility.
   *
   * @return the visibility ("public" or "private"), or null if not set
   */
  public String getVisibility() {
    return visibility;
  }

  /**
   * Gets the description.
   *
   * @return the description, or null if not set
   */
  public String getDescription() {
    return description;
  }

  /**
   * Gets the location.
   *
   * @return the location, or null if not set
   */
  public String getLocation() {
    return location;
  }

  /**
   * Checks if this is an all-day event.
   *
   * @return true if the event has no specific times
   */
  public boolean isAllDay() {
    return startTime == null && endTime == null;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (!(obj instanceof Event)) {
      return false;
    }
    Event other = (Event) obj;
    return subject.equals(other.subject)
        && startDate.equals(other.startDate)
        && Objects.equals(startTime, other.startTime);
  }

  @Override
  public int hashCode() {
    return Objects.hash(subject, startDate, startTime);
  }

  @Override
  public String toString() {
    return String.format("Event[%s, %s %s-%s]", subject, startDate,
        startTime != null ? startTime : "all-day",
        endTime != null ? endTime : "");
  }
}

