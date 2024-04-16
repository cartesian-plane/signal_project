package com.cardiogenerator.generators;

import com.cardiogenerator.outputs.OutputStrategy;

/** Specifies the methods that must be implemented by all PatientDataGenerators.
 * If a new data generator is to be added, it must implement this interface.
 */
public interface PatientDataGenerator {
    /** Generate data for a patient, with a given output strategy*/
    void generate(int patientId, OutputStrategy outputStrategy);
}
