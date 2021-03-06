package alex.shepel.hdl_testbench.frontend;

import alex.shepel.hdl_testbench.frontend.buttonsPanel.ButtonsPanel;
import alex.shepel.hdl_testbench.frontend.configurationPanel.ConfigurationPanel;
import alex.shepel.hdl_testbench.frontend.helper.Helper;
import alex.shepel.hdl_testbench.frontend.progressPanel.ProgressPanel;
import alex.shepel.hdl_testbench.frontend.widgets.PresetButton;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

/*
 * File: Frontend.java
 * -----------------------------------------------
 * Creates and manage a GUI.
 *
 * Uses the standard built-in javax.swing library.
 * Consists of the few pages
 * that allow user to configure properties
 * of the resulting files.
 */
public class Frontend extends JFrame implements FrontendParameters {

    /* Top panel of the app's window.
    Allows user to configure properties of the resulting files. */
    private static ConfigurationPanel confPanel;

    /* Central panel of the app's window.
    Monitors a progress of the configuration. */
    private static ProgressPanel progPanel;

    /* Bottom panel of the app's window.
    Contains navigation buttons. */
    private static ButtonsPanel buttPanel;

    /**
     * The class constructor.
     */
    public Frontend() {
        /* Sets the main properties of the frame. */
        setTitle("Test Environment Generation");
        setLayout(new BoxLayout(this.getContentPane(), BoxLayout.Y_AXIS));
        getContentPane().setBackground(BACKGROUND_COLOR);
        setResizable(false);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        /* Creates panels that will be added to the frame. */
        confPanel = new ConfigurationPanel();
        progPanel = new ProgressPanel();
        buttPanel = new ButtonsPanel();

        /* Adds panels to the frame. */
        add(confPanel);
        add(progPanel);
        add(buttPanel);

        /* Sets frame size according to panels sizes. */
        /* Makes frame visible. */
        pack();
        Dimension monitorSize = Toolkit.getDefaultToolkit().getScreenSize();
        Dimension windowSize = getSize();
        setLocation((monitorSize.width - windowSize.width) / 2, (monitorSize.height - windowSize.height) / 2);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    /**
     * Returns user to the previous configuration step.
     * Switches app's window to the appropriate page.
     */
    public void back() {
        progPanel.previousStep();
        confPanel.setOngoingPage(progPanel.getStep());
        buttPanel.setOngoingButtonStatus(progPanel.getStep());
    }

    /**
     * Directs user to the next configuration step.
     * Switches app's window to the appropriate page.
     */
    public void next() {
        progPanel.nextStep();
        confPanel.setOngoingPage(progPanel.getStep());
        buttPanel.setOngoingButtonStatus(progPanel.getStep());
    }

    /**
     * Opens a manual of using of an application.
     */
    public void help() {
        // TODO: implement opening of a help.txt file.
        //       Also it can be great to use autogenerated
        //       java-doc.
    }

    /**
     * Finishes running of an application.
     */
    public void finish() {
        System.exit(0);
    }

    /**
     * Refreshes the application's window.
     * Method can be used to change window's page,
     * because changing a visibility of the frame's
     * components runs a paintComponent method.
     */
    public void refresh() {
        setVisible(false);
        setVisible(true);
    }

    /**
     * Returns the name of the ongoing configuration page,
     * that is showing on the app's window at the moment.
     *
     * @return The String object that contains name
     *         of the ongoing page.
     */
    public String getPageName() {
        return confPanel.getPageName();
    }

    /**
     * Returns a HashMap that contains all buttons represented
     * on the bottom panel of the app's window.
     *
     * @return The HashMap object.
     *         Value contains a MyButton object.
     *         Key contains a name of this button.
     */
    public static HashMap<String, PresetButton> getButtonsHashMap() {
        return ButtonsPanel.getButtonsHashMap();
    }

    /**
     * Returns a DUT.sv file, that is specified by user.
     *
     * @return The File object of the DUT.sv file.
     */
    public File getDutFile() throws NullPointerException {
        if (confPanel.getDutFile() == null) {
            throw new NullPointerException("Specified DUT file does not exist.");
        }

        return confPanel.getDutFile();
    }

    /**
     * Returns a working folder,
     * where must be placed created
     * test environment.
     *
     * @return The File object of the working folder.
     *         There must be placed test environment.
     */
    public File getWorkingFolder() throws IOException {
        if (!confPanel.getWorkingFolder().exists()) {
            throw new IOException("Specify correct working folder.");
        }

        return confPanel.getWorkingFolder();
    }

    /**
     * Sets the ports list of clk_hub.sv module.
     * Any of that ports can be connected to the DUT
     * and be used as clock signals.
     *
     * @param hubClocks The ArrayList object
     *                  that contains available clocks.
     */
    public void setHubClocks(ArrayList<String> hubClocks) {
        confPanel.setHubClocks(hubClocks);
    }

    /**
     * Sets the list of clock inputs of the DUT.
     * They are received from a parsed DUT.sv file.
     *
     * @param dutClocks The ArrayList object
     *                  that contains list of DUT's clock inputs.
     */
    public void setDutClocks(ArrayList<String> dutClocks) {
        confPanel.setDutClocks(dutClocks);
    }

    /**
     * Returns a HashMap that describes a connection
     * between DUT's and clk_hub's modules.
     * Key is name of DUT's clock input
     * and value is name of clk_hub output.
     * This is specified by user.
     *
     * @return The HashMap object that contains correspondence
     *         between DUT's and clk_hub's modules.
     */
    public HashMap<String, String> getClocksHashMap() {
        return confPanel.getClocksHashMap();
    }

    /**
     * Returns a sampling frequency.
     * Simulation points will be written
     * to the report files every tact
     * of this frequency.
     * It is specified by a user.
     *
     * @return Sampling frequency that is used
     *         in the test environment for writing
     *         output data to the report file.
     */
    public String getReportSamplingFrequency() {
        return confPanel.getReportSamplingFrequency();
    }

    /**
     * Appeals to the Helper object
     * that shows error message on the app's window.
     *
     * @param exception The exception that occurred
     *                  and contains error message.
     */
    public void showExceptionMessage(Exception exception) {
        Helper.showExceptionMessage(exception);
    }
}
