package easycriteria.where;

import java.util.Map;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Path;
import jakarta.persistence.criteria.Predicate;

import easycriteria.EasyCriteriaSubquery;
import easycriteria.meta.EntityPathNode;

public class InSubqueryCondition<A> extends WhereCondition {

	private final EasyCriteriaSubquery<?, A> subquery;
	private boolean positiveCondition = true;

	public InSubqueryCondition(String attribute, EasyCriteriaSubquery<?, A> subquery, EntityPathNode parentAttribute) {
		this.attribute = attribute;
		this.subquery = subquery;
		this.parentAttribute = parentAttribute;
	}
	
	public InSubqueryCondition(String attribute, EasyCriteriaSubquery<?, A> subquery, EntityPathNode parentAttribute, boolean positiveCondition) {
		this.attribute = attribute;
		this.subquery = subquery;
		this.parentAttribute = parentAttribute;
		this.positiveCondition = positiveCondition;
	}

	@Override
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public Predicate buildJPAPredicate(CriteriaBuilder builder, Path path, Map<String, Path> queryParts) {

		Predicate inConditionPredicate = path.get(attribute).in(subquery.getCriteriaSubquery());
		if (!positiveCondition) {
			inConditionPredicate = builder.not(inConditionPredicate);
		}
		return inConditionPredicate;
	}

	@Override
	public String toString() {
		String not = (positiveCondition) ? "" : " not";
		return parentPath.toString() + "." + attribute + not + " in " + subquery.toString();
	}
}
