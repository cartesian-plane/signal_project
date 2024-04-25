package com.cardiogenerator.outputs;

/**
 * Strategy used by the strategy pattern.
 */
public class ConsoleOutputStrategy implements OutputStrategy {

  /**
   * Outputs patient data to the console
   */
  @Override
  public void output(int patientId, long timestamp, String label, String data) {
    System.out.printf("Patient ID: %d, Timestamp: %d, Label: %s, Data: %s%n", patientId, timestamp,
        label, data);
  }
}
