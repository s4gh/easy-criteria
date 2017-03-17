package easycriteria.where;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;

import easycriteria.meta.EntityPathNode;

public class IsNullCondition extends WhereCondition {


	public IsNullCondition(String attribute) {
		this.attribute = attribute;
	}
	
	public IsNullCondition(String attribute, EntityPathNode parentAttribute) {
		this.attribute = attribute;
		this.parentAttribute = parentAttribute;
	}

	@Override
	public Predicate buildJPAPredicate(CriteriaBuilder builder, Path path) {
		return builder.isNull(path.get(attribute));
	}

	@Override
	public String toString() {

		return parentAttribute.toString() + "." + attribute + " is null ";
	}
}
