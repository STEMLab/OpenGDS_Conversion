package edu.pnu.stem.convert;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.io.*;
import java.util.Objects;
import java.util.prefs.Preferences;

public class MainForm {
    private JPanel panelMain;
    private JComboBox comboBoxSourceFormat;
    private JComboBox comboBoxTargetFormat;
    private JButton buttonTransform;
    private JTextArea textAreaSourceInfo;
    private JTextArea textAreaTargetInfo;

    private final static String newline = "\n";

    private String jarLocation;
    private String haleLocation;
    private String ogr2ogrLocation;
    private String sourceFileLocation;
    private String targetFileLocation;
    private String transRuleLocation;
    private GISFormat[] formatList = {GISFormat.IndoorGML, GISFormat.CityGML, GISFormat.SimpleFeatureGML,GISFormat.KML,GISFormat.GeoJSON};

    private MainForm() {
        JFrame mainFrame = new JFrame("OpenGDS/Conversion");
        mainFrame.setContentPane(panelMain);
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainFrame.setSize(800,400);
        mainFrame.pack();
        mainFrame.setVisible(true);
        mainFrame.setJMenuBar(createMenuBar());

        for (GISFormat aFormatList : formatList) {
            comboBoxSourceFormat.addItem(aFormatList.name());
            comboBoxTargetFormat.addItem(aFormatList.name());
        }
        comboBoxTargetFormat.setSelectedIndex(1);

        Preferences staticPrefs = Preferences.userNodeForPackage(StaticInfoSettingForm.class);
        Preferences dynamicPrefs = Preferences.userNodeForPackage(DynamicInfoSettingForm.class);
        jarLocation         = "\"" + staticPrefs.get(StaticInfoSettingForm.HALE_DIR, null) + "\\jre\\bin\\java\"";
        haleLocation        = "\"" + staticPrefs.get(StaticInfoSettingForm.HALE_DIR, null) + "\\plugins\\org.eclipse.equinox.launcher_1.5.0.v20180512-1130.jar\"";
        ogr2ogrLocation     = "\"" + staticPrefs.get(StaticInfoSettingForm.GDAL_DIR, null) + "\\ogr2ogr.exe\"";

        buttonTransform.addActionListener(e -> {
            try {
                if(Objects.requireNonNull(comboBoxSourceFormat.getSelectedItem()).toString().equals(GISFormat.IndoorGML.name())
                    || Objects.requireNonNull(comboBoxSourceFormat.getSelectedItem()).toString().equals(GISFormat.CityGML.name())
                    || Objects.requireNonNull(comboBoxTargetFormat.getSelectedItem()).toString().equals(GISFormat.IndoorGML.name())
                    || Objects.requireNonNull(comboBoxTargetFormat.getSelectedItem()).toString().equals(GISFormat.CityGML.name())) {
                    // Conversion by Hale
                    if(staticPrefs.get(StaticInfoSettingForm.HALE_DIR, null) == null) {
                        JOptionPane.showMessageDialog(null, "Please set static information", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                    else {
                        transRuleLocation   = "\"" + dynamicPrefs.get(DynamicInfoSettingForm.TRANS_RULE_DIR, null) + "\"";
                        sourceFileLocation  = "\"" + dynamicPrefs.get(DynamicInfoSettingForm.SOURCE_DIR, null) + "\"";
                        targetFileLocation  = "\"" + dynamicPrefs.get(DynamicInfoSettingForm.TARGET_DIR, null) + "\"";

                        if(transRuleLocation == null
                                || sourceFileLocation == null
                                || targetFileLocation == null) {
                            JOptionPane.showMessageDialog(null, "Please set dynamic information", "Error", JOptionPane.ERROR_MESSAGE);
                        }
                        else {
                            refresh();
                            String preset;
                            if(Objects.requireNonNull(comboBoxTargetFormat.getSelectedItem()).toString().equals(GISFormat.SimpleFeatureGML.name())){
                                preset = "CustomGML";
                            }
                            else {
                                preset = comboBoxTargetFormat.getSelectedItem().toString();
                            }
                            String batch_Command = jarLocation + " -jar " + haleLocation + " -application hale.transform -project " + transRuleLocation + " -source " + sourceFileLocation + " -target " + targetFileLocation + " -preset " + preset;
                            System.out.println(batch_Command);

                            File batchFile = new File("workspace\\transform.bat");
                            FileWriter fw = new FileWriter(batchFile, false);
                            fw.write(batch_Command);
                            fw.flush();
                            fw.close();

                            String[] command = new String[] {"workspace\\transform.bat"};
                            ProcessBuilder builder = new ProcessBuilder(command);
                            Process process = builder.start();
                            BufferedReader reader =  new BufferedReader(new InputStreamReader(process.getInputStream()));
                            StringBuilder sb = new StringBuilder();
                            String line;
                            while ( (line = reader.readLine()) != null) {
                                sb.append(line + newline);
                            }
                            textAreaTargetInfo.setText(sb.toString());
                        }
                    }
                }
                else {
                    // Conversion by GDAL
                    if(staticPrefs.get(StaticInfoSettingForm.GDAL_DIR, null) == null) {
                        JOptionPane.showMessageDialog(null, "Please set static information", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                    else {
                        sourceFileLocation  = "\"" + dynamicPrefs.get(DynamicInfoSettingForm.SOURCE_DIR, null) + "\"";
                        targetFileLocation  = "\"" + dynamicPrefs.get(DynamicInfoSettingForm.TARGET_DIR, null) + "\"";

                        if(sourceFileLocation == null  || targetFileLocation == null) {
                            JOptionPane.showMessageDialog(null, "Please set dynamic information", "Error", JOptionPane.ERROR_MESSAGE);
                        }
                        else {
                            refresh();
                            String preset;
                            if (Objects.requireNonNull(comboBoxTargetFormat.getSelectedItem()).toString().equals(GISFormat.SimpleFeatureGML.name())) {
                                preset = "GML";
                            } else {
                                preset = comboBoxTargetFormat.getSelectedItem().toString();
                            }
                            String batch_Command = ogr2ogrLocation + " -f " + preset + " " + targetFileLocation + " " + sourceFileLocation;
                            System.out.println(batch_Command);

                            File batchFile = new File("workspace\\transform.bat");
                            FileWriter fw = new FileWriter(batchFile, false);
                            fw.write(batch_Command);
                            fw.flush();
                            fw.close();

                            String[] command = new String[]{"workspace\\transform.bat"};
                            ProcessBuilder builder = new ProcessBuilder(command);
                            Process process = builder.start();
                            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
                            StringBuilder sb = new StringBuilder();
                            String line;
                            while ((line = reader.readLine()) != null) {
                                sb.append(line + newline);
                            }
                            sb.append("Conversion complete!!" + newline);
                            textAreaTargetInfo.setText(sb.toString());
                        }
                    }
                }
                clear();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        });
    }

    private JMenuBar createMenuBar() {
        JMenuBar jMenuBar = new JMenuBar();

        JMenu fileMenu = new JMenu("File");
        fileMenu.setMnemonic(KeyEvent.VK_F);

        JMenuItem dynamicInfo = new JMenuItem("Set Dynamic Location Information");
        dynamicInfo.setMnemonic(KeyEvent.VK_D);
        dynamicInfo.setToolTipText("Select source and target file location");
        dynamicInfo.addActionListener((ActionEvent event) -> {
            new DynamicInfoSettingForm(this);
        });

        fileMenu.add(dynamicInfo);

        JMenuItem staticInfo = new JMenuItem("Set Static Location Information");
        staticInfo.setMnemonic(KeyEvent.VK_D);
        staticInfo.setToolTipText("Select JAR, HALE, GDAL execute file location");
        staticInfo.addActionListener((ActionEvent event) -> {
            new StaticInfoSettingForm(this);
        });

        fileMenu.add(staticInfo);
        jMenuBar.add(fileMenu);

        /*
        JMenu presetMenu = new JMenu("Preset");
        fileMenu.setMnemonic(KeyEvent.VK_P);

        JMenuItem dbconnect = new JMenuItem("PostgreSQL Connect");
        dbconnect.setMnemonic(KeyEvent.VK_D);
        dbconnect.setToolTipText("Set information for connect postgreSQL");
        dbconnect.addActionListener((ActionEvent event) -> {

        });
        presetMenu.add(dbconnect);
        jMenuBar.add(presetMenu);
        */

        return jMenuBar;
    }

    public static void main(String[] args) {
        MainForm mainForm = new MainForm();
    }

    void refresh() {
        Preferences dynamicPrefs = Preferences.userNodeForPackage(DynamicInfoSettingForm.class);
        if(dynamicPrefs.get(DynamicInfoSettingForm.SOURCE_DIR, null) != null) {
            File file = new File(dynamicPrefs.get(DynamicInfoSettingForm.SOURCE_DIR, null));
            textAreaSourceInfo.append("Source File Path : " + file.getAbsolutePath() + newline);
            textAreaSourceInfo.append("Source File Format : " + Objects.requireNonNull(comboBoxSourceFormat.getSelectedItem()).toString() + newline);
            textAreaSourceInfo.append("Source File Size : " + Long.toString(file.length()) + "Bytes" + newline);
        }
        if(dynamicPrefs.get(DynamicInfoSettingForm.TARGET_DIR, null) != null) {
            textAreaSourceInfo.append("Target File Path : " + dynamicPrefs.get(DynamicInfoSettingForm.TARGET_DIR, null) + newline);
            textAreaSourceInfo.append("Target File Format : " + Objects.requireNonNull(comboBoxTargetFormat.getSelectedItem()).toString() + newline);
        }
        if(dynamicPrefs.get(DynamicInfoSettingForm.TRANS_RULE_DIR, null) != null) {
            textAreaSourceInfo.append("Transformation Rule Path : " + dynamicPrefs.get(DynamicInfoSettingForm.TRANS_RULE_DIR, null) + newline);
        }
        /*
        if(sourceFileLocation != null || targetFileLocation != null || transRuleLocation != null) {
            textAreaSourceInfo.append("=================================================" + newline + newline);
        }
        */

        textAreaTargetInfo.setText("");
    }

    void clear() {
        sourceFileLocation = null;
        targetFileLocation = null;
        transRuleLocation = null;
    }
}
