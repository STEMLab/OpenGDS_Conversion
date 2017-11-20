import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.io.File;

public class mainForm {
    private JPanel panelMain;
    private JComboBox comboBoxSourceFormat;
    private JComboBox comboBoxTargetFormat;
    private JButton buttonTransform;
    private JTextArea textAreaSourceInfo;
    private JTextArea textAreaTargetInfo;
    private final static String newline = "\n";

    public mainForm() {
        comboBoxSourceFormat.addItem("CityGML");
        comboBoxTargetFormat.addItem("IndoorGML");

        File sfile = new File("D:\\Data\\CityGMLSample.gml");
        textAreaSourceInfo.append("File Path : " + sfile.getAbsolutePath() + newline);
        textAreaSourceInfo.append("File Format : CityGML" + newline);
        textAreaSourceInfo.append("File Size : " + Long.toString(sfile.length()) + "Bytes" + newline);
        textAreaTargetInfo.append("File Path : D:\\Data\\IndoorGMLSample.gml" + newline);
        textAreaTargetInfo.append("File Format : IndoorGML" + newline);
    }

    private static JMenuBar createMenuBar() {
        JMenuBar jMenuBar = new JMenuBar();

        JMenu fileMenu = new JMenu("File");
        fileMenu.setMnemonic(KeyEvent.VK_F);

        JMenuItem openSourceFile = new JMenuItem("Open Source File");
        openSourceFile.setMnemonic(KeyEvent.VK_S);
        openSourceFile.setToolTipText("Select source file");
        openSourceFile.addActionListener((ActionEvent event) -> {

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
        JFrame mainFrame = new JFrame("OpenGDS/Conversion");
        mainFrame.setContentPane(new mainForm().panelMain);
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainFrame.pack();
        mainFrame.setVisible(true);
        mainFrame.setJMenuBar(createMenuBar());
    }
}
