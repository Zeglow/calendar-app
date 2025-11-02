package org.example;

import java.time.LocalDate;
import java.time.LocalTime;

/**
 * Represents a calendar event.
 */
public class Event {

  private final String subject;
  private final LocalDate startDate;
  private final LocalDate endDate;
  private final LocalTime startTime;
  private final LocalTime endTime;

  /**
   * Creates a new all-day event.
   *
   * @param subject   the event subject
   * @param startDate the start date
   * @param endDate   the end date
   */
  public Event(String subject, LocalDate startDate, LocalDate endDate) {
    this(subject, startDate, endDate, null, null);
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

    if (startTime != null && endTime != null &&
        startDate.equals(endDate) && endTime.isBefore(startTime)) {
      throw new IllegalArgumentException(
          "End time cannot be before start time on the same day");
    }

    this.subject = subject;
    this.startDate = startDate;
    this.endDate = endDate;
    this.startTime = startTime;
    this.endTime = endTime;
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
   * Checks if this is an all-day event.
   *
   * @return true if the event has no specific times
   */
  public boolean isAllDay() {
    return startTime == null && endTime == null;
  }
}