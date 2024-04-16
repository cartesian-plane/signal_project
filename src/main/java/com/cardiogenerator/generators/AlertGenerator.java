package com.cardiogenerator.generators; // package name contained underscores. It cannot contain
// underscores (5.2.1).

// The imports were separated by a space, which is wrong. All imports (non-static) 
// must be placed in a single block with no spaces in-between (3.3.3).
// Imports' names are not in ASCII sort order. All imports' names within a block
// must be in ASCII sort order (3.3.3).
import com.cardiogenerator.outputs.OutputStrategy;
import java.util.Random;

public class AlertGenerator implements PatientDataGenerator {

    public static final Random randomGenerator = new Random();

    // AlertStates was renamed to alertStates because local variable 
    // names should be written in lowerCamelCase (5.2.7)
    private boolean[] alertStates; // false = resolved, true = pressed

    public AlertGenerator(int patientCount) {
        alertStates = new boolean[patientCount + 1];
    }

    @Override
    public void generate(int patientId, OutputStrategy outputStrategy) {
        try {
            if (alertStates[patientId]) {
                if (randomGenerator.nextDouble() < 0.9) { // 90% chance to resolve
                    alertStates[patientId] = false;
                    // Output the alert
                    outputStrategy.output(patientId, System.currentTimeMillis(),
                     "Alert", "resolved");
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
                     "Alert", "triggered");
                }
            }
        } catch (Exception e) {
            System.err.println("An error occurred while generating alert data for patient "
             + patientId);
            e.printStackTrace();
        }
    }
}
