# Alert Generation System

The **AlertGenerator** (add link here) integrates information from the data generators and the 
storage system, triggering alerts based on each patient's thresholds.

The following information is used:
* Patient thresholds (retrieved from the Data Storage System, in the form of a DTO)
* Real-time data form the patient monitoring system

Prior to retrieving information from the Data Storage system, the request is passed through the 
Patient Identification system, to match the PatientId with the appropriate  record.

This information is continuously evaluated via the *evaluateData()* method, which will trigger 
an alert if the conditions are met (some data crosses a threshold).

As quick access to a patient's profile is important, they are stored in a HashMap, as shown on
the diagram. 

An Alert object is instantiated with all the relevant information for the type of alert.
For the sake of simplicity, not all the fields are shown in the **Alert** diagram,
and the fields have been left public, as the class is just a DTO.

As the precise mechanism by which the **Alert** is sent
to the **Alert Manager** is not relevant for the diagram, it was not shown.

The **Alert Manager** uses a Strategy pattern for the dispatch of alerts.
The alert is evaluated via the *evaluateAlert()* method, and the appropriate Strategy is set, 
to be used by the *dispatchAlert() method*.
