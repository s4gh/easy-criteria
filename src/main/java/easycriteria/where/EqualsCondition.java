package easycriteria.where;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;

import easycriteria.meta.EntityPathNode;

public class EqualsCondition<A> extends WhereCondition {

	private final A value;

	public EqualsCondition(String attribute, A object) {
		this.attribute = attribute;
		this.value = object;
	}
//
//	public EqualsCondition(String attribute, A object, Path parentPath) {
//		this.attribute = attribute;
//		this.value = object;
//		this.parentPath = parentPath;
//	}
	
	public EqualsCondition(String attribute, A object, EntityPathNode parentAttribute) {
		this.attribute = attribute;
		this.value = object;
		this.parentAttribute = parentAttribute;
	}

	@Override
	protected Predicate buildJPAPredicate(CriteriaBuilder builder, Path path) {
		return builder.equal(path.get(attribute), value);
	}

	@Override
	public String toString() {

		return parentPath.toString() + "." + attribute + " == " + value;
	}
}
