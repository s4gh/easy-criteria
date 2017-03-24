package easycriteria.where;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;

import easycriteria.meta.EntityPathNode;

public class LikeCondition<E> extends WhereCondition {

	private final String likeString;

	@SuppressWarnings("rawtypes")
	public <T> LikeCondition(String attribute, String likeString, Path parentPath,
			EntityPathNode parentAttribute) {
		super();
		this.attribute = attribute;
		this.likeString = likeString;
		this.parentPath = parentPath;
		this.parentAttribute = parentAttribute;
	}

	@Override
	public String toString() {
		return parentPath.toString() + "." + attribute + " like " + likeString;
	}

	@Override
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public Predicate buildJPAPredicate(CriteriaBuilder builder, Path path) {
		
		return builder.like(path.get(attribute), likeString);
	}
}
