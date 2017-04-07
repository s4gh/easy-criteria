package easycriteria.where;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;

import easycriteria.EasyCriteriaSubQuery;
import easycriteria.meta.EntityPathNode;

public class InSubQueryCondition<A> extends WhereCondition {

	private final EasyCriteriaSubQuery<?, A> subQuery;
	private boolean positiveCondition = true;

	public InSubQueryCondition(String attribute, EasyCriteriaSubQuery<?, A> subQuery, EntityPathNode parentAttribute) {
		this.attribute = attribute;
		this.subQuery = subQuery;
		this.parentAttribute = parentAttribute;
	}
	
	public InSubQueryCondition(String attribute, EasyCriteriaSubQuery<?, A> subQuery, EntityPathNode parentAttribute, boolean positiveCondition) {
		this.attribute = attribute;
		this.subQuery = subQuery;
		this.parentAttribute = parentAttribute;
		this.positiveCondition = positiveCondition;
	}

	@Override
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public Predicate buildJPAPredicate(CriteriaBuilder builder, Path path) {

		Predicate inConditionPredicate = path.get(attribute).in(subQuery.getCriteriaSubQuery());
		if (!positiveCondition) {
			inConditionPredicate = builder.not(inConditionPredicate);
		}
		return inConditionPredicate;
	}

	@Override
	public String toString() {
		String not = (positiveCondition) ? "" : " not";
		return parentPath.toString() + "." + attribute + not + " in " + subQuery.toString();
	}
}
