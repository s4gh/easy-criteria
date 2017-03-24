package easycriteria.meta;

public interface EntityPathNode {

	public String getAttribute();

	public EntityPathNode getParent();

	public void setParent(EntityPathNode parent);

}
