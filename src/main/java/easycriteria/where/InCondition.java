package easycriteria.where;

import java.util.Collection;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.metamodel.SingularAttribute;

public class InCondition<E, A> implements WhereCondition {

    private final SingularAttribute<E, A> attribute;
    private final Collection<A> args;
    private Path<E> parentPath;

    public InCondition(SingularAttribute<E, A> attribute, Collection<A> args, Path<E> parentPath) {
        this.attribute = attribute;
        this.args = args;
        this.parentPath = parentPath;
    }
    
	@Override
    public Predicate buildPredicate(CriteriaBuilder builder) {
        
        return parentPath.get(attribute).in(args);
    }
    
    @Override
    public String toString() {
        
        return parentPath.toString() + "." + attribute.getName() + " in " + args;
    }
}
