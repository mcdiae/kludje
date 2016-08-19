KλudJe
======

KludJe is a Java API that aims to exploit features introduced in Java 8 to reduce verbosity and enhance expressiveness.

Use it to:

 - Implement less verbose equals, hashCode, and toString in your classes
 - Avoid try/catch blocks in lambdas/method references with a generalized exception throwing/catching mechanism
   - Add this capability for any functional interface using the Kludje annotation processor
 - Avoid nested null checks in getter chains with a one-line call

Documentation
=============

See the [documentation](http://mcdiae.github.io/kludje/) for binary downloads and usage information.

Build Environment
=================

Software:

 - [JDK 8](https://jdk8.java.net/)
 - [Gradle](http://www.gradle.org/) (2.1 used)

To build:

```
  gradle build
```

License
=======

[Apache 2.0](https://github.com/mcdiae/kludje/blob/master/LICENSE)

Stability
=========

Limited testing; breaking changes possible in future versions.


Release Notes
=============

version 0.3: breaking API changes in nary package (consumer methods "apply" changed to "accept")
version 0.7: breaking API changes: Meta interfaces deprecated; will be removed in later releases