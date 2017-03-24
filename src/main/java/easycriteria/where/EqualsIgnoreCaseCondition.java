package easycriteria.where;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;

import easycriteria.meta.EntityPathNode;

public class EqualsIgnoreCaseCondition extends WhereCondition {

	private final String value;

	public <T> EqualsIgnoreCaseCondition(String attribute, String object) {
		this.attribute = attribute;
		this.value = object;
	}

	public <T> EqualsIgnoreCaseCondition(String attribute, String object, EntityPathNode parentAttribute) {
		this.attribute = attribute;
		this.value = object;
		this.parentAttribute = parentAttribute;
	}

	@Override
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public Predicate buildJPAPredicate(CriteriaBuilder builder, Path path) {
		return builder.equal(builder.upper(path.get(attribute)), value.toUpperCase());
	}

	@Override
	public String toString() {

		return parentPath.toString() + "." + attribute + " == " + value;
	}
}
