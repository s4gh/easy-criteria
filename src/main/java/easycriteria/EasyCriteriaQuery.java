package easycriteria;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Root;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;

import easycriteria.where.CompoundAndCondition;
import easycriteria.where.CompoundOrCondition;
import easycriteria.where.WhereCondition;

public class EasyCriteriaQuery<E,S> implements WhereConditionsContainer {

    private final List<WhereCondition> whereClauses;
	private final List<OrderBy<E, ?>> orderClauses;
    private final CriteriaQuery<S> criteriaQuery;
    protected final QueryRunner queryRunner;
    private final WhereTransformer whereTransformer;
    private final OrderByTransformer<E> orderByTransformer;
    private final Root<E> root;
	private int offset;
	private int rowCount;
    
    public EasyCriteriaQuery(CriteriaQuery<S> criteriaQuery, QueryRunner queryRunner, WhereTransformer whereTransformer,
                            OrderByTransformer<E> orderByTransformer, Root<E> root) {
        this.whereClauses = new ArrayList<>();
        this.orderClauses = new ArrayList<>();
        this.criteriaQuery = criteriaQuery;
        this.queryRunner = queryRunner;
        this.whereTransformer = whereTransformer;
        this.orderByTransformer = orderByTransformer;
        this.root = root;
    }
    
    public CompoundOrCondition<E, S, EasyCriteriaQuery<E, S>> whereOr() {
        
        return new CompoundOrCondition<E, S, EasyCriteriaQuery<E, S>>(this, whereTransformer, root);
    }
    
    public CompoundAndCondition<E, S, EasyCriteriaQuery<E, S>> whereAnd() {
        
        return new CompoundAndCondition<E, S, EasyCriteriaQuery<E, S>>(this, whereTransformer, root);
    }    
    
    public <A> WhereConditionBuilder<E, A, S, EasyCriteriaQuery<E, S>> where(SingularAttribute<E, A> attribute) {
    	return new WhereConditionBuilder<E, A, S, EasyCriteriaQuery<E, S>>(this, attribute, root);
    }

    public <A> OrderByBuilder<E, A, S> orderBy(SingularAttribute<E, A> attribute) {
        
        return new OrderByBuilder<E, A, S>(this, attribute);
    }
    
    public <A> JoinBuilder<E, A, S, EasyCriteriaQuery<E, S>> join(SingularAttribute<E, A> attribute, JoinType joinType) {
        
        return new JoinBuilder<E, A, S, EasyCriteriaQuery<E, S>>(attribute, this, whereTransformer, joinType, root);
    }
    
    public <A> JoinBuilder<E, A, S, EasyCriteriaQuery<E, S>> join(ListAttribute<E, A> attribute, JoinType joinType) {
        
        return new JoinBuilder<E, A, S, EasyCriteriaQuery<E, S>>(attribute, this, whereTransformer, joinType, root);
    }

    public void addWhereClause(WhereCondition whereClause) {
        
        whereClauses.add(whereClause);
    }

    void addOrderBy(OrderBy<E, ?> orderBy) {
        
        orderClauses.add(orderBy);
    }
   
	private List<OrderBy<E, ?>> getOrderClauses() {
        
        return orderClauses;
    }

    private List<WhereCondition> getWhereClauses() {
        
        return whereClauses;
    }

    public List<S> getResultList() {
        
        return queryRunner.getResultList(this);
    }
    
    public S getSingleResult() {
        return queryRunner.getSingleResult(this);
    }

    protected CriteriaQuery<S> getCriteriaQuery() {
        return criteriaQuery.where(whereTransformer.transform(getWhereClauses())).orderBy(orderByTransformer.transform(getOrderClauses()));
    }    
    
    public Root<E> getRoot(){
    	return root;
    }
    
    public EasyCriteriaQuery<E,S> distinct(){
    	criteriaQuery.distinct(true);
    	return this;
    }
    
    public EasyCriteriaQuery<E,S> limit(int rowCount){
    	this.rowCount = rowCount;
    	return this;
    }
    
    public EasyCriteriaQuery<E,S> limit(int offset, int rowCount){
    	this.offset = offset;
    	this.rowCount = rowCount;
    	return this;
    }    
    
    protected int getOffset() {
		return offset;
	}

	protected int getRowCount() {
		return rowCount;
	}

	@Override
    public String toString() {
        
        return "FROM " + root.getJavaType() + "WHERE " + getWhereClauses() + " ORDER BY " + getOrderClauses() + " LIMIT " + offset + "," + rowCount;
    }
}