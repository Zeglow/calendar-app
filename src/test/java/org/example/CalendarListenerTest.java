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

}