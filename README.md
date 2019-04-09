[![Maven Central](https://maven-badges.herokuapp.com/maven-central/com.github.wnameless/advanced-optional/badge.svg)](https://maven-badges.herokuapp.com/maven-central/com.github.wnameless/advanced-optional)

advanced-optional
=============
An advanced version of Java Optional by adding an additional message and some convenient methods

## Purpose
Java Optional is a good approach to prevent null exception. However when a nullable Optional is created and returned, it is hard to determine the reason why a nullable Optional has been made. To provide an additional message to decribe an Optional is quiet useful in some scenarios.

# Maven Repo
```xml
<dependency>
	<groupId>com.github.wnameless</groupId>
	<artifactId>advanced-optional</artifactId>
	<version>1.0.0</version>
</dependency>
```

## Quick Start

Advanced Optional contains all methods inside Java Optional and some more:<br>
<br>
Static factory methods:
```java
AdvOpt<String> valueButMsg = AdvOpt.of("Test"); // Same as AdvOpt.of("Test", null);
AdvOpt<String> valueAndMsg = AdvOpt.of("Test", "Msg");
AdvOpt<String> nullAndMsg = AdvOpt.ofNullable(null, "Msg");
AdvOpt<String> nullButMsg = AdvOpt.ofNullable(null);
AdvOpt<String> nullAndNull = AdvOpt.ofNullable(null, null);
AdvOpt<String> optButMsg = AdvOpt.of(Optional.of("Test"));
AdvOpt<String> optAndMsg = AdvOpt.of(Optional.of("Test"), "Msg");
```

isAbsent():
```java
assertFalse(valueAndMsg.isAbsent());
assertTrue(nullAndMsg.isAbsent());
```

toOptional():
```java
assertEquals(Optional.of("Test"), valueAndMsg.toOptional());
```

hasMessage(), getMessage() & getMessage(Function):
```javascript
assertFalse(valueButMsg.hasMessage());
assertTrue(valueAndMsg.hasMessage());

assertEquals("Msg", valueAndMsg.getMessage());
assertEquals("Msg", nullAndMsg.getMessage());

assertEquals("msg", valueAndMsg.getMessage(String::toLowerCase));
assertEquals("MSG", nullAndMsg.getMessage(String::toUpperCase));
```
