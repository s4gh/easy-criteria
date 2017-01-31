package easycriteria;

import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Predicate;

import easycriteria.where.WhereCondition;

public class WhereTransformer {

	private final CriteriaBuilder criteriaBuilder;

	public WhereTransformer(CriteriaBuilder criteriaBuilder) {
		this.criteriaBuilder = criteriaBuilder;
	}

	public Predicate[] transform(List<WhereCondition> whereClauses) {
		return whereClauses.stream().map(whereClause -> whereClause.buildPredicate(criteriaBuilder))
				.toArray(size -> new Predicate[size]);
	}
}
