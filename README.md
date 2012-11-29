# SASS Resource Handler for JSF

Use SASS with JSF.

## Installation

**Step 1.** Download the code.

**Step 2.** Run `mvn install`.

**Step 3.** Add the following to `pom.xml`:

``` xml
<dependency>
    <groupId>com.bc</groupId>
    <artifactId>sassy-faces</artifactId>
    <version>0.1.0</version>
</dependency>
```

**Step 4.** Add the following to `faces-config.xml`:

``` xml
<application>
    <resource-handler>com.bc.sass.faces.SassResourceHandler</resource-handler>
</application>
```

**Step 5.** Include the SASS scripts with `<h:outputStylesheet />` tag:

``` xml
<h:outputStylesheet library="sass" name="style.scss" />
```

## Limitations

* `@import` does not work.
* EL variables cannot be used, as SASS scripts are compiled and cached.

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

