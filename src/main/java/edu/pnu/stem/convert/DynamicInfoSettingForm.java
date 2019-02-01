package edu.pnu.stem.convert;

import javax.swing.*;
import java.io.File;
import java.util.prefs.Preferences;

public class DynamicInfoSettingForm {
    static final  String SOURCE_DIR = "source_dir";
    static final  String TARGET_DIR = "target_dir";
    static final  String TRANS_RULE_DIR = "trans_rule_dir";

    private JTextField textFieldSourceFileLocation;
    private JTextField textFieldTargetFileLocation;
    private JTextField textFieldTransformRuleLocation;
    private JButton buttonSourceFileLoad;
    private JButton buttonTargetFileLoad;
    private JButton buttonTransRuleLoad;
    private JButton buttonSaveInfo;
    private JPanel panelDynamicSetting;
    private JFrame settingFrame;
    private MainForm parent;
    private Preferences prefs;

    DynamicInfoSettingForm(MainForm mainForm) {
        parent = mainForm;
        prefs = Preferences.userNodeForPackage(DynamicInfoSettingForm.class);

        settingFrame = new JFrame("Dynamic Setting");
        settingFrame.setContentPane(panelDynamicSetting);
        settingFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        settingFrame.pack();
        settingFrame.setVisible(true);

        if(prefs.get(SOURCE_DIR, null) != null) {
            textFieldSourceFileLocation.setText(prefs.get(SOURCE_DIR, null));
        }
        if(prefs.get(TARGET_DIR, null) != null) {
            textFieldTargetFileLocation.setText(prefs.get(TARGET_DIR, null));
        }
        if(prefs.get(TRANS_RULE_DIR, null) != null) {
            textFieldTransformRuleLocation.setText(prefs.get(TRANS_RULE_DIR, null));
        }

        buttonSourceFileLoad.addActionListener(e -> {
            JFileChooser fileChooser = new JFileChooser();
            if(prefs.get(SOURCE_DIR, null) != null) {
                File prevFile = new File(prefs.get(SOURCE_DIR, null));
                fileChooser.setCurrentDirectory(prevFile);
            }

            int returnVal = fileChooser.showOpenDialog(settingFrame);
            if (returnVal == JFileChooser.APPROVE_OPTION) {
                File file = fileChooser.getSelectedFile();
                textFieldSourceFileLocation.setText(file.getAbsolutePath());

                prefs.put(SOURCE_DIR, file.getAbsolutePath());
            }
        });
        buttonTargetFileLoad.addActionListener(e -> {
            JFileChooser fileChooser = new JFileChooser();
            if(prefs.get(TARGET_DIR, null) != null) {
                File prevFile = new File(prefs.get(TARGET_DIR, null));
                fileChooser.setCurrentDirectory(prevFile);
            }

            int returnVal = fileChooser.showOpenDialog(settingFrame);
            if (returnVal == JFileChooser.APPROVE_OPTION) {
                File file = fileChooser.getSelectedFile();
                textFieldTargetFileLocation.setText(file.getAbsolutePath());

                prefs.put(TARGET_DIR, file.getAbsolutePath());
            }
        });
        buttonTransRuleLoad.addActionListener(e -> {
            JFileChooser fileChooser = new JFileChooser();
            if(prefs.get(TRANS_RULE_DIR, null) != null) {
                File prevFile = new File(prefs.get(TRANS_RULE_DIR, null));
                fileChooser.setCurrentDirectory(prevFile);
            }

            int returnVal = fileChooser.showOpenDialog(settingFrame);
            if (returnVal == JFileChooser.APPROVE_OPTION) {
                File file = fileChooser.getSelectedFile();
                textFieldTransformRuleLocation.setText(file.getAbsolutePath());

                prefs.put(TRANS_RULE_DIR, textFieldTransformRuleLocation.getText());
            }
        });
        buttonSaveInfo.addActionListener(e -> {
            parent.refresh();
            settingFrame.dispose();
        });
    }
}
