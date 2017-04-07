package easycriteria;

import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.Table;

@Entity
@Table(name = "dog_tbl")
@Inheritance(strategy = InheritanceType.JOINED)
public class Dog extends HomeAnimal {
	
	private String dogBreed;
	private int age;
	
	public String getDogBreed() {
		return dogBreed;
	}
	public void setDogBreed(String dogBreed) {
		this.dogBreed = dogBreed;
	}
	public int getAge() {
		return age;
	}
	public void setAge(int age) {
		this.age = age;
	}
	@Override
	public String toString() {
		return "Dog [dogBreed=" + dogBreed + ", age=" + age + ", getPetAddress()=" + getPetAddress() + "]";
	}
}
