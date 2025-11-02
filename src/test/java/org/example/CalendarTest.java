package org.example;

import java.time.LocalDate;
import java.time.LocalTime;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class CalendarTest {

  @Test
  void testCreateCalendarWithTitle() {
    Calendar calendar = new Calendar("My Calendar");

    assertEquals("My Calendar", calendar.getTitle());
    assertNotNull(calendar.getEvents());
    assertTrue(calendar.getEvents().isEmpty());
  }

  @Test
  void testAddDuplicateEventThrowsException() {
    Calendar calendar = new Calendar("My Calendar");

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

    calendar.addEvent(event1);

    assertThrows(IllegalArgumentException.class, () -> {
      calendar.addEvent(event2);
    });
  }

  @Test
  void testAddSameSubjectDifferentTimeAllowed() {

    Calendar calendar = new Calendar("My Calendar");

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

    calendar.addEvent(event1);
    calendar.addEvent(event2);

    assertEquals(2, calendar.getEvents().size());
  }

  @Test
  void testAddDuplicateAllDayEventThrowsException() {
    Calendar calendar = new Calendar("My Calendar");

    Event event1 = new Event("Birthday",
        LocalDate.of(2024, 11, 1),
        LocalDate.of(2024, 11, 1));

    Event event2 = new Event("Birthday",
        LocalDate.of(2024, 11, 1),
        LocalDate.of(2024, 11, 1));

    calendar.addEvent(event1);

    assertThrows(IllegalArgumentException.class, () -> {
      calendar.addEvent(event2);
    });
  }

  @Test
  void testCalendarAllowsConflictsByDefault() {
    Calendar calendar = new Calendar("My Calendar");

    Event event1 = new Event("Meeting 1",
        LocalDate.of(2024, 11, 1),
        LocalDate.of(2024, 11, 1),
        LocalTime.of(9, 0),
        LocalTime.of(10, 0));

    Event event2 = new Event("Meeting 2",
        LocalDate.of(2024, 11, 1),
        LocalDate.of(2024, 11, 1),
        LocalTime.of(9, 30),
        LocalTime.of(10, 30));

    calendar.addEvent(event1);
    calendar.addEvent(event2);

    assertEquals(2, calendar.getEvents().size());
  }

  @Test
  void testCalendarWithConflictDetectionEnabled() {
    Calendar calendar = new Calendar("My Calendar", false);

    Event event1 = new Event("Meeting 1",
        LocalDate.of(2024, 11, 1),
        LocalDate.of(2024, 11, 1),
        LocalTime.of(9, 0),
        LocalTime.of(10, 0));

    Event event2 = new Event("Meeting 2",
        LocalDate.of(2024, 11, 1),
        LocalDate.of(2024, 11, 1),
        LocalTime.of(9, 30),
        LocalTime.of(10, 30));

    calendar.addEvent(event1);

    assertThrows(IllegalArgumentException.class, () -> {
      calendar.addEvent(event2);
    });
  }

  @Test
  void testNonOverlappingEventsAllowed() {
    Calendar calendar = new Calendar("My Calendar", false);

    Event event1 = new Event("Meeting 1",
        LocalDate.of(2024, 11, 1),
        LocalDate.of(2024, 11, 1),
        LocalTime.of(9, 0),
        LocalTime.of(10, 0));

    Event event2 = new Event("Meeting 2",
        LocalDate.of(2024, 11, 1),
        LocalDate.of(2024, 11, 1),
        LocalTime.of(10, 0),
        LocalTime.of(11, 0));

    calendar.addEvent(event1);
    calendar.addEvent(event2);

    assertEquals(2, calendar.getEvents().size());
  }

  @Test
  void testAllDayEventConflictDetection() {
    Calendar calendar = new Calendar("My Calendar", false);

    Event allDay = new Event("All Day Event",
        LocalDate.of(2024, 11, 1),
        LocalDate.of(2024, 11, 1));

    Event timed = new Event("Meeting",
        LocalDate.of(2024, 11, 1),
        LocalDate.of(2024, 11, 1),
        LocalTime.of(9, 0),
        LocalTime.of(10, 0));

    calendar.addEvent(allDay);

    assertThrows(IllegalArgumentException.class, () -> {
      calendar.addEvent(timed);
    });
  }
}