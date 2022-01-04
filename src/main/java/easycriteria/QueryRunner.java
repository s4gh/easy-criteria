package easycriteria;

import java.util.List;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;

public class QueryRunner {

	private final EntityManager entityManager;

	public QueryRunner(EntityManager entityManager) {
		this.entityManager = entityManager;
	}

	public <E, S> List<S> getResultList(EasyCriteriaQuery<E, S> easyQuery) {

		TypedQuery<S> query = entityManager.createQuery(easyQuery.getCriteriaQuery());

		if (easyQuery.getRowCount() > 0) {
			query.setMaxResults(easyQuery.getRowCount());
		}
		if (easyQuery.getOffset() > 0) {
			query.setFirstResult(easyQuery.getOffset());
		}

		return query.getResultList();
	}

	public <E, S> S getSingleResult(EasyCriteriaQuery<E, S> easyQuery) {

		return entityManager.createQuery(easyQuery.getCriteriaQuery()).getSingleResult();
	}
}
