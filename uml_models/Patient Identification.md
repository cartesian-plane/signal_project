# Patient Identification

The Patient Identification system is designed to match incoming patient data with existing patient records while making sure that all data is accurately attributed to the correct patient. This system consists of three key components: PatientIdentifier, PatientRecord, and IdentityManager.

## PatientIdentifier

 Responsible for matching patient IDs from incoming data with patient records in the database. It provides methods such as matchPatientId(String patientId) to match patient IDs and getPatientRecord(String patientId) to retrieve patient records.

## PatientRecord
 
 This class contains attributes such as patientId, name, dateOfBirth, and medicalHistory for a particular patient. These records can be obtained automatically through an interface with the hospital’s patient record database.

## IdentityManager

 This class oversees the matching process, handling discrepancies or anomalies in data linkage to maintain the integrity of patient records. It interfaces with the Data Storage system to update patient records with incoming data and supports the Alert Generation system by allowing it to fetch patient details based on IDs.

Additional classes:

    AuditLog: Tracks activities within the system, monitoring changes to patient identifiers and records. It provides a detailed audit trail for monitoring and accountability purposes.

    Patient: Represents individual patients and contains personal and administrative details (more information than in PatientRecord). The Patient class facilitates patient-related operations and interactions within the system such as obtaining the record of a patient or their medical history.