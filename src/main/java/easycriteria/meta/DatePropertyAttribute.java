package easycriteria.meta;

import easycriteria.where.BetweenCondition;
import easycriteria.where.GreaterThanCondition;
import easycriteria.where.GreaterThanEqualsCondition;
import easycriteria.where.LessThanCondition;
import easycriteria.where.LessThanEqualsCondition;
import easycriteria.where.WhereCondition;

public class DatePropertyAttribute<X, T> extends PropertyAttribute<X, T>{

	public DatePropertyAttribute(String attribute) {
		super(attribute);
	}

	public DatePropertyAttribute(String attribute, EntityPathNode parentPath) {
		super(attribute, parentPath);
	}
	
	public WhereCondition greaterThan(T value) {
		return new GreaterThanCondition<T>(getAttribute(), value, getParent());
	}
	
	public WhereCondition lessThan(T value) {
		return new LessThanCondition<T>(getAttribute(), value, getParent());
	}
	
	public WhereCondition greaterThanOrEqualTo(T value) {
		return new GreaterThanEqualsCondition<T>(getAttribute(), value, getParent());
	}
	
	public WhereCondition lessThanOrEqualTo(T value) {
		return new LessThanEqualsCondition<T>(getAttribute(), value, getParent());
	}
	
	public WhereCondition between(T from, T to) {
		return new BetweenCondition<T>(getAttribute(), from, to , getParent());
	}

}
