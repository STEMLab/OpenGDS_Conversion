import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

public class SettingForm {
    private JTextField textFieldSourceFileLocation;
    private JTextField textFieldTargetFileLocation;
    private JTextField textFieldTransformRuleLocation;
    private JButton buttonSourceFileLoad;
    private JButton buttonTargetFileLoad;
    private JButton buttonTransRuleLoad;
    private JButton buttonSaveInfo;
    private JPanel panelSetting;
    private JFrame settingFrame;
    private MainForm parent;

    public SettingForm(MainForm mainForm) {
        parent = mainForm;

        settingFrame = new JFrame("Setting");
        settingFrame.setContentPane(panelSetting);
        settingFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        settingFrame.pack();
        settingFrame.setVisible(true);

        buttonSourceFileLoad.addActionListener(e -> {
            JFileChooser fileChooser = new JFileChooser();
            //FileNameExtensionFilter filter = new FileNameExtensionFilter("txt (*.txt)", "txt");
            //fileChooser.setFileFilter(filter);

            int returnVal = fileChooser.showOpenDialog(settingFrame);
            if (returnVal == JFileChooser.APPROVE_OPTION) {
                File file = fileChooser.getSelectedFile();
                parent.setSourceFileLocation(file.getAbsolutePath());
                textFieldSourceFileLocation.setText(file.getAbsolutePath());
            }
        });
        buttonTargetFileLoad.addActionListener(e -> {
            JFileChooser fileChooser = new JFileChooser();

            int returnVal = fileChooser.showOpenDialog(settingFrame);
            if (returnVal == JFileChooser.APPROVE_OPTION) {
                File file = fileChooser.getSelectedFile();
                parent.setTargetFileLocation(file.getAbsolutePath());
                textFieldTargetFileLocation.setText(file.getAbsolutePath());
            }
        });
        buttonTransRuleLoad.addActionListener(e -> {
            JFileChooser fileChooser = new JFileChooser();

            int returnVal = fileChooser.showOpenDialog(settingFrame);
            if (returnVal == JFileChooser.APPROVE_OPTION) {
                File file = fileChooser.getSelectedFile();
                parent.setTransRuleLocation(file.getAbsolutePath());
                textFieldTransformRuleLocation.setText(file.getAbsolutePath());
            }
        });
        buttonSaveInfo.addActionListener(e -> {
            parent.refresh();
            settingFrame.dispose();
        });
    }
}
