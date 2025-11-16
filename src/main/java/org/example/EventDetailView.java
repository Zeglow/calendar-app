package org.example;

import java.awt.GridLayout;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

/**
 * EventDetailView class provides a GUI for viewing and editing existing calendar events.
 */
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

  /**
   * Constructs an EventDetailView with the specified calendar and event.
   *
   * @param calendar the calendar containing the event
   * @param event    the event to display and edit
   */
  public EventDetailView(Calendar calendar, Event event) {
    this.calendar = calendar;
    this.event = event;
    setupUserIterface();
    loadEventData();
  }

  // AI Generated
  private void setupUserIterface() {
    setTitle("Event Details");
    setSize(400, 500);
    setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    setLayout(new GridLayout(10, 2, 5, 5));

    add(new JLabel("Subject:"));
    subjectField = new JTextField();
    add(subjectField);

    add(new JLabel("Start Date (YYYY-MM-DD):"));
    startDateField = new JTextField();
    add(startDateField);

    add(new JLabel("End Date (YYYY-MM-DD):"));
    endDateField = new JTextField();
    add(endDateField);

    add(new JLabel("Start Time (HH:MM, optional):"));
    startTimeField = new JTextField();
    add(startTimeField);

    add(new JLabel("End Time (HH:MM, optional):"));
    endTimeField = new JTextField();
    add(endTimeField);

    add(new JLabel("Description (optional):"));
    descriptionField = new JTextField();
    add(descriptionField);

    add(new JLabel("Location (optional):"));
    locationField = new JTextField();
    add(locationField);

    add(new JLabel("Visibility:"));
    visibilityBox = new JComboBox<>(new String[]{"public", "private"});
    add(visibilityBox);

    JButton saveButton = new JButton("Save Changes");
    saveButton.addActionListener(e -> saveChanges());
    add(saveButton);

    JButton cancelButton = new JButton("Cancel");
    cancelButton.addActionListener(e -> dispose());
    add(cancelButton);
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

      try {
        new CalendarStorage("calendars.dat").saveAllCalendars(List.of(calendar));
      } catch (Exception saveEx) {
        // ignore save errors
      }

      JOptionPane.showMessageDialog(this, "Event updated successfully!");
      dispose();

    } catch (Exception ex) {
      JOptionPane.showMessageDialog(this,
          "Error: " + ex.getMessage(),
          "Error", JOptionPane.ERROR_MESSAGE);
    }
  }
}