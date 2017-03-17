package easycriteria;

import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;

import easycriteria.where.WhereCondition;

public class WhereTransformer {

	private final CriteriaBuilder criteriaBuilder;

	public WhereTransformer(CriteriaBuilder criteriaBuilder) {
		this.criteriaBuilder = criteriaBuilder;
	}
	
	@SuppressWarnings("rawtypes")
	public Predicate[] transform(List<WhereCondition> whereClauses, Path root) {
		return whereClauses.stream().map(whereClause -> whereClause.buildPredicate(criteriaBuilder, root))
				.toArray(size -> new Predicate[size]);
	}
}
