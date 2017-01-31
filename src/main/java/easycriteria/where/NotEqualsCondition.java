package easycriteria.where;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.metamodel.SingularAttribute;

public class NotEqualsCondition<E, A> implements WhereCondition {

	private final SingularAttribute<E, A> attribute;
	private final A value;
	private Path<E> parentPath;

	public NotEqualsCondition(SingularAttribute<E, A> attribute, A value) {
		this.attribute = attribute;
		this.value = value;
	}

	public NotEqualsCondition(SingularAttribute<E, A> attribute, A value, Path<E> parentPath) {
		this.attribute = attribute;
		this.value = value;
		this.parentPath = parentPath;
	}

	@Override
	public Predicate buildPredicate(CriteriaBuilder builder) {

		return builder.notEqual(parentPath.get(attribute), value);
	}

	@Override
	public String toString() {

		return parentPath.toString() + "." + attribute.getName() + " != " + value;
	}

}
