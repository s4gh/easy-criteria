package easycriteria;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Root;
import javax.persistence.criteria.Subquery;

import easycriteria.meta.ObjectAttribute;
import easycriteria.where.WhereCondition;

public class EasyCriteriaSubQuery<E, T> implements WhereConditionsContainer {

	private List<WhereCondition> whereClauses;
	private Subquery<T> criteriaSubQuery;
	private WhereTransformer whereTransformer;
	private Root<E> root;

	public EasyCriteriaSubQuery(Subquery<T> criteriaSubQuery, WhereTransformer whereTransformer, Root<E> root) {
		this.whereClauses = new ArrayList<>();
		this.criteriaSubQuery = criteriaSubQuery;
		this.whereTransformer = whereTransformer;
		this.root = root;
	}
	
	public EasyCriteriaSubQuery<E, T> where(WhereCondition whereCondition){
		addWhereCondition(whereCondition);
		return this;
	}
	
	public <A> JoinBuilder<E, A, T, EasyCriteriaSubQuery<E, T>> join(ObjectAttribute<A> attribute,
			JoinType joinType) {

		return new JoinBuilder<E, A, T, EasyCriteriaSubQuery<E, T>>(attribute.getAttribute(), this, whereTransformer, joinType, root);
	}
	
	public <A> JoinBuilder<E, A, T, EasyCriteriaSubQuery<E, T>> join(ObjectAttribute<A> attribute,
			JoinType joinType, ObjectAttribute<A> alias) {

		return new JoinBuilder<E, A, T, EasyCriteriaSubQuery<E, T>>(attribute.getAttribute(), this, whereTransformer, joinType, root);
	}

	public void addWhereCondition(WhereCondition whereClause) {

		whereClauses.add(whereClause);
	}

	private List<WhereCondition> getWhereClauses() {

		return whereClauses;
	}

	public Subquery<T> getCriteriaSubQuery() {
		return criteriaSubQuery.where(whereTransformer.transform(getWhereClauses(), getRoot()));
	}

	public Root<E> getRoot() {
		return root;
	}

	@Override
	public String toString() {

		return "FROM " + root.getJavaType() + "WHERE " + getWhereClauses();
	}
}