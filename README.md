# Warehouse App

## Requirements
* Java: 23+.
* Maven: 3.9.9+

## How to build:
* From top level source directory, run "mvn install" to build this application. This will generate executable jar file.


## How to run:
* java -jar target/warehouse-app-1.0-SNAPSHOT-jar-with-dependencies.jar

# OOP2 - Assignment references
Below are sample references in the code for assignment requirements.

## Fundamentals:
* lambdas: for example: Consumer, Predicate, Supplier, Functio etc..
* streams
* * terminal operations
* * * min(), max(), count(), findAny(), findFirst(), allMatch(), anyMatch(), noneMatch(), forEach()
* * * collect() - Collectors.toMap(), Collectors.groupingBy() and Collectors.partitioningBy()
* * intermediate operations e.g. filter(), distinct(), limit(), map() and sorted()
* switch expressions and pattern matching
* sealed classes and interfaces
* Date/Time API
* records

## Advanced:
* collections/generics - for example: use of Comparator.comparing() for sorting
* concurrency e.g. using ExecutorService to process a list of Callable’s
* NIO2
* Localisation