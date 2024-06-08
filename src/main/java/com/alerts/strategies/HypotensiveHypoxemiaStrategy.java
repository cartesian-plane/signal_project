package com.alerts.strategies;

import com.alerts.Alert;
import com.alerts.ThresholdValue;
import com.alerts.alert_factory.HypotensiveHypoxemiaFactory;
import com.data_management.DataStorage;
import com.data_management.Patient;
import com.data_management.PatientRecord;
import java.util.List;

public class HypotensiveHypoxemiaStrategy implements AlertStrategy {
  private static final HypotensiveHypoxemiaFactory FACTORY = new HypotensiveHypoxemiaFactory();
  @Override
  public Alert checkAlert(Patient patient) {
    var dataStorage = DataStorage.getInstance();

    List<PatientRecord> systolicPressureRecords = dataStorage.getRecords(patient.getId(),
            System.currentTimeMillis() - (10 * 60 * 1000),
            System.currentTimeMillis() + 1000)
        .stream()
        .filter(record -> record.recordType().equalsIgnoreCase("SystolicPressure"))
        .toList();

    List<PatientRecord> saturationRecords = dataStorage.getRecords(patient.getId(),
            System.currentTimeMillis() - (10 * 60 * 1000),
            System.currentTimeMillis() + 1000)
        .stream()
        .filter(record -> record.recordType().equals("Saturation"))
        .toList();

    boolean lowSystolicPressure = false;
    boolean lowSpO2 = false;

    System.out.println(systolicPressureRecords.size());
    for (PatientRecord pressureRecord : systolicPressureRecords) {
      if (pressureRecord.measurementValue() < ThresholdValue.MIN_SYSTOLIC_PRESSURE.getValue()) {
        lowSystolicPressure = true;
      }

    }

    for (PatientRecord oxygenRecord : saturationRecords) {
      if (oxygenRecord.measurementValue() < ThresholdValue.MIN_SATURATION.getValue()) {
        lowSpO2 = true;
      }

      if (lowSpO2 && lowSystolicPressure) {
        return FACTORY.createAlert(patient.getId(), "CRITICAL: "
            + "HYPOTENSIVE HYPOXEMIA", oxygenRecord.timestamp());
      }

    }

    return null;
  }
}
