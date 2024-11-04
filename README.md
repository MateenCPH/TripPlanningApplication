# Documentation
NOTE: Before project run: Create a file calles 'config.properties' in the ressources folder
with the following content:
```
SECRET_KEY=4c9f92b04b1e85fa56e7b7b0a34f2de4f5b08cd9bb4dfe8ac4d73b4f7f6ef37b
ISSUER=Mateen Rafiq
TOKEN_EXPIRE_TIME=1800000
DB_NAME=tripplanner
```

## Task 3.3.3 routes

### get all trips:
```
HTTP/1.1 200 OK
Date: Mon, 04 Nov 2024 09:43:25 GMT
Content-Type: application/json
Content-Encoding: gzip
Content-Length: 498

[
{
"id": 1,
"name": "Beach Paradise",
"price": 499.99,
"category": "beach",
"startTime": "2023-06-01 09:00",
"endTime": "2023-06-10 18:00",
"longitude": 123456,
"latitude": 654321,
"guide": {
"id": 1,
"firstName": "John",
"lastName": "Doe",
"email": "john.doe@example.com",
"phoneNumber": "1234567890",
"yearsOfExperience": 5
}
},
{
"id": 2,
"name": "City Adventure",
"price": 299.99,
"category": "city",
"startTime": "2023-07-15 08:00",
"endTime": "2023-07-20 17:00",
"longitude": 123457,
"latitude": 654322,
"guide": {
"id": 1,
"firstName": "John",
"lastName": "Doe",
"email": "john.doe@example.com",
"phoneNumber": "1234567890",
"yearsOfExperience": 5
}
},
{
"id": 3,
"name": "Forest Escape",
"price": 399.99,
"category": "forest",
"startTime": "2023-08-05 07:00",
"endTime": "2023-08-15 16:00",
"longitude": 123458,
"latitude": 654323,
"guide": {
"id": 2,
"firstName": "Jane",
"lastName": "Smith",
"email": "jane.smith@example.com",
"phoneNumber": "0987654321",
"yearsOfExperience": 10
}
},
{
"id": 4,
"name": "Lake Retreat",
"price": 199.99,
"category": "lake",
"startTime": "2023-09-10 06:00",
"endTime": "2023-09-20 15:00",
"longitude": 123459,
"latitude": 654324,
"guide": {
"id": 2,
"firstName": "Jane",
"lastName": "Smith",
"email": "jane.smith@example.com",
"phoneNumber": "0987654321",
"yearsOfExperience": 10
}
},
{
"id": 5,
"name": "Sea Expedition",
"price": 599.99,
"category": "sea",
"startTime": "2023-10-01 05:00",
"endTime": "2023-10-10 14:00",
"longitude": 123460,
"latitude": 654325,
"guide": {
"id": 3,
"firstName": "Emily",
"lastName": "Brown",
"email": "emily.brown@example.com",
"phoneNumber": "1112223333",
"yearsOfExperience": 3
}
},
{
"id": 6,
"name": "Snowy Peaks",
"price": 699.99,
"category": "snow",
"startTime": "2023-11-15 04:00",
"endTime": "2023-11-25 13:00",
"longitude": 123461,
"latitude": 654326,
"guide": {
"id": 3,
"firstName": "Emily",
"lastName": "Brown",
"email": "emily.brown@example.com",
"phoneNumber": "1112223333",
"yearsOfExperience": 3
}
}
]
Response file saved.
> 2024-11-04T104325.200.json
```

### get trip by id:
```
GET http://localhost:7007/api/trips/1

HTTP/1.1 200 OK
Date: Mon, 04 Nov 2024 09:43:59 GMT
Content-Type: application/json
Content-Length: 296

{
"id": 1,
"name": "Beach Paradise",
"price": 499.99,
"category": "beach",
"startTime": "2023-06-01 09:00",
"endTime": "2023-06-10 18:00",
"longitude": 123456,
"latitude": 654321,
"guide": {
"id": 1,
"firstName": "John",
"lastName": "Doe",
"email": "john.doe@example.com",
"phoneNumber": "1234567890",
"yearsOfExperience": 5
}
}
Response file saved.
> 2024-11-04T104359.200.json

### post a trip
POST http://localhost:7007/api/trips

HTTP/1.1 201 Created
Date: Mon, 04 Nov 2024 09:45:02 GMT
Content-Type: application/json
Content-Length: 140

{
"id": 7,
"name": "CreatedTrip",
"price": 100.0,
"category": "beach",
"startTime": "13:50",
"endTime": "15:50",
"longitude": 0,
"latitude": 0,
"guide": null
}
Response file saved.
> 2024-11-04T104502.201.json
```

### update a trip
```
PUT http://localhost:7007/api/trips/7

HTTP/1.1 200 OK
Date: Mon, 04 Nov 2024 09:45:37 GMT
Content-Type: application/json
Content-Length: 140

{
"id": 7,
"name": "updatedTrip",
"price": 100.0,
"category": "beach",
"startTime": "13:50",
"endTime": "15:50",
"longitude": 0,
"latitude": 0,
"guide": null
}
Response file saved.
> 2024-11-04T104537.200.json
```

### delete a trip
```
DELETE http://localhost:7007/api/trips/1

HTTP/1.1 204 No Content
Date: Mon, 04 Nov 2024 09:46:08 GMT
Content-Type: text/plain

<Response body is empty>

Response code: 204 (No Content); Time: 42ms (42 ms); Content length: 0 bytes (0 B)

### add an existing guide to an existing trip
PUT http://localhost:7007/api/trips/7/guides/1

HTTP/1.1 200 OK
Date: Mon, 04 Nov 2024 09:47:42 GMT
Content-Type: application/json
Content-Length: 41

Guide with id: 1 added to Trip with id: 7
Response file saved.
> 2024-11-04T104742.200.json
```

### populate the database
```
POST http://localhost:7007/api/trips/populate

HTTP/1.1 200 OK
Date: Mon, 04 Nov 2024 09:48:27 GMT
Content-Type: application/json
Content-Length: 18

Database populated
Response file saved.
> 2024-11-04T104827.200.json
```



## Task 3.3.5
It is because we are modifying an existing Trip ressource by updating it
and adding an existing guide to it. We are not creating a new entity as we do in POST-methods.