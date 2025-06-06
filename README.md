# Startup process
### Requirements
* Docker 27.5.1+

### Misc
Modular conference management system built with Spring Boot 3.5.0 and PostgreSQL database. 
Hexagonal architecture was used with sample tests provided. For database migration Liquibase was used. Integration tests use testcontainers.
Deployment is done via Docker Compose.


### Running application
* In the application root run `docker-compose up`
* Application starts latest postgreSQL container accessible on port `5432` next to 2 separate gateway services - one client facing and another for back-office. 
  Currently database acts essentially as an in memory db since volume is not set. See `docker-compose.yml`.
* Back-office gateway is accessible via `http://localhost:8080/api`; Conference gateway `http://localhost:8081/api`.
* To connect to the database use following credentials: 
   ```
  database: conferencedb
  user: postgres
  password: postgres
   ```
### Running tests
* Sample tests were created for each layer: controller, business, persistence (See `ConferenceControllerTest.java`, `CreateConferenceTest.java`, `ConferenceRepositoryAdapterTest.java`)
* In the application root run `./gradlew test`

### Possible improvements
* Validation - no validation object was used, instead internal exceptions were used and translated to the client via 
  RestExceptionHandler in the form of response codes which I'm not happy about in hindsight.
* No indexes were created via liquibase scripts
* I'm not happy about the relation of gateways and conference-platform dependency, configuration is messy and fragile.
  E.g. liquibase is initialized from common child module - currently solved via application.yml - liquibase is enabled only for back office gateway.
* Had time only for a couple of test classes
* There are implementations where things should be optimized depending on the use case - i.e. there are conferences with thousands of registrations taking place frequently. 