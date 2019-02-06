package easycriteria.where;

import java.util.Map;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;

import easycriteria.meta.EntityPathNode;

public class CollectionSizeGreaterCondition extends WhereCondition {

	private final int value;

	public CollectionSizeGreaterCondition(String attribute, int value, EntityPathNode parentAttribute) {
		this.attribute = attribute;
		this.value = value;
		this.parentAttribute = parentAttribute;
	}

	@Override
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public Predicate buildJPAPredicate(CriteriaBuilder builder, Path path, Map<String, Path> queryParts) {
		
		return builder.greaterThan(builder.size(path.get(attribute)), value);
	}

	@Override
	public String toString() {

		return parentPath.toString() + "." + attribute + " size > " + value;
	}
}
