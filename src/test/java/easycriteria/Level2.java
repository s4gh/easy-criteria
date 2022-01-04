package easycriteria;


import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "level2_tbl")
public class Level2 {

	@Id
	@GeneratedValue
	private int id;	
	private String name;
	
	@OneToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="PARENT_ID")
	private Level1 parent;

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

	public Level1 getParent() {
		return parent;
	}

	public void setParent(Level1 parent) {
		this.parent = parent;
	}

	@Override
	public String toString() {
		return "TreeNode [id=" + id + ", name=" + name + ", parent=" + parent + "]";
	}
}
