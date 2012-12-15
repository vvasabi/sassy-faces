# Sassy Faces: A Resource Handler for JSF that Supports SASS

## Features

* Use SASS in a JSF project with simple installation
* Use JRuby or native Ruby (POSIX OSes only for now)
* Expression Language is supported (see below for its limitation)
* Compass is also supported (requires
  [Compass gem-in-a-jar](https://github.com/vvasabi/compass-gem-in-a-jar)
  in JRuby mode)

## Installation

**Step 1.** Download the code.

**Step 2.** Run `mvn install -DskipTests`.

**Step 3.** Add the following to `pom.xml`:

``` xml
<dependency>
    <groupId>com.bc</groupId>
    <artifactId>sassy-faces</artifactId>
    <version>0.3.1-SNAPSHOT</version>
</dependency>
```

**Step 4.** Include SASS scripts with `<h:outputStylesheet />` tag, for example:

``` xml
<h:outputStylesheet library="sass" name="style.scss" />
```

**Step 5. (Optional)** Install native SASS gem. (Linux and OS X only for now.)

Sassy Faces can optionally use SASS gem via native Ruby. This can dramatically
improve performance. Currently only POSIX operating systems are supported, and
this feature does not yet support Windows.

## Limitations

* ~~`@import` does not work.~~ Support has been added for importing files with
  relative paths. Partials are also supported, too.
* ~~Importing scripts of different syntaxes currently is not supported. For
  example, importing `scss` from `sass` or vise versa is not supported.~~ Cross
  importing between different scripts is now supported.
* ~~EL variables cannot be used.~~ Support for EL variables, such as
  `#{bean.var}`, has been added. Please note that, as SASS scripts are compiled
  and cached, EL evaluation results remain static once cached. Also, SASS
  variables with the syntax `#{$var}` are not evaluated as EL variables.

## License

```
  Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
```

