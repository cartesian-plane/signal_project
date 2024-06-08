package com.alerts;

// Represents an alert
public class Alert implements IAlert {

  private int patientId;
  private String condition;
  private long timestamp;

  public Alert(int patientId, String condition, long timestamp) {
    this.patientId = patientId;
    this.condition = condition;
    this.timestamp = timestamp;
  }

  public int getPatientId() {
    return patientId;
  }

  public String getCondition() {
    return condition;
  }

  public long getTimestamp() {
    return timestamp;
  }

  // toString method
  @Override
  public String toString() {
    return "Patient ID: " + patientId + ", Condition: " + condition + ", Timestamp: " + timestamp;
  }

  @Override
  public boolean equals(Object alert) {
    if (alert instanceof Alert) {
      Alert other = (Alert) alert;
      return (this.patientId == other.patientId) && this.condition.equals(other.condition) && this.timestamp == other.timestamp;
    } else {
      return false;
    }
  }
}
