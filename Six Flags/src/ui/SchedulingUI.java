package ui;
 
import controllers.SchedulingController;
import controllers.SchedulingSystem;
import data.EmployeeFileHandler;
import data.ScheduleFileHandler;
import interfaces.ScheduleFileHandlerInterface;
import interfaces.SchedulingControllerInterface;
import models.Employee;
import models.EmployeeSchedule;
 
import javax.swing.*;
import java.awt.*;
import java.util.List;
 
public class SchedulingUI {
    private final JFrame frame;
    private final UserInterface router;
    private final SchedulingControllerInterface schedulingController;
    private final ScheduleFileHandlerInterface scheduleFileHandler;
    private final EmployeeFileHandler employeeFileHandler;
 
    public SchedulingUI(JFrame frame, UserInterface router) {
        this.frame               = frame;
        this.router              = router;
        this.employeeFileHandler = new EmployeeFileHandler("C:/Users/longi/COMS/coms3620/six-flags/Iteration 1/Six Flags/data/employees.txt");
        this.scheduleFileHandler = new ScheduleFileHandler("C:/Users/longi/COMS/coms3620/six-flags/Iteration 1/Six Flags/data/schedules.txt");
        this.schedulingController = new SchedulingController(
            new SchedulingSystem(employeeFileHandler, scheduleFileHandler));
    }
 
    public void showSchedulingMenu() {
        JPanel panel = new JPanel(new GridLayout(4, 1, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(40, 80, 40, 80));
        JLabel label = new JLabel("Employee Management", SwingConstants.CENTER);
        label.setFont(new Font("Arial", Font.BOLD, 18));
        JButton scheduleBtn = new JButton("Schedule Employee");
        JButton viewBtn     = new JButton("View Schedules");
        JButton backBtn     = new JButton("Back");
        scheduleBtn.addActionListener(e -> showEmployeeSelection());
        viewBtn.addActionListener(e     -> showViewSchedules());
        backBtn.addActionListener(e     -> router.showMainMenu());
        panel.add(label); panel.add(scheduleBtn); panel.add(viewBtn); panel.add(backBtn);
        router.setPanel(panel);
    }
 
    private void showEmployeeSelection() {
        List<Employee> employees;
        try { employees = employeeFileHandler.readAllEmployees(); }
        catch (Exception e) { showError("Could not load employees."); return; }
 
        JPanel panel = new JPanel(new GridLayout(employees.size() + 2, 1, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 60, 20, 60));
        JLabel label = new JLabel("Select Employee:", SwingConstants.CENTER);
        label.setFont(new Font("Arial", Font.BOLD, 16));
        panel.add(label);
        for (Employee emp : employees) {
            JButton btn = new JButton(emp.getEmployeeName() + " — " + emp.getDepartmentName());
            btn.addActionListener(e -> showScheduleForm(emp));
            panel.add(btn);
        }
        JButton back = new JButton("Back");
        back.addActionListener(e -> showSchedulingMenu());
        panel.add(back);
        router.setPanel(panel);
    }
 
    private void showScheduleForm(Employee employee) {
        // Spinners
        JSpinner month = new JSpinner(new SpinnerNumberModel(1,    1,    12,   1));
        JSpinner day   = new JSpinner(new SpinnerNumberModel(1,    1,    31,   1));
        JSpinner year  = new JSpinner(new SpinnerNumberModel(2025, 2024, 2030, 1));
        JSpinner sHour = new JSpinner(new SpinnerNumberModel(8,    1,    12,   1));
        JSpinner sMin  = new JSpinner(new SpinnerNumberModel(0,    0,    59,   1));
        JComboBox<String> sAmPm = new JComboBox<>(new String[]{"AM", "PM"});
        JSpinner eHour = new JSpinner(new SpinnerNumberModel(5,    1,    12,   1));
        JSpinner eMin  = new JSpinner(new SpinnerNumberModel(0,    0,    59,   1));
        JComboBox<String> eAmPm = new JComboBox<>(new String[]{"AM", "PM"});
        eAmPm.setSelectedItem("PM");
 
        // Date row
        JPanel dateRow = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        dateRow.add(new JLabel("Month:")); dateRow.add(month);
        dateRow.add(new JLabel("Day:"));   dateRow.add(day);
        dateRow.add(new JLabel("Year:"));  dateRow.add(year);
 
        // Start time row
        JPanel startRow = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        startRow.add(new JLabel("Hour:")); startRow.add(sHour);
        startRow.add(new JLabel("Min:"));  startRow.add(sMin);
        startRow.add(sAmPm);
 
        // End time row
        JPanel endRow = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        endRow.add(new JLabel("Hour:")); endRow.add(eHour);
        endRow.add(new JLabel("Min:"));  endRow.add(eMin);
        endRow.add(eAmPm);
 
        // Button row
        JButton submit = new JButton("Submit");
        JButton back   = new JButton("Back");
        JPanel btnRow  = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        btnRow.add(back); btnRow.add(submit);
 
        // Stack everything vertically
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 40, 20, 40));
 
        panel.add(centered("Scheduling: " + employee.getEmployeeName(), Font.BOLD, 16));
        panel.add(Box.createVerticalStrut(10));
        panel.add(leftLabel("Date:"));     panel.add(dateRow);
        panel.add(Box.createVerticalStrut(8));
        panel.add(leftLabel("Start Time:")); panel.add(startRow);
        panel.add(Box.createVerticalStrut(8));
        panel.add(leftLabel("End Time:"));   panel.add(endRow);
        panel.add(Box.createVerticalStrut(15));
        panel.add(btnRow);
 
        submit.addActionListener(e -> {
            String date  = String.format("%02d/%02d/%04d", month.getValue(), day.getValue(), year.getValue());
            String start = toTime((int)sHour.getValue(), (int)sMin.getValue(), (String)sAmPm.getSelectedItem());
            String end   = toTime((int)eHour.getValue(), (int)eMin.getValue(), (String)eAmPm.getSelectedItem());
            String result = schedulingController.manageEmployeeSchedule(employee.getEmployeeId(), date, start, end);
            boolean ok = result.contains("successfully");
            JOptionPane.showMessageDialog(frame, result, "Result", ok ? JOptionPane.INFORMATION_MESSAGE : JOptionPane.ERROR_MESSAGE);
            if (ok) showSchedulingMenu();
        });
        back.addActionListener(e -> showEmployeeSelection());
 
        router.setPanel(panel);
    }
 
    private void showViewSchedules() {
        List<EmployeeSchedule> schedules;
        try { schedules = scheduleFileHandler.readAllSchedules(); }
        catch (Exception e) { showError("Could not load schedules."); return; }
 
        String[] cols = {"Employee", "Department", "Date", "Start", "End"};
        Object[][] data = schedules.stream().map(s ->
            new Object[]{s.getEmployeeName(), s.getDepartmentName(), s.getScheduleDate(), to12Hour(s.getStartTime()), to12Hour(s.getEndTime())}
        ).toArray(Object[][]::new);
 
        JTable table = new JTable(data, cols) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        JPanel outer = new JPanel(new BorderLayout());
        JLabel lbl = new JLabel("Employee Schedules", SwingConstants.CENTER);
        lbl.setFont(new Font("Arial", Font.BOLD, 16));
        outer.add(lbl, BorderLayout.NORTH);
        outer.add(new JScrollPane(table), BorderLayout.CENTER);
        JButton back = new JButton("Back");
        back.addActionListener(e -> showSchedulingMenu());
        JPanel south = new JPanel(); south.add(back);
        outer.add(south, BorderLayout.SOUTH);
        router.setContentPane(outer);
    }
 
    private JLabel centered(String text, int style, int size) {
        JLabel lbl = new JLabel(text, SwingConstants.CENTER);
        lbl.setFont(new Font("Arial", style, size));
        lbl.setAlignmentX(Component.CENTER_ALIGNMENT);
        return lbl;
    }
 
    private JLabel leftLabel(String text) {
        JLabel lbl = new JLabel(text);
        lbl.setFont(new Font("Arial", Font.BOLD, 13));
        return lbl;
    }
 
    private String toTime(int hour, int min, String amPm) {
        if (amPm.equals("AM") && hour == 12) hour = 0;
        else if (amPm.equals("PM") && hour != 12) hour += 12;
        return String.format("%02d:%02d", hour, min);
    }
 
    private String to12Hour(String time24) {
        String[] parts = time24.split(":");
        int hour = Integer.parseInt(parts[0]);
        int min  = Integer.parseInt(parts[1]);
        String amPm = hour < 12 ? "AM" : "PM";
        if (hour == 0) hour = 12;
        else if (hour > 12) hour -= 12;
        return String.format("%d:%02d %s", hour, min, amPm);
    }
 
    private void showError(String msg) {
        JOptionPane.showMessageDialog(frame, msg, "Error", JOptionPane.ERROR_MESSAGE);
    }
}
 