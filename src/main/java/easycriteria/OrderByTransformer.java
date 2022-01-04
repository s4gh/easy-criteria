package easycriteria;

import java.util.List;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Order;
import jakarta.persistence.criteria.Root;

public class OrderByTransformer<E> {

	private final CriteriaBuilder criteriaBuilder;
	private final Root<E> root;

	public OrderByTransformer(CriteriaBuilder criteriaBuilder, Root<E> root) {
		this.criteriaBuilder = criteriaBuilder;
		this.root = root;
	}

	public Order[] transform(List<OrderBy<E, ?>> orderBys) {

		return orderBys.stream().map(orderBy -> orderBy.buildOrder(criteriaBuilder, root))
				.toArray(size -> new Order[size]);
	}
}
