package easycriteria;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.From;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Root;
import javax.persistence.criteria.Subquery;

import easycriteria.meta.ObjectAttribute;
import easycriteria.meta.PropertyAttribute;
import easycriteria.where.WhereCondition;

public class EasyCriteriaQuery<E, S> implements WhereConditionsContainer {

	private final List<WhereCondition> whereClauses;
	private final List<OrderBy<E, ?>> orderClauses;
	private final CriteriaQuery<S> criteriaQuery;
	protected final QueryRunner queryRunner;
	private final WhereTransformer whereTransformer;
	private final OrderByTransformer<E> orderByTransformer;
	private final Root<E> root;
	private int offset;
	private int rowCount;
	Map<String, Path> queryParts = new HashMap<>();

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
	
	public EasyCriteriaQuery<E, S> where(WhereCondition whereCondition){
		addWhereCondition(whereCondition);
		return this;
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public EasyCriteriaQuery<E, S> orderBy(OrderBy... orderBys) {

		for (int i = 0; i < orderBys.length; i++) {
			this.addOrderBy(orderBys[i]);
		}
		return this;
	}
	
	public EasyCriteriaQuery<E, S>  join(ObjectAttribute attribute, JoinType joinType, ObjectAttribute alias) {
		Join join;
		if (queryParts.containsKey(attribute.getParent().getAttribute())) {
			Path path = queryParts.get(attribute.getParent().getAttribute());
			join = ((From) path).join(attribute.getAttribute(), joinType);
		} else {
			join = ((From) root).join(attribute.getAttribute(), joinType);
		}
		queryParts.put(alias.getAttribute(),join);
		return this;
	}

	public void addWhereCondition(WhereCondition whereClause) {

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
		return criteriaQuery.where(whereTransformer.transform(getWhereClauses(), getRoot(), queryParts))
				.orderBy(orderByTransformer.transform(getOrderClauses()));
	}

	public Root<E> getRoot() {
		return root;
	}

	public EasyCriteriaQuery<E, S> distinct() {
		criteriaQuery.distinct(true);
		return this;
	}

	public EasyCriteriaQuery<E, S> limit(int rowCount) {
		this.rowCount = rowCount;
		return this;
	}

	public EasyCriteriaQuery<E, S> limit(int offset, int rowCount) {
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
	
	@SuppressWarnings("unchecked")
	public <T> EasyCriteriaSubquery<E, T> subquerySelect(PropertyAttribute<E, T> selectAttribute) {

		Subquery<T> criteriaSubQuery = criteriaQuery.subquery(selectAttribute.getPropertyType());		

		Root<E> root = (Root<E>) criteriaSubQuery.from(selectAttribute.getEntityType());

		criteriaSubQuery.select(root.get(selectAttribute.getAttribute()));

		return new EasyCriteriaSubquery<>(criteriaSubQuery, whereTransformer, root);
	}
	
	public <T> EasyCriteriaSubquery<T, T> subquerySelect(Class<T> entityClass) {

		Subquery<T> criteriaSubQuery = criteriaQuery.subquery(entityClass);
		
		Root<T> root = criteriaSubQuery.from(entityClass);
		
		criteriaSubQuery.select(root);

		return new EasyCriteriaSubquery<>(criteriaSubQuery, whereTransformer, root);
	}

	@Override
	public String toString() {

		return "FROM " + root.getJavaType() + "WHERE " + getWhereClauses() + " ORDER BY " + getOrderClauses()
				+ " LIMIT " + offset + "," + rowCount;
	}
}