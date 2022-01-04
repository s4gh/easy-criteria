package easycriteria.where;

import java.util.Map;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Path;
import jakarta.persistence.criteria.Predicate;

import easycriteria.meta.EntityPathNode;

public class BetweenCondition<A> extends WhereCondition {

	private final A to;
	private final A from;

	public BetweenCondition(String attribute, A from, A to, EntityPathNode parentAttribute) {
		this.attribute = attribute;
		this.from = from;
		this.to = to;
		this.parentAttribute = parentAttribute;
	}

	@Override
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public Predicate buildJPAPredicate(CriteriaBuilder builder, Path root, Map<String, Path> queryParts) {
		return builder.between(parentPath.get(attribute), (Comparable) from, (Comparable) to);
	}

	@Override
	public String toString() {
		return parentPath.toString() + "." + attribute + " between " + from + " and " + to;
	}
}
