package easycriteria.where;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.metamodel.SingularAttribute;

import easycriteria.WhereConditionBuilder;
import easycriteria.WhereConditionsContainer;
import easycriteria.WhereTransformer;

public class CompoundAndCondition<E, S, B extends WhereConditionsContainer> implements WhereCondition, WhereConditionsContainer {
    
    private final List<WhereCondition> whereClauses;
    private final WhereTransformer whereTransformer;
    private final B queryBuilder;
    private Path<E> parentPath;
    
    public CompoundAndCondition(B queryBuilder, WhereTransformer whereTransformer, Path<E> parentPath) {
    	this.whereClauses = new ArrayList<>();
        this.queryBuilder = queryBuilder;
        this.whereTransformer = whereTransformer;
        this.parentPath = parentPath;
    }
    
    public <A> WhereConditionBuilder<E, A, S, CompoundAndCondition<E, S, B>> where(SingularAttribute<E, A> attribute) {        
        return new WhereConditionBuilder<E, A, S, CompoundAndCondition<E, S, B>>(this, attribute, parentPath);
    }
    
    public CompoundOrCondition<E, S, CompoundAndCondition<E, S, B> > whereOr() {
    	return new CompoundOrCondition<E, S, CompoundAndCondition<E, S, B>>(this, whereTransformer, parentPath);
    }
    
    public B endAnd(){
    	queryBuilder.addWhereClause(this);
    	return queryBuilder;
    }

	public void addWhereClause(WhereCondition whereClause) {
		whereClauses.add(whereClause);
	}

	@Override
	public Predicate buildPredicate(CriteriaBuilder cb) {
		Predicate[] constraints = whereTransformer.transform(whereClauses);
		return cb.and(constraints);		
	}
}
