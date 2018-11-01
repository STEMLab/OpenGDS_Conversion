package edu.pnu.stem.convert;

import javax.swing.*;
import java.io.File;
import java.util.prefs.Preferences;

public class StaticInfoSettingForm {
    static final String HALE_DIR = "hale_dir";
    static final String GDAL_DIR = "gdal_dir";

    private JTextField textFieldHALELoc;
    private JTextField textFieldGDALLoc;
    private JButton buttonHALELoad;
    private JButton buttonGDALLoad;
    private JButton buttonSaveInfo;
    private JPanel panelStaticSetting;
    private JFrame settingFrame;
    private MainForm parent;
    private Preferences prefs;

    public StaticInfoSettingForm(MainForm mainForm) {
        parent = mainForm;
        prefs = Preferences.userNodeForPackage(StaticInfoSettingForm.class);

        settingFrame = new JFrame("Static Setting");
        settingFrame.setContentPane(panelStaticSetting);
        settingFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        settingFrame.pack();
        settingFrame.setVisible(true);

        buttonHALELoad.addActionListener(e -> {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
            int returnVal = fileChooser.showOpenDialog(settingFrame);
            if (returnVal == JFileChooser.APPROVE_OPTION) {
                File file = fileChooser.getSelectedFile();
                textFieldHALELoc.setText(file.getAbsolutePath());

                prefs.put(HALE_DIR, file.getAbsolutePath());
            }
        });
        buttonGDALLoad.addActionListener(e -> {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
            int returnVal = fileChooser.showOpenDialog(settingFrame);
            if (returnVal == JFileChooser.APPROVE_OPTION) {
                File file = fileChooser.getSelectedFile();
                textFieldGDALLoc.setText(file.getAbsolutePath());

                prefs.put(GDAL_DIR, file.getAbsolutePath());
            }
        });
        buttonSaveInfo.addActionListener(e -> {
            parent.refresh();
            settingFrame.dispose();
        });
    }
}
