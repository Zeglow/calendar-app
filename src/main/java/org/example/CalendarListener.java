package org.example;

/**
 * Listener interface for calendar events.
 */
public interface CalendarListener {

  /**
   * Called when an event is added to the calendar.
   *
   * @param event the event that was added
   */
  void onEventAdded(Event event);

  /**
   * Called when an event is modified in the calendar.
   *
   * @param event the event that was modified
   */
  void onEventModified(Event event);
}