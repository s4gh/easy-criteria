package easycriteria;

import static org.junit.Assert.*;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Arrays;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.Persistence;
import javax.persistence.criteria.JoinType;

import org.junit.Before;
import org.junit.Test;

public class EasyCriteriaQueryTest {

	private EntityManager entityManager;
	private JPAQuery query;

	@Before
	public void setup() {
		EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("easycriteria");

		entityManager = entityManagerFactory.createEntityManager();

		query = new JPAQuery(entityManager);
	}

	@Test
	public void testGetAll() {
		createEmployee("MyName");

		List<Employee> employees = query.select(Employee.class).getResultList();

		assertEquals(1, employees.size());
	}
	
	@Test
	public void testSelectCount() {
		createEmployee("SomeName1");
		createEmployee("SomeName2");
		createEmployee("SomeName3");
		
		QEmployee_ employee = new QEmployee_();
		List<Long> employees = query.count(Employee.class).where(employee.fullName.in(Arrays.asList("SomeName1", "SomeName3"))).getResultList();

		assertEquals(1, employees.size());
		assertEquals(2, employees.get(0).intValue());
	}

	@Test
	public void testIn() {
		createEmployee("SomeName1");
		createEmployee("SomeName2");
		createEmployee("SomeName3");

		QEmployee_ employee = new QEmployee_();
		List<Employee> employees = query.select(Employee.class).where(employee.fullName.in(Arrays.asList("SomeName1", "SomeName3"))).getResultList();

		assertEquals(2, employees.size());
	}

	@Test
	public void testLike() {
		createEmployee("name1");
		createEmployee("name2");
		createEmployee("otherName3");

		QEmployee_ employee = new QEmployee_();
		List<Employee> employees = query.select(Employee.class).where(employee.fullName.like("name%"))
				.getResultList();

		assertEquals(2, employees.size());
	}

	@Test
	public void testSelectAttribute() {
		createEmployee("i1");
		createEmployee("i2");
		createEmployee("i3");

		QEmployee_ employee = new QEmployee_();
		List<String> employees = query.select(employee.fullName).getResultList();

		assertTrue(Arrays.asList("i1", "i2", "i3").containsAll(employees));
	}

	@Test
	public void testSingle() {
		createEmployee("desc1");
		createEmployee("desc2");
		createEmployee("desc3");

		QEmployee_ employee = new QEmployee_();
		Employee employees = query.select(Employee.class).where(employee.fullName.eq("desc3")).getSingleResult();

		assertEquals("desc3", employees.getFullName());

		try {
			query.select(Employee.class).getSingleResult();
			fail();

		} catch (NonUniqueResultException e) {
			System.out.println(e.getClass() + ": " + e.getMessage());
			// it is expected
		}
	}

	@Test
	public void testOrder() {

		Employee employee1 = createEmployee("name", 3);
		Employee employee2 = createEmployee("name", 1);
		Employee employee3 = createEmployee("name2", 2);

		QEmployee_ employee = new QEmployee_();
		List<Employee> employees = query.select(Employee.class).orderBy(employee.age.asc())
				.getResultList();

		assertTrue(Arrays.asList(employee2, employee3, employee1).containsAll(employees));
		assertTrue(employees.equals(Arrays.asList(employee2, employee3, employee1)));
		assertFalse(employees.equals(Arrays.asList(employee1, employee2, employee3)));

		employees = query.select(Employee.class).where(employee.fullName.eq("name")).orderBy(employee.age.asc())
				.getResultList();

		assertTrue(Arrays.asList(employee2, employee1).containsAll(employees));
		assertTrue(employees.equals(Arrays.asList(employee2, employee1)));
		assertFalse(employees.equals(Arrays.asList(employee1, employee2)));
	}
	
	@Test
	public void testComplexOrder() {

		Employee employee1 = createEmployee("ddd", 3);
		Employee employee2 = createEmployee("bbb", 1);
		Employee employee3 = createEmployee("ccc", 2);
		Employee employee4 = createEmployee("aaa", 3);					

		QEmployee_ employee = new QEmployee_();
		List<Employee> employees = query.select(Employee.class)
				.orderBy(employee.age.asc(),employee.fullName.asc()) 
				.getResultList();

		assertTrue(Arrays.asList(employee2, employee3, employee4, employee1).containsAll(employees));
		assertTrue(employees.equals(Arrays.asList(employee2, employee3, employee4, employee1)));
		assertFalse(employees.equals(Arrays.asList(employee2, employee3, employee1, employee4)));
	}

	@Test
	public void testCount() {

		createEmployee("name", 3);
		createEmployee("name", 1);
		createEmployee("name2", 2);
		
		QEmployee_ employee = new QEmployee_();

		assertEquals(Long.valueOf(3), query.count(Employee.class).getSingleResult());

		assertEquals(Long.valueOf(2), query.count(Employee.class).where(employee.fullName.eq("name")).getSingleResult());

		assertFalse(query.count(Employee.class).where(employee.fullName.eq("tedf")).getSingleResult() > 0);

		assertTrue(query.count(Employee.class).getSingleResult() > 0);
	}

	@Test
	public void testNotFound() {
		try {
			QEmployee_ employee = new QEmployee_();
			query.select(Employee.class).where(employee.id.eq(15736)).getSingleResult();
		} catch (NoResultException e) {
			// expected
			return;
		}
		fail("Should have thrown exception");
	}

	@Test
	public void testCompoundOrCondition() {
		/**
		 * select * from employee where model = 'Sergio' or model = 'Name1' or
		 * model like 'Second%'
		 */
		Employee employee1 = createEmployee("Name1");
		Employee employee2 = createEmployee("Name2");
		Employee employee3 = createEmployee("Sergio");
		Employee employee4 = createEmployee("Second 1");
		Employee employee5 = createEmployee("Second 2");

		QEmployee_ employee = new QEmployee_();
		
		List<Employee> employees = query.select(Employee.class)
					.where(employee.fullName.eq("Sergio").or(employee.fullName.eq("Name1")).or(employee.fullName.like("Second%")))
				.getResultList();

		assertEquals(4, employees.size());
		assertTrue(Arrays.asList(employee3, employee1, employee4, employee5).containsAll(employees));
		assertFalse(employees.contains(employee2));
	}

	@Test
	public void testCompoundAndCondition() {
		/**
		 * select * from employee where 
		 * fullName = 'Something' 
		 * or (fullName = 'Other 1' and age = 5)
		 * or (fullName = 'Name 1' and age = 1)'
		 */

		Employee employee1 = createEmployee("Name1", 1);
		Employee employee2 = createEmployee("Name1", 2);
		Employee employee3 = createEmployee("Something", 3);
		Employee employee4 = createEmployee("Other 1", 4);
		Employee employee5 = createEmployee("Other 1", 5);

		QEmployee_ employee = new QEmployee_();
		
		List<Employee> employees = query.select(Employee.class)
				.where(
					employee.fullName.eq("Something").or(
							employee.fullName.eq("Other 1").and(
									employee.age.eq(5)
							)
					).or(
							employee.fullName.eq("Name1")
							.and(
									employee.age.eq(1)
							)
					)					
				)
				.getResultList();

		assertEquals(3, employees.size());
		assertTrue(Arrays.asList(employee3, employee5, employee1).containsAll(employees));
		assertFalse(employees.contains(employee2));
		assertFalse(employees.contains(employee4));
	}
	
	@Test
	public void testBetweenDateCondition() {
		setupDepartementsTestData();
				
		ZonedDateTime from = ZonedDateTime.of(2015, 1, 1, 0, 0, 0, 0, ZoneId.of("UTC+01:00"));
		ZonedDateTime to = ZonedDateTime.of(2018, 1, 1, 0, 0, 0, 0, ZoneId.of("UTC+05:00"));
		
		QEmployee_ employee = new QEmployee_();
		
		List<Employee> emp = query.select(Employee.class)
				.where(employee.dateTimeProperty.between(from, to))
				.getResultList();
		assertEquals(4, emp.size());		
	}

	@Test
	public void testNestedPropertiesCondition() {
		setupDepartementsTestData();
		
		QEmployee_ employee = new QEmployee_();
		QAddress_ address = new QAddress_();
		QDepartment_ department = new QDepartment_();
		
		List<Employee> emp = query.select(Employee.class)
			.where(employee.address.address.eq("address1"))
			.getResultList();
		assertEquals(2, emp.size());
		
		
		List<Employee> empJoin = query.select(Employee.class)
				.join(employee.address, JoinType.INNER, address)
					.on(address.address.eq("address1"))
				.endJoin()				
				.getResultList();
			assertEquals(2, empJoin.size());
		
		List<Employee> emp1 = query.select(Employee.class)
				.where(
					employee.address.address.eq("address1").or(employee.position.eq("position2"))				
				)
				.getResultList();
		assertEquals(3, emp1.size());
		
		
		List<Department> departments = query.select(Department.class)
				.where(department.manager.address.address.eq("address1"))				
				.getResultList();
		assertEquals(1, departments.size());
		assertEquals("dep_prod", departments.get(0).getName());
				
		List<Department> departmentsJoin = query.select(Department.class)
				.join(department.manager, JoinType.INNER)
					.join(employee.address, JoinType.INNER, address)
						.on(address.address.eq("address1"))
					.endJoin()
				.endJoin()				
				.getResultList();
		assertEquals(1, departmentsJoin.size());
		assertEquals("dep_prod", departmentsJoin.get(0).getName());
	}
	
	@Test
	public void testJoins() {
		setupDepartementsTestData();
		
		QEmployee_ employee = new QEmployee_();
		QAddress_ address = new QAddress_();
		QDepartment_ department = new QDepartment_();
		
		List<Department> departments = query.select(Department.class)		
		.join(department.manager, JoinType.LEFT, employee)
			.on(employee.fullName.eq("fullName1"))					
		.endJoin()
		.getResultList();		
		assertEquals("dep_prod", departments.get(0).getName());
		
		
		List<Department> departments1 = query.select(Department.class)		
				.join(department.manager, JoinType.LEFT)
					.join(employee.address, JoinType.INNER, address)
						.on(address.address.eq("address3"))
					.endJoin()
				.endJoin()
				.getResultList();
		assertEquals("dep_sales", departments1.get(0).getName());
		
		List<Department> departments2 = query.select(Department.class)		
				.join(department.employees, JoinType.INNER, employee)
					.on(employee.position.like("position2%"))
				.endJoin()
				.distinct()
				.getResultList();
		assertEquals("dep_sales", departments2.get(0).getName());
		assertEquals("dep_prod", departments2.get(1).getName());
		assertEquals(2, departments2.size());
	}
	
	private Employee createEmployee(String fullName) {		
		return createEmployee(fullName, 0);
	}
	
	private Employee createEmployee(String fullName, int age) {
		beginTx();
		Employee employee = new Employee();
		employee.setFullName(fullName);
		employee.setAge(age);
		entityManager.persist(employee);
		commitTx();
		return employee;
	}

	private void setupDepartementsTestData() {
		beginTx();

		ZonedDateTime timestamp1 = ZonedDateTime.of(2017, 1, 1, 0, 0, 0, 0, ZoneId.of("UTC"));
		ZonedDateTime timestamp2 = ZonedDateTime.of(2017, 1, 1, 0, 0, 0, 0, ZoneId.of("UTC+02:00"));
		ZonedDateTime timestamp3 = ZonedDateTime.of(2017, 1, 1, 0, 0, 0, 0, ZoneId.of("UTC+04:00"));
		ZonedDateTime timestamp4 = ZonedDateTime.of(2017, 1, 1, 0, 0, 0, 0, ZoneId.of("UTC+06:00"));
		
		Employee w1 = new Employee("fullName1", "position1", 30);
		w1.setDateTimeProperty(timestamp1);
		Address a1 = new Address("address1");
		w1.setAddress(a1);
		entityManager.persist(a1);
		entityManager.persist(w1);

		Employee w2 = new Employee("fullName1", "position2", 25);
		w2.setDateTimeProperty(timestamp2);
		Address a2 = new Address("address1");
		w2.setAddress(a2);
		entityManager.persist(a2);
		entityManager.persist(w2);

		Employee w3 = new Employee("fullName3", "position2", 35);
		w3.setDateTimeProperty(timestamp3);
		Address a3 = new Address("address3");
		w3.setAddress(a3);
		entityManager.persist(a3);
		entityManager.persist(w3);

		Employee w4 = new Employee("fullName4", "position3", 37);
		w4.setDateTimeProperty(timestamp4);
		entityManager.persist(w4);

		 Department departmentSales = new Department("dep_sales");
		 departmentSales.setManager(w3);
		 departmentSales.addEmployees(w3);
		 entityManager.persist(departmentSales);
		
		 Department departmentMarketing = new Department("dep_marketing");
		 departmentMarketing.addEmployees(w4);
		 departmentMarketing.setManager(w4);
		 entityManager.persist(departmentMarketing);
		
		 Department departmentProduction = new Department("dep_prod");
		 departmentProduction.setManager(w1);
		 departmentProduction.addEmployees(w1);
		 departmentProduction.addEmployees(w2);
		 entityManager.persist(departmentProduction);

		commitTx();
	}

	private void beginTx() {
		entityManager.getTransaction().begin();
	}

	private void commitTx() {
		entityManager.getTransaction().commit();
	}
}