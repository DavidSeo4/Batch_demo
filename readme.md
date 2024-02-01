# Spring Batch CSV to Database Project.

The **main branch** of this project shows the use of Spring Batch to 
process and transfer 10000 records from a CSV file to a database, 
while the **RESTtoSQL branch** shows how to access data stored in the database 
using Batch to read an endpoint, reprocess the data, and save it back to 
the database.

The application includes the Batch configuration class with 2 different Job Beans and 2 different Step Beans in it for each process, 
the DAO/Repository class and 2 custom processors to modify the 
records before persisting the data in the database.

_The CSVReader is within the BatchConfig class while the ApiRest reader has its own class._

### Endpoints

- POST http://localhost:8080/students/import/CSV : imports the data from the CSV file to the DB.
- POST http://localhost:8080/students/import/REST : access the DB through an endpoint, gets all the records, process them and saves them with new information.
- GET http://localhost:8080/students/get_all : it gives back every record saved on the database:

#### David Seoane.