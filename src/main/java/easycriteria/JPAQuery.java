package easycriteria;

import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;

import easycriteria.meta.ObjectAttribute;
import easycriteria.meta.PropertyAttribute;

public class JPAQuery {

	private final EntityManager entityManager;

	public JPAQuery(EntityManager entityManager) {
		this.entityManager = entityManager;
	}	

	public <S> EasyCriteriaQuery<S, S> select(Class<S> entityClass) {

		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();

		CriteriaQuery<S> criteriaQuery = criteriaBuilder.createQuery(entityClass);		

		Root<S> root = criteriaQuery.from(entityClass);

		criteriaQuery.select(root);

		return createEasyQuery(criteriaQuery, entityManager, criteriaBuilder, root);
	}
	
	public <S> EasyCriteriaQuery<S, S> select(ObjectAttribute<S> objectAttribute) {
		return select(objectAttribute.getEntityType());
	}

	@SuppressWarnings("unchecked")
	public <X, T> EasyCriteriaQuery<X, T> select(PropertyAttribute<X, T> selectAttribute) {

		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();

		CriteriaQuery<T> criteriaQuery = criteriaBuilder.createQuery(selectAttribute.getPropertyType());

		Root<X> root = (Root<X>) criteriaQuery.from(selectAttribute.getEntityType());

		criteriaQuery.select(root.get(selectAttribute.getAttribute()));

		return createEasyQuery(criteriaQuery, entityManager, criteriaBuilder, root);
	}

	public <E> EasyCriteriaQuery<E, Long> count(Class<E> entityClass) {

		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();

		CriteriaQuery<Long> criteriaQuery = criteriaBuilder.createQuery(Long.class);

		Root<E> root = criteriaQuery.from(entityClass);

		criteriaQuery.select(criteriaBuilder.count(root));

		return createEasyQuery(criteriaQuery, entityManager, criteriaBuilder, root);
	}

	private <E, S> EasyCriteriaQuery<E, S> createEasyQuery(CriteriaQuery<S> criteriaQuery, EntityManager entityManager,
			CriteriaBuilder criteriaBuilder, Root<E> root) {

		return new EasyCriteriaQuery<E, S>(criteriaQuery, new QueryRunner(entityManager),
				new WhereTransformer(criteriaBuilder), new OrderByTransformer<E>(criteriaBuilder, root), root);
	}
}
