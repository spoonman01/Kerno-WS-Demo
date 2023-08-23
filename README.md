# Kerno WS Homework
### Author: Luca Rospocher

### Tech

* Java 17
* Spring Webflux
* R2DBC + PostgreSQL
* Kafka

### Run

1. Start Kafka + PostgreSQL and the actual backend service using `docker-compose up`

OR

1. Start Kafka + PostgreSQL using `docker-compose -f docker-compose-dependencies.yaml up`
2. Run the service yourself with you favourite IDE or with `./gradlew bootRun`

Then the web socket is reachable at `ws://localhost:8080/chat`

### Notes
Hi there, it was "hinted" to split WS and message logic, I've decided
to use a single backend service in order to have only one service reading and writing
on the same database, avoiding breaking microservices best practices.
Basically the service is both the producer and consumer of the Kafka queue,
 but that still improves availability and being able to handle high-traffic
or delay some operations (database message storage). I'm open to discussions.

The app is built in a reactive / async manner providing functional
web-socket logic, kafka and database management.

### Issues
1. Depending by the dockerised gradle build and the Kafka partition setup, the application
can be kinda slow. I have not spent time on this as any real app will have 
it's own CI/CD setup to run the app
2. WebSockerHandler in a functional style leads to some complexity
3. R2DBC drivers and libraries still have major bugs

