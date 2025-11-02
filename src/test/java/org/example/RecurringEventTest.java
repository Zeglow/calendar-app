package org.example;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public class RecurringEventTest {

  @Test
  void testCreateRecurringEventWithCount() {
    Event baseEvent = new Event("Weekly Meeting",
        LocalDate.of(2024, 11, 1),
        LocalDate.of(2024, 11, 1),
        LocalTime.of(9, 0),
        LocalTime.of(10, 0));

    RecurringEvent recurring = new RecurringEvent(baseEvent,
        DayOfWeek.FRIDAY, 3);

    List<Event> instances = recurring.generateInstances();

    assertEquals(3, instances.size());
    assertEquals(LocalDate.of(2024, 11, 1), instances.get(0).getStartDate());
    assertEquals(LocalDate.of(2024, 11, 8), instances.get(1).getStartDate());
    assertEquals(LocalDate.of(2024, 11, 15), instances.get(2).getStartDate());
  }

  @Test
  void testCreateRecurringEventWithEndDate() {
    Event baseEvent = new Event("Weekly Meeting",
        LocalDate.of(2024, 11, 1),
        LocalDate.of(2024, 11, 1),
        LocalTime.of(9, 0),
        LocalTime.of(10, 0));

    RecurringEvent recurring = new RecurringEvent(baseEvent,
        DayOfWeek.FRIDAY,
        LocalDate.of(2024, 11, 20));

    List<Event> instances = recurring.generateInstances();

    assertEquals(3, instances.size());
    assertEquals(LocalDate.of(2024, 11, 15),
        instances.get(instances.size() - 1).getStartDate());
  }

  @Test
  void testRecurringAllDayEvent() {
    Event baseEvent = new Event("Weekly Report",
        LocalDate.of(2024, 11, 4),
        LocalDate.of(2024, 11, 4));

    RecurringEvent recurring = new RecurringEvent(baseEvent,
        DayOfWeek.MONDAY, 4);

    List<Event> instances = recurring.generateInstances();

    assertEquals(4, instances.size());
    assertTrue(instances.get(0).isAllDay());
    assertTrue(instances.get(1).isAllDay());
  }

  @Test
  void testRecurringEventMustNotSpanMultipleDays() {
    Event spanningEvent = new Event("Bad Event",
        LocalDate.of(2024, 11, 1),
        LocalDate.of(2024, 11, 2),
        LocalTime.of(9, 0),
        LocalTime.of(10, 0));

    assertThrows(IllegalArgumentException.class, () -> {
      new RecurringEvent(spanningEvent, DayOfWeek.FRIDAY, 3);
    });
  }
}