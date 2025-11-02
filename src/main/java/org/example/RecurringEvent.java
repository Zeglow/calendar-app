package org.example;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents a recurring event that repeats weekly.
 */
public class RecurringEvent {

  private final Event baseEvent;
  private final DayOfWeek dayOfWeek;
  private final Integer count;  // 重复次数（可选）
  private final LocalDate endDate;  // 结束日期（可选）

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
    // 验证：事件不能跨天
    if (!baseEvent.getStartDate().equals(baseEvent.getEndDate())) {
      throw new IllegalArgumentException(
          "Recurring event cannot span multiple days");
    }

    // 验证：必须指定count或endDate之一
    if (count == null && endDate == null) {
      throw new IllegalArgumentException(
          "Must specify either count or end date");
    }

    if (count != null && count <= 0) {
      throw new IllegalArgumentException(
          "Count must be positive");
    }

    this.baseEvent = baseEvent;
    this.dayOfWeek = dayOfWeek;
    this.count = count;
    this.endDate = endDate;
  }

  /**
   * Generates all instances of the recurring event.
   *
   * @return list of event instances
   */
  public List<Event> generateInstances() {
    List<Event> instances = new ArrayList<>();

    // 找到第一个匹配dayOfWeek的日期
    LocalDate currentDate = baseEvent.getStartDate();
    if (currentDate.getDayOfWeek() != dayOfWeek) {
      currentDate = currentDate.with(TemporalAdjusters.next(dayOfWeek));
    }

    // 生成实例
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
   * Creates an event instance for the given date.
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
        baseEvent.getLocation()
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