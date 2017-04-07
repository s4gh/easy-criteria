package easycriteria.where;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;

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
	public Predicate buildJPAPredicate(CriteriaBuilder builder, Path path) {

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
