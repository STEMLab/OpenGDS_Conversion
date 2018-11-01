package edu.pnu.stem.convert;

import javax.swing.*;
import java.io.File;
import java.util.prefs.Preferences;

public class StaticInfoSettingForm {
    private final String JAR_DIR = "jar_dir";
    private final String HALE_DIR = "hale_dir";
    private final String GDAL_DIR = "gdal_dir";

    private JTextField textFieldJARLoc;
    private JTextField textFieldHALELoc;
    private JTextField textFieldGDALLoc;
    private JButton buttonJARLoad;
    private JButton buttonHALELoad;
    private JButton buttonGDALLoad;
    private JButton buttonSaveInfo;
    private JPanel panelStaticSetting;
    private JFrame settingFrame;
    private MainForm parent;
    private Preferences prefs;

    public StaticInfoSettingForm(MainForm mainForm) {
        String jarDir = "";
        String haleDir = "";
        String gdalDir = "";

        parent = mainForm;
        prefs = Preferences.userNodeForPackage(StaticInfoSettingForm.class);

        settingFrame = new JFrame("Static Setting");
        settingFrame.setContentPane(panelStaticSetting);
        settingFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        settingFrame.pack();
        settingFrame.setVisible(true);

        buttonJARLoad.addActionListener(e -> {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setCurrentDirectory(new File("C:\\Transform"));
            int returnVal = fileChooser.showOpenDialog(settingFrame);
            if (returnVal == JFileChooser.APPROVE_OPTION) {
                File file = fileChooser.getSelectedFile();
                //parent.setSourceFileLocation(file.getAbsolutePath());
                textFieldJARLoc.setText(file.getAbsolutePath());

                prefs.put(JAR_DIR, file.getAbsolutePath());
            }
        });
        buttonHALELoad.addActionListener(e -> {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setCurrentDirectory(new File("C:\\Transform"));
            int returnVal = fileChooser.showOpenDialog(settingFrame);
            if (returnVal == JFileChooser.APPROVE_OPTION) {
                File file = fileChooser.getSelectedFile();
                //parent.setSourceFileLocation(file.getAbsolutePath());
                textFieldHALELoc.setText(file.getAbsolutePath());

                prefs.put(HALE_DIR, file.getAbsolutePath());
            }
        });
        buttonGDALLoad.addActionListener(e -> {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setCurrentDirectory(new File("C:\\Transform"));
            int returnVal = fileChooser.showOpenDialog(settingFrame);
            if (returnVal == JFileChooser.APPROVE_OPTION) {
                File file = fileChooser.getSelectedFile();
                //parent.setSourceFileLocation(file.getAbsolutePath());
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
