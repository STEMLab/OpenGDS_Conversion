import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.io.*;

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
    private String[] formatList = {"IndoorGML", "CityGML", "SimpleFeature GML", "KML", "GeoJSON"};

    public MainForm() {
        JFrame mainFrame = new JFrame("OpenGDS/Conversion");
        mainFrame.setContentPane(panelMain);
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainFrame.setSize(800,400);
        mainFrame.pack();
        mainFrame.setVisible(true);
        mainFrame.setJMenuBar(createMenuBar());

        for(int i = 0; i < formatList.length; i++) {
            comboBoxSourceFormat.addItem(formatList[i]);
            comboBoxTargetFormat.addItem(formatList[i]);
        }

        buttonTransform.addActionListener(e -> {
            try {
                String java_jar_Loc = "\"D:\\Program Files\\wetransform\\HALE\\jre\\bin\\java\"";
                String hale_Exec_Loc = "\"D:\\Program Files\\wetransform\\HALE\\plugins\\org.eclipse.equinox.launcher_1.3.100.v20150511-1540.jar\"";
                String transform_Rule_Loc = "\"" + transRuleLocation + "\"";
                String source_Loc = "\"" + sourceFileLocation + "\"";
                String target_Loc = "\"" + targetFileLocation + "\"";
                String preset;
                if(comboBoxTargetFormat.getSelectedItem().toString().equals("SimpleFeature GML")){
                    preset = "CustomGML";
                }
                else {
                    preset = comboBoxTargetFormat.getSelectedItem().toString();
                }

                String batch_Command = java_jar_Loc + " -jar " + hale_Exec_Loc + " -application hale.transform -project " + transform_Rule_Loc + " -source " + source_Loc + " -target " + target_Loc + " -preset " + preset;
                System.out.println(batch_Command);
                File batchFile = new File("transform.bat");
                FileWriter fw = new FileWriter(batchFile, false);
                fw.write(batch_Command);
                fw.flush();
                fw.close();

                String[] command = new String[] {"transform.bat"};

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
                textAreaTargetInfo.setText(sb.toString());


                /*
                command = new String[] {"D:\\Program Files\\wetransform\\HALE\\clisample\\transform_GML2CityGML.bat"};
                builder = new ProcessBuilder(command);
                process = builder.start();
                reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
                while ( (line = reader.readLine()) != null) {}
                textAreaTargetInfo.setText(sb.toString());
*/
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
            SettingForm settingForm = new SettingForm(this);
        });

        fileMenu.add(openSourceFile);

        JMenu presetMenu = new JMenu("Preset");
        fileMenu.setMnemonic(KeyEvent.VK_P);

        JMenuItem dbconnect = new JMenuItem("PostgreSQL Connect");
        openSourceFile.setMnemonic(KeyEvent.VK_D);
        openSourceFile.setToolTipText("Set information for connect postgreSQL");
        openSourceFile.addActionListener((ActionEvent event) -> {

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

    public void setSourceFileLocation(String sourceFileLocation) {
        this.sourceFileLocation = sourceFileLocation;
    }

    public String getTargetFileLocation() {
        return targetFileLocation;
    }

    public void setTargetFileLocation(String targetFileLocation) {
        this.targetFileLocation = targetFileLocation;
    }

    public String getTransRuleLocation() {
        return transRuleLocation;
    }

    public void setTransRuleLocation(String transRuleLocation) {
        this.transRuleLocation = transRuleLocation;
    }

    public void refresh() {
        if(sourceFileLocation != null) {
            File file = new File(sourceFileLocation);
            textAreaSourceInfo.append("Source File Path : " + file.getAbsolutePath() + newline);
            textAreaSourceInfo.append("Source File Format : " + comboBoxSourceFormat.getSelectedItem().toString() + newline);
            textAreaSourceInfo.append("Source File Size : " + Long.toString(file.length()) + "Bytes" + newline);
        }

        if(targetFileLocation != null) {
            textAreaSourceInfo.append("Target File Path : " + targetFileLocation + newline);
            textAreaSourceInfo.append("Target File Format : " + comboBoxTargetFormat.getSelectedItem().toString() + newline);
        }

        if(transRuleLocation != null) {
            textAreaSourceInfo.append("Transformation Rule Path : " + transRuleLocation + newline);
        }

        textAreaTargetInfo.setText("");
    }

    public void clear() {
        sourceFileLocation = null;
        targetFileLocation = null;
        transRuleLocation = null;
    }
}
