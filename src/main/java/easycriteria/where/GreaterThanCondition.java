package easycriteria.where;

import java.util.Map;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;

import easycriteria.meta.EntityPathNode;

public class GreaterThanCondition<A> extends WhereCondition {

	private final A value;

	public GreaterThanCondition(String attribute, A value, EntityPathNode parentAttribute) {
		this.attribute = attribute;
		this.value = value;
		this.parentAttribute = parentAttribute;
	}

	@Override
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public Predicate buildJPAPredicate(CriteriaBuilder builder, Path path, Map<String, Path> queryParts) {

		return builder.greaterThan(path.get(attribute), (Comparable) value);
	}

	@Override
	public String toString() {

		return parentPath.toString() + "." + attribute + " > " + value;
	}
}
