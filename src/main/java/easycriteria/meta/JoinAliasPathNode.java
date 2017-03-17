package easycriteria.meta;

/**
 * 
 * @param <X> The type containing the represented attribute
 * @param <T> The type of the represented attribute
 */
public class JoinAliasPathNode<X, T> implements EntityPathNode{
	
	private String attribute;	
	private EntityPathNode parentPath;
	
	public JoinAliasPathNode(String attribute) {
		this.attribute = attribute;				
	}
	
	public JoinAliasPathNode(String attribute, EntityPathNode parentPath) {
		this.attribute = attribute;
		this.parentPath = parentPath;
	}
		
	public String getAttribute() {
		return attribute;
	}

	@Override
	public EntityPathNode getParent() {
		return parentPath;
	}

	@Override
	public void setParent(EntityPathNode parent) {
		this.parentPath = parent;
	}
}