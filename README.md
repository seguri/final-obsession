# final-obsession

![Build](https://github.com/seguri/final-obsession/workflows/Build/badge.svg)
[![Version](https://img.shields.io/jetbrains/plugin/v/com.github.seguri.finalobsession.svg)][official]
[![Downloads](https://img.shields.io/jetbrains/plugin/d/com.github.seguri.finalobsession.svg)][official]

<!-- Plugin description -->
This plugin adds the `final` modifier to each possible local parameter and variable when you save a Java class. Fields are left untouched.

Before:

```java
class Foo {
  int couldBeFinalField = 0;
  
  void bar(int canBeFinalParameter) {
    int canBeFinalVariable = 1;
  }
}

```

After:

```java
class Foo {
  int couldBeFinalField = 0;
  
  void bar(final int canBeFinalParameter) {
           ^^^^^
    final int canBeFinalVariable = 1;
    ^^^^^
  }
}
```

The plugin is meant to be used as quickly as possible. It has no configuration nor UI. It's a dynamic plugin, so you can install and uninstall it without restarting IntelliJ.
<!-- Plugin description end -->

## Installation

You can find this plugin in the [marketplace][official].

## Q&A

### Why?

Why not? Adding `final` wherever possible is a convention in my team. The Save Actions Plugin is archived and didn't work correctly on newer versions of the IDE. IntelliJ's new "Actions on Save" tool requires too much configuration compared to the aforementioned plugin. I wanted something simple that just works. I also wanted to learn how to write a formatting plugin, so here we are.

## Thanks to

- [Save Actions Plugin][dubreuia] for the inspiration.
- [Flaticon.com][flaticon] for the plugin icon.


---
Plugin based on the [IntelliJ Platform Plugin Template][template].

[official]: https://plugins.jetbrains.com/plugin/21687-final-obsession
[dubreuia]: https://github.com/dubreuia/intellij-plugin-save-actions
[template]: https://github.com/JetBrains/intellij-platform-plugin-template
[flaticon]: https://www.flaticon.com/free-icons/mental
