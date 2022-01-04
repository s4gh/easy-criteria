package easycriteria;

import java.util.List;
import java.util.Map;

import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.MapKeyClass;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;

@MappedSuperclass
//@Entity
public class Project {
	
	@Id
    @GeneratedValue
    private int id;
	
	@Column(name="NAME")
	private String name;
	
	private ProjectType type;
	
	@OneToMany
	@JoinColumn(name="OWNER_ID")
	private List<Animal> animals;
	
	@OneToOne
	private Animal theAnimal;
	
	@OneToOne
	private HomeAnimal homeAnimal;
	
	@OneToOne
	private Dog dog;
	
	@OneToMany
	@MapKeyClass(ProjectType.class)
	private Map<ProjectType, Dog> dogsByProject;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public ProjectType getType() {
		return type;
	}

	public void setType(ProjectType type) {
		this.type = type;
	}

	public List<Animal> getAnimals() {
		return animals;
	}

	public void setAnimals(List<Animal> animals) {
		this.animals = animals;
	}

	public Animal getTheAnimal() {
		return theAnimal;
	}

	public void setTheAnimal(Animal theAnimal) {
		this.theAnimal = theAnimal;
	}

	public HomeAnimal getHomeAnimal() {
		return homeAnimal;
	}

	public void setHomeAnimal(HomeAnimal homeAnimal) {
		this.homeAnimal = homeAnimal;
	}

	public Dog getDog() {
		return dog;
	}

	public void setDog(Dog dog) {
		this.dog = dog;
	}

	public Map<ProjectType, Dog> getDogsByProject() {
		return dogsByProject;
	}

	public void setDogsByProject(Map<ProjectType, Dog> dogsByProject) {
		this.dogsByProject = dogsByProject;
	}
}
