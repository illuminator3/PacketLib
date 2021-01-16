# PacketLib (b0.1)

[![GitHub issues](https://img.shields.io/github/issues/illuminator3/PacketLib)](https://github.com/illuminator3/PacketLib/issues) [![GitHub forks](https://img.shields.io/github/forks/illuminator3/PacketLib)](https://github.com/illuminator3/PacketLib/network) [![GitHub stars](https://img.shields.io/github/stars/illuminator3/PacketLib)](https://github.com/illuminator3/PacketLib/stargazers) [![GitHub license](https://img.shields.io/github/license/illuminator3/PacketLib)](https://github.com/illuminator3/PacketLib/blob/master/LICENSE) ![dependencies](https://img.shields.io/badge/dependencies-none-orange) ![Hits](https://hitcounter.pythonanywhere.com/count/tag.svg?url=https%3A%2F%2Fgithub.com%2Filluminator3%2FPacketLib) [![](https://jitpack.io/v/illuminator3/PacketLib.svg)](https://jitpack.io/#illuminator3/PacketLib)

## Building

Building the project is really easy. First you'll have to clone the repository: ``git clone https://github.com/illuminator3/PacketLib`` after that's done building the project can be done by running ``gradle build ``.

## Dependency installation

### Maven

Add the jitpack repository:

```xml
<repository>
    <id>jitpack.io</id>
    <url>https://jitpack.io</url>
</repository>
```

Find the correct commit version (e.g. 7c52229960) (can also be found [here](https://jitpack.io/#illuminator3/PacketLib)). Use it as the dependency version:

```xml
<dependency>
    <groupId>com.github.illuminator3</groupId>
    <artifactId>PacketLib</artifactId>
    <version>[version]</version>
</dependency>
```

---

### Gradle
Register the jitpack repository:

```groovy
maven { url 'https://jitpack.io' }
```

Find the version [here](https://jitpack.io/#illuminator3/PacketLib) and add the dependency:

```groovy
implementation 'com.github.illuminator3:PacketLib:<version>'
```

## Usage

Import the root package:

```java
import me.illuminator3.event.*;
```

You can now add a listener by using the ``PacketLib#addListener`` function (who could've guessed lol):

```java
PacketLib.addListener(myListener);
```

Calling an event is done by invoking the ``PacketLib#call`` method:

```java
PacketLib.call(myEvent);
```

Adding a filter is also really easy, just call the ``PacketLib#addFilter`` method:

``````java
PacketLib.addFilter(myFilter);
``````

Removing a certain listener/filter can be done by invoking either the ``PacketLib#removeListener`` or ``PacketLib#addListener`` method:

```java
PacketLib.removeFilter(myFilter); // Removing a filter
PacketLib.removeListener(myListener); // Remove a listener
```

## Contribution

You can contribute by creating an [issue](https://github.com/illuminator3/PacketLib/issues/new) or [pull request](https://github.com/illuminator3/PacketLib/compare). Please keep the code clean and readable. All contributed code must be in the already present code format.

## License

This project is licensed under the Apache License 2.0 - see the [LICENSE](https://github.com/illuminator3/PacketLib/blob/master/LICENSE) file for details

### Permissions & Limitations

#### Permissions

-  Commercial use
-  Modification
-  Distribution
-  Patent use
-  Private use

#### Limitations

-  Trademark use
-  Liability
-  Warranty