package edu.pnu.stem.convert;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.io.*;
import java.util.Objects;

public class MainForm {
    private JPanel panelMain;
    private JComboBox comboBoxSourceFormat;
    private JComboBox comboBoxTargetFormat;
    private JButton buttonTransform;
    private JTextArea textAreaSourceInfo;
    private JTextArea textAreaTargetInfo;

    private final static String newline = "\n";

    private String sourceFileLocation = null;
    private String targetFileLocation = null;
    private String transRuleLocation = null;
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

        buttonTransform.addActionListener(e -> {
            try {
                String java_jar_Loc = "\"C:\\Program Files\\wetransform\\HALE\\jre\\bin\\java\"";
                String hale_Exec_Loc = "\"C:\\Program Files\\wetransform\\HALE\\plugins\\org.eclipse.equinox.launcher_1.3.100.v20150511-1540.jar\"";
                String transform_Rule_Loc = "\"" + transRuleLocation + "\"";
                String source_Loc = "\"" + sourceFileLocation + "\"";
                String target_Loc = "\"" + targetFileLocation + "\"";
                String preset;
                if(Objects.requireNonNull(comboBoxTargetFormat.getSelectedItem()).toString().equals(GISFormat.SimpleFeatureGML.name())){
                    preset = "CustomGML";
                }
                else {
                    preset = comboBoxTargetFormat.getSelectedItem().toString();
                }

                if(Objects.requireNonNull(comboBoxSourceFormat.getSelectedItem()).toString().equals(GISFormat.IndoorGML.name())
                    || Objects.requireNonNull(comboBoxSourceFormat.getSelectedItem()).toString().equals(GISFormat.CityGML.name())
                    || Objects.requireNonNull(comboBoxTargetFormat.getSelectedItem()).toString().equals(GISFormat.IndoorGML.name())
                    || Objects.requireNonNull(comboBoxTargetFormat.getSelectedItem()).toString().equals(GISFormat.CityGML.name())) {
                    // Conversion by Hale
                    String batch_Command = java_jar_Loc + " -jar " + hale_Exec_Loc + " -application hale.transform -project " + transform_Rule_Loc + " -source " + source_Loc + " -target " + target_Loc + " -preset " + preset;
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
                else {
                    // Conversion by GDAL

                }

/*
                if(comboBoxSourceFormat.getSelectedItem().toString().equals("IndoorGML") && comboBoxTargetFormat.getSelectedItem().toString().equals("CityGML")) {
                    String prev_target_Loc = target_Loc;
                    target_Loc = "workspace\\IndoorGML2GML.gml";
                    preset = "CustomGML";
                    String batch_Command = java_jar_Loc + " -jar " + hale_Exec_Loc + " -application hale.transform -project " + transform_Rule_Loc + " -source " + source_Loc + " -target " + target_Loc + " -preset " + preset;
                    System.out.println(batch_Command);
                    File batchFile = new File("workspace\\transform.bat");
                    FileWriter fw = new FileWriter(batchFile, false);
                    fw.write(batch_Command);
                    fw.flush();
                    fw.close();

                    transform_Rule_Loc = "\"C:\\Transform\\TranRule\\GML2CityGML.halez\"";
                    source_Loc = "workspace\\IndoorGML2GML.gml";
                    target_Loc = prev_target_Loc;
                    preset = "CityGML";
                    batch_Command = java_jar_Loc + " -jar " + hale_Exec_Loc + " -application hale.transform -project " + transform_Rule_Loc + " -source " + source_Loc + " -target " + target_Loc + " -preset " + preset;
                    System.out.println(batch_Command);
                    batchFile = new File("workspace\\transform_next.bat");
                    fw = new FileWriter(batchFile, false);
                    fw.write(batch_Command);
                    fw.flush();
                    fw.close();

                    String[] command = new String[] {"workspace\\transform.bat"};

                    ProcessBuilder builder = new ProcessBuilder(command);
                    //builder.redirectOutput(ProcessBuilder.Redirect.INHERIT);
                    //builder.redirectError(ProcessBuilder.Redirect.INHERIT);
                    Process process = builder.start();
                    BufferedReader reader =  new BufferedReader(new InputStreamReader(process.getInputStream()));
                    StringBuilder sb = new StringBuilder();
                    String line = null;
                    while ( (line = reader.readLine()) != null) {
                        sb.append(line + newline);
                    }

                    command = new String[] {"workspace\\transform_next.bat"};
                    builder = new ProcessBuilder(command);
                    process = builder.start();
                    reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
                    while ( (line = reader.readLine()) != null) {}
                    textAreaTargetInfo.setText(sb.toString());
                }
                else {
*/

//                }

                clear();
            } catch (IOException e1) {
                e1.printStackTrace();
            }

            if(sourceFileLocation != null && targetFileLocation != null) {

            }
        });
    }

    private JMenuBar createMenuBar() {
        JMenuBar jMenuBar = new JMenuBar();

        JMenu fileMenu = new JMenu("File");
        fileMenu.setMnemonic(KeyEvent.VK_F);

        JMenuItem openSourceFile = new JMenuItem("Set File Location");
        openSourceFile.setMnemonic(KeyEvent.VK_S);
        openSourceFile.setToolTipText("Select source and target file location");
        openSourceFile.addActionListener((ActionEvent event) -> {
            DynamicInfoSettingForm settingForm = new DynamicInfoSettingForm(this);
        });

        fileMenu.add(openSourceFile);

        JMenu presetMenu = new JMenu("Preset");
        fileMenu.setMnemonic(KeyEvent.VK_P);

        JMenuItem dbconnect = new JMenuItem("PostgreSQL Connect");
        dbconnect.setMnemonic(KeyEvent.VK_D);
        dbconnect.setToolTipText("Set information for connect postgreSQL");
        dbconnect.addActionListener((ActionEvent event) -> {

        });

        presetMenu.add(dbconnect);

        jMenuBar.add(fileMenu);
        jMenuBar.add(presetMenu);

        return jMenuBar;
    }

    public static void main(String[] args) {
        MainForm mainForm = new MainForm();
    }

    public String getSourceFileLocation() {
        return sourceFileLocation;
    }

    void setSourceFileLocation(String sourceFileLocation) {
        this.sourceFileLocation = sourceFileLocation;
    }

    public String getTargetFileLocation() {
        return targetFileLocation;
    }

    void setTargetFileLocation(String targetFileLocation) {
        this.targetFileLocation = targetFileLocation;
    }

    public String getTransRuleLocation() {
        return transRuleLocation;
    }

    void setTransRuleLocation(String transRuleLocation) {
        this.transRuleLocation = transRuleLocation;
    }

    void refresh() {
        if(sourceFileLocation != null) {
            File file = new File(sourceFileLocation);
            textAreaSourceInfo.append("Source File Path : " + file.getAbsolutePath() + newline);
            textAreaSourceInfo.append("Source File Format : " + Objects.requireNonNull(comboBoxSourceFormat.getSelectedItem()).toString() + newline);
            textAreaSourceInfo.append("Source File Size : " + Long.toString(file.length()) + "Bytes" + newline);
        }

        if(targetFileLocation != null) {
            textAreaSourceInfo.append("Target File Path : " + targetFileLocation + newline);
            textAreaSourceInfo.append("Target File Format : " + Objects.requireNonNull(comboBoxTargetFormat.getSelectedItem()).toString() + newline);
        }

        if(transRuleLocation != null) {
            textAreaSourceInfo.append("Transformation Rule Path : " + transRuleLocation + newline);
        }
        textAreaSourceInfo.append("=================================================" + newline + newline);

        textAreaTargetInfo.setText("");
    }

    void clear() {
        sourceFileLocation = null;
        targetFileLocation = null;
        transRuleLocation = null;
    }
}
