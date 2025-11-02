package org.example;

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
}