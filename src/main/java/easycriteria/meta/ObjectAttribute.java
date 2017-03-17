package easycriteria.meta;

import easycriteria.where.IsNotNullCondition;
import easycriteria.where.IsNullCondition;
import easycriteria.where.WhereCondition;

/**
 * 
 * @param <X> The type containing the represented attribute
 * @param <T> The type of the represented attribute
 */
public class ObjectAttribute<T> implements EntityPathNode {
	
	private String attribute;
	private EntityPathNode parentPath;
	
	public ObjectAttribute(String attribute, EntityPathNode parentPath) {
		this.attribute = attribute;		
		this.parentPath = parentPath;
	}

	public WhereCondition isNull() {
		return new IsNullCondition(attribute, parentPath);
	}
	
	public WhereCondition isNotNull() {
		return new IsNotNullCondition(attribute, parentPath);
	}

	public String getAttribute() {
		return attribute;
	}

	@Override
	public EntityPathNode getParent() {
		return parentPath;
	}

	@Override
	public void setParent(EntityPathNode parentPath) {
		this.parentPath = parentPath;
	}
}