package easycriteria;

import jakarta.persistence.Entity;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.Table;

@Entity
@Table(name = "cat_tbl")
@Inheritance(strategy = InheritanceType.JOINED)
public class Cat extends HomeAnimal {
	
	private String catBreed;

	public String getCatBreed() {
		return catBreed;
	}

	public void setCatBreed(String catBreed) {
		this.catBreed = catBreed;
	}

	@Override
	public String toString() {
		return "Cat [catBreed=" + catBreed + "]";
	}
}
