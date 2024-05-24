# Data Storage System

The **Data Storage System** is responsible for all operations regarding 
stored patient data.

As updating the Patient records database is relatively simple, the diagram centers around the 
data retrieval process, which is (arguably) a bit more involved.

The **DataRetriever** API is made available to the medical staff in the form of a GUI
(details irrelevant for the scope of this explanation).

**DataRetriever** enables support for security checks, as detailed below:
<details>
<summary>View details</summary>

```
1. The user inputs their credentials.
2. The credentials are stored, and used for login.
3. On every requst, the Authenticator checks for credentials compatibility.
4. If the user does not have the authorization for that type of data, the request fails.
5. If the user is authorized, the request is complete and the returned data is made available.
```

</details>

The **DataStorage** communicates with the Patient database, retrieving the requested data via the
*requestData()* method, in the form of a **PatientData** DTO.

In some cases, data may also be added manually by the Users, via the *storeData()* method.

Throughout all this, to prevent data loss/corruption, the data is backed up in 2 separate systems.

## Notes

- **DTO**: Data Transfer Object. (https://en.wikipedia.org/wiki/Data_transfer_object)
 