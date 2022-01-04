package easycriteria;

import java.time.ZonedDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;


@Entity
@Table(name="employee_tbl")
public class Employee {
    
	@Id
    @GeneratedValue 
    private int id;
    
    private String fullName;
    
    private String position;
    
    private int age;
    
    @OneToOne(fetch=FetchType.EAGER)
    @JoinColumn(name="ADDRESS_ID")
    private Address address;
    
//    @Type(type ="org.hibernate.type.ZonedDateTimeType")
    private ZonedDateTime dateTimeProperty;
    
    public Employee() {
		super();
	}

    public Employee(String fullName, String position, int age) {
		super();
		this.fullName = fullName;
		this.position = position;
		this.age = age;
	}
        
//    @ManyToOne(fetch=FetchType.LAZY)
//	@JoinColumn(name = "DEPARTMENT_ID")
//    private Department department;    
    
    public ZonedDateTime getDateTimeProperty() {
		return dateTimeProperty;
	}

	public void setDateTimeProperty(ZonedDateTime dateTimeProperty) {
		this.dateTimeProperty = dateTimeProperty;
	}
       
    public int getId() {
        return id;
    }

	public void setId(int id) {
        this.id = id;
    }

//	public Department getDepartment() {
//		return department;
//	}
//
//	public void setDepartment(Department department) {
//		this.department = department;
//	}

	public String getFullName() {
		return fullName;
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

	public String getPosition() {
		return position;
	}

	public void setPosition(String position) {
		this.position = position;
	}

	public int getAge() {
		return age;
	}

	public void setAge(int age) {
		this.age = age;
	}

	public Address getAddress() {
		return address;
	}

	public void setAddress(Address address) {
		this.address = address;
	}

	@Override
	public String toString() {
		return "Employee [id=" + id + ", fullName=" + fullName + ", position=" + position + ", age=" + age
				+ ", address=" + address + ", dateTime=" + dateTimeProperty +"]";
	}
}
