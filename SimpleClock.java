//package SimpleClock;

import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.TimeZone;
import javax.swing.*;


public class SimpleClock extends JFrame {

        Calendar calendar;
        SimpleDateFormat timeFormat;
        SimpleDateFormat dayFormat;
        SimpleDateFormat dateFormat;

        JLabel timeLabel;
        JLabel dayLabel;
        JLabel dateLabel;
        String time;
        String day;
        String date;

        Thread clockThread;

        boolean is24Hour = false;
        boolean isGMT = false;

        JButton formatButton;
        JButton timezoneButton;

        SimpleClock() {
            this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            this.setTitle("Digital Clock");
            this.setResizable(true);

            timeFormat = new SimpleDateFormat("hh:mm:ss a");
            dayFormat = new SimpleDateFormat("EEEE");
            dateFormat = new SimpleDateFormat("dd MMMM, yyyy");

            timeLabel = new JLabel();
            timeLabel.setFont(new Font("SANS_SERIF", Font.PLAIN, 36));
            timeLabel.setBackground(Color.BLACK);
            timeLabel.setForeground(Color.WHITE);
            timeLabel.setOpaque(true);
            timeLabel.setHorizontalAlignment(SwingConstants.CENTER);
            timeLabel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

            dayLabel = new JLabel();
            dayLabel.setFont(new Font("Ink Free", Font.BOLD, 24));
            dayLabel.setHorizontalAlignment(SwingConstants.CENTER);

            dateLabel = new JLabel();
            dateLabel.setFont(new Font("Ink Free", Font.BOLD, 20));
            dateLabel.setHorizontalAlignment(SwingConstants.CENTER);

            // 12/24hr toggle button
            formatButton = new JButton("Switch to 24-hr");
            formatButton.setFont(new Font("SansSerif", Font.PLAIN, 13));
            formatButton.setFocusPainted(false);
            formatButton.addActionListener(e -> {
                is24Hour = !is24Hour;
                updateFormats();
                formatButton.setText(is24Hour ? "Switch to 12-hr" : "Switch to 24-hr");
            });

            // GMT toggle button
            timezoneButton = new JButton("Switch to GMT");
            timezoneButton.setFont(new Font("SansSerif", Font.PLAIN, 13));
            timezoneButton.setFocusPainted(false);
            timezoneButton.addActionListener(e -> {
                isGMT = !isGMT;
                updateFormats();
                timezoneButton.setText(isGMT ? "Switch to Local" : "Switch to GMT");
            });

            // button panel
            JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
            buttonPanel.add(formatButton);
            buttonPanel.add(timezoneButton);

            // main panel
            JPanel mainPanel = new JPanel();
            mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
            mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));

            timeLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
            dayLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
            dateLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
            buttonPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

            mainPanel.add(timeLabel);
            mainPanel.add(Box.createVerticalStrut(10));
            mainPanel.add(dayLabel);
            mainPanel.add(Box.createVerticalStrut(6));
            mainPanel.add(dateLabel);
            mainPanel.add(Box.createVerticalStrut(14));
            mainPanel.add(buttonPanel);

            this.add(mainPanel);
            this.pack();
            this.setMinimumSize(this.getSize());
            this.setLocationRelativeTo(null);
            this.setVisible(true);

            setTimer();
        }

        private void updateFormats() {
            String timePattern = is24Hour ? "HH:mm:ss" : "hh:mm:ss a";
            TimeZone tz = isGMT ? TimeZone.getTimeZone("GMT") : TimeZone.getDefault();

            timeFormat = new SimpleDateFormat(timePattern);
            dayFormat  = new SimpleDateFormat("EEEE");
            dateFormat = new SimpleDateFormat("dd MMMM, yyyy");

            timeFormat.setTimeZone(tz);
            dayFormat.setTimeZone(tz);
            dateFormat.setTimeZone(tz);
        }

        public void setTimer() {
            clockThread = new Thread(() -> {
                while (!Thread.currentThread().isInterrupted()) {
                    time = timeFormat.format(Calendar.getInstance().getTime());
                    day = dayFormat.format(Calendar.getInstance().getTime());
                    date = dateFormat.format(Calendar.getInstance().getTime());

                    SwingUtilities.invokeLater(() -> {
                        timeLabel.setText(time);
                        dayLabel.setText(day);
                        dateLabel.setText(date);
                    });

                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }
                }
            });

            clockThread.setDaemon(true);
            clockThread.start();
        }

        public static void main(String[] args) {
            SwingUtilities.invokeLater(SimpleClock::new);
        }
    }