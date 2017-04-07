package easycriteria.meta;

import easycriteria.where.IsNotNullCondition;
import easycriteria.where.IsNullCondition;
import easycriteria.where.WhereCondition;

/**
 * 
 * @param <T> The type of the represented attribute
 */
public class ObjectAttribute<T> implements EntityPathNode {
	
	private String attribute;
	private EntityPathNode parentPath;
	private Class<T> entityType;
	
	public ObjectAttribute(String attribute, EntityPathNode parentPath, Class<T> entityType) {
		this.attribute = attribute;		
		this.parentPath = parentPath;
		this.entityType = entityType;
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

	@Override
	public Class<T> getEntityType() {
		return entityType;
	}
}