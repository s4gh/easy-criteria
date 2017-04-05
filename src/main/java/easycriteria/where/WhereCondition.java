package easycriteria.where;

import java.util.LinkedList;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;

import easycriteria.meta.EntityPathNode;

public abstract class WhereCondition {
	
	protected String attribute;
	@SuppressWarnings("rawtypes")
	protected Path parentPath;
	protected EntityPathNode parentAttribute;
	
	public WhereCondition and(WhereCondition otherCondition) {
		return new AndCondition(this, otherCondition);
	}
	
	public WhereCondition or(WhereCondition otherCondition) {
		return new OrCondition(this, otherCondition);
	}

	protected abstract Predicate buildJPAPredicate(CriteriaBuilder builder, @SuppressWarnings("rawtypes") Path root);
	
	@SuppressWarnings("rawtypes")
	public Predicate buildPredicate(CriteriaBuilder builder, Path root){
		if (parentPath == null) {
			parentPath = root;
		}

		if (parentAttribute != null && parentAttribute.getParent() != null) {
			parentPath = buildParentPath(root, parentAttribute);
		}
		
		return buildJPAPredicate(builder , parentPath);
	}
	
	@SuppressWarnings("rawtypes")
	private Path buildParentPath(Path parentPath, EntityPathNode parentAttribute) {
				
		LinkedList<EntityPathNode> hierarchy = new LinkedList<>();
		reversePathHierarchy(hierarchy, parentAttribute);
		for (EntityPathNode hierarchyNode : hierarchy) {
			if (hierarchyNode.getAttribute() != null) {
				parentPath = parentPath.get(hierarchyNode.getAttribute());
			}
		}
	
		return parentPath;
	}
	
	private void reversePathHierarchy(LinkedList<EntityPathNode> hierarchy, EntityPathNode leafNode) {
		if (leafNode != null) {
			if (leafNode.getParent() == null) {
				return;
			} else {
				hierarchy.addFirst(leafNode);
				reversePathHierarchy(hierarchy, leafNode.getParent());
			} 
		}
	}
	
	
	public String getAttribute() {
		return attribute;
	}

	public void setAttribute(String attribute) {
		this.attribute = attribute;
	}

	@SuppressWarnings("rawtypes")
	public Path getParentPath() {
		return parentPath;
	}

	@SuppressWarnings("rawtypes")
	public void setParentPath(Path parentPath) {
		this.parentPath = parentPath;
	}

	public EntityPathNode getParentAttribute() {
		return parentAttribute;
	}

	public void setParentAttribute(EntityPathNode parentAttribute) {
		this.parentAttribute = parentAttribute;
	}	
}
