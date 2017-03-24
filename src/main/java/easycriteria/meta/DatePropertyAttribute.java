package easycriteria.meta;

public class DatePropertyAttribute<X, T> extends NumberPropertyAttribute<X, T>{

	public DatePropertyAttribute(String attribute) {
		super(attribute);
	}

	public DatePropertyAttribute(String attribute, EntityPathNode parentPath) {
		super(attribute, parentPath);
	}
}
