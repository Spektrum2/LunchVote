# LunchVote

LunchVote is a REST API for voting on where to have lunch today. The project is developed using **Spring Boot**, **Spring MVC**, **Hibernate** and **Spring Data JPA**, allowing management of restaurants, their daily menus, and user voting. This project does not include a frontend, so the API is intended for integration with a separately developed client application.

## Features

- **User roles:** Two types of users — administrators and regular users.
- **Restaurant management:**  Administrators can create, edit, and delete restaurants.
- **Menu management:** For each restaurant, the administrator sets the menu for the current day (usually 2–5 items, each containing a dish name and price). The menu is updated daily.
- **Voting system:** Regular users can vote for their preferred restaurant for today's lunch. Each user can cast only one vote per day.
- **Vote change:** If a user votes again on the same day, before **11:00**, it is considered that they changed their decision; after 11:00 AM, the vote cannot be changed.
- **Flexibility:** The API is designed for use by frontend developers when creating a client application.

## Technologies Used

- **Spring Boot** – Rapid development and auto-configuration.
- **Spring MVC** – Implementation of RESTful web services.
- **Spring Data JPA / Hibernate** – Database interaction via ORM.
- **Spring Security** – Authentication and authorization.
- **Swagger/OpenAPI 3** – Interactive API documentation.
- **H2** – Data storage.

## Requirements

- **Java 11+**
- **Maven 3.6+** (or another build system)
- DBMS – **H2** is used by default for development