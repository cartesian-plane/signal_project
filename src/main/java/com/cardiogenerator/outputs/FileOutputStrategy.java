package com.cardiogenerator.outputs;
// Changed package name by removing underscore
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.concurrent.ConcurrentHashMap;

/** Strategy used by the strategy pattern. */
public class FileOutputStrategy implements OutputStrategy {

    // Changed variable name to lowerCamelCase
    private String baseDirectory;

    // Changed variable name to lowerCamelCase
    public final ConcurrentHashMap<String, String> fileMap = new ConcurrentHashMap<>();

    public FileOutputStrategy(String baseDirectory) {

        this.baseDirectory = baseDirectory;
    }

    /**
     * Writes patient data to a file in the {@code baseDirectory} specified by the user.
     *
     * <p>If a directory cannot be created, it will throw an error.</p>
     *
     * <p>Every type of data will be stored in its own file (i.e. cholesterol.txt).
     * If such a file does not exist, one will be created.</p>
     *
     * <p>The data format is: patientID, timestamp, Label, Data</p>
     *
     * @param patientId the id of the patient
     * @param timestamp the timestamp of the data
     * @param label the type of data (i.e. DiastolicPressure, Cholesterol)
     * @param data the data to be written (could come from any generator)
     */
    @Override
    public void output(int patientId, long timestamp, String label, String data) {
        try {
            // Create the directory
            Files.createDirectories(Paths.get(baseDirectory));
        } catch (IOException e) {
            System.err.println("Error creating base directory: " + e.getMessage());
            return;
        }
        // Set the filePath variable
        // Changed variable name to lowerCamelCase
        // Wrap line
        String filePath = fileMap.computeIfAbsent(label, k -> Paths.get(baseDirectory, label
                + ".txt").toString());

        // Wrap line at 100 characters
        // Write the data to the file
        try (PrintWriter out = new PrintWriter(
                Files.newBufferedWriter(Paths.get(filePath), StandardOpenOption.CREATE,
                        StandardOpenOption.APPEND))) {
            out.printf("Patient ID: %d, Timestamp: %d, Label: %s, Data: %s%n", patientId,
                    timestamp, label, data);
        } catch (Exception e) {
            System.err.println("Error writing to file " + filePath + ": " + e.getMessage());
        }
    }
}