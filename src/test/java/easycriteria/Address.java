package easycriteria;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Size;

@Entity
@Table(name="address_tbl")
public class Address {
    
	@Id
    @GeneratedValue
    private int id;
    
    @Size(min=1, max=20)
    private String address;
	
    public Address() {
		super();
	}

	public Address(String address) {
		super();
		this.address = address;
	}

	public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	@Override
	public String toString() {
		return "Address [id=" + id + ", address=" + address + "]";
	}
}
