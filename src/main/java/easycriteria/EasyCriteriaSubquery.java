package easycriteria;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jakarta.persistence.criteria.From;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Path;
import jakarta.persistence.criteria.Root;
import jakarta.persistence.criteria.Subquery;

import easycriteria.meta.ObjectAttribute;
import easycriteria.where.WhereCondition;

public class EasyCriteriaSubquery<E, T> implements WhereConditionsContainer {

	private List<WhereCondition> whereClauses;	
	private WhereTransformer whereTransformer;
	private Subquery<T> criteriaSubquery;
	private Root<E> root;
	Map<String, Path> queryParts = new HashMap<>();

	public EasyCriteriaSubquery(Subquery<T> criteriaSubquery, WhereTransformer whereTransformer, Root<E> root) {
		this.whereClauses = new ArrayList<>();
		this.criteriaSubquery = criteriaSubquery;
		this.whereTransformer = whereTransformer;
		this.root = root;
	}
	
	public EasyCriteriaSubquery<E, T> where(WhereCondition whereCondition){
		addWhereCondition(whereCondition);
		return this;
	}

	public EasyCriteriaSubquery<E, T>  join(ObjectAttribute attribute, JoinType joinType, ObjectAttribute alias) {
		Join join;
		if (queryParts.containsKey(attribute.getParent().getAttribute())) {
			Path path = queryParts.get(attribute.getParent().getAttribute());
			join = ((From) path).join(attribute.getAttribute(), joinType);
		} else {
			join = ((From) root).join(attribute.getAttribute(), joinType);
		}
		queryParts.put(alias.getAttribute(),join);
		return this;
	}

	public void addWhereCondition(WhereCondition whereClause) {

		whereClauses.add(whereClause);
	}

	private List<WhereCondition> getWhereClauses() {

		return whereClauses;
	}

	public Subquery<T> getCriteriaSubquery() {
		return criteriaSubquery.where(whereTransformer.transform(getWhereClauses(), getRoot(), queryParts));
	}

	public Root<E> getRoot() {
		return root;
	}

	@Override
	public String toString() {

		return "FROM " + root.getJavaType() + "WHERE " + getWhereClauses();
	}
}