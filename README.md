# Audition API

The purpose of this Spring Boot application is to test general knowledge of SpringBoot, Java, Gradle etc. It is created
for hiring needs of our company but can be used for other purposes.

## Overarching expectations & Assessment areas

<pre>
This is not a university test. 
This is meant to be used for job applications and MUST showcase your full skillset. 
<b>As such, PRODUCTION-READY code must be written and submitted. </b> 
</pre>

- clean, easy to understand code
- good code structures
- Proper code encapsulation
- unit tests with minimum 80% coverage.
- A Working application to be submitted.
- Observability. Does the application contain Logging, Tracing and Metrics instrumentation?
- Input validation.
- Proper error handling.
- Ability to use and configure rest template. We allow for half-setup object mapper and rest template
- Not all information in the Application is perfect. It is expected that a person would figure these out and correct.

## Getting Started

### Prerequisite tooling

- Any Springboot/Java IDE. Ideally IntelliJIdea.
- Java 17
- Gradle 8

### Prerequisite knowledge

- Java
- SpringBoot
- Gradle
- Junit

### Importing Google Java codestyle into INtelliJ

```
- Go to IntelliJ Settings
- Search for "Code Style"
- Click on the "Settings" icon next to the Scheme dropdown
- Choose "Import -> IntelliJ Idea code style XML
- Pick the file "google_java_code_style.xml" from root directory of the application
__Optional__
- Search for "Actions on Save"
    - Check "Reformat Code" and "Organise Imports"
```

---
**NOTE** -
It is highly recommended that the application be loaded and started up to avoid any issues.

---

## Audition Application information

This section provides information on the application and what the needs to be completed as part of the audition
application.

The audition consists of multiple TODO statements scattered throughout the codebase. The applicants are expected to:

- Complete all the TODO statements.
- Add unit tests where applicants believe it to be necessary.
- Make sure that all code quality check are completed.
- Gradle build completes sucessfully.
- Make sure the application if functional.

## Submission process

Applicants need to do the following to submit their work:

- Clone this repository
- Complete their work and zip up the working application.
- Applicants then need to send the ZIP archive to the email of the recruiting manager. This email be communicated to the
  applicant during the recruitment process.

  
---

## Additional Information based on the implementation

This REST API endpoints to retrieve posts and comments by a user.

### Get Posts by UserId and keyword Request

`GET /posts?userId=:userId&keyword=:keyword`

http://localhost:8080/posts?userId=1&keyword=ut

### Response

[{"userId":1,"id":1,"title":"sunt aut ","body":"quia et "},{"userId":1,"id":2,"title":"qui est esse","body":"ut"}]

---

### Get Post by id Request

`GET /posts/:id`

http://localhost:8080/posts/1

### Response

{"userId":1,"id":1,"title":"sunt aut facere repellat provident occaecati excepturi optio reprehenderit","body":"quia et
suscipit"}

---

### Get Comments by postId Request

`GET /comments?postId=:postId`

http://localhost:8080/comments?postId=1

### Response

[{"postId":1,"id":1,"name":"id labore ex et quam laborum","email":"Eliseo@gardner.biz","body":"laudantium enim quasi est quidem magnam voluptate ipsam eos\ntempora quo necessitatibus"},{"postId":1,"id":2,"name":"quo vero reiciendis velit similique earum","email":"Jayne_Kuhic@sydney.com","body":"est natus enim nihil est dolore omnis voluptatem numquam","email":"Nikita@garfield.biz","body":"quia molestiae reprehenderit quasi aspernatur"},{"postId":1,"id":4,"name":"alias odio sit","email":"Lew@alysha.tv","body":"non et atque\noccaecati deserunt "},{"postId":1,"id":5,"name":"vero eaque aliquid doloribus et culpa","email":"Hayden@althea.biz","body":"harum non quasi et ratione"}]

