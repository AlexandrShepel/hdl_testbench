package alex.shepel.hdl_testbench.backend;

import alex.shepel.hdl_testbench.backend.filesWriter.FilesWriter;
import alex.shepel.hdl_testbench.backend.parser.Parser;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

/*
 * File: Backend.java
 * -----------------------------------------------
 * Backend of an application.
 *
 * Parses a specified DUT.sv.
 * Creates a test environment files based
 * on the information received.
 * Operates with resource package
 * where templates of the test environment files
 * are placed.
 */
public class Backend {

    /* Parses DUT.sv file. File must be specified by a user. */
    private Parser dutParser;

    /* Parses clk_hub.sv file.
    File is specified by default path and stores in the java-archive. */
    private final Parser clkHubParser;

    /* Generates the .sv-classes, clk_hub.sv module
    and tb.sv module that is top level module of the testbench. */
    private final FilesWriter filesWriter;

    /**
     * The class constructor.
     */
    public Backend() throws IOException {
        filesWriter = new FilesWriter();
        clkHubParser = new Parser(BackendParameters.CLK_HUB_SV, Parser.INTERNAL_RESOURCE);
    }

    /**
     * Sets the absolute path of the DUT file.
     * Sends it to the Parser object.
     *
     * @param dutFile The File object.
     */
    public void setDutFile(File dutFile) throws IOException {
        dutParser = new Parser(dutFile, Parser.EXTERNAL_RESOURCE);
    }

    /**
     * Sets the absolute path of the working directory.
     * Test environment will be placed there.
     *
     * @param workingFolder The File object that contains absolute path
     *                      of the working folder.
     */
    public void setWorkingFolder(File workingFolder) {
        filesWriter.setWorkingFolder(workingFolder);
    }

    /**
     * Returns the ports list of clk_hub.sv module.
     * Any of that ports can be connected to the DUT
     * and be used as clock signals.
     *
     * @return The list of available clocks.
     */
    public ArrayList<String> getHubClocks() throws FileNotFoundException {
        /* Checks if clk_hub.sv file exists in the resource directory. */
        if (clkHubParser.getInputClocks().isEmpty() || clkHubParser.getOutputClocks().isEmpty()) {
            throw new FileNotFoundException("Check clk_hub.sv file in the java archive. " +
                    "It does not exist or is corrupted.");
        }

        /* Prepares clocks for further use in the FilesWriter object. */
        ArrayList<String> hubClocks = new ArrayList<>();
        hubClocks.addAll(clkHubParser.getInputClocks());
        hubClocks.addAll(clkHubParser.getOutputClocks());
        System.out.println("Hub clocks are: " + hubClocks);
        filesWriter.setHubClocks(hubClocks);

        return hubClocks;
    }

    /**
     * Returns the list of clock inputs of the DUT.
     * Gets that data from the Parser object.
     *
     * @return The ArrayList object that contains list of DUT's clock inputs.
     */
    public ArrayList<String> getDutClocks() {
        System.out.println("Dut clocks are: " + dutParser.getInputClocks());
        return dutParser.getInputClocks();
    }

    /**
     * Sets a connection between DUT's and clk_hub's modules.
     * Connection is represented as HashMap
     * which key is name of DUT's clock input
     * and value is name of clk_hub output.
     *
     * @param clocksHashMap The HashMap object that contains correspondence
     *                      between DUT's and clk_hub's modules.
     */
    public void setClocksHashMap(HashMap<String, String> clocksHashMap) {
        filesWriter.setClocksHashMap(clocksHashMap);
        System.out.println("Clocks hashmap is: " + clocksHashMap);
    }

    /**
     * Sets a sampling frequency of the created test environment.
     * Simulation points will be written to the report files
     * every tact of this frequency.
     *
     * @param reportSamplingFrequency Sampling frequency that is used
     *                                in the test environment for writing
     *                                output data to the report file.
     */
    public void setReportSamplingFrequency(String reportSamplingFrequency) {
        filesWriter.setReportSamplingFrequency(reportSamplingFrequency);
        System.out.println("reportSamplingFrequency is: " + reportSamplingFrequency);
    }

    /**
     * Creates ".sv-class" files.
     * Those files describe test environment for a DUT.
     */
    public void generateEnvironment() throws IOException {
        filesWriter.setDutName(dutParser.getFile().getName());
        System.out.println("DUT name is: " + dutParser.getFile().getName());

        filesWriter.setParameters(dutParser.getParameters());
        System.out.println("Parameters are: " + dutParser.getParameters().keySet());

        filesWriter.setDutOutputs(dutParser.getOutputPorts());
        System.out.println("Outputs are: " + dutParser.getOutputPorts().keySet());

        filesWriter.setDutInputs(dutParser.getInputPorts());
        System.out.println("Inputs are: " + dutParser.getInputPorts().keySet());

        filesWriter.generate();
    }

}