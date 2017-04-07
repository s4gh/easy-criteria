package easycriteria.meta;

import easycriteria.where.IsNotNullCondition;
import easycriteria.where.IsNullCondition;
import easycriteria.where.WhereCondition;

/**
 * 
 * @param <X> The type containing the represented attribute
 * @param <K> The type of the key
 * @param <V> The type of the value
 */
public class MapAttribute<X, K, V> implements EntityPathNode {
	
	private String attribute;
	private EntityPathNode parentPath;
	private Class<X> entityType;
	private Class<K> keyType;
	private Class<V> valueType;
	
	public MapAttribute(String attribute, EntityPathNode parentPath, Class<X> entityType, Class<K> keyType, Class<V> valueType) {
		this.attribute = attribute;		
		this.parentPath = parentPath;
		this.entityType = entityType;
		this.keyType = keyType;
		this.valueType = valueType;
	}

	public WhereCondition isNull() {
		return new IsNullCondition(attribute, parentPath);
	}
	
	public WhereCondition isNotNull() {
		return new IsNotNullCondition(attribute, parentPath);
	}

	public String getAttribute() {
		return attribute;
	}

	@Override
	public EntityPathNode getParent() {
		return parentPath;
	}

	@Override
	public void setParent(EntityPathNode parentPath) {
		this.parentPath = parentPath;
	}

	@Override
	public Class<X> getEntityType() {
		return entityType;
	}

	public Class<K> getKeyType() {
		return keyType;
	}

	public Class<V> getValueType() {
		return valueType;
	}
}