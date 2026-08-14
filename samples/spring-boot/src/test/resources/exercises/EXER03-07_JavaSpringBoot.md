# Java / Spring Boot / JPA Exercises 

## Java Streams and Lambdas

Requirement: Use streams and lambdas to process collections of objects within the EventHub application

Criteria:
1. Demonstrates proper usage of stream operations and lambdas (4pt)
2. Realistic / relates to actual EventHub app functionality (3pt)
3. Has corresponding unit test that returns correct result (1pt)
Total: 8pt

References:
[Java 8 Stream Tutorial](https://www.geeksforgeeks.org/java/java-8-stream-tutorial/)
[Java Streams Part 1](https://dzone.com/articles/become-a-master-of-java-streams-part-1-creating-st)


## Spring Boot

Requirement: Structure the application into Controller, Service, Repository, Model, and Dtos, and annotate them with Spring annotations accordingly.

Criteria:
1. Appropriate dependency between controller, service, and repository (3pt)
2. Proper use of Models and Dtos (1pt)
3. Proper use of Spring annotations; the app or test runs and loads dependencies (5pt)
4. The unit test exercises the end-to-end process and returns correct result (1pt)
Total: 10pt

References:
[Spring Boot Tutorial](https://www.geeksforgeeks.org/advance-java/spring-boot/)
[Spring Boot](https://dzone.com/articles/spring-boot-entity-scan)

## JPA

Requirement: Replace mock repositories with JPA, and use the JPA-enabled repositories in the services

Criteria:
1. Proper JPA annotation, inheritance, and configuration (2pt)
2. The unit test exercises the end-to-end process and returns correct result (1pt)
Total: 3pt

References:
[Spring Data JPA](https://www.geeksforgeeks.org/springboot/what-is-spring-data-jpa/)
[Spring Boot with Spring Data JPA](https://dzone.com/articles/spring-boot-with-spring-data-jpa)

## Report query

Requirement: Query the EventHub database for aggregate report data

Criteria:
1. Proper setup of query annotation, projection, entity adjustments (3pt)
2. Test data preparation (1pt)
3. SQL count query correctness and appropriateness for the requirement (3pt)
4. The unit test exercises the end-to-end process and returns correct result (1pt)
5. Demonstration of `sum` function (2pt)
6. Demonstration of `ave` function (2pt)
7. Demonstration of `min` function (1pt)
8. Demonstration of `min` function (1pt)
Total: 14pt

References:
[Spring Data JPA @Query Annotation](https://www.geeksforgeeks.org/java/spring-data-jpa-query-annotation-with-example/)

## Search query

Requirement: Demonstrate string/text comparison search and LIKE queries

Criteria:
1. Functioning LIKE query, either via query annotation or query methods (3pt)
2. Optimization via indexing and other query and entity improvements (1pt)
3. The unit test exercises the end-to-end process and returns correct result (1pt)
Total: 5pt

References:
[JPA Query Methods](https://www.geeksforgeeks.org/advance-java/jpa-introduction-to-query-methods/)


Grand total points: 45