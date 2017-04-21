package easycriteria.meta;

import java.util.Collection;

import easycriteria.EasyCriteriaSubquery;
import easycriteria.OrderBy;
import easycriteria.where.EqualsCondition;
import easycriteria.where.InCondition;
import easycriteria.where.InSubqueryCondition;
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
	private Class<T> propertyType;
	
	public PropertyAttribute(String attribute, Class<T> propertyType) {
		this.attribute = attribute;
		this.propertyType = propertyType;		
	}
	
	public PropertyAttribute(String attribute, EntityPathNode parentPath, Class<T> propertyType) {
		this.attribute = attribute;

		this.parentPath = parentPath;
				
		this.propertyType = propertyType;		
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
	
	public WhereCondition notIn(Collection<T> args) {
		return new InCondition<T>(attribute, args, parentPath, false);
	}
	
	public WhereCondition in(EasyCriteriaSubquery<?, T> subquery) {
		return new InSubqueryCondition<T>(attribute, subquery, parentPath);
	}
	
	public WhereCondition notIn(EasyCriteriaSubquery<?, T> subquery) {
		return new InSubqueryCondition<T>(attribute, subquery, parentPath, false);
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


	public Class<T> getPropertyType() {
		return propertyType;
	}

	@Override
	public Class<?> getEntityType() {
		return parentPath.getEntityType();
	}	
}