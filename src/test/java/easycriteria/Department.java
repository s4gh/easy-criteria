package easycriteria;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name="department_tbl")
public class Department {
    
	@Id
    @GeneratedValue
    private int id;
    
    private String name;
    
       
    @OneToOne(fetch=FetchType.EAGER)
    @JoinColumn(name="MANAGER_ID")
    private Employee manager;
    
    @OneToMany()
//    @OneToMany(mappedBy="department")
    private List<Employee> employees = new ArrayList<>();
	
    public Employee getManager() {
		return manager;
	}

	public void setManager(Employee manager) {
		this.manager = manager;
	}    

	public Department() {
		super();
	}

	public Department(String name) {
		super();
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

	public List<Employee> getEmployees() {
		return employees;
	}

	public void setEmployees(List<Employee> employees) {
		this.employees = employees;
	}
	
	public void addEmployees(Employee employee) {
		this.employees.add(employee);
	}

	@Override
	public String toString() {
		return "Department [id=" + id + ", name=" + name + ", manager=" + manager + ", employees=" + employees + "]";
	}
}
