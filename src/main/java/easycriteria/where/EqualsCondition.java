package easycriteria.where;

import java.util.Map;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Path;
import jakarta.persistence.criteria.Predicate;

import easycriteria.meta.EntityPathNode;

public class EqualsCondition<A> extends WhereCondition {

	private final A value;

	public EqualsCondition(String attribute, A object) {
		this.attribute = attribute;
		this.value = object;
	}
	
	public EqualsCondition(String attribute, A object, EntityPathNode parentAttribute) {
		this.attribute = attribute;
		this.value = object;
		this.parentAttribute = parentAttribute;
	}

	@SuppressWarnings("rawtypes")
	@Override
	protected Predicate buildJPAPredicate(CriteriaBuilder builder, Path path, Map<String, Path> queryParts) {
		return builder.equal(path.get(attribute), value);
	}

	@Override
	public String toString() {

		return parentPath.toString() + "." + attribute + " == " + value;
	}
}
