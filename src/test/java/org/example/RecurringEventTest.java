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

  @Test
  void testEventHasUniqueId() {
    Event event1 = new Event("Meeting",
        LocalDate.of(2024, 11, 1),
        LocalDate.of(2024, 11, 1));

    Event event2 = new Event("Meeting",
        LocalDate.of(2024, 11, 1),
        LocalDate.of(2024, 11, 1));

    // Each event should have a unique ID
    assertNotNull(event1.getId());
    assertNotNull(event2.getId());
    assertNotEquals(event1.getId(), event2.getId());
  }

  @Test
  void testEventSeriesId() {
    // Regular event has no series ID
    Event regularEvent = new Event("Meeting",
        LocalDate.of(2024, 11, 1),
        LocalDate.of(2024, 11, 1));

    assertNull(regularEvent.getSeriesId());

    // Event with series ID
    Event seriesEvent = new Event("Meeting", LocalDate.of(2024, 11, 1), LocalDate.of(2024, 11, 1),
        null, null, null, null, null,
        "series-123");  // series ID

    assertEquals("series-123", seriesEvent.getSeriesId());
  }

  @Test
  void testEqualsDoesNotIncludeId() {
    // Two events with same key but different IDs should still be equal
    // (for duplicate detection based on subject+date+time)
    Event event1 = new Event("Meeting",
        LocalDate.of(2024, 11, 1),
        LocalDate.of(2024, 11, 1),
        LocalTime.of(9, 0),
        LocalTime.of(10, 0));

    Event event2 = new Event("Meeting",
        LocalDate.of(2024, 11, 1),
        LocalDate.of(2024, 11, 1),
        LocalTime.of(9, 0),
        LocalTime.of(10, 0));

    // Different IDs but should be equal
    assertNotEquals(event1.getId(), event2.getId());
    assertEquals(event1, event2);
  }
}