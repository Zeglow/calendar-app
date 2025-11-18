package org.example;

import java.time.DayOfWeek;
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

  @Test
  void testUpdateEvent() {
    Calendar calendar = new Calendar("My Calendar");

    Event original = new Event("Meeting",
        LocalDate.of(2024, 11, 1),
        LocalDate.of(2024, 11, 1),
        LocalTime.of(9, 0),
        LocalTime.of(10, 0));

    calendar.addEvent(original);

    Event updated = new Event("Meeting",
        LocalDate.of(2024, 11, 1),
        LocalDate.of(2024, 11, 1),
        LocalTime.of(9, 0),
        LocalTime.of(11, 0),  // 新的结束时间
        "public",
        "Updated meeting",
        "Room 202");

    calendar.updateEvent(original, updated);

    assertEquals(1, calendar.getEvents().size());
    Event result = calendar.getEvents().get(0);
    assertEquals("Room 202", result.getLocation());
    assertEquals(LocalTime.of(11, 0), result.getEndTime());
  }

  @Test
  void testUpdateEventChangingSubject() {
    Calendar calendar = new Calendar("My Calendar");

    Event original = new Event("Old Subject",
        LocalDate.of(2024, 11, 1),
        LocalDate.of(2024, 11, 1));

    calendar.addEvent(original);

    Event updated = new Event("New Subject",
        LocalDate.of(2024, 11, 1),
        LocalDate.of(2024, 11, 1));

    calendar.updateEvent(original, updated);

    assertEquals(1, calendar.getEvents().size());
    assertEquals("New Subject", calendar.getEvents().get(0).getSubject());
  }

  @Test
  void testUpdateEventCausingDuplicate() {
    Calendar calendar = new Calendar("My Calendar");

    Event event1 = new Event("Meeting 1",
        LocalDate.of(2024, 11, 1),
        LocalDate.of(2024, 11, 1),
        LocalTime.of(9, 0),
        LocalTime.of(10, 0));

    Event event2 = new Event("Meeting 2",
        LocalDate.of(2024, 11, 1),
        LocalDate.of(2024, 11, 1),
        LocalTime.of(14, 0),
        LocalTime.of(15, 0));

    calendar.addEvent(event1);
    calendar.addEvent(event2);

    Event updatedEvent2 = new Event("Meeting 1",
        LocalDate.of(2024, 11, 1),
        LocalDate.of(2024, 11, 1),
        LocalTime.of(9, 0),
        LocalTime.of(10, 0));

    assertThrows(IllegalArgumentException.class, () -> {
      calendar.updateEvent(event2, updatedEvent2);
    });
  }

  @Test
  void testUpdateEventWithInvalidData() {
    Calendar calendar = new Calendar("My Calendar");

    Event original = new Event("Meeting",
        LocalDate.of(2024, 11, 1),
        LocalDate.of(2024, 11, 1),
        LocalTime.of(9, 0),
        LocalTime.of(10, 0));

    calendar.addEvent(original);

    assertThrows(IllegalArgumentException.class, () -> {
      Event invalid = new Event("Meeting",
          LocalDate.of(2024, 11, 2),
          LocalDate.of(2024, 11, 1),
          LocalTime.of(9, 0),
          LocalTime.of(10, 0));
      calendar.updateEvent(original, invalid);
    });
  }

  @Test
  void testUpdateNonExistentEvent() {
    Calendar calendar = new Calendar("My Calendar");

    Event existing = new Event("Meeting",
        LocalDate.of(2024, 11, 1),
        LocalDate.of(2024, 11, 1));

    Event nonExistent = new Event("Other Meeting",
        LocalDate.of(2024, 11, 2),
        LocalDate.of(2024, 11, 2));

    Event updated = new Event("Updated Meeting",
        LocalDate.of(2024, 11, 2),
        LocalDate.of(2024, 11, 2));

    calendar.addEvent(existing);

    assertThrows(IllegalArgumentException.class, () -> {
      calendar.updateEvent(nonExistent, updated);
    });
  }

  @Test
  void testUpdateEventWithConflictDetection() {
    Calendar calendar = new Calendar("My Calendar", false);

    Event event1 = new Event("Meeting 1",
        LocalDate.of(2024, 11, 1),
        LocalDate.of(2024, 11, 1),
        LocalTime.of(9, 0),
        LocalTime.of(10, 0));

    Event event2 = new Event("Meeting 2",
        LocalDate.of(2024, 11, 1),
        LocalDate.of(2024, 11, 1),
        LocalTime.of(14, 0),
        LocalTime.of(15, 0));

    calendar.addEvent(event1);
    calendar.addEvent(event2);

    Event updatedEvent2 = new Event("Meeting 2",
        LocalDate.of(2024, 11, 1),
        LocalDate.of(2024, 11, 1),
        LocalTime.of(9, 30),
        LocalTime.of(10, 30));

    assertThrows(IllegalArgumentException.class, () -> {
      calendar.updateEvent(event2, updatedEvent2);
    });
  }

  @Test
  void testAddRecurringEvent() {
    Calendar calendar = new Calendar("My Calendar");

    Event baseEvent = new Event("Weekly Meeting",
        LocalDate.of(2024, 11, 1),
        LocalDate.of(2024, 11, 1),
        LocalTime.of(9, 0),
        LocalTime.of(10, 0));

    RecurringEvent recurring = new RecurringEvent(baseEvent,
        DayOfWeek.FRIDAY, 3);

    calendar.addRecurringEvent(recurring);

    assertEquals(3, calendar.getEvents().size());
  }

  @Test
  void testAddRecurringEventWithConflict() {
    Calendar calendar = new Calendar("My Calendar", false);

    Event existing = new Event("Existing Meeting",
        LocalDate.of(2024, 11, 8),
        LocalDate.of(2024, 11, 8),
        LocalTime.of(9, 0),
        LocalTime.of(10, 0));
    calendar.addEvent(existing);

    Event baseEvent = new Event("Weekly Meeting",
        LocalDate.of(2024, 11, 1),
        LocalDate.of(2024, 11, 1),
        LocalTime.of(9, 0),
        LocalTime.of(10, 0));

    RecurringEvent recurring = new RecurringEvent(baseEvent,
        DayOfWeek.FRIDAY, 3);

    assertThrows(IllegalArgumentException.class, () -> {
      calendar.addRecurringEvent(recurring);
    });

    assertEquals(1, calendar.getEvents().size());
    assertEquals("Existing Meeting", calendar.getEvents().get(0).getSubject());
  }

  @Test
  void testAddRecurringEventWithDuplicate() {
    Calendar calendar = new Calendar("My Calendar");

    Event duplicate = new Event("Weekly Meeting",
        LocalDate.of(2024, 11, 8),
        LocalDate.of(2024, 11, 8),
        LocalTime.of(9, 0),
        LocalTime.of(10, 0));
    calendar.addEvent(duplicate);

    Event baseEvent = new Event("Weekly Meeting",
        LocalDate.of(2024, 11, 1),
        LocalDate.of(2024, 11, 1),
        LocalTime.of(9, 0),
        LocalTime.of(10, 0));

    RecurringEvent recurring = new RecurringEvent(baseEvent,
        DayOfWeek.FRIDAY, 3);

    assertThrows(IllegalArgumentException.class, () -> {
      calendar.addRecurringEvent(recurring);
    });

    assertEquals(1, calendar.getEvents().size());
  }

  @Test
  void testAddRecurringEventAllowsConflictsWhenEnabled() {
    Calendar calendar = new Calendar("My Calendar", true);  // Allow conflicts

    Event existing = new Event("Existing Meeting",
        LocalDate.of(2024, 11, 8),
        LocalDate.of(2024, 11, 8),
        LocalTime.of(9, 0),
        LocalTime.of(10, 0));
    calendar.addEvent(existing);

    Event baseEvent = new Event("Weekly Meeting",
        LocalDate.of(2024, 11, 1),
        LocalDate.of(2024, 11, 1),
        LocalTime.of(9, 0),
        LocalTime.of(10, 0));

    RecurringEvent recurring = new RecurringEvent(baseEvent,
        DayOfWeek.FRIDAY, 3);

    calendar.addRecurringEvent(recurring);
    assertEquals(4, calendar.getEvents().size());
  }

  @Test
  void testFindEventById() {
    Calendar calendar = new Calendar("My Calendar");

    Event event = new Event("Meeting",
        LocalDate.of(2024, 11, 1),
        LocalDate.of(2024, 11, 1));

    calendar.addEvent(event);

    Event found = calendar.findEventById(event.getId());

    assertNotNull(found);
    assertEquals(event.getId(), found.getId());
    assertEquals("Meeting", found.getSubject());
  }

  @Test
  void testFindEventByIdNotFound() {
    Calendar calendar = new Calendar("My Calendar");

    Event found = calendar.findEventById("non-existent-id");

    assertNull(found);
  }

  @Test
  void testFindEventsBySeriesId() {
    Calendar calendar = new Calendar("My Calendar");

    Event baseEvent = new Event("Weekly Meeting",
        LocalDate.of(2024, 11, 1),
        LocalDate.of(2024, 11, 1),
        LocalTime.of(9, 0),
        LocalTime.of(10, 0));

    RecurringEvent recurring = new RecurringEvent(baseEvent,
        DayOfWeek.FRIDAY, 3);

    calendar.addRecurringEvent(recurring);

    List<Event> seriesEvents = calendar.findEventsBySeriesId(recurring.getSeriesId());

    assertEquals(3, seriesEvents.size());

    // All should have the same series ID
    for (Event e : seriesEvents) {
      assertEquals(recurring.getSeriesId(), e.getSeriesId());
    }
  }

  @Test
  void testUpdateSingleRecurringInstance() {
    // Edit only one instance of a recurring event
    Calendar calendar = new Calendar("My Calendar");

    Event baseEvent = new Event("Weekly Meeting",
        LocalDate.of(2024, 11, 1),
        LocalDate.of(2024, 11, 1),
        LocalTime.of(9, 0),
        LocalTime.of(10, 0));

    RecurringEvent recurring = new RecurringEvent(baseEvent,
        DayOfWeek.FRIDAY, 3);

    calendar.addRecurringEvent(recurring);

    // Get the second instance (11/8)
    List<Event> instances = calendar.findEventsBySeriesId(recurring.getSeriesId());
    Event secondInstance = instances.get(1);

    // Update only this instance
    Event updated = new Event("Weekly Meeting - UPDATED",
        LocalDate.of(2024, 11, 8),
        LocalDate.of(2024, 11, 8),
        LocalTime.of(10, 0),  // Different time
        LocalTime.of(11, 0),
        "public",
        "This instance is special",
        "Different Room");

    calendar.updateRecurringInstance(secondInstance.getId(), updated);

    // Should still have 3 events
    assertEquals(3, calendar.getEvents().size());

    // Second instance should be updated
    Event updatedInstance = calendar.findEventById(secondInstance.getId());
    assertEquals("Weekly Meeting - UPDATED", updatedInstance.getSubject());
    assertEquals(LocalTime.of(10, 0), updatedInstance.getStartTime());

    // Other instances should be unchanged
    Event firstInstance = instances.get(0);
    Event foundFirst = calendar.findEventById(firstInstance.getId());
    assertEquals("Weekly Meeting", foundFirst.getSubject());
    assertEquals(LocalTime.of(9, 0), foundFirst.getStartTime());
  }

  @Test
  void testUpdateRecurringSeriesFromDate() {
    // Edit all instances from a specific date onwards
    Calendar calendar = new Calendar("My Calendar");

    Event baseEvent = new Event("Weekly Meeting",
        LocalDate.of(2024, 11, 1),
        LocalDate.of(2024, 11, 1),
        LocalTime.of(9, 0),
        LocalTime.of(10, 0));

    RecurringEvent recurring = new RecurringEvent(baseEvent,
        DayOfWeek.FRIDAY, 4);  // 4 instances

    calendar.addRecurringEvent(recurring);

    // Update from 11/8 onwards
    Event updatedTemplate = new Event("Weekly Meeting - New Time",
        LocalDate.of(2024, 11, 8),
        LocalDate.of(2024, 11, 8),
        LocalTime.of(14, 0),  // Different time
        LocalTime.of(15, 0));

    calendar.updateRecurringSeriesFromDate(
        recurring.getSeriesId(),
        LocalDate.of(2024, 11, 8),
        updatedTemplate);

    // Should still have 4 events
    assertEquals(4, calendar.getEvents().size());

    // First instance (11/1) should be unchanged
    List<Event> nov1 = calendar.findEventsByDate(LocalDate.of(2024, 11, 1));
    assertEquals("Weekly Meeting", nov1.get(0).getSubject());
    assertEquals(LocalTime.of(9, 0), nov1.get(0).getStartTime());

    // Later instances (11/8, 11/15, 11/22) should be updated
    List<Event> nov8 = calendar.findEventsByDate(LocalDate.of(2024, 11, 8));
    assertEquals("Weekly Meeting - New Time", nov8.get(0).getSubject());
    assertEquals(LocalTime.of(14, 0), nov8.get(0).getStartTime());

    List<Event> nov15 = calendar.findEventsByDate(LocalDate.of(2024, 11, 15));
    assertEquals("Weekly Meeting - New Time", nov15.get(0).getSubject());
  }

  @Test
  void testUpdateEntireRecurringSeries() {
    // Edit all instances of a recurring event
    Calendar calendar = new Calendar("My Calendar");

    Event baseEvent = new Event("Weekly Meeting",
        LocalDate.of(2024, 11, 1),
        LocalDate.of(2024, 11, 1),
        LocalTime.of(9, 0),
        LocalTime.of(10, 0));

    RecurringEvent recurring = new RecurringEvent(baseEvent,
        DayOfWeek.FRIDAY, 3);

    calendar.addRecurringEvent(recurring);

    // Update entire series
    Event updatedTemplate = new Event("Weekly Sync",  // New subject
        LocalDate.of(2024, 11, 1),
        LocalDate.of(2024, 11, 1),
        LocalTime.of(14, 0),  // New time
        LocalTime.of(15, 0),
        "private",
        "Updated description",
        "New Room");

    calendar.updateEntireRecurringSeries(recurring.getSeriesId(), updatedTemplate);

    // Should still have 3 events
    assertEquals(3, calendar.getEvents().size());

    // All instances should be updated
    List<Event> allInstances = calendar.findEventsBySeriesId(recurring.getSeriesId());
    for (Event instance : allInstances) {
      assertEquals("Weekly Sync", instance.getSubject());
      assertEquals(LocalTime.of(14, 0), instance.getStartTime());
      assertEquals("private", instance.getVisibility());
      assertEquals("New Room", instance.getLocation());
    }

    // Dates should be preserved
    assertEquals(LocalDate.of(2024, 11, 1), allInstances.get(0).getStartDate());
    assertEquals(LocalDate.of(2024, 11, 8), allInstances.get(1).getStartDate());
    assertEquals(LocalDate.of(2024, 11, 15), allInstances.get(2).getStartDate());
  }
}