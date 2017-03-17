package easycriteria;

public class OrderByBuilder<E, A, S> {

	private final EasyCriteriaQuery<E, S> queryBuilder;
	private final String attribute;

	public OrderByBuilder(EasyCriteriaQuery<E, S> queryBuilder, String attribute) {
		this.queryBuilder = queryBuilder;
		this.attribute = attribute;
	}

	public EasyCriteriaQuery<E, S> asc() {

		queryBuilder.addOrderBy(new OrderBy<E, A>(attribute, true));

		return queryBuilder;
	}

	public EasyCriteriaQuery<E, S> desc() {

		queryBuilder.addOrderBy(new OrderBy<E, A>(attribute, false));

		return queryBuilder;
	}
}
