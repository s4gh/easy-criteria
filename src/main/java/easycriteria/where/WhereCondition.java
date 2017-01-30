package easycriteria.where;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Predicate;

public interface WhereCondition {
    
    Predicate buildPredicate(CriteriaBuilder builder);
}
