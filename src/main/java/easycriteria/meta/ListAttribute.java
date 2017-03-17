package easycriteria.meta;

/**
 * 
 * @param <X> The type containing the represented attribute
 * @param <T> The type of the represented attribute
 */
public class ListAttribute<X, T> extends ObjectAttribute<T> {
	
	public ListAttribute(String attribute, EntityPathNode parentPath) {
		super(attribute, parentPath);
	}
}