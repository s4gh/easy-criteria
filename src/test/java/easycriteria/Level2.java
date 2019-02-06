package easycriteria;


import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

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
