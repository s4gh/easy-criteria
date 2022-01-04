package easycriteria.where;

import java.util.Map;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Path;
import jakarta.persistence.criteria.Predicate;

import easycriteria.meta.EntityPathNode;

public class CollectionEmptyCondition extends WhereCondition {

	private boolean positiveCondition = false;

	public CollectionEmptyCondition(String attribute, EntityPathNode parentAttribute) {
		this.attribute = attribute;
		this.parentAttribute = parentAttribute;
	}
	
	public CollectionEmptyCondition(String attribute, EntityPathNode parentAttribute, boolean positiveCondition) {
		this.attribute = attribute;
		this.parentAttribute = parentAttribute;
		this.positiveCondition = positiveCondition;
	}

	@Override
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public Predicate buildJPAPredicate(CriteriaBuilder builder, Path path, Map<String, Path> queryParts) {

		Predicate predicate = builder.isEmpty(path.get(attribute));
		if (!positiveCondition) {
			predicate = builder.not(predicate);
		}
		return predicate;
	}

	@Override
	public String toString() {
		String not = (positiveCondition) ? "" : " not";
		return parentPath.toString() + "." + attribute + " is" + not + " empty";
	}
}
