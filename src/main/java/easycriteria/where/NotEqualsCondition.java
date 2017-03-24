package easycriteria.where;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;

public class NotEqualsCondition<A> extends WhereCondition {

	private final A value;

	public NotEqualsCondition(String attribute, A object) {
		this.attribute = attribute;
		this.value = object;
	}

	@SuppressWarnings("rawtypes")
	public NotEqualsCondition(String attribute, A object, Path parentPath) {
		this.attribute = attribute;
		this.value = object;
		this.parentPath = parentPath;
	}

	@SuppressWarnings("rawtypes")
	@Override
	public Predicate buildJPAPredicate(CriteriaBuilder builder, Path path) {
		return builder.notEqual(path.get(attribute), value);
	}

	@Override
	public String toString() {

		return parentPath.toString() + "." + attribute + " == " + value;
	}
}
