package easycriteria;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Root;
import javax.persistence.criteria.Subquery;

import easycriteria.meta.ObjectAttribute;
import easycriteria.where.WhereCondition;

public class EasyCriteriaSubquery<E, T> implements WhereConditionsContainer {

	private List<WhereCondition> whereClauses;	
	private WhereTransformer whereTransformer;
	private Subquery<T> criteriaSubquery;
	private Root<E> root;

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
	
	public <A> JoinBuilder<E, A, T, EasyCriteriaSubquery<E, T>> join(ObjectAttribute<A> attribute,
			JoinType joinType) {

		return new JoinBuilder<E, A, T, EasyCriteriaSubquery<E, T>>(attribute.getAttribute(), this, whereTransformer, joinType, root);
	}
	
	public <A> JoinBuilder<E, A, T, EasyCriteriaSubquery<E, T>> join(ObjectAttribute<A> attribute,
			JoinType joinType, ObjectAttribute<A> alias) {

		return new JoinBuilder<E, A, T, EasyCriteriaSubquery<E, T>>(attribute.getAttribute(), this, whereTransformer, joinType, root);
	}

	public void addWhereCondition(WhereCondition whereClause) {

		whereClauses.add(whereClause);
	}

	private List<WhereCondition> getWhereClauses() {

		return whereClauses;
	}

	public Subquery<T> getCriteriaSubquery() {
		return criteriaSubquery.where(whereTransformer.transform(getWhereClauses(), getRoot()));
	}

	public Root<E> getRoot() {
		return root;
	}

	@Override
	public String toString() {

		return "FROM " + root.getJavaType() + "WHERE " + getWhereClauses();
	}
}