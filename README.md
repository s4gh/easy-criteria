# Overview
This is convenience layer on top of JPA Criteria API which allows to write following queries:
```java
List<Employee> employees = query.select(Employee.class)
				.where(Employee_.fullName).like("John%")
				.orderBy(Employee_.age).asc()				
				.getResultList();
```
Design goals used during creation of this library:
* Reuse all available JPA functionality and infrastructure
* Be simple and minimalistic so that any developer was able to change library code just after few hours of code investigation. Reuse of JPA metamodel imposes serious limitations on the possible syntax provided by this library compared to Querydsl and jOOQ. At the same time this makes this libabry much more simpler since there is no need to generate own metamodel.
* Provide simpler, more convenient and shorter syntax compared to JPA Criteria API 

# Examples
## Simple query
```java
List<Employee> employees = query.select(Employee.class)
				.where(Employee_.fullName).like("John%")
				.orderBy(Employee_.age).asc()				
				.getResultList();
```
## Complex WHERE clause
To reproduce SQL query
```sql
select * from employee 
        where 
        fullName = 'Something' 
		 or (fullName = 'Other 1' and age = 5)
		 or (fullName = 'Name 1' and age = 1)'
```
you need to use following Java code
```java
List<Employee> employees = query.select(Employee.class)
				.whereOr()
					.where(Employee_.fullName).eq("Something")
					.whereAnd()					
						.where(Employee_.fullName).eq("Other 1")
						.where(Employee_.age).eq(5)
					.endAnd()
					.whereAnd()
						.where(Employee_.fullName).eq("Name1")
						.where(Employee_.age).eq(1)
					.endAnd()
				.endOr()
				.getResultList();
```
In this example if any individual "where" statements are put inside "whereOr"
```java
.whereOr()
    .where(property1).eq(value1)
    .where(property2).like(value2)
    .where(property3).eq(value3)
.endOr()
```
library takes each indidual "where" statement and combines all of them using "or" operator. So example above translates into
```sql
(property1 = value1) or (property2 like value2) or (property3 = value3)
```
The same way individual "where" expressions inside of "whereAnd" are combined using logical "and" operator.

# See Also
If you are looking for typesafe ways to execute SQL queries in Java please also check
* [Querydsl](http://www.querydsl.com)
* [jOOQ](https://www.jooq.org/)
* [Torpedo Query](http://torpedoquery.org)
* [Blaze-persistence](https://github.com/Blazebit/blaze-persistence)

## Generics:
Most of the classes use generics with the following meanings:
* E - entity POJO type
* A - POJO property/attribute type. For example, Class A {public String foo;} In this scenario A.foo has type "String"
* S - query return type. It may differ from entity you use in where expression
* B - parent builder which was used to create child builder.

