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

- smart: Parses any input with three numbers in it separated in any way (eg. <mark>Lorem 12 ipsum 34 dolor 45 sit amet</mark> parses to <mark>12x34x45</mark>)
- sci (Strict Case-Insensitive): Parses any input with a [NUMBER]X[NUMBER]X[NUMBER] format case-insensitively (eg. <mark>12X34X45</mark>)
- scs (Strict Case-Insensitive): Only parses inputs with a [NUMBER]x[NUMBER]x[NUMBER] format case-sensitively (eg. <mark>12x34x45</mark>)

### Room equivalency strategy

Parameter: -e

Default value: flex

Possible values:

- flex: Considers two rooms equivalent if the room dimensions match in any combination (eg. <mark>12x34x45</mark> is equivalent to <mark>45x34x12</mark>, <mark>12x34x45</mark> is equivalent to <mark>34x12x45</mark>)
- base: Considers two rooms equivalent if the room base dimensions (length and width) match in any combination (eg. <mark>12x34x45</mark> is equivalent to <mark>34x12x45</mark>, but NOT equivalent to <mark>45x34x1</mark>)
- strict: Considers two rooms equivalent if all room dimensions match respectively (eg. length matches with length, width matches with width, height matches with height)

