# Alert Generator State

The **Alert Generator** receives information from the patient monitoring equipment.

During all this, historical patient data is retrieved from the Data Storage System.

If conditions exceeding the thresholds are met, an **Alert** is triggered.

The triggered **Alert** is then sent to the hospital staff.

(The repeat of the **Alert** is not modeled, as the hospital staff will end up acknowledging
the alert at some point anyway).

After the alert is acknowledged, it can either resolve by itself or via the intervention of 
hospital staff (both cases are modeled).


After an alert is resolved, the system revers back to its original state, and the process 
can be repeated.

## Notes

- An **Alert** can only resolve itself if it's not critical. In all other cases,
staff intervention is assumed to be required.