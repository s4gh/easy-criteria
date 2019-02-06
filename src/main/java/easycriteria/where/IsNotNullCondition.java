package easycriteria.where;

import java.util.Map;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;

import easycriteria.meta.EntityPathNode;

public class IsNotNullCondition extends WhereCondition {


	public IsNotNullCondition(String attribute) {
		this.attribute = attribute;
	}
	
	public IsNotNullCondition(String attribute, EntityPathNode parentAttribute) {
		this.attribute = attribute;
		this.parentAttribute = parentAttribute;
	}

	@SuppressWarnings("rawtypes")
	@Override
	public Predicate buildJPAPredicate(CriteriaBuilder builder, Path path, Map<String, Path> queryParts) {
		return builder.isNotNull(path.get(attribute));
	}

	@Override
	public String toString() {

		return parentAttribute.toString() + "." + attribute + " is not null ";
	}
}
