package oop.ex5.data_structures;

import java.util.Iterator;
import java.util.LinkedList;

/**
 * This class is implementing an AVL Tree with his related methods.
 * 
 * @author mutazmanaa
 * 
 */
public class AvlTree {
	private final static int MAXDIFFERENCE = 1;
	private final static int NOTFOUND = -1;
	private final static int LNRS = 1;
	private final static int RNLS = 2;
	private final static int LNLS = 3;
	private final static int RNRS = 4;
	private Node node,root;
	private Iterator<Integer> itr;

	/**
	 * A default constructor
	 */
	public AvlTree() {
		this.node = null;

	}

	/**
	 * A data constructor- a constructor that builds the tree by adding the
	 * element in the input array one-by-on if the same value appears twice (or
	 * more) in the list it is ignored.
	 * 
	 * @param data
	 *            data values to add to tree
	 */
	public AvlTree(int[] data) {
		for (int i = 0; i < data.length; i++) {
			add(data[i]);
		}
	}

	/**
	 * A copy constructor a constructor that builds the tree a copy of an
	 * existing tree.
	 * 
	 * @param tree
	 *            an AvlTree
	 */
	public AvlTree(AvlTree tree) {
		LinkedList<Integer> array = new LinkedList<Integer>();
        this.root = tree.getNode();
		copyMethod(array, this.node);
		itr = array.iterator();
		while (itr.hasNext()) {
			add(itr.next());
		}
		

	}

	// ------------------------Methods----------------------
	/**
	 * Add a new node with key newValue into the tree.
	 * 
	 * @param newValue
	 *            newValue new value to add to the tree.
	 * @return false iff newValue already exist in the tree
	 * 
	 */

	public boolean add(int newValue) {

		if (this.contains(newValue) != NOTFOUND)
			return false;
		if (this.node == null) {
			this.node = new Node(newValue, null, null, null);
			return true;
		}
		balancedAdd(new Node(newValue, null, null, null), this.node);

		return true;

	}

	/**
	 * Does tree contain a given input value
	 * 
	 * @param search
	 *            val value to search for
	 * @return if val is found in the tree, return the depth of its node (where
	 *         0 is the root) Otherwise-return -1.
	 */

	public int contains(int searchVal) {
		if (this.node == null) {
			return NOTFOUND;
		}
		int nodeDepth = 0;
		Node tempNode = this.node;
		while (tempNode != null) {
			if (tempNode.getKey() == searchVal) {
				return nodeDepth;
			}
			if (searchVal > tempNode.getKey()) {
				tempNode = tempNode.getRightNode();
			} else {
				tempNode = tempNode.getLeftNode();
			}
			nodeDepth++;
		}
		return NOTFOUND;
	}

	/**
	 * Remove a node from the tree, if it exists.
	 * 
	 * @param toDelete
	 *            value to delete
	 * @return true iff toDelete is found and deleted
	 */

	public boolean delete(int toDelete) {
		if (contains(toDelete) == NOTFOUND) {
			return false;
		}

		return balancedDelete(search(toDelete));

	}

	/**
	 * @return number of nodes in the tree
	 */

	public int size() {

		if (this.node == null) {
			return 0;
		}
		return recursiveSize(this.node) + 1;

	}

	/**
	 * get the root of the tree
	 * 
	 * @return the root of the tree
	 */
	public Node getNode() {
		return this.node;
	}

	/**
	 * @return iterator to the Avl Tree. The returned iterator can pass over the
	 *         tree nodes in asceng order.
	 */
	public Iterator<Integer> iterator() {
		return new AvlTreeIterator();
	}

	// ------------------nested class for iterator--------
	/**
	 * * a class which represent an iterator to our tree nodes
	 * 
	 * @author mutazmanaa
	 * 
	 */
	public class AvlTreeIterator implements Iterator<Integer> {
		Node item = treeMinimum(root);
		Node current;

		public AvlTreeIterator() {
		}

		// --------nested class functions------------------
		/**
		 * check the next element in the tree by successor
		 * 
		 * @return true if their is another element
		 */
		public boolean hasNext() {
			if (item != null) {
				return true;
			}
			return false;
		}

		/**
		 * return the next element itarator have passed.
		 * 
		 * @return the next element itarator have passed.
		 */
		public Integer next() {
			Node result = successor(item);
			current = item;
			item = result;
			return current.getKey();

		}

		/**
		 * remove the last element that the itarator pass over
		 */
		public void remove() {
			delete(node.getKey());
		}

	}
	
	// ------------------------------end of nested class-------------------

	// ---------------------------Static
	// Method-----------------------------------------------------------------

	/**
	 * This method calculates the minimum number of nodes in an AVL tree of
	 * heigh h,
	 * 
	 * @param h
	 *            height of the tree (a non-negative number).
	 * @return minimum number of nodes in the tree
	 */
	public static int findMinNodes(int h) {
		if (h == 0 || h == 1) {
			return 1;
		}
		return (findMinNodes(h - 1) + findMinNodes(h - 2) + 1);
	}

	// ----------------------- Help
	// Methods-----------------------------------------------------------------------------------
	/**
	 * 
	 * this function is a helper function for add
	 * 
	 * @param toAdd
	 *            value to add to the tree
	 * @param node
	 *            the root of the tree
	 */
	private void balancedAdd(Node toAdd, Node node) {
		Node tempNode1 = null;
		Node tempNode2 = node;
		while (tempNode2 != null) {
			tempNode1 = tempNode2;
			if (toAdd.getKey() <= tempNode2.getKey()) {
				tempNode2 = tempNode2.getLeftNode();
			} else {
				tempNode2 = tempNode2.getRightNode();
			}
		}
		if (toAdd.getKey() <= tempNode1.getKey()) {
			tempNode1.setLeftNode(toAdd);
		} else {
			tempNode1.setRightNode(toAdd);
		}
		toAdd.setParent(tempNode1);
		checkBalance(tempNode1);
	}

	/**
	 * search a value in the tree
	 * 
	 * @param toFind
	 *            value to search
	 * @return the node that has the value toFind,,else return null
	 */
	private Node search(int toFind) {
		Node tempTree = this.node;
		while (tempTree != null) {
			if (tempTree.getKey() == toFind) {
				return tempTree;
			}
			if (toFind > tempTree.getKey()) {
				tempTree = tempTree.getRightNode();
			} else {
				tempTree = tempTree.getLeftNode();
			}

		}
		return null;
	}

	/**
	 * remove a node that contain the toDelete value
	 * 
	 * @param toDelete
	 *            a value to delete
	 * @return return true if toDelete is deleted
	 */
	private boolean balancedDelete(Node toDelete) {

		if (toDelete == null) {
			return false;
		}

		Node tempNode1, tempNode2;
		if (toDelete.getLeftNode() == null || toDelete.getRightNode() == null) {
			tempNode2 = toDelete;
		} else {
			tempNode2 = successor(toDelete);
		}
		if (tempNode2.getLeftNode() != null) {
			tempNode1 = tempNode2.getLeftNode();
		} else {
			tempNode1 = tempNode2.getRightNode();
		}
		if (tempNode1 != null) {
			tempNode1.setParent(tempNode2.getParent());
		}
		if (tempNode2.getParent() == null) {
			this.node = tempNode1;
		} else {
			if (tempNode2 == tempNode2.getParent().getLeftNode()) {
				tempNode2.getParent().setLeftNode(tempNode1);
			} else {
				tempNode2.getParent().setRightNode(tempNode1);
			}
		}
		if (tempNode2 != toDelete) {
			toDelete.setKey(tempNode2.getKey());
		}
		checkBalance(tempNode2);
		return true;

	}

	/**
	 * recursive function to calculate left and right sides nodes
	 * 
	 * @param node
	 *            a root of the tree
	 * @return number nodes in the tree without the root
	 */
	private int recursiveSize(Node node) {
		int sizeRight = 0, sizeLeft = 0;
		if (node.getRightNode() != null) {
			sizeRight++;
			sizeRight += recursiveSize(node.getRightNode());
		}
		if (node.getLeftNode() != null) {
			sizeLeft++;
			sizeLeft += recursiveSize(node.getLeftNode());
		}
		return sizeLeft + sizeRight;

	}

	/**
	 * checking and fix wrong tree to be correct avl
	 * 
	 * @param node
	 *            the node we want to rotate
	 * @param form
	 *            who's form is wrong
	 */
	private void fixBalance(Node node, int form) {
		if (form == LNRS) {
			rotateLeft(node.getLeftNode());
			rotateRight(node);
		}
		if (form == RNLS) {
			rotateRight(node.getRightNode());
			rotateLeft(node);
		}
		if (form == LNLS) {
			rotateRight(node);
		}
		if (form == RNRS) {
			rotateLeft(node);
		}
	}

	/**
	 * checking balancity of a node
	 * 
	 * @param node
	 *            not to check his balance
	 */
	private void checkBalance(Node node) {
		node = node.getParent();
		while (node != null) {
			if (node != null
					&& Math.abs(height(0, node.getLeftNode())
							- height(0, node.getRightNode())) > MAXDIFFERENCE) {
				if (height(0, node.getLeftNode()) < height(0,
						node.getRightNode())) {
					if (height(0, node.getRightNode().getLeftNode()) <= height(
							0, node.getRightNode().getRightNode())) {
						fixBalance(node, RNRS);
					} else {
						fixBalance(node, RNLS);
					}
				} else {
					if (height(0, node.getLeftNode().getRightNode()) < height(
							0, node.getLeftNode().getLeftNode())) {
						fixBalance(node, LNLS);
					} else {
						fixBalance(node, LNRS);
					}
				}
			}
			node = node.getParent();
		}
	}

	/**
	 * search the successor
	 * 
	 * @param node
	 *            a node to search his successor
	 * @return return the successor
	 */
	private Node successor(Node node) {
		Node tempNode1 = node;
		Node tempNode2;
		if (tempNode1.getRightNode() != null) {
			return treeMinimum(tempNode1);

		}
		tempNode2 = tempNode1.getParent();
		while (tempNode2 != null && tempNode1 == tempNode2.getRightNode()) {
			tempNode1 = tempNode2;
			tempNode2 = tempNode2.getParent();
		}
		return tempNode2;

	}

	/**
	 * rotate to the left
	 * 
	 * @param node
	 *            a node to rotate
	 */
	private void rotateLeft(Node node) {
		Node tempNode = node.getRightNode();
		node.setRightNode(tempNode.getLeftNode());
		if (node.getRightNode() != null) {
			node.getRightNode().setParent(node);
		}
		tempNode.setLeftNode(node);
		tempNode.setParent(node.getParent());
		node.setParent(tempNode);
		if (tempNode.getParent() != null) {
			if (tempNode.getParent().getRightNode() != null
					&& tempNode.getParent().getRightNode().equals(node)) {
				tempNode.getParent().setRightNode(tempNode);
			} else {
				tempNode.getParent().setLeftNode(tempNode);
			}
		} else {
			this.node = tempNode;
		}
	}

	/**
	 * rotate to the right
	 * 
	 * @param node
	 *            a node to rotate
	 */
	private void rotateRight(Node node) {
		Node tempNode = node.getLeftNode();
		node.setLeftNode(tempNode.getRightNode());
		if (node.getLeftNode() != null) {
			node.getLeftNode().setParent(node);
		}
		tempNode.setRightNode(node);
		tempNode.setParent(node.getParent());
		node.setParent(tempNode);
		if (tempNode.getParent() != null) {
			if (tempNode.getParent().getRightNode() != null
					&& tempNode.getParent().getRightNode().equals(node)) {
				tempNode.getParent().setRightNode(tempNode);
			} else {
				tempNode.getParent().setLeftNode(tempNode);
			}
		} else {
			this.node = tempNode;
		}
	}

	/**
	 * calculate the height of the node
	 * 
	 * @param height
	 *            current height of the tree
	 * @param head
	 *            the head node of the sub tree
	 * @return the height of the node
	 */
	private int height(int height, Node head) {
		if (head == null) {
			return height;
		}
		return Math.max(height(height + 1, head.getRightNode()),
				height(height + 1, head.getLeftNode()));
	}

	/**
	 * the minimum of the tree
	 * 
	 * @param tempNode
	 *            head of subtree
	 * @return minumu value in the sub tree
	 */
	private Node treeMinimum(Node tempNode) {
		while (tempNode.getLeftNode() != null) {
			tempNode = tempNode.getLeftNode();
		}
		return tempNode;
	}

	/**
	 * copy method that get the values from a tree to copy to another
	 * 
	 * @param array
	 *            array to save values
	 * @param node
	 *            our sub tree
	 */
	private void copyMethod(LinkedList<Integer> array, Node node) {
		array.add(node.getKey());
		if (node.getLeftNode() != null) {
			copyMethod(array, node.getLeftNode());
		}
		if (node.getRightNode() != null) {
			copyMethod(array, node.getRightNode());
		}
	}

}