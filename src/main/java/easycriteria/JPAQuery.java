package easycriteria;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import javax.persistence.metamodel.SingularAttribute;

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

	public <E, S> EasyCriteriaQuery<E, S> select(SingularAttribute<E, S> selectAttribute) {

		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();

		CriteriaQuery<S> criteriaQuery = criteriaBuilder.createQuery(selectAttribute.getJavaType());

		Root<E> root = criteriaQuery.from(selectAttribute.getDeclaringType().getJavaType());

		criteriaQuery.select(root.get(selectAttribute));

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
