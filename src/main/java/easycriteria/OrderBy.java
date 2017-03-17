package easycriteria;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Order;
import javax.persistence.criteria.Root;

public class OrderBy<E, A> {

	private final String attribute;
	private boolean orderAscending = false;

	public OrderBy(String attribute, boolean orderAscending) {
		this.attribute = attribute;
		this.orderAscending = orderAscending;
	}

	public Order buildOrder(CriteriaBuilder criteriaBuilder, Root<E> from) {

		return orderAscending ? criteriaBuilder.asc(from.get(attribute)) : criteriaBuilder.desc(from.get(attribute));
	}

}
