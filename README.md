the API currently handles CRUD operations, and includes filtering options for keywords and minCapacity.

GET    /api/rooms             200 -> Gets all rooms
GET    /api/rooms/{id}        200 / 404 -> Reads a specific ID, and returns 200 when successful and 404 when unable to retrieve.
POST   /api/rooms             201 + Location header -> Creates data and returns a location header.
PUT    /api/rooms/{id}        200 / 404 -> Updates data for the specified ID and returns 404 if no ID is found.
DELETE /api/rooms/{id}        204 / 404 -> Deletes a room with the specified ID and returns 404 if no ID is found.

how to run it:
run helloserverapplication.java, then go to api.http and run each request line by line.
