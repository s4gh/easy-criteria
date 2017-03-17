package easycriteria.where;

import java.util.LinkedList;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;

import easycriteria.meta.EntityPathNode;

public abstract class WhereCondition {
	
	protected String attribute;
	protected Path parentPath;
	protected EntityPathNode parentAttribute;
	
	public WhereCondition and(WhereCondition otherCondition) {
		return new AndCondition(this, otherCondition);
	}
	
	public WhereCondition or(WhereCondition otherCondition) {
		return new OrCondition(this, otherCondition);
	}

	protected abstract Predicate buildJPAPredicate(CriteriaBuilder builder, Path root);
	
	public Predicate buildPredicate(CriteriaBuilder builder, Path root){
		if (parentPath == null) {
			parentPath = root;
		}

		if (parentAttribute != null) {
			parentPath = buildParentPath(root, parentAttribute);
		}
		
		return buildJPAPredicate(builder , parentPath);
	}
	
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
		if (leafNode.getParent() == null) {
			return;
		} else {
			hierarchy.addFirst(leafNode);
			reversePathHierarchy(hierarchy, leafNode.getParent());
		}
	}
	
	
	public String getAttribute() {
		return attribute;
	}

	public void setAttribute(String attribute) {
		this.attribute = attribute;
	}

	public Path getParentPath() {
		return parentPath;
	}

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
