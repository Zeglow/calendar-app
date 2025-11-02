package org.example;

import java.time.LocalTime;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;
import java.time.LocalTime;

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
    assertThrows(IllegalArgumentException.class, () -> {
      new Event("Meeting",
          LocalDate.of(2024, 11, 1),
          LocalDate.of(2024, 11, 1),
          LocalTime.of(10, 0),
          LocalTime.of(9, 0));
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

  @Test
  void testCreateEventWithAllFields() {
    Event event = new Event("Team Meeting",
        LocalDate.of(2024, 11, 1),
        LocalDate.of(2024, 11, 1),
        LocalTime.of(9, 0),
        LocalTime.of(10, 0),
        "public",              // visibility
        "Discuss Q4 goals",    // description
        "Conference Room A");  // location

    assertEquals("Team Meeting", event.getSubject());
    assertEquals("public", event.getVisibility());
    assertEquals("Discuss Q4 goals", event.getDescription());
    assertEquals("Conference Room A", event.getLocation());
  }

  @Test
  void testCreateEventWithOptionalFieldsNull() {
    Event event = new Event("Birthday",
        LocalDate.of(2024, 11, 1),
        LocalDate.of(2024, 11, 1),
        null,
        null,
        null,  // visibility
        null,  // description
        null); // location

    assertTrue(event.isAllDay());
    assertNull(event.getVisibility());
    assertNull(event.getDescription());
    assertNull(event.getLocation());
  }

  @Test
  void testVisibilityCanBePublicOrPrivate() {
    Event publicEvent = new Event("Public Meeting",
        LocalDate.of(2024, 11, 1),
        LocalDate.of(2024, 11, 1),
        null, null,
        "public", null, null);

    Event privateEvent = new Event("Private Meeting",
        LocalDate.of(2024, 11, 1),
        LocalDate.of(2024, 11, 1),
        null, null,
        "private", null, null);

    assertEquals("public", publicEvent.getVisibility());
    assertEquals("private", privateEvent.getVisibility());
  }

  @Test
  void testEqualEventsWithSameKey() {
    // subject + startDate + startTime -> same
    Event event1 = new Event("Meeting",
        LocalDate.of(2024, 11, 1),
        LocalDate.of(2024, 11, 1),
        LocalTime.of(9, 0),
        LocalTime.of(10, 0));

    Event event2 = new Event("Meeting",
        LocalDate.of(2024, 11, 1),
        LocalDate.of(2024, 11, 1),
        LocalTime.of(9, 0),
        LocalTime.of(11, 0));

    assertEquals(event1, event2);
    assertEquals(event1.hashCode(), event2.hashCode());
  }

  @Test
  void testDifferentEventsWithDifferentSubject() {
    Event event1 = new Event("Meeting",
        LocalDate.of(2024, 11, 1),
        LocalDate.of(2024, 11, 1));

    Event event2 = new Event("Lunch",
        LocalDate.of(2024, 11, 1),
        LocalDate.of(2024, 11, 1));

    assertNotEquals(event1, event2);
  }

  @Test
  void testDifferentEventsWithDifferentDate() {
    Event event1 = new Event("Meeting",
        LocalDate.of(2024, 11, 1),
        LocalDate.of(2024, 11, 1));

    Event event2 = new Event("Meeting",
        LocalDate.of(2024, 11, 2),
        LocalDate.of(2024, 11, 2));

    assertNotEquals(event1, event2);
  }

  @Test
  void testDifferentEventsWithDifferentTime() {
    Event event1 = new Event("Meeting",
        LocalDate.of(2024, 11, 1),
        LocalDate.of(2024, 11, 1),
        LocalTime.of(9, 0),
        LocalTime.of(10, 0));

    Event event2 = new Event("Meeting",
        LocalDate.of(2024, 11, 1),
        LocalDate.of(2024, 11, 1),
        LocalTime.of(14, 0),
        LocalTime.of(15, 0));

    assertNotEquals(event1, event2);
  }

  @Test
  void testAllDayEventsEquality() {
    Event event1 = new Event("Birthday",
        LocalDate.of(2024, 11, 1),
        LocalDate.of(2024, 11, 1));

    Event event2 = new Event("Birthday",
        LocalDate.of(2024, 11, 1),
        LocalDate.of(2024, 11, 1));

    assertEquals(event1, event2);
  }

  @Test
  void testToStringContainsSubject() {
    Event event = new Event("Team Meeting",
        LocalDate.of(2024, 11, 1),
        LocalDate.of(2024, 11, 1));

    String str = event.toString();
    assertTrue(str.contains("Team Meeting"));
  }
}