package org.example;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

// Practice review version

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

  /**
   * Finds all events with the given subject.
   *
   * @param subject the subject to search for
   * @return list of matching events
   */
  public List<Event> findEventsBySubject(String subject) {
    List<Event> result = new ArrayList<>();
    for (Event event : events) {
      if (event.getSubject().equals(subject)) {
        result.add(event);
      }
    }
    return result;
  }

  /**
   * Finds all events on the given date.
   *
   * @param date the date to search for
   * @return list of events on that date
   */
  public List<Event> findEventsByDate(LocalDate date) {
    List<Event> result = new ArrayList<>();
    for (Event event : events) {
      if (!date.isBefore(event.getStartDate())
          &&
          !date.isAfter(event.getEndDate())) {
        result.add(event);
      }
    }
    return result;
  }

  /**
   * Finds all events within the given date range.
   *
   * @param startDate the start of the range
   * @param endDate   the end of the range
   * @return list of events in the range
   */
  public List<Event> findEventsInRange(LocalDate startDate, LocalDate endDate) {
    List<Event> result = new ArrayList<>();
    for (Event event : events) {
      // event and range overlaps
      if (!event.getEndDate().isBefore(startDate)
          &&
          !event.getStartDate().isAfter(endDate)) {
        result.add(event);
      }
    }
    return result;
  }

  /**
   * Checks if the calendar is busy at the given date and time.
   *
   * @param dateTime the date and time to check
   * @return true if there is an event at that time
   */
  public boolean isBusy(LocalDateTime dateTime) {
    for (Event event : events) {
      if (isEventActiveAt(event, dateTime)) {
        return true;
      }
    }
    return false;
  }

  /**
   * Checks if an event is active at the given date and time.
   *
   * @param event    the event to check
   * @param dateTime the date and time
   * @return true if the event is active at that time
   */
  private boolean isEventActiveAt(Event event, LocalDateTime dateTime) {
    LocalDate date = dateTime.toLocalDate();

    // check date range
    if (date.isBefore(event.getStartDate()) || date.isAfter(event.getEndDate())) {
      return false;
    }

    if (event.isAllDay()) {
      return true;
    }

    LocalDateTime eventStart = LocalDateTime.of(event.getStartDate(), event.getStartTime());
    LocalDateTime eventEnd = LocalDateTime.of(event.getEndDate(), event.getEndTime());

    return !dateTime.isBefore(eventStart) && dateTime.isBefore(eventEnd);
  }

  /**
   * Updates an existing event with new information. Preserves the event ID.
   *
   * @param oldEvent the event to update
   * @param newEvent the new event data
   * @throws IllegalArgumentException if oldEvent doesn't exist, newEvent would create duplicate, or
   *                                  newEvent would cause conflict
   */
  public void updateEvent(Event oldEvent, Event newEvent) {
    // Check if old event exists
    if (!events.contains(oldEvent)) {
      throw new IllegalArgumentException("Event to update does not exist");
    }

    // Create updated event with preserved ID
    Event updatedEvent = oldEvent.withUpdatedFields(
        newEvent.getSubject(),
        newEvent.getStartDate(),
        newEvent.getEndDate(),
        newEvent.getStartTime(),
        newEvent.getEndTime(),
        newEvent.getVisibility(),
        newEvent.getDescription(),
        newEvent.getLocation()
    );

    // Temporarily remove old event
    events.remove(oldEvent);

    try {
      // Check for duplicate (excluding the event being updated)
      if (events.contains(updatedEvent)) {
        throw new IllegalArgumentException(
            "Updated event would create duplicate");
      }

      // Check for conflict (if enabled)
      if (!allowConflicts && hasConflict(updatedEvent)) {
        throw new IllegalArgumentException(
            "Updated event would cause conflict");
      }

      // Add updated event
      events.add(updatedEvent);

    } catch (Exception e) {
      // Rollback on failure
      events.add(oldEvent);
      throw e;
    }
  }

  /**
   * Adds a recurring event to the calendar. All instances must be valid, or the entire operation
   * fails (atomic).
   *
   * @param recurring the recurring event
   * @throws IllegalArgumentException if any instance would create duplicate or conflict
   */
  public void addRecurringEvent(RecurringEvent recurring) {
    List<Event> instances = recurring.generateInstances();

    for (Event instance : instances) {
      // Check duplicate
      if (events.contains(instance)) {
        throw new IllegalArgumentException(
            "Recurring event would create duplicate at " + instance.getStartDate());
      }

      // Check conflict
      if (!allowConflicts && hasConflict(instance)) {
        throw new IllegalArgumentException(
            "Recurring event would cause conflict at " + instance.getStartDate());
      }
    }

    // Only when all validations pass, add all instances
    events.addAll(instances);
  }

  /**
   * Finds an event by its unique ID.
   *
   * @param id the event ID
   * @return the event, or null if not found
   */
  public Event findEventById(String id) {
    for (Event event : events) {
      if (event.getId().equals(id)) {
        return event;
      }
    }
    return null;
  }

  /**
   * Finds all events belonging to a recurring series.
   *
   * @param seriesId the series ID
   * @return list of events in the series
   */
  public List<Event> findEventsBySeriesId(String seriesId) {
    List<Event> result = new ArrayList<>();
    for (Event event : events) {
      if (seriesId.equals(event.getSeriesId())) {
        result.add(event);
      }
    }
    return result;
  }

  /**
   * Updates a single instance of a recurring event.
   *
   * @param instanceId the ID of the instance to update
   * @param newEvent   the new event data
   * @throws IllegalArgumentException if instance not found or update would cause issues
   */
  public void updateRecurringInstance(String instanceId, Event newEvent) {
    Event oldEvent = findEventById(instanceId);
    if (oldEvent == null) {
      throw new IllegalArgumentException("Event instance not found");
    }

    if (oldEvent.getSeriesId() == null) {
      throw new IllegalArgumentException("Event is not part of a recurring series");
    }

    updateEvent(oldEvent, newEvent);
  }

  /**
   * Updates all instances of a recurring series starting from a specific date.
   */
  public void updateRecurringSeriesFromDate(String seriesId, LocalDate fromDate, Event template) {
    List<Event> seriesEvents = findEventsBySeriesId(seriesId);

    if (seriesEvents.isEmpty()) {
      throw new IllegalArgumentException("Recurring series not found");
    }

    // Find instances from the specified date onwards
    List<Event> toUpdate = new ArrayList<>();
    for (Event event : seriesEvents) {
      if (!event.getStartDate().isBefore(fromDate)) {
        toUpdate.add(event);
      }
    }

    if (toUpdate.isEmpty()) {
      throw new IllegalArgumentException("No instances found from the specified date");
    }

    // Update each instance while preserving its date and ID
    for (Event oldEvent : toUpdate) {
      Event newEvent = oldEvent.withUpdatedFields(
          template.getSubject(),
          oldEvent.getStartDate(),  // Preserve original date
          oldEvent.getEndDate(),
          template.getStartTime(),
          template.getEndTime(),
          template.getVisibility(),
          template.getDescription(),
          template.getLocation()
      );

      updateEvent(oldEvent, newEvent);
    }
  }

  /**
   * Updates all instances of an entire recurring series.
   */
  public void updateEntireRecurringSeries(String seriesId, Event template) {
    List<Event> seriesEvents = findEventsBySeriesId(seriesId);

    if (seriesEvents.isEmpty()) {
      throw new IllegalArgumentException("Recurring series not found");
    }

    // Update all instances while preserving their dates and IDs
    for (Event oldEvent : seriesEvents) {
      Event newEvent = oldEvent.withUpdatedFields(
          template.getSubject(),
          oldEvent.getStartDate(),  // Preserve original date
          oldEvent.getEndDate(),
          template.getStartTime(),
          template.getEndTime(),
          template.getVisibility(),
          template.getDescription(),
          template.getLocation()
      );

      updateEvent(oldEvent, newEvent);
    }
  }
}