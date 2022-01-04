package easycriteria.where;

import java.util.Map;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Path;
import jakarta.persistence.criteria.Predicate;

import easycriteria.meta.EntityPathNode;

public class IsNullCondition extends WhereCondition {


	public IsNullCondition(String attribute) {
		this.attribute = attribute;
	}
	
	public IsNullCondition(String attribute, EntityPathNode parentAttribute) {
		this.attribute = attribute;
		this.parentAttribute = parentAttribute;
	}

	@SuppressWarnings("rawtypes")
	@Override
	public Predicate buildJPAPredicate(CriteriaBuilder builder, Path path, Map<String, Path> queryParts) {
		return builder.isNull(path.get(attribute));
	}

	@Override
	public String toString() {

		return parentAttribute.toString() + "." + attribute + " is null ";
	}
}
