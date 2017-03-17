package easycriteria.meta;

import easycriteria.OrderBy;
import easycriteria.where.EqualsCondition;
import easycriteria.where.IsNotNullCondition;
import easycriteria.where.IsNullCondition;
import easycriteria.where.NotEqualsCondition;
import easycriteria.where.WhereCondition;

/**
 * 
 * @param <X> The type containing the represented attribute
 * @param <T> The type of the represented attribute
 */
public class CollectionAttribute<X, T> implements EntityPathNode {
	
	private String attribute;	
	private EntityPathNode parentPath;
	
	public CollectionAttribute(String attribute) {
		this.attribute = attribute;				
	}
	
	public CollectionAttribute(String attribute, EntityPathNode parentPath) {
		this.attribute = attribute;
		if (parentPath != null && parentPath.getParent() != null) {
			this.parentPath = parentPath;
		}
	}
	
	public WhereCondition isNull() {
		return new IsNullCondition(attribute, parentPath);
	}
	
	public WhereCondition isNotNull() {
		return new IsNotNullCondition(attribute, parentPath);
	}

	public WhereCondition eq(T value) {
		return new EqualsCondition<T>(attribute, value, parentPath);
	}
	
	public WhereCondition notEq(T value) {
		return new NotEqualsCondition<T>(attribute, value);
	}

	public OrderBy<X, T> desc() {
		return new OrderBy<X, T>(attribute, false);
	}
	
	public OrderBy<X, T> asc() {
		return new OrderBy<X, T>(attribute, true);
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