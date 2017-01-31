package easycriteria.where;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.metamodel.SingularAttribute;

public class BetweenCondition<E, A> implements WhereCondition {

	@SuppressWarnings("rawtypes")
	private final SingularAttribute attribute;
	private final A to;
	private final A from;
	private final Path<E> parentPath;

	public BetweenCondition(SingularAttribute<E, A> attribute, A from, A to, Path<E> parentPath) {
		this.attribute = attribute;
		this.from = from;
		this.to = to;
		this.parentPath = parentPath;
	}

	@Override
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public Predicate buildPredicate(CriteriaBuilder builder) {
		return builder.between(parentPath.get(attribute), (Comparable) from, (Comparable) to);
	}

	@Override
	public String toString() {
		return parentPath.toString() + "." + attribute.getName() + " between " + from + " and " + to;
	}
}
