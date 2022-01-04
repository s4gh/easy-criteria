package easycriteria.where;

import java.util.Map;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Path;
import jakarta.persistence.criteria.Predicate;

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
	public Predicate buildJPAPredicate(CriteriaBuilder builder, Path path, Map<String, Path> queryParts) {
		return builder.notEqual(path.get(attribute), value);
	}

	@Override
	public String toString() {

		return parentPath.toString() + "." + attribute + " == " + value;
	}
}
