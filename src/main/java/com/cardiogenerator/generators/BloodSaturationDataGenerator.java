package com.cardiogenerator.generators;

import java.util.Random;

import com.cardiogenerator.outputs.OutputStrategy;

/**
 * BloodSaturationDataGenerator generates simulated blood saturation data for patients. It simulates
 * small fluctuations in blood saturation levels and ensures that the saturation stays within a
 * realistic and healthy range.
 */
public class BloodSaturationDataGenerator implements PatientDataGenerator {

  private static final Random random = new Random();

  private int[] lastSaturationValues;

  /**
   * Constructs a BloodSaturationDataGenerator object with a specified number of patients.
   *
   * @param patientCount The total number of patients for which blood saturation data is generated.
   */
  public BloodSaturationDataGenerator(int patientCount) {
    lastSaturationValues = new int[patientCount + 1];

    // Initialize with baseline saturation values for each patient
    for (int i = 1; i <= patientCount; i++) {
      lastSaturationValues[i] =
          95 + random.nextInt(6); // Initializes with a value between 95 and 100
    }
  }

  /**
   * Generates blood saturation data for a specific patient and outputs it using the provided output
   * strategy {@link OutputStrategy}.
   *
   * @param patientId      the ID of the patient for which the blood saturation data is generated
   * @param outputStrategy the output strategy to use for displaying the saturation data
   */
  @Override
  public void generate(int patientId, OutputStrategy outputStrategy) {
    try {
      // Simulate blood saturation values
      int variation = random.nextInt(3) - 1; // -1, 0, or 1 to simulate small fluctuations
      int newSaturationValue = lastSaturationValues[patientId] + variation;

      // Ensure the saturation stays within a realistic and healthy range
      newSaturationValue = Math.min(Math.max(newSaturationValue, 90), 100);
      lastSaturationValues[patientId] = newSaturationValue;
      outputStrategy.output(patientId, System.currentTimeMillis(), "Saturation",
          Double.toString(newSaturationValue) + "%");
    } catch (Exception e) {
      System.err.println(
          "An error occurred while generating blood saturation data for patient " + patientId);
      // This will print the stack trace to help identify where the error occurred.
      e.printStackTrace();
    }
  }
}
