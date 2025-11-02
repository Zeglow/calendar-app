package org.example;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Represents a recurring event that repeats weekly.
 */
public class RecurringEvent {

  private final String seriesId;      // Unique identifier for this series
  private final Event baseEvent;
  private final DayOfWeek dayOfWeek;
  private final Integer count;
  private final LocalDate endDate;

  /**
   * Creates a recurring event with a specific count.
   *
   * @param baseEvent the template event
   * @param dayOfWeek the day of week to repeat on
   * @param count     the number of occurrences
   */
  public RecurringEvent(Event baseEvent, DayOfWeek dayOfWeek, int count) {
    this(baseEvent, dayOfWeek, count, null);
  }

  /**
   * Creates a recurring event with an end date.
   *
   * @param baseEvent the template event
   * @param dayOfWeek the day of week to repeat on
   * @param endDate   the last date to generate events
   */
  public RecurringEvent(Event baseEvent, DayOfWeek dayOfWeek, LocalDate endDate) {
    this(baseEvent, dayOfWeek, null, endDate);
  }

  /**
   * Creates a recurring event.
   *
   * @param baseEvent the template event
   * @param dayOfWeek the day of week to repeat on
   * @param count     the number of occurrences (null if using endDate)
   * @param endDate   the last date (null if using count)
   */
  private RecurringEvent(Event baseEvent, DayOfWeek dayOfWeek,
      Integer count, LocalDate endDate) {
    if (!baseEvent.getStartDate().equals(baseEvent.getEndDate())) {
      throw new IllegalArgumentException(
          "Recurring event cannot span multiple days");
    }

    if (count == null && endDate == null) {
      throw new IllegalArgumentException(
          "Must specify either count or end date");
    }

    if (count != null && count <= 0) {
      throw new IllegalArgumentException(
          "Count must be positive");
    }

    this.seriesId = UUID.randomUUID().toString();
    this.baseEvent = baseEvent;
    this.dayOfWeek = dayOfWeek;
    this.count = count;
    this.endDate = endDate;
  }

  /**
   * Gets the series ID.
   *
   * @return the unique series ID
   */
  public String getSeriesId() {
    return seriesId;
  }

  /**
   * Generates all instances of the recurring event. All instances will share the same series ID.
   *
   * @return list of event instances
   */
  public List<Event> generateInstances() {
    List<Event> instances = new ArrayList<>();
    LocalDate currentDate = baseEvent.getStartDate();
    if (currentDate.getDayOfWeek() != dayOfWeek) {
      currentDate = currentDate.with(TemporalAdjusters.next(dayOfWeek));
    }

    int generated = 0;
    while (shouldGenerateMore(currentDate, generated)) {
      Event instance = createInstance(currentDate);
      instances.add(instance);

      generated++;
      currentDate = currentDate.plusWeeks(1);
    }

    return instances;
  }

  /**
   * Checks if more instances should be generated.
   *
   * @param currentDate the current date being considered
   * @param generated   the number of instances already generated
   * @return true if more instances should be created
   */
  private boolean shouldGenerateMore(LocalDate currentDate, int generated) {
    if (count != null) {
      return generated < count;
    } else {
      return !currentDate.isAfter(endDate);
    }
  }

  /**
   * Creates an event instance for the given date. The instance will have the series ID.
   *
   * @param date the date for the instance
   * @return the event instance
   */
  private Event createInstance(LocalDate date) {
    return new Event(
        baseEvent.getSubject(),
        date,
        date,
        baseEvent.getStartTime(),
        baseEvent.getEndTime(),
        baseEvent.getVisibility(),
        baseEvent.getDescription(),
        baseEvent.getLocation(),
        seriesId  // Set series ID for all instances
    );
  }

  /**
   * Gets the base event template.
   *
   * @return the base event
   */
  public Event getBaseEvent() {
    return baseEvent;
  }

  /**
   * Gets the day of week.
   *
   * @return the day of week
   */
  public DayOfWeek getDayOfWeek() {
    return dayOfWeek;
  }
}