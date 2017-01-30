package easycriteria.where;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.metamodel.SingularAttribute;

public class EqualsCondition<E, A> implements WhereCondition{
    
    private final SingularAttribute<E, A> attribute;
    private final A value;
    private Path<E> parentPath;

    public EqualsCondition(SingularAttribute<E, A> attribute, A object) {
        this.attribute = attribute;
        this.value = object;
    }
    
    public EqualsCondition(SingularAttribute<E, A> attribute, A object, Path<E> parentPath) {
        this.attribute = attribute;
        this.value = object;
        this.parentPath = parentPath;
    }

    @Override
    public Predicate buildPredicate(CriteriaBuilder builder) {        
        return builder.equal(parentPath.get(attribute), value);
    }
    
    @Override
    public String toString() {
        
        return parentPath.toString() + "." + attribute.getName() + " == " + value;
    }
}
