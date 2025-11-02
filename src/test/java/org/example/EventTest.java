package org.example;

import java.time.LocalTime;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;

public class EventTest {

  @Test
  void testCreateEventWithRequiredFields() {
    Event event = new Event("Team Meeting",
        LocalDate.of(2025, 11, 1),
        LocalDate.of(2025, 11, 1));

    assertEquals("Team Meeting", event.getSubject());
    assertEquals(LocalDate.of(2025, 11, 1), event.getStartDate());
    assertEquals(LocalDate.of(2025, 11, 1), event.getEndDate());
  }

  @Test
  void testEndDateCannotBeBeforeStartDate() {
    assertThrows(IllegalArgumentException.class, () -> {
      new Event("Meeting",
          LocalDate.of(2025, 11, 2),
          LocalDate.of(2025, 11, 1));
    });
  }

  @Test
  void testCreateEventWithTime() {
    Event event = new Event("Team Meeting",
        LocalDate.of(2024, 11, 1),
        LocalDate.of(2024, 11, 1),
        LocalTime.of(9, 0),
        LocalTime.of(10, 0));

    assertEquals("Team Meeting", event.getSubject());
    assertEquals(LocalTime.of(9, 0), event.getStartTime());
    assertEquals(LocalTime.of(10, 0), event.getEndTime());
    assertFalse(event.isAllDay());
  }

  @Test
  void testCreateAllDayEvent() {
    // no start time and end time -> all day event
    Event event = new Event("Birthday",
        LocalDate.of(2024, 11, 1),
        LocalDate.of(2024, 11, 1));

    assertTrue(event.isAllDay());
  }

  @Test
  void testStartTimeMustHaveEndTime() {
    assertThrows(IllegalArgumentException.class, () -> {
      new Event("Meeting",
          LocalDate.of(2024, 11, 1),
          LocalDate.of(2024, 11, 1),
          LocalTime.of(9, 0),
          null);
    });
  }

  @Test
  void testEndTimeRequiresStartTime() {
    assertThrows(IllegalArgumentException.class, () -> {
      new Event("Meeting",
          LocalDate.of(2024, 11, 1),
          LocalDate.of(2024, 11, 1),
          null,
          LocalTime.of(10, 0));
    });
  }

  @Test
  void testEndTimeCannotBeBeforeStartTime() {
    // 同一天的事件，结束时间不能早于开始时间
    assertThrows(IllegalArgumentException.class, () -> {
      new Event("Meeting",
          LocalDate.of(2024, 11, 1),
          LocalDate.of(2024, 11, 1),
          LocalTime.of(10, 0),  // 开始时间
          LocalTime.of(9, 0));  // 结束时间更早！
    });
  }

  @Test
  void testEventCanSpanMultipleDays() {
    Event event = new Event("Conference",
        LocalDate.of(2024, 11, 1),
        LocalDate.of(2024, 11, 2),
        LocalTime.of(9, 0),
        LocalTime.of(17, 0));

    assertEquals(LocalDate.of(2024, 11, 1), event.getStartDate());
    assertEquals(LocalDate.of(2024, 11, 2), event.getEndDate());
  }

  @Test
  void testSameDayEventWithSameTime() {
    Event event = new Event("Reminder",
        LocalDate.of(2024, 11, 1),
        LocalDate.of(2024, 11, 1),
        LocalTime.of(9, 0),
        LocalTime.of(9, 0));

    assertEquals(LocalTime.of(9, 0), event.getStartTime());
    assertEquals(LocalTime.of(9, 0), event.getEndTime());
  }
}