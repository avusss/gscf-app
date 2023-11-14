# GSCF Calculator App

### Prerequisites

Java 17

## Running tests

```bash
./mvnw clean verify
```

## Compiling the application

```bash
./mvnw clean package
```

## Running the application

```bash
java -jar target/gscf-app-jar-with-dependencies.jar
```
or
```bash
java -jar target/gscf-app-jar-with-dependencies.jar -h
```
or, specifying sample input file
```bash
java -jar target/gscf-app-jar-with-dependencies.jar sample-input.txt
```

## Customizing the application

### Line validation strategy

Parameter: -v

Default value: smart

Possible values:

- smart: Parses any input with three numbers in it separated in any way (eg. "Lorem 12 ipsum 34 dolor 45 sit amet" parses to "12x34x45")
- sci (Strict Case-Insensitive): Parses any input with a [NUMBER]X[NUMBER]X[NUMBER] format case-insensitively (eg. "12X34X45")
- scs (Strict Case-Insensitive): Only parses inputs with a [NUMBER]x[NUMBER]x[NUMBER] format case-sensitively (eg. "12x34x45")

### Room equivalency strategy

Parameter: -e

Default value: flex

Possible values:

- flex: Considers two rooms equivalent if the room dimensions match in any combination (eg. "12x34x45" is equivalent to "45x34x12", "12x34x45" is equivalent to "34x12x45")
- base: Considers two rooms equivalent if the room base dimensions (length and width) match in any combination (eg. "12x34x45" is equivalent to "34x12x45", but NOT equivalent to "45x34x1")
- strict: Considers two rooms equivalent if all room dimensions match respectively (eg. length matches with length, width matches with width, height matches with height)

