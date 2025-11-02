package org.example;

import java.time.LocalDate;

/**
 * Represents a calendar event.
 */
public class Event {

  private final String subject;
  private final LocalDate startDate;
  private final LocalDate endDate;

  /**
   * Creates a new event with required fields.
   *
   * @param subject   the event subject
   * @param startDate the start date
   * @param endDate   the end date
   */
  public Event(String subject, LocalDate startDate, LocalDate endDate) {
    if (endDate.isBefore(startDate)) {
      throw new IllegalArgumentException(
          "End date cannot be before start date");
    }
    this.subject = subject;
    this.startDate = startDate;
    this.endDate = endDate;
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
}