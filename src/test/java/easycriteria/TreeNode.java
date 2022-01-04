package easycriteria;


import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "tree_node_tbl")
public class TreeNode {

	@Id
	@GeneratedValue
	private int id;	
	private String name;
	
	@OneToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="PARENT_ID")
	private TreeNode parent;

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

	public TreeNode getParent() {
		return parent;
	}

	public void setParent(TreeNode parent) {
		this.parent = parent;
	}

	@Override
	public String toString() {
		return "TreeNode [id=" + id + ", name=" + name + ", parent=" + parent + "]";
	}
}
