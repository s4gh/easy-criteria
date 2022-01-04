package easycriteria.where;

import java.util.Map;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Path;
import jakarta.persistence.criteria.Predicate;

import easycriteria.meta.EntityPathNode;

public class CollectionSizeBetweenCondition extends WhereCondition {

	private final int start;
	private final int end;

	public CollectionSizeBetweenCondition(String attribute, int start, int end, EntityPathNode parentAttribute) {
		this.attribute = attribute;
		this.start = start;
		this.end = end;
		this.parentAttribute = parentAttribute;
	}

	@Override
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public Predicate buildJPAPredicate(CriteriaBuilder builder, Path path, Map<String, Path> queryParts) {
		
		return builder.between(builder.size(path.get(attribute)), start, end);
	}

	@Override
	public String toString() {

		return parentPath.toString() + "." + attribute + " size between " + start + " and " + end;
	}
}
