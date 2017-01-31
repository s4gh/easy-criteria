package easycriteria.where;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.metamodel.SingularAttribute;

public class LikeCondition<E> implements WhereCondition {

	private final SingularAttribute<E, String> attribute;
	private final String likeString;
	private Path<E> parentPath;

	public LikeCondition(SingularAttribute<E, String> attribute, String likeString) {
		this.attribute = attribute;
		this.likeString = likeString;
	}

	public LikeCondition(SingularAttribute<E, String> attribute, String likeString, Path<E> parentPath) {
		this.attribute = attribute;
		this.likeString = likeString;
		this.parentPath = parentPath;
	}

	@Override
	public Predicate buildPredicate(CriteriaBuilder builder) {
		return builder.like(parentPath.get(attribute), likeString);
	}

	@Override
	public String toString() {

		return parentPath.toString() + "." + attribute.getName() + " like " + likeString;
	}
}
