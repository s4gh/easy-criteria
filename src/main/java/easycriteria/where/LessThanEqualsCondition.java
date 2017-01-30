package easycriteria.where;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.metamodel.SingularAttribute;

public class LessThanEqualsCondition<E, A> implements WhereCondition{
    
    @SuppressWarnings("rawtypes")
	private final SingularAttribute attribute;
    private final A value;
    private final Path<E> parentPath;

    public LessThanEqualsCondition(SingularAttribute<E, A> attribute, A value, Path<E> parentPath) {
        this.attribute = attribute;
        this.value = value;
        this.parentPath = parentPath;
    }

    @Override
    @SuppressWarnings({ "unchecked", "rawtypes" })	
    public Predicate buildPredicate(CriteriaBuilder builder) {        
        return builder.lessThanOrEqualTo(parentPath.get(attribute), (Comparable)value);
    }
    
    @Override
    public String toString() {
        
        return parentPath.toString() + "." + attribute.getName() + " <= " + value;
    }
}
