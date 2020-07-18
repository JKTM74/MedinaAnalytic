package com.analitic;

import com.analitic.services.Main;
import org.jdesktop.swingx.JXDatePicker;
import org.springframework.stereotype.Component;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Постейший GUI с выбором даты и ивентом на запуск расчета аналитики.
 */
@Component
public class GUI extends JFrame {
    private final Main main;
    private JXDatePicker datePicker = new JXDatePicker();

    public GUI(Main main) {
        super("Аналитика");
        this.main = main;
        this.setBounds(250, 70, 250, 70);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        Container container = this.getContentPane();
        container.setLayout(new GridLayout(1, 2, 20, 0));

        datePicker.setDate(Calendar.getInstance().getTime());
        datePicker.setFormats(new SimpleDateFormat("MM-yyyy"));
        container.add(datePicker);

        JButton button = new JButton("Старт");
        button.addActionListener(new ButtonEventListener());
        container.add(button);

        this.setVisible(true);
    }

    class ButtonEventListener implements ActionListener {

        public void actionPerformed(ActionEvent e) {
            main.startAnalytic(datePicker.getDate());
            JOptionPane.showMessageDialog(GUI.this, "Расчет аналитики завершен", "Аналитика", JOptionPane.PLAIN_MESSAGE);
        }
    }
}