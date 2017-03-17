package easycriteria.where;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;

public class AndCondition extends WhereCondition {

	private final WhereCondition left;
	private final WhereCondition right;

	public AndCondition(WhereCondition left, WhereCondition right) {
		super();
		this.left = left;
		this.right = right;
	}

	@Override
	public Predicate buildJPAPredicate(CriteriaBuilder builder, Path parentPath) {
		return builder.and(left.buildPredicate(builder, parentPath), right.buildPredicate(builder, parentPath));
	}

	@Override
	public String toString() {
		return left.toString() + " AND " + right.toString();
	}
}
