package easycriteria.where;

import java.util.Map;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Path;
import jakarta.persistence.criteria.Predicate;

import easycriteria.meta.EntityPathNode;

public class LessThanEqualsCondition<A> extends WhereCondition {

	private final A value;

	public LessThanEqualsCondition(String attribute, A value, EntityPathNode parentAttribute) {
		this.attribute = attribute;
		this.value = value;
		this.parentAttribute = parentAttribute;
	}
	
	@Override
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public Predicate buildJPAPredicate(CriteriaBuilder builder, Path path, Map<String, Path> queryParts) {
		return builder.lessThanOrEqualTo(path.get(attribute), (Comparable) value);
	}

	@Override
	public String toString() {

		return parentPath.toString() + "." + attribute + " <= " + value;
	}
}
