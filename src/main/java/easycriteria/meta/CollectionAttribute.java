package easycriteria.meta;

import java.util.Collection;

import easycriteria.where.CollectionEmptyCondition;
import easycriteria.where.InCondition;
import easycriteria.where.CollectionMemberOfCondition;
import easycriteria.where.CollectionSizeBetweenCondition;
import easycriteria.where.CollectionSizeEqualsCondition;
import easycriteria.where.CollectionSizeGreaterCondition;
import easycriteria.where.CollectionSizeGreaterOrEqCondition;
import easycriteria.where.CollectionSizeLessThanCondition;
import easycriteria.where.CollectionSizeLessThanOrEqCondition;
import easycriteria.where.WhereCondition;

/**
 * 
 * @param <X> The type containing the represented attribute
 * @param <T> The type of the represented attribute
 */
public class CollectionAttribute<X, T> extends ObjectAttribute<T> {
	
	public CollectionAttribute(String attribute, EntityPathNode parentPath, Class<T> entityType) {
		super(attribute, parentPath, entityType);
	}

	public WhereCondition in(Collection<T> args) {
		return new InCondition<T>(getAttribute(), args, getParent());
	}
	
	public WhereCondition notIn(Collection<T> args) {
		return new InCondition<T>(getAttribute(), args, getParent(), false);
	}
	
	public WhereCondition contains(T element) {
		return new CollectionMemberOfCondition<T>(getAttribute(), element, getParent(), true);
	}
	
	public WhereCondition notContains(T element) {
		return new CollectionMemberOfCondition<T>(getAttribute(), element, getParent(), false);
	}
	
	public WhereCondition isEmpty() {
		return new CollectionEmptyCondition(getAttribute(), getParent(), true);
	}
	
	public WhereCondition isNotEmpty() {
		return new CollectionEmptyCondition(getAttribute(), getParent(), false);
	}
	
	public WhereCondition sizeEq(int size) {
		return new CollectionSizeEqualsCondition(getAttribute(), size, getParent());
	}
	
	public WhereCondition sizeGreaterThan(int size) {
		return new CollectionSizeGreaterCondition(getAttribute(), size, getParent());
	}
	
	public WhereCondition sizeGreaterThanOrEqualTo(int size) {
		return new CollectionSizeGreaterOrEqCondition(getAttribute(), size, getParent());
	}
	
	public WhereCondition sizeLessThan(int size) {
		return new CollectionSizeLessThanCondition(getAttribute(), size, getParent());
	}
	
	public WhereCondition sizeLessThanOrEqualTo(int size) {
		return new CollectionSizeLessThanOrEqCondition(getAttribute(), size, getParent());
	}
	
	public WhereCondition sizeBetween(int start, int end) {
		return new CollectionSizeBetweenCondition(getAttribute(), start, end, getParent());
	}
}