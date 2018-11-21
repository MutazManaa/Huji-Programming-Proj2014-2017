package oop.ex5.data_structures;

/**
 * This class implementing Node in an AVLTREE
 * 
 * @author mutazmanaa
 * 
 */

public class Node {
	private int key;
	private Node parent, left, right;

	// -------------contructors-------------------------------

	/**
	 * constructor to initial one Node
	 * 
	 * @param key
	 *            Node value
	 * @param parent
	 *            Node parent
	 * @param left
	 *            left Nod
	 * @param right
	 *            right Node
	 */
	public Node(int key, Node parent, Node left, Node right) {
		this.key = key;
		this.parent = parent;
		this.left = left;
		this.right = right;
	}

	// ------------Methods-------------------------------------
	/**
	 * set key Value
	 * 
	 * @param key
	 *            Node Value
	 */
	public void setKey(int key) {
		this.key = key;
	}

	/**
	 * getting key value
	 * 
	 * @return key value in the Node
	 */
	public int getKey() {
		return this.key;
	}

	/**
	 * set the a parent Node
	 * 
	 * @param parent
	 *            Node's parent
	 */
	public void setParent(Node parent) {
		this.parent = parent;

	}

	/**
	 * get parent Node
	 * 
	 * @param parent
	 *            parent Node
	 * @return Nod's Parent
	 */
	public Node getParent() {
		return this.parent;
	}

	/**
	 * set left Node
	 * 
	 * @param left
	 *            left Node
	 */
	public void setLeftNode(Node left) {
		this.left = left;
	}

	/**
	 * gets the left Node
	 * 
	 * @param left
	 *            left Node
	 * @return left Node
	 */
	public Node getLeftNode() {
		return this.left;
	}

	/**
	 * set the right Node
	 * 
	 * @param right
	 *            the right Node
	 */
	public void setRightNode(Node right) {
		this.right = right;
	}

	/**
	 * get the right Node
	 * 
	 * @return the right Node
	 */
	public Node getRightNode() {
		return this.right;

	}
}
