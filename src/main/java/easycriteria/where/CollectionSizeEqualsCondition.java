package easycriteria.where;

import java.util.Map;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;

import easycriteria.meta.EntityPathNode;

public class CollectionSizeEqualsCondition extends WhereCondition {

	private final int size;

	public CollectionSizeEqualsCondition(String attribute, int size, EntityPathNode parentAttribute) {
		this.attribute = attribute;
		this.size = size;
		this.parentAttribute = parentAttribute;
	}

	@Override
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public Predicate buildJPAPredicate(CriteriaBuilder builder, Path path, Map<String, Path> queryParts) {
		return builder.equal(builder.size(path.get(attribute)), size);
	}

	@Override
	public String toString() {

		return parentPath.toString() + "." + attribute + " size = " + size;
	}
}
