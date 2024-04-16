package com.cardiogenerator.outputs;

/** Specifies the methods that must be implemented by every OutputStrategy.
 * If a new output strategy is to be added, it must implement this interface.
 * */
public interface OutputStrategy {
    void output(int patientId, long timestamp, String label, String data);
}
