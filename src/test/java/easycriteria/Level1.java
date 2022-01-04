package easycriteria;


import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "level1_tbl")
public class Level1 {

	@Id
	@GeneratedValue
	private int id;	
	private String name;
	
	@OneToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="LEVEL2_ID")
	private Level2 level2;

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

	public Level2 getLevel2() {
		return level2;
	}

	public void setLevel2(Level2 level2) {
		this.level2 = level2;
	}

	@Override
	public String toString() {
		return "TreeNode [id=" + id + ", name=" + name + ", level2=" + level2 + "]";
	}
}
