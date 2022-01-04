package easycriteria.where;

import java.util.Map;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Path;
import jakarta.persistence.criteria.Predicate;

import easycriteria.meta.EntityPathNode;

public class CollectionSizeGreaterOrEqCondition extends WhereCondition {

	private final int value;

	public CollectionSizeGreaterOrEqCondition(String attribute, int value, EntityPathNode parentAttribute) {
		this.attribute = attribute;
		this.value = value;
		this.parentAttribute = parentAttribute;
	}

	@Override
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public Predicate buildJPAPredicate(CriteriaBuilder builder, Path path, Map<String, Path> queryParts) {
		
		return builder.greaterThanOrEqualTo(builder.size(path.get(attribute)), value);
	}

	@Override
	public String toString() {

		return parentPath.toString() + "." + attribute + " size >= " + value;
	}
}
