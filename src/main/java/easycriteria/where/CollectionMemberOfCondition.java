package easycriteria.where;

import java.util.Map;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Path;
import jakarta.persistence.criteria.Predicate;

import easycriteria.meta.EntityPathNode;

public class CollectionMemberOfCondition<A> extends WhereCondition {

	private final A element;
	private boolean positiveCondition = false;

	public CollectionMemberOfCondition(String attribute, A element, EntityPathNode parentAttribute) {
		this.attribute = attribute;
		this.element = element;
		this.parentAttribute = parentAttribute;
	}
	
	public CollectionMemberOfCondition(String attribute, A element, EntityPathNode parentAttribute, boolean positiveCondition) {
		this.attribute = attribute;
		this.element = element;
		this.parentAttribute = parentAttribute;
		this.positiveCondition = positiveCondition;
	}

	@Override
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public Predicate buildJPAPredicate(CriteriaBuilder builder, Path path, Map<String, Path> queryParts) {

		Predicate predicate = builder.isMember(element, path.get(attribute));
		if (!positiveCondition) {
			predicate = builder.not(predicate);
		}
		return predicate;
	}

	@Override
	public String toString() {
		String not = (positiveCondition) ? "" : " not";
		return parentPath.toString() + "." + attribute + not + " has member " + element;
	}
}
