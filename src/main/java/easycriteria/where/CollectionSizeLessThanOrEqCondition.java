package easycriteria.where;

import java.util.Map;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;

import easycriteria.meta.EntityPathNode;

public class CollectionSizeLessThanOrEqCondition extends WhereCondition {

	private final int value;

	public CollectionSizeLessThanOrEqCondition(String attribute, int value, EntityPathNode parentAttribute) {
		this.attribute = attribute;
		this.value = value;
		this.parentAttribute = parentAttribute;
	}

	@Override
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public Predicate buildJPAPredicate(CriteriaBuilder builder, Path path, Map<String, Path> queryParts) {
		
		return builder.lessThanOrEqualTo(builder.size(path.get(attribute)), value);
	}

	@Override
	public String toString() {

		return parentPath.toString() + "." + attribute + " size <= " + value;
	}
}
