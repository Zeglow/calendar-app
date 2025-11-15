package org.example;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;
import java.time.LocalTime;

public class EventDetailView extends JFrame {

  private Calendar calendar;
  private Event event;

  private JTextField subjectField;
  private JTextField startDateField;
  private JTextField endDateField;
  private JTextField startTimeField;
  private JTextField endTimeField;
  private JTextField descriptionField;
  private JTextField locationField;
  private JComboBox<String> visibilityBox;

  public EventDetailView(Calendar calendar, Event event) {
    this.calendar = calendar;
    this.event = event;
    setupUI();
    loadEventData();
  }

  // AI Generated
  private void setupUI() {
  }

  private void loadEventData() {
    subjectField.setText(event.getSubject());
    startDateField.setText(event.getStartDate().toString());
    endDateField.setText(event.getEndDate().toString());

    if (event.getStartTime() != null) {
      startTimeField.setText(event.getStartTime().toString());
    }
    if (event.getEndTime() != null) {
      endTimeField.setText(event.getEndTime().toString());
    }
    if (event.getDescription() != null) {
      descriptionField.setText(event.getDescription());
    }
    if (event.getLocation() != null) {
      locationField.setText(event.getLocation());
    }
    if (event.getVisibility() != null) {
      visibilityBox.setSelectedItem(event.getVisibility());
    }
  }

  private void saveChanges() {
    try {
      // AI Generated
      String subject = subjectField.getText().trim();
      LocalDate startDate = LocalDate.parse(startDateField.getText().trim());
      LocalDate endDate = LocalDate.parse(endDateField.getText().trim());

      String startTimeStr = startTimeField.getText().trim();
      String endTimeStr = endTimeField.getText().trim();
      LocalTime startTime = startTimeStr.isEmpty() ? null : LocalTime.parse(startTimeStr);
      LocalTime endTime = endTimeStr.isEmpty() ? null : LocalTime.parse(endTimeStr);

      String description = descriptionField.getText().trim();
      String location = locationField.getText().trim();
      String visibility = (String) visibilityBox.getSelectedItem();

      Event newEvent = new Event(
          subject, startDate, endDate, startTime, endTime,
          visibility,
          description.isEmpty() ? null : description,
          location.isEmpty() ? null : location
      );
      calendar.updateEvent(event, newEvent);

    } catch (Exception ex) {
    }
  }
}