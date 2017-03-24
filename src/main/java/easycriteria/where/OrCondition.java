package easycriteria.where;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;

public class OrCondition extends WhereCondition {

	private final WhereCondition left;
	private final WhereCondition right;

	public OrCondition(WhereCondition left, WhereCondition right) {
		super();
		this.left = left;
		this.right = right;
	}

	@SuppressWarnings("rawtypes")
	@Override
	public Predicate buildJPAPredicate(CriteriaBuilder builder, Path parentPath) {
		return builder.or(left.buildPredicate(builder, parentPath), right.buildPredicate(builder, parentPath));
	}

	@Override
	public String toString() {
		return left.toString() + " OR " + right.toString();
	}
}
