package easycriteria.meta;

import java.util.Collection;

import easycriteria.OrderBy;
import easycriteria.where.EqualsCondition;
import easycriteria.where.InCondition;
import easycriteria.where.IsNotNullCondition;
import easycriteria.where.IsNullCondition;
import easycriteria.where.NotEqualsCondition;
import easycriteria.where.WhereCondition;

/**
 * 
 * @param <X> The type containing the represented attribute
 * @param <T> The type of the represented attribute
 */
public class PropertyAttribute<X, T> implements EntityPathNode {
	
	private String attribute;	
	private EntityPathNode parentPath;
	
	public PropertyAttribute(String attribute) {
		this.attribute = attribute;				
	}
	
	public PropertyAttribute(String attribute, EntityPathNode parentPath) {
		this.attribute = attribute;
		if (parentPath != null && parentPath.getParent() != null) {
			this.parentPath = parentPath;
		}
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
	
	public WhereCondition in(Collection<T> args) {
		return new InCondition<T>(attribute, args, parentPath);
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
	public void setParent(EntityPathNode parent) {
		this.parentPath = parent;
	}
}