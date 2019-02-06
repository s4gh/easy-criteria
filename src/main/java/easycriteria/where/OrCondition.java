package easycriteria.where;

import java.util.Map;

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
	public Predicate buildJPAPredicate(CriteriaBuilder builder, Path parentPath, Map<String, Path> queryParts) {
		return builder.or(left.buildPredicate(builder, parentPath, queryParts), right.buildPredicate(builder, parentPath,
				queryParts));
	}

	@Override
	public String toString() {
		return left.toString() + " OR " + right.toString();
	}
}
