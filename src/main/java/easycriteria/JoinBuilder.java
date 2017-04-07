package easycriteria;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.From;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;

import easycriteria.meta.ObjectAttribute;
import easycriteria.where.WhereCondition;

public class JoinBuilder<E, A, S, B extends WhereConditionsContainer>
		extends WhereCondition implements WhereConditionsContainer {

	private final List<WhereCondition> whereClauses;
	private final B queryBuilder;
	private final WhereTransformer whereTransformer;
	private Path<E> parentPath;
	private Join<E, A> join;
	private ObjectAttribute<A> alias; //TODO Alias is not used currently

	public JoinBuilder(String attribute, B queryBuilder, WhereTransformer whereTransformer,
			JoinType joinType, Path<E> parentPath) {
		this.whereClauses = new ArrayList<>();
		this.queryBuilder = queryBuilder;
		this.whereTransformer = whereTransformer;
		this.parentPath = parentPath;

		performJoin(attribute, joinType);
	}
	
	public JoinBuilder(String attribute, B queryBuilder, WhereTransformer whereTransformer,
			JoinType joinType, Path<E> parentPath, ObjectAttribute<A> alias) {
		this.whereClauses = new ArrayList<>();
		this.queryBuilder = queryBuilder;
		this.whereTransformer = whereTransformer;
		this.parentPath = parentPath;
		this.alias = alias;

		performJoin(attribute, joinType);
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	private void performJoin(String attribute, JoinType joinType) {
		this.join = ((From) parentPath).join(attribute, joinType);
	}
	
	public JoinBuilder<E, A, S, B> on(WhereCondition whereCondition) {
		whereCondition.setParentPath(join);
		addWhereCondition(whereCondition);
		return this;
	}
	
	public <A1> JoinBuilder<A, A1, S, JoinBuilder<E, A, S, B>> join(ObjectAttribute<A1> attribute, JoinType joinType) {
		return new JoinBuilder<A, A1, S, JoinBuilder<E, A, S, B>>(attribute.getAttribute(), this, whereTransformer, joinType, join);
	}
	
	public <A1> JoinBuilder<A, A1, S, JoinBuilder<E, A, S, B>> join(ObjectAttribute<A1> attribute, JoinType joinType, ObjectAttribute<A1> alias) {
		return new JoinBuilder<A, A1, S, JoinBuilder<E, A, S, B>>(attribute.getAttribute(), this, whereTransformer, joinType, join, alias);
	}

	public B endJoin() {
		queryBuilder.addWhereCondition(this);
		return queryBuilder;
	}

	public void addWhereCondition(WhereCondition whereClause) {
		whereClauses.add(whereClause);
	}
	
	@Override
	@SuppressWarnings("rawtypes")
	protected Predicate buildJPAPredicate(CriteriaBuilder cb, Path path) {
		Predicate[] constraints = whereTransformer.transform(whereClauses, path);
		return cb.and(constraints);
	}
}