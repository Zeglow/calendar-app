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
 * CreateEventView class provides a GUI for creating new calendar events.
 */
public class CreateEventView extends JFrame {

  private Calendar calendar;

  private JTextField subjectField;
  private JTextField startDateField;
  private JTextField endDateField;
  private JTextField startTimeField;
  private JTextField endTimeField;
  private JTextField descriptionField;
  private JTextField locationField;
  private JComboBox<String> visibilityBox;

  /**
   * Constructs a CreateEventView with the specified calendar.
   *
   * @param calendar the calendar to add events to
   */
  public CreateEventView(Calendar calendar) {
    this.calendar = calendar;
    setupUserInterface();
  }

  /**
   * Sets up the user interface components. AI generated UI.
   */
  private void setupUserInterface() {
    setTitle("Create New Event");
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

    JButton saveButton = new JButton("Create Event");
    saveButton.addActionListener(e -> createEvent());
    add(saveButton);

    JButton cancelButton = new JButton("Cancel");
    cancelButton.addActionListener(e -> dispose());
    add(cancelButton);
  }

  /**
   * Gets data from view and creates a new event.
   */
  private void createEvent() {
    try {
      String subject = subjectField.getText().trim();
      if (subject.isEmpty()) {
        JOptionPane.showMessageDialog(this, "Subject is required",
            "Error", JOptionPane.ERROR_MESSAGE);
        return;
      }

      LocalDate startDate = LocalDate.parse(startDateField.getText().trim());
      LocalDate endDate = LocalDate.parse(endDateField.getText().trim());

      String startTimeStr = startTimeField.getText().trim();
      String endTimeStr = endTimeField.getText().trim();
      LocalTime startTime = startTimeStr.isEmpty() ? null : LocalTime.parse(startTimeStr);
      LocalTime endTime = endTimeStr.isEmpty() ? null : LocalTime.parse(endTimeStr);

      String description = descriptionField.getText().trim();
      String location = locationField.getText().trim();
      String visibility = (String) visibilityBox.getSelectedItem();

      Event event = new Event(subject, startDate, endDate, startTime, endTime,
          visibility,
          description.isEmpty() ? null : description,
          location.isEmpty() ? null : location);

      calendar.addEvent(event);

      try {
        new CalendarStorage("calendars.dat").saveAllCalendars(List.of(calendar));
      } catch (Exception saveEx) {
        // Ignore save errors during event creation （not sure about this）
      }

      JOptionPane.showMessageDialog(this, "Event created successfully!");
      dispose();

    } catch (Exception ex) {
      JOptionPane.showMessageDialog(this,
          "Error: " + ex.getMessage(),
          "Error", JOptionPane.ERROR_MESSAGE);
    }
  }
}