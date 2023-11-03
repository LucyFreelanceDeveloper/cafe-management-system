# cafe-management-system
A full stack web application for managing information about products, categories and orders in cafe restaurant.
Application is build on three microservices: cafe-management-system-db, cafe-management-system-be and cafe-management-system-fe.

The application uses technology:
* **Backend**: Java, Spring Boot, Hibernate, Maven, Lombok, MapStruct 
* **Frontend**: HTML, CSS, NPM, TypeScript, Angular
* **Database**: MySQL

## Services
### cafe-management-system-fe
Service provide frontend implementation of the application, 
it consumes REST API from cafe-management-system-be service.

![Application screens gif](docs/images/application_screens.gif)

### cafe-management-system-be
Service provide backend implementation of application - connection to database, REST API and JWT authentication.

![Swagger api](docs/images/swagger_api_screen.jpg)

### cafe-management-system-db
Service contains cafe database in MySQL database for store all application data.

![Database tables](docs/images/database_tables.jpg)

## How to run
```bash
docker build --tag cafe-management-system-db cafe-management-system-db/.
docker build --tag cafe-management-system-be cafe-management-system-be/.
docker build --tag cafe-management-system-fe cafe-management-system-fe/.

docker compose up
```

### Application endpoints
* **cafe-management-system-db**: jdbc:mysql://localhost:33061/cafe
* **cafe-management-system-be**: http://localhost:8081/swagger-ui/index.html
* **cafe-management-system-fe**: http://localhost:4200/

### Resources:
[**Cafe management system:** BTech Days](https://www.youtube.com/watch?v=d1MfY7MpX4c&list=PLdRq0mbeEBmwdwZF3lWwCcWmD76GfEFVT&ab_channel=BTechDays)