import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class SettingForm {
    private JButton buttonSourceFileLoad;
    private JTextField textFieldSourceFileLocation;
    private JTextField textFieldTargetFileLocation;
    private JButton buttonTargetFileLoad;
    private JPanel panelSetting;
    private MainForm parent;

    public SettingForm(MainForm mainForm) {
        parent = mainForm;

        JFrame settingFrame = new JFrame("Setting");
        settingFrame.setContentPane(panelSetting);
        settingFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        settingFrame.pack();
        settingFrame.setVisible(true);
        buttonSourceFileLoad.addActionListener(e -> {

        });
        buttonTargetFileLoad.addActionListener(e -> {

        });
    }
}
