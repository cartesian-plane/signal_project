# Alert Generator State

The system always starts from the **Resolved** state.

If the conditions are sufficient to trigger an alert, the system goes into the **Generated** state,
with the relevant state variables.

<details>
<summary>State variables</summary>

```
patientId - id of the patient for which the alert was triggered
condition - the condition that triggerred the alert (subject to change, if regenerated)
timestamp - time at which the alert was triggered (subject to change, if regenerated)
```

</details>


While in the **Generated** state, the data is kept updated. If the patient's condition
were to somehow get worse than it already was at the moment of the first alert, the alert
will be regenerated, reflecting the new conditions. This ensures that the staff is always updated
with the patient's latest condition.

Once the Alert moves into the **Sent** state, it will keep notifying the staff on their devices
via the *notify()* method.


Upon interacting with the device and having seen the notification, the staff can *acknowledge*
the situation, moving the alert into the **Acknowledged** state.

Alternatively, while still in the **Sent** state, the alert can resolve itself,
if the triggering condition was not too severe
and the readings stabilise.

The only possible action available to the staff is to *intervene*, until the critical condition
is resolved; thus moving the system back into the **Resolved** state.

The system will remain in this "Idling" **Resolved** state until the next alert. 

## Notes

- Although not shown for the sake of simplicity, it is assumed that the AlertGenerator
is interacting with the DataStorage, fetching historical data.
 
 