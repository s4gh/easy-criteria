package easycriteria.meta;

public class DatePropertyAttribute<X, T> extends NumberPropertyAttribute<X, T>{

	public DatePropertyAttribute(String attribute, Class<T> entityType) {
		super(attribute, entityType);
	}

	public DatePropertyAttribute(String attribute, EntityPathNode parentPath, Class<T> entityType) {
		super(attribute, parentPath, entityType);
	}
}
