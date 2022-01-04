package easycriteria.where;

import java.util.Collection;
import java.util.Map;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Path;
import jakarta.persistence.criteria.Predicate;

import easycriteria.meta.EntityPathNode;

public class InCondition<A> extends WhereCondition {

	private final Collection<A> args;
	private boolean positiveCondition = true;

	public InCondition(String attribute, Collection<A> args, EntityPathNode parentAttribute) {
		this.attribute = attribute;
		this.args = args;
		this.parentAttribute = parentAttribute;
	}
	
	public InCondition(String attribute, Collection<A> args, EntityPathNode parentAttribute, boolean positiveCondition) {
		this.attribute = attribute;
		this.args = args;
		this.parentAttribute = parentAttribute;
		this.positiveCondition = positiveCondition;
	}

	@Override
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public Predicate buildJPAPredicate(CriteriaBuilder builder, Path path, Map<String, Path> queryParts) {

		Predicate inConditionPredicate = path.get(attribute).in(args);
		if (!positiveCondition) {
			inConditionPredicate = builder.not(inConditionPredicate);
		}
		return inConditionPredicate;
	}

	@Override
	public String toString() {
		String not = (positiveCondition) ? "" : " not";
		return parentPath.toString() + "." + attribute + not + " in " + args;
	}
}
