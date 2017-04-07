package easycriteria;

import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.Table;

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
