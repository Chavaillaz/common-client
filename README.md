# Common Client

[CompletableFuture]: https://docs.oracle.com/en/java/javase/17/docs/api/java.base/java/util/concurrent/CompletableFuture.html

[Jackson]: https://github.com/FasterXML/jackson

[JavaHttp]: https://openjdk.org/groups/net/httpclient/intro.html

[ApacheHttp]: https://hc.apache.org/httpcomponents-client-5.2.x/

[OkHttp]: https://square.github.io/okhttp/

![Dependency Check](https://github.com/chavaillaz/common-client/actions/workflows/snyk.yml/badge.svg)
[![Maven Central](https://maven-badges.herokuapp.com/maven-central/com.chavaillaz/common-client/badge.svg)](https://maven-badges.herokuapp.com/maven-central/com.chavaillaz/common-client)
[![License](https://img.shields.io/badge/License-Apache%202.0-blue.svg)](https://opensource.org/licenses/Apache-2.0)

This library helps you to easily create other libraries interacting with services using asynchronous requests and
returning [CompletableFuture][CompletableFuture]. The serialization and deserialization of domain objects are performed
using [Jackson][Jackson].

Presently, it supports the following HTTP clients:

- [Java HTTP client][JavaHttp] (included since Java 11)
- [Apache HTTP client][ApacheHttp] 5.2
- [OkHttp client][OkHttp] 4.12

## Installation

The dependency is available in maven central (see badge for version):

```xml

<dependency>
    <groupId>com.chavaillaz</groupId>
    <artifactId>common-client</artifactId>
</dependency>
```

## Usage

- Create the interfaces corresponding to every API you want to interact with (e.g. `UserClient`)
- Create the authentication method for your service extending the `Authentication` class
- Create an implementation of your interfaces and extending the available abstract classes for each HTTP client
- Create a central client extending `AbstractClient` to manage and get the other ones

## Contributing

If you have a feature request or found a bug, you can:

- Write an issue
- Create a pull request

### Code style

The code style is based on the default one from IntelliJ, except for the following cases:

#### Imports

Imports are ordered as follows:

- All static imports in a block
- Java non-static imports in a block
- All non-static imports in a block

A single blank line separates every block. Within each block the imported names appear in alphabetical sort order.
Wildcard imports, static or otherwise, are not used.

## License

This project is under Apache 2.0 License.