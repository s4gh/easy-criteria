package easycriteria;

import java.util.List;
import java.util.Map;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Path;
import jakarta.persistence.criteria.Predicate;

import easycriteria.where.WhereCondition;

public class WhereTransformer {

	private final CriteriaBuilder criteriaBuilder;

	public WhereTransformer(CriteriaBuilder criteriaBuilder) {
		this.criteriaBuilder = criteriaBuilder;
	}
	
	@SuppressWarnings("rawtypes")
	public Predicate[] transform(List<WhereCondition> whereClauses, Path root,
															 Map<String, Path> queryParts) {
		return whereClauses.stream().map(whereClause -> whereClause.buildPredicate(criteriaBuilder, root, queryParts))
				.toArray(size -> new Predicate[size]);
	}
}
