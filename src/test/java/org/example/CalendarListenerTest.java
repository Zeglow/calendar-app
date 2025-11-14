package org.example;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;
import java.time.LocalTime;

public class CalendarListenerTest {

  private static class TestListener implements CalendarListener {

    int addCount = 0;
    int modifyCount = 0;
    Event lastAdded = null;
    Event lastModified = null;

    @Override
    public void onEventAdded(Event event) {
      addCount++;
      lastAdded = event;
    }

    @Override
    public void onEventModified(Event event) {
      modifyCount++;
      lastModified = event;
    }
  }

  @Test
  public void testListenerCalledOnAddEvent() {
    Calendar calendar = new Calendar("Test");
    TestListener listener = new TestListener();

    calendar.addCalendarListener(listener);

    Event event = new Event("Meeting",
        LocalDate.of(2025, 11, 14),
        LocalDate.of(2025, 11, 14));
    calendar.addEvent(event);

    assertEquals(1, listener.addCount);
    assertEquals("Meeting", listener.lastAdded.getSubject());
  }

  @Test
  public void testListenerCalledOnModifyEvent() {
    Calendar calendar = new Calendar("Test");
    TestListener listener = new TestListener();

    calendar.addCalendarListener(listener);

    Event oldEvent = new Event("Meeting",
        LocalDate.of(2025, 11, 14),
        LocalDate.of(2025, 11, 14));
    calendar.addEvent(oldEvent);

    Event newEvent = new Event("Updated Meeting",
        LocalDate.of(2025, 11, 15),
        LocalDate.of(2025, 11, 15));
    calendar.updateEvent(oldEvent, newEvent);

    assertEquals(1, listener.modifyCount);
    assertEquals("Updated Meeting", listener.lastModified.getSubject());
  }

  @Test
  public void testMultipleListenersCalled() {
    Calendar calendar = new Calendar("Test");
    TestListener listener1 = new TestListener();
    TestListener listener2 = new TestListener();
    TestListener listener3 = new TestListener();

    calendar.addCalendarListener(listener1);
    calendar.addCalendarListener(listener2);
    calendar.addCalendarListener(listener3);

    calendar.addEvent(new Event("Meeting",
        LocalDate.of(2025, 11, 15),
        LocalDate.of(2025, 11, 15)));

    assertEquals(1, listener1.addCount);
    assertEquals(1, listener2.addCount);
    assertEquals(1, listener3.addCount);
  }

  @Test
  public void testRemovedListenerNotCalled() {
    Calendar calendar = new Calendar("Test");
    TestListener listener = new TestListener();

    calendar.addCalendarListener(listener);
    calendar.removeCalendarListener(listener);

    calendar.addEvent(new Event("Meeting",
        LocalDate.of(2025, 11, 15),
        LocalDate.of(2025, 11, 15)));

    assertEquals(0, listener.addCount);
  }

}