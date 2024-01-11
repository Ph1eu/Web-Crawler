
# Web-crawler Application

## Description
This web-crawler application is a multi-threaded application that crawls a website concurrently and returns number of crawled sites and total downloaded bytes. It is written in Java and uses Maven as a build tool.This project tries to utilize clean code practices so that it be extended, scaled and maintained easily.

## Libraries Used
* [Jsoup](https://jsoup.org/) - Java HTML Parser
* [Spring boot](https://spring.io/projects/spring-boot) - Spring Boot makes it easy to create stand-alone, production-grade Spring based Applications that you can "just run".
* [ConcurentPackage](https://docs.oracle.com/javase/7/docs/api/java/util/concurrent/package-summary.html) - A package of the Java concurrency API.

## Installation

```bash
mvn install
```

## Usage


```bash
crawl <url> <depth> <number of threads>
```

## Testing

Explain how to run the automated tests for this system.

```bash
mvn test
```

## Contributing

Details about how to contribute to your project.

## License

Include a short description of the license (MIT, Apache, etc.)

