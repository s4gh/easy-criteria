package easycriteria;

import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "homeanimal_tbl")
public class HomeAnimal extends Animal {
	
	private String owner;

	@Embedded
	private PetAddress petAddress;
	
	public String getOwner() {
		return owner;
	}

	public void setOwner(String owner) {
		this.owner = owner;
	}
	
	public PetAddress getPetAddress() {
		return petAddress;
	}

	public void setPetAddress(PetAddress petAddress) {
		this.petAddress = petAddress;
	}

	@Override
	public String toString() {
		return "HomeAnimal [owner=" + owner + ", petAddress=" + petAddress + "]";
	}
}
