package easycriteria;

import static org.junit.Assert.assertEquals;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.criteria.JoinType;

import org.junit.Before;
import org.junit.Test;

public class TreeStructureTest {

	private EntityManager entityManager;

	@Before
	public void setup() {
		EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("easycriteria");

		entityManager = entityManagerFactory.createEntityManager();
	}

//	@Test
	public void testMultipleJoins() {
		setupTestTreeNodesData();
		QTreeNode_ root = new QTreeNode_();
		QTreeNode_ child1 = new QTreeNode_("child1", root);
		QTreeNode_ child2 = new QTreeNode_("child2", child1);

		List<TreeNode> nodes = new JPAQuery(entityManager).select(TreeNode.class)
				.join(child2.parent, JoinType.LEFT, child1)
				.join(child1.parent, JoinType.INNER, root)
				.where(root.name.eq("root2"))
				.getResultList();
		
		assertEquals("child22", nodes.get(0).getName());
	}
	
	@Test
	public void testMultipleJoinsWith2Levels() {
		setupTestLevelsData();
		
		QLevel1_ level11 = new QLevel1_();
		QLevel2_ level12 = new QLevel2_();
		QLevel1_ level13 = new QLevel1_("parent", level12);

		List<Level1> nodes = new JPAQuery(entityManager).select(Level1.class)
				.join(level11.level2, JoinType.LEFT, level12)
				.join(level12.parent, JoinType.INNER, level13)
				.where(level13.name.eq("level13"))
				.getResultList();
		
		assertEquals("level11", nodes.get(0).getName());
	}
	
	private void setupTestLevelsData() {
		beginTx();

		Level1 level11 = new Level1();
		level11.setName("level11");
		
		Level2 level12 = new Level2();
		level12.setName("level12");		
		
		Level1 level13 = new Level1();
		level13.setName("level13");
		
		level11.setLevel2(level12);
		level12.setParent(level13);
		
		entityManager.persist(level13);
		entityManager.persist(level12);
		entityManager.persist(level11);
		
		
		Level1 level21 = new Level1();
		level21.setName("level21");
		
		Level2 level22 = new Level2();
		level22.setName("level22");		
		
		Level1 level23 = new Level1();
		level23.setName("level23");
		
		level21.setLevel2(level22);
		level22.setParent(level23);
		
		entityManager.persist(level23);
		entityManager.persist(level22);
		entityManager.persist(level21);		

		commitTx();
	}

	private void setupTestTreeNodesData() {
		beginTx();

		TreeNode root1 = new TreeNode();
		root1.setName("root1");
		entityManager.persist(root1);
		
		TreeNode child11 = new TreeNode();
		child11.setName("child11");
		child11.setParent(root1);
		entityManager.persist(child11);
		
		TreeNode child12 = new TreeNode();
		child12.setName("child12");
		child12.setParent(child11);
		entityManager.persist(child12);
		
		
		TreeNode root2 = new TreeNode();
		root2.setName("root2");
		entityManager.persist(root2);
		
		TreeNode child21 = new TreeNode();
		child21.setName("child21");
		child21.setParent(root2);
		entityManager.persist(child21);
		
		TreeNode child22 = new TreeNode();
		child22.setName("child22");
		child22.setParent(child21);
		entityManager.persist(child22);

		commitTx();
	}

	private void beginTx() {
		entityManager.getTransaction().begin();
	}

	private void commitTx() {
		entityManager.getTransaction().commit();
	}
}