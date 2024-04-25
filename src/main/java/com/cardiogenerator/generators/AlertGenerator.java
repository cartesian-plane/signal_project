package com.cardiogenerator.generators; // package name contained underscores. It cannot contain
// underscores (5.2.1).

// The imports were separated by a space, which is wrong. All imports 
// must have no spaces in-between (3.3.3).
// Imports' names are not in ASCII sort order. All imports' names within a block
// must be in ASCII sort order (3.3.3).

import com.cardiogenerator.outputs.OutputStrategy;
import java.util.Random;

/**
 * AlertGenerator generates alert data for patients and handles the resolution of alerts based on a
 * probability distribution.
 * <p>
 * Indexing of patients starts from 1.
 */
public class AlertGenerator implements PatientDataGenerator {

  public static final Random randomGenerator = new Random();

  // AlertStates was renamed to alertStates because local variable
  // names should be written in lowerCamelCase (5.2.7)
  private boolean[] alertStates; // false = resolved, true = pressed

  /**
   * Constructs an AlertGenerator object with a specified number of patients.
   *
   * @param patientCount The total number of patients for which alerts are to be generated.
   */
  public AlertGenerator(int patientCount) {
    alertStates = new boolean[patientCount + 1];
  }

  /**
   * Generates alert data for a specific patient and outputs it.
   *
   * @param patientId      the ID of the patient
   * @param outputStrategy the output strategy to use for displaying the alert data
   */
  @Override
  public void generate(int patientId, OutputStrategy outputStrategy) {
    try {
      if (alertStates[patientId]) {
        if (randomGenerator.nextDouble() < 0.9) { // 90% chance to resolve
          alertStates[patientId] = false;
          // Output the alert
          outputStrategy.output(patientId, System.currentTimeMillis(),
              "Alert", "resolved"); // Line wrapped to not exceed 100 characters (4.4)
        }
      } else {
        // Lambda was renamed to lambda because local variable names
        // should be written in lowerCamelCase (5.2.7)
        double lambda = 0.1; // Average rate (alerts per period), adjust
        // based on desired frequency

        double p = -Math.expm1(-lambda); // Probability of at least one
        // alert in the period
        boolean alertTriggered = randomGenerator.nextDouble() < p;

        if (alertTriggered) {
          alertStates[patientId] = true;
          // Output the alert
          outputStrategy.output(patientId, System.currentTimeMillis(),
              "Alert", "triggered"); // Line wrapped to not exceed 100 characters (4.4)
        }
      }
    } catch (Exception e) {
      System.err.println("An error occurred while generating alert data for patient "
          + patientId); // Line wrapped to not exceed 100 characters (4.4)
      e.printStackTrace();
    }
  }
}
