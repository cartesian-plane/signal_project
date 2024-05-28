package com.cardiogenerator;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import com.cardiogenerator.generators.AlertGenerator;

import com.cardiogenerator.generators.BloodPressureDataGenerator;
import com.cardiogenerator.generators.BloodSaturationDataGenerator;
import com.cardiogenerator.generators.BloodLevelsDataGenerator;
import com.cardiogenerator.generators.ECGDataGenerator;
import com.cardiogenerator.outputs.ConsoleOutputStrategy;
import com.cardiogenerator.outputs.FileOutputStrategy;
import com.cardiogenerator.outputs.OutputStrategy;
import com.cardiogenerator.outputs.TcpOutputStrategy;
import com.cardiogenerator.outputs.WebSocketOutputStrategy;
import com.data_management.DataStorage;
import com.data_management.Patient;
import com.data_management.PatientRecord;
import com.data_management.Reader;

import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

/**
 * Simulates health data using pseudorandom generation.
 * The user can pass several arguments, as documented in the README.md
 *
 * <p>Patients are indexed from 1 onward, and the list is randomly shuffled,
 * to simulate data diversity.</p>
 *
 * <p>Supports multiple types of output, via the strategy pattern
 * provided by {@link OutputStrategy} </p>
 *
 * <p>If no arguments are specified, it will default to console output.
 * Console output should only be used for debugging purposes.</p>
 */
public class HealthDataSimulator {

    private static int patientCount = 50; // Default number of patients
    private static ScheduledExecutorService scheduler;
    // Default output strategy
    // Fix line wrapping
    private static OutputStrategy outputStrategy = new ConsoleOutputStrategy();
    private static final Random random = new Random();
    private static int PORT;

    public static void main(String[] args) throws IOException {

        parseArguments(args);

        scheduler = Executors.newScheduledThreadPool(patientCount * 4);

        List<Integer> patientIds = initializePatientIds(patientCount);
        // Randomize the order of patient IDs, to simulate data diversity
        Collections.shuffle(patientIds);

        scheduleTasksForPatients(patientIds);
        var dataStorage = new DataStorage();
        var reader = new Reader();
      try {
        reader.readDataFromWebSocket(new URI("ws://localhost:" + PORT), dataStorage);
      } catch (URISyntaxException e) {
        throw new RuntimeException(e);
      }
    }

    /** Parses the user's arguments */
    private static void parseArguments(String[] args) throws IOException {
        for (int i = 0; i < args.length; i++) {
            switch (args[i]) {
                case "-h":
                    printHelp();
                    System.exit(0);
                    break;
                case "--patient-count":
                    if (i + 1 < args.length) {
                        try {
                            patientCount = Integer.parseInt(args[++i]);
                        } catch (NumberFormatException e) {
                            System.err
                                    // Fix line wrapping
                                    .println("Error: Invalid number of patients." +
                                            " Using default value: " + patientCount);
                        }
                    }
                    break;
                case "--output":
                    if (i + 1 < args.length) {
                        String outputArg = args[++i];
                        if (outputArg.equals("console")) {
                          outputStrategy = new ConsoleOutputStrategy();
                        } else if (outputArg.startsWith("file:")) {
                            String baseDirectory = outputArg.substring(5);
                            Path outputPath = Paths.get(baseDirectory);
                            if (!Files.exists(outputPath)) {
                                Files.createDirectories(outputPath);
                            }
                            outputStrategy = new FileOutputStrategy(baseDirectory);
                        } else if (outputArg.startsWith("websocket:")) {
                            try {
                                PORT = Integer.parseInt(outputArg.substring(10));
                                if (0 < PORT && PORT < 65535) {
                                  // Initialize your WebSocket output strategy here
                                  outputStrategy = new WebSocketOutputStrategy(PORT);
                                  System.out.println("WebSocket output will be on port: " + PORT);
                                } else {
                                  System.out.println("Invalid port number!");
                                  System.exit(-1);
                                }

                            } catch (NumberFormatException e) {
                                System.err.println(
                                        "Invalid port for WebSocket output." +
                                                " Please specify a valid port number.");
                            }
                        } else if (outputArg.startsWith("tcp:")) {
                            try {
                                int port = Integer.parseInt(outputArg.substring(4));
                                // Initialize your TCP socket output strategy here
                                outputStrategy = new TcpOutputStrategy(port);
                                System.out.println("TCP socket output will be on port: " + port);
                            } catch (NumberFormatException e) {
                                System.err.println("Invalid port for TCP output." +
                                        " Please specify a valid port number.");
                            }
                        } else {
                            System.err.println("Unknown output type. Using default (console).");
                        }
                    }
                    break;
                default:
                    System.err.println("Unknown option '" + args[i] + "'");
                    printHelp();
                    System.exit(1);
            }
        }
    }

    /** Prints help to the terminal output*/
    private static void printHelp() {
        System.out.println("Usage: java HealthDataSimulator [options]");
        System.out.println("Options:");
        System.out.println("  -h                       Show help and exit.");
        System.out.println(
                "  --patient-count <count>  Specify the number of patients to simulate data for" +
                        " (default: 50).");
        System.out.println("  --output <type>          Define the output method. Options are:");
        System.out.println("                             'console' for console output,");
        System.out.println("                             'file:<directory>' for file output,");
        System.out.println("                             'websocket:<port>' for WebSocket output,");
        System.out.println("                             'tcp:<port>' for TCP socket output.");
        System.out.println("Example:");
        System.out.println("  java HealthDataSimulator --patient-count 100 " +
                "--output websocket:8080");
        System.out.println(
                "  This command simulates data for 100 patients and sends the output to WebSocket" +
                        " clients connected to port 8080.");
    }

    /**
     * Initializes a list based on the patient count.
     * Patients are indexed from 1 onward.
     * The patient count has to be greater than or equal to 1.
     *
     * @param patientCount number of patients
     * @return list of patient Ids
     */
    private static List<Integer> initializePatientIds(int patientCount) {
        List<Integer> patientIds = new ArrayList<>();
        for (int i = 1; i <= patientCount; i++) {
            patientIds.add(i);
        }
        return patientIds;
    }

    /**
     * Schedules tasks that generate patient data at fixed intervals
     *
     * <p>Instantiates all the data generators, and then a task is scheduled to run for each
     * type of data.</p>
     *
     * <p>To simulate real data, some tasks are run more frequently than others
     * (i.e. {@link ECGDataGenerator}: 1/s, {@link AlertGenerator}: 1/20s</p>
     *
     * <p>Generated data is passed to the output strategy set by the user.</p>
     *
     * @param patientIds list of patients to generate data for
     */
    private static void scheduleTasksForPatients(List<Integer> patientIds) {
        ECGDataGenerator ecgDataGenerator = new ECGDataGenerator(patientCount);
        BloodSaturationDataGenerator bloodSaturationDataGenerator =
                new BloodSaturationDataGenerator(patientCount);
        BloodPressureDataGenerator bloodPressureDataGenerator =
                new BloodPressureDataGenerator(patientCount);
        BloodLevelsDataGenerator bloodLevelsDataGenerator =
                new BloodLevelsDataGenerator(patientCount);
        AlertGenerator alertGenerator =
                new AlertGenerator(patientCount);

        for (int patientId : patientIds) {
            scheduleTask(() -> ecgDataGenerator.generate(patientId, outputStrategy),
                    1, TimeUnit.SECONDS);
            scheduleTask(() -> bloodSaturationDataGenerator.generate(patientId, outputStrategy),
                    1, TimeUnit.SECONDS);
            scheduleTask(() -> bloodPressureDataGenerator.generate(patientId, outputStrategy),
                    1, TimeUnit.MINUTES);
            scheduleTask(() -> bloodLevelsDataGenerator.generate(patientId, outputStrategy),
                    2, TimeUnit.MINUTES);
            scheduleTask(() -> alertGenerator.generate(patientId, outputStrategy),
                    20, TimeUnit.SECONDS);
        }
    }

    /** Helper method to schedule a task. */
    private static void scheduleTask(Runnable task, long period, TimeUnit timeUnit) {
        scheduler.scheduleAtFixedRate(task, random.nextInt(5), period, timeUnit);
    }

    private static void triggeredAlert(Patient patient) {
        // Gets the medical records of the patient
        List<PatientRecord> records = patient.getRecords(System.currentTimeMillis() - 60000, System.currentTimeMillis());

        // Check if any of the records indicate an alert
        for (PatientRecord record : records) {
            if (record.recordType().equals("Alert")) {
                // If an alert record is found, handle the triggered alert here
                System.out.println("Triggered Alert for Patient ID: " + patient.getId());
                break;
            }
        }
    }
}
