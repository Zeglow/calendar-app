package org.example;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
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

  @Test
  void testFindEventsBySubject() {
    Calendar calendar = new Calendar("My Calendar");

    Event event1 = new Event("Meeting",
        LocalDate.of(2024, 11, 1),
        LocalDate.of(2024, 11, 1));
    Event event2 = new Event("Meeting",
        LocalDate.of(2024, 11, 2),
        LocalDate.of(2024, 11, 2));
    Event event3 = new Event("Lunch",
        LocalDate.of(2024, 11, 1),
        LocalDate.of(2024, 11, 1));

    calendar.addEvent(event1);
    calendar.addEvent(event2);
    calendar.addEvent(event3);

    List<Event> meetings = calendar.findEventsBySubject("Meeting");

    assertEquals(2, meetings.size());
    assertTrue(meetings.contains(event1));
    assertTrue(meetings.contains(event2));
  }

  @Test
  void testFindEventsByDate() {
    Calendar calendar = new Calendar("My Calendar");

    Event event1 = new Event("Meeting",
        LocalDate.of(2024, 11, 1),
        LocalDate.of(2024, 11, 1));
    Event event2 = new Event("Lunch",
        LocalDate.of(2024, 11, 1),
        LocalDate.of(2024, 11, 1));
    Event event3 = new Event("Dinner",
        LocalDate.of(2024, 11, 2),
        LocalDate.of(2024, 11, 2));

    calendar.addEvent(event1);
    calendar.addEvent(event2);
    calendar.addEvent(event3);

    List<Event> nov1Events = calendar.findEventsByDate(LocalDate.of(2024, 11, 1));

    assertEquals(2, nov1Events.size());
    assertTrue(nov1Events.contains(event1));
    assertTrue(nov1Events.contains(event2));
  }

  @Test
  void testFindEventsInRange() {
    Calendar calendar = new Calendar("My Calendar");

    Event event1 = new Event("Event 1",
        LocalDate.of(2024, 11, 1),
        LocalDate.of(2024, 11, 1));
    Event event2 = new Event("Event 2",
        LocalDate.of(2024, 11, 5),
        LocalDate.of(2024, 11, 5));
    Event event3 = new Event("Event 3",
        LocalDate.of(2024, 11, 10),
        LocalDate.of(2024, 11, 10));

    calendar.addEvent(event1);
    calendar.addEvent(event2);
    calendar.addEvent(event3);

    // query 11/1 - 11/6
    List<Event> rangeEvents = calendar.findEventsInRange(
        LocalDate.of(2024, 11, 1),
        LocalDate.of(2024, 11, 6));

    assertEquals(2, rangeEvents.size());
    assertTrue(rangeEvents.contains(event1));
    assertTrue(rangeEvents.contains(event2));
    assertFalse(rangeEvents.contains(event3));
  }

  @Test
  void testIsBusy() {
    Calendar calendar = new Calendar("My Calendar");

    Event event = new Event("Meeting",
        LocalDate.of(2024, 11, 1),
        LocalDate.of(2024, 11, 1),
        LocalTime.of(9, 0),
        LocalTime.of(10, 0));

    calendar.addEvent(event);

    assertTrue(calendar.isBusy(LocalDateTime.of(2024, 11, 1, 9, 30)));

    assertFalse(calendar.isBusy(LocalDateTime.of(2024, 11, 1, 8, 0)));

    assertFalse(calendar.isBusy(LocalDateTime.of(2024, 11, 1, 10, 0)));
  }

  @Test
  void testIsBusyWithAllDayEvent() {
    Calendar calendar = new Calendar("My Calendar");

    Event allDay = new Event("All Day Event",
        LocalDate.of(2024, 11, 1),
        LocalDate.of(2024, 11, 1));

    calendar.addEvent(allDay);

    assertTrue(calendar.isBusy(LocalDateTime.of(2024, 11, 1, 0, 0)));
    assertTrue(calendar.isBusy(LocalDateTime.of(2024, 11, 1, 12, 0)));
    assertTrue(calendar.isBusy(LocalDateTime.of(2024, 11, 1, 23, 59)));
  }
}