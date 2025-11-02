package org.example;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents a calendar that contains events.
 */
public class Calendar {

  private final String title;
  private final List<Event> events;
  private final boolean allowConflicts;

  /**
   * Creates a new calendar with a title. By default, allows time conflicts.
   *
   * @param title the calendar title
   */
  public Calendar(String title) {
    this(title, true);
  }

  /**
   * Creates a new calendar with a title and conflict setting.
   *
   * @param title          the calendar title
   * @param allowConflicts whether to allow time conflicts
   */
  public Calendar(String title, boolean allowConflicts) {
    this.title = title;
    this.events = new ArrayList<>();
    this.allowConflicts = allowConflicts;
  }

  /**
   * Gets the calendar title.
   *
   * @return the title
   */
  public String getTitle() {
    return title;
  }

  /**
   * Gets the list of events.
   *
   * @return the events list
   */
  public List<Event> getEvents() {
    return new ArrayList<>(events);
  }

  /**
   * Adds an event to the calendar.
   *
   * @param event the event to add
   * @throws IllegalArgumentException if duplicate event exists or conflict detected
   */
  public void addEvent(Event event) {
    // 检查重复
    if (events.contains(event)) {
      throw new IllegalArgumentException(
          "Event with same subject, date, and time already exists");
    }

    if (!allowConflicts && hasConflict(event)) {
      throw new IllegalArgumentException(
          "Event conflicts with existing event");
    }

    events.add(event);
  }

  /**
   * Checks if the given event conflicts with any existing event.
   *
   * @param newEvent the event to check
   * @return true if there is a conflict
   */
  private boolean hasConflict(Event newEvent) {
    for (Event existing : events) {
      if (eventsOverlap(existing, newEvent)) {
        return true;
      }
    }
    return false;
  }

  /**
   * Checks if two events overlap in time.
   *
   * @param event1 the first event
   * @param event2 the second event
   * @return true if the events overlap
   */
  private boolean eventsOverlap(Event event1, Event event2) {
    // Date not overlap -> not conflict
    if (event1.getEndDate().isBefore(event2.getStartDate())
        ||
        event2.getEndDate().isBefore(event1.getStartDate())) {
      return false;
    }

    // any of them is All Day Event
    if (event1.isAllDay() || event2.isAllDay()) {
      return datesOverlap(event1, event2);
    }

    // check time overlap
    return timeRangesOverlap(event1, event2);
  }

  /**
   * Checks if two events have overlapping dates.
   *
   * @param event1 the first event
   * @param event2 the second event
   * @return true if dates overlap
   */
  private boolean datesOverlap(Event event1, Event event2) {
    return !(event1.getEndDate().isBefore(event2.getStartDate())
        ||
        event2.getEndDate().isBefore(event1.getStartDate()));
  }

  /**
   * Checks if two timed events have overlapping time ranges.
   *
   * @param event1 the first event
   * @param event2 the second event
   * @return true if time ranges overlap
   */
  private boolean timeRangesOverlap(Event event1, Event event2) {
    LocalDateTime start1 = LocalDateTime.of(event1.getStartDate(), event1.getStartTime());
    LocalDateTime end1 = LocalDateTime.of(event1.getEndDate(), event1.getEndTime());
    LocalDateTime start2 = LocalDateTime.of(event2.getStartDate(), event2.getStartTime());
    LocalDateTime end2 = LocalDateTime.of(event2.getEndDate(), event2.getEndTime());

    // start1 < end2 AND start2 < end1
    return start1.isBefore(end2) && start2.isBefore(end1);
  }
}