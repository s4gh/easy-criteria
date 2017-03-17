package easycriteria.where;

import java.util.Collection;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;

import easycriteria.meta.EntityPathNode;

public class NotInCondition<A> extends WhereCondition {

	private final Collection<A> args;

	public NotInCondition(String attribute, Collection<A> args, EntityPathNode parentAttribute) {
		this.attribute = attribute;
		this.args = args;
		this.parentAttribute = parentAttribute;
	}

	@Override
	public Predicate buildJPAPredicate(CriteriaBuilder builder, Path path) {

		return builder.not(path.get(attribute).in(args));
	}

	@Override
	public String toString() {

		return parentPath.toString() + "." + attribute + " in " + args;
	}
}
