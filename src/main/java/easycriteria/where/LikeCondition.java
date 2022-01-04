package easycriteria.where;

import java.util.Map;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Path;
import jakarta.persistence.criteria.Predicate;

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
	public Predicate buildJPAPredicate(CriteriaBuilder builder, Path path, Map<String, Path> queryParts) {
		
		return builder.like(path.get(attribute), likeString);
	}
}
