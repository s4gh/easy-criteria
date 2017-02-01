package easycriteria;

import java.util.Collection;

import javax.persistence.criteria.Path;
import javax.persistence.metamodel.SingularAttribute;

import easycriteria.where.BetweenCondition;
import easycriteria.where.EqualsCondition;
import easycriteria.where.GreaterThanCondition;
import easycriteria.where.GreaterThanEqualsCondition;
import easycriteria.where.InCondition;
import easycriteria.where.LessThanCondition;
import easycriteria.where.LessThanEqualsCondition;
import easycriteria.where.LikeCondition;
import easycriteria.where.NotEqualsCondition;

public class WhereConditionBuilder<E, A, S, B extends WhereConditionsContainer> {

	private final SingularAttribute<E, A> attribute;
	private B whereClausesContainer;
	private Path<E> parentPath;

	public WhereConditionBuilder(B queryBuilder, SingularAttribute<E, A> attribute, Path<E> parentPath) {

		this.attribute = attribute;
		this.whereClausesContainer = queryBuilder;
		this.parentPath = parentPath;
	}

	public B eq(A object) {

		whereClausesContainer.addWhereClause(new EqualsCondition<E, A>(attribute, object, parentPath));

		return whereClausesContainer;
	}

	public B in(Collection<A> collection) {

		whereClausesContainer.addWhereClause(new InCondition<E, A>(attribute, collection, parentPath));

		return whereClausesContainer;
	}

	public B notEq(A object) {

		whereClausesContainer.addWhereClause(new NotEqualsCondition<E, A>(attribute, object, parentPath));

		return whereClausesContainer;
	}

	public B greaterThan(A value) {

		whereClausesContainer
				.addWhereClause(new GreaterThanCondition<E, A>((SingularAttribute<E, A>) attribute, value, parentPath));

		return whereClausesContainer;
	}

	public B lessThan(A value) {

		whereClausesContainer
				.addWhereClause(new LessThanCondition<E, A>((SingularAttribute<E, A>) attribute, value, parentPath));

		return whereClausesContainer;
	}

	public B greaterThanOrEqualTo(A value) {

		whereClausesContainer.addWhereClause(new GreaterThanEqualsCondition<E, A>(attribute, value, parentPath));

		return whereClausesContainer;
	}

	public B lessThanOrEqualTo(A value) {

		whereClausesContainer.addWhereClause(new LessThanEqualsCondition<E, A>(attribute, value, parentPath));

		return whereClausesContainer;
	}

	@SuppressWarnings("unchecked")
	public B like(String likeString) {

		whereClausesContainer
				.addWhereClause(new LikeCondition<E>((SingularAttribute<E, String>) attribute, likeString, parentPath));

		return whereClausesContainer;
	}

	public B between(A from, A to) {

		whereClausesContainer.addWhereClause(new BetweenCondition<E, A>(attribute, from, to, parentPath));

		return whereClausesContainer;
	}

	/**
	 * Access nested property. Access to nested property triggers inner join so
	 * parent entities without nested property (null) will not be included into
	 * result set
	 * 
	 * @param nestedAttribute - nested attribute
	 * @return parent builder node
	 */
	public <A1> WhereConditionBuilder<A, A1, S, B> nested(SingularAttribute<A, A1> nestedAttribute) {
		return new WhereConditionBuilder<A, A1, S, B>(whereClausesContainer, nestedAttribute,
				parentPath.get(attribute));
	}
}
