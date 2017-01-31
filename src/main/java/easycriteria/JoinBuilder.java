package easycriteria;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.From;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.metamodel.Attribute;
import javax.persistence.metamodel.CollectionAttribute;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.MapAttribute;
import javax.persistence.metamodel.SetAttribute;
import javax.persistence.metamodel.SingularAttribute;

import easycriteria.where.CompoundAndCondition;
import easycriteria.where.CompoundOrCondition;
import easycriteria.where.WhereCondition;

/**
 * 
 * @author sveryov
 *
 * @param <E>
 * @param <A>
 * @param <S>
 * @param <B>
 */
public class JoinBuilder<E, A, S, B extends WhereConditionsContainer>
		implements WhereCondition, WhereConditionsContainer {

	private final List<WhereCondition> whereClauses;
	private final B queryBuilder;
	private final WhereTransformer whereTransformer;
	private final JoinType joinType;
	private Path<E> parentPath;
	private Join<E, A> join;

	public JoinBuilder(SingularAttribute<E, A> attribute, B queryBuilder, WhereTransformer whereTransformer,
			JoinType joinType, Path<E> parentPath) {
		this.whereClauses = new ArrayList<>();
		this.queryBuilder = queryBuilder;
		this.whereTransformer = whereTransformer;
		this.joinType = joinType;
		this.parentPath = parentPath;

		performJoin(attribute, this.joinType);
	}

	public JoinBuilder(ListAttribute<E, A> attribute, B queryBuilder, WhereTransformer whereTransformer,
			JoinType joinType, Path<E> parentPath) {
		this.whereClauses = new ArrayList<>();
		this.queryBuilder = queryBuilder;
		this.whereTransformer = whereTransformer;
		this.joinType = joinType;
		this.parentPath = parentPath;

		performJoin(attribute, this.joinType);
	}

	public JoinBuilder(SetAttribute<E, A> attribute, B queryBuilder, WhereTransformer whereTransformer,
			JoinType joinType, Path<E> parentPath) {
		this.whereClauses = new ArrayList<>();
		this.queryBuilder = queryBuilder;
		this.whereTransformer = whereTransformer;
		this.joinType = joinType;
		this.parentPath = parentPath;

		performJoin(attribute, joinType);
	}

	public JoinBuilder(CollectionAttribute<E, A> attribute, B queryBuilder, WhereTransformer whereTransformer,
			JoinType joinType, Path<E> parentPath) {
		this.whereClauses = new ArrayList<>();
		this.queryBuilder = queryBuilder;
		this.whereTransformer = whereTransformer;
		this.joinType = joinType;
		this.parentPath = parentPath;

		performJoin(attribute, joinType);
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	private void performJoin(Attribute attribute, JoinType joinType) {
		if (attribute instanceof SingularAttribute) {
			this.join = ((From) parentPath).join((SingularAttribute<E, A>) attribute, joinType);
		} else if (attribute instanceof ListAttribute) {
			this.join = ((From) parentPath).join((ListAttribute<E, A>) attribute, joinType);
		} else if (attribute instanceof SetAttribute) {
			this.join = ((From) parentPath).join((SetAttribute<E, A>) attribute, joinType);
		} else if (attribute instanceof CollectionAttribute) {
			this.join = ((From) parentPath).join((CollectionAttribute<E, A>) attribute, joinType);
		} else if (attribute instanceof MapAttribute) {
			this.join = ((From) parentPath).join((MapAttribute) attribute, joinType);
		}
	}

	public <A1> WhereConditionBuilder<A, A1, S, JoinBuilder<E, A, S, B>> where(SingularAttribute<A, A1> attribute) {
		return new WhereConditionBuilder<A, A1, S, JoinBuilder<E, A, S, B>>(this, attribute, join);
	}

	public CompoundAndCondition<A, S, JoinBuilder<E, A, S, B>> whereAnd() {
		return new CompoundAndCondition<A, S, JoinBuilder<E, A, S, B>>(this, whereTransformer, join);
	}

	public CompoundOrCondition<A, S, JoinBuilder<E, A, S, B>> whereOr() {
		return new CompoundOrCondition<A, S, JoinBuilder<E, A, S, B>>(this, whereTransformer, join);
	}

	public <A1> JoinBuilder<A, A1, S, JoinBuilder<E, A, S, B>> join(SingularAttribute<A, A1> attribute,
			JoinType joinType) {
		return new JoinBuilder<A, A1, S, JoinBuilder<E, A, S, B>>(attribute, this, whereTransformer, joinType, join);
	}

	public B endJoin() {
		queryBuilder.addWhereClause(this);
		return queryBuilder;
	}

	public void addWhereClause(WhereCondition whereClause) {
		whereClauses.add(whereClause);
	}

	@Override
	public Predicate buildPredicate(CriteriaBuilder cb) {
		Predicate[] constraints = whereTransformer.transform(whereClauses);
		return cb.and(constraints);
	}
}