package easycriteria;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.Size;

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
