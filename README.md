# Introduction

This is a Kotlin port of the Micronaut Data JDBC guide project, https://guides.micronaut.io/latest/micronaut-data-jdbc-repository-maven-java.html.


# Original MN create-app README content

## Micronaut 2.5.7 Documentation

- [User Guide](https://docs.micronaut.io/2.5.7/guide/index.html)
- [API Reference](https://docs.micronaut.io/2.5.7/api/index.html)
- [Configuration Reference](https://docs.micronaut.io/2.5.7/guide/configurationreference.html)
- [Micronaut Guides](https://guides.micronaut.io/index.html)
---

## Feature http-client documentation

- [Micronaut HTTP Client documentation](https://docs.micronaut.io/latest/guide/index.html#httpClient)



# Additional TODOs/Hints on the Guide, by section
## 4.1
This section looks like it wants you to paste all that XML in the same place. But don't. Paste the dependencies in the dependencies section. Then add the path element listed here to the empty `annotationProcessorPaths` element below so that it looks like this (if you don't include a version, you're gonna have a bad time.):

    <annotationProcessorPaths combine.self="override">
    <path> 
        <groupId>io.micronaut.data</groupId>
        <artifactId>micronaut-data-processor</artifactId>
        <version>${micronaut.version} may not work, I ended up using 2.5.0, check your version in Maven Central</version>
    </path>
    <annotationProcessorPath>

## 4.3
The class has an id field decorated with @io.micronaut.data.annotation.Id, but I got missing @Id exceptions, so based on some web searching I switched out to javax.persistence.Id. This led to another runtime error, which after a web search led to the JPA config in application.yml.

Also, this started out as a "data" class but three of the generated methods, `component1`, `component2`, and `copy` were generated with the final modifier and conflicted with AOP. Nothing I tried could prevent them from having that `final` modifier.

https://github.com/micronaut-projects/micronaut-data/issues/32

https://stackoverflow.com/questions/26203446/spring-hibernate-could-not-obtain-transaction-synchronized-session-for-current (I saw this error *a lot*)

https://stackoverflow.com/questions/58308475/micronaut-data-no-backing-repositoryoperations-configured-for-repository

## 4.4
The `@AutoNumber` annotation was not working for the `Genre.id` field for me. I modified this to make the `id` field explicit in the repo action.

## 4.6
The test is building, but wasn't able to get the tests up and running successfully. The failure seems to be a failure to resolve a base URL for the underlying server, so could be an issue with the @Inject(ed) HttpClient instantiation?