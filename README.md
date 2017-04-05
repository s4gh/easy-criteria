# Overview
This is convenience layer on top of JPA Criteria API which allows to write following queries:
```java
QEmployee_ employee = new QEmployee_();
List<Employee> employees = query.select(Employee.class)
				.where(employee.fullName.like("John%"))
				.orderBy(employee.age.asc())				
				.getResultList();
```
Design goals used during creation of this library:
* Reuse all available JPA functionality and infrastructure
* Be simple and minimalistic so that any developer was able to change library code just after few hours of code investigation. Version 1.x used JPA metamodel which imposes serious limitations on the possible query syntax. Version 2.x work with own custom generated metamodel. Main motivation for generating own metamodel is to improve query syntax compared to version 1.x. 
* Provide simpler, more convenient and shorter syntax compared to JPA Criteria API 

# Examples
## Simple query
```java
QEmployee_ employee = new QEmployee_();
List<Employee> employees = query.select(Employee.class)
				.where(employee.fullName.like("John%"))
				.orderBy(employee.age.asc())				
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

QEmployee_ employee = new QEmployee_();
List<Employee> employees = query.select(Employee.class)
				.where(
					employee.fullName.eq("Something")
					.or(
						employee.fullName.eq("Other 1")
						.and(employee.age.eq(5))
					).or(
						employee.fullName.like("Name 1")
						.and(employee.age.eq(1))
					)
				)
				.getResultList();
```

## JOIN support
Object model:
```java
@Entity
public class Project {
	
	private String name;
	...
	@OneToOne
	private HomeAnimal homeAnimal;
}

@Entity
public class HomeAnimal extends Animal {
	
	private String owner;
	...
}
```
We want join "Project" and "HomeAnimal" and filter results where ```homeAnimal.owner ="owner1"```
```java
QLargeProject_ largeProject = new QLargeProject_();
QHomeAnimal_ homeAnimal = new QHomeAnimal_();

List<LargeProject> projects = query.select(LargeProject.class)
				.join(largeProject.homeAnimal, JoinType.INNER, homeAnimal)
					.on(homeAnimal.owner.eq("owner1"))
				.endJoin()
				.getResultList();
```
In this example when we perform join ```join(largeProject.homeAnimal, JoinType.INNER, homeAnimal)``` we specify join type ```JoinType.INNER``` and join table alias ```homeAnimal``` which is later used to filter results.

## Limitations
Currently map joins are not supported. Use JPA API directly to construct queries which require map joins.

# See Also
If you are looking for typesafe ways to execute SQL queries in Java please also check
* [Querydsl](http://www.querydsl.com)
* [jOOQ](https://www.jooq.org/)
* [Torpedo Query](http://torpedoquery.org)
* [Blaze-persistence](https://github.com/Blazebit/blaze-persistence)

