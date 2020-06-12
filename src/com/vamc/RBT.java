package com.vamc;
import java.util.ArrayList;

/**
 * RedBlackTree Implementation for risingCity
 * @author Vamsidhar Bada
 *
 */
public class RBT {
	private static final String RED_COLOUR = "R";
	private static final String BLACK_COLOUR = "B";

	/**
	 * Node Structure of RedblackTree
	 * @author Vamsidhar Bada
	 *
	 */
	static class RedBlackTreeNode {
		Building building;
		RedBlackTreeNode left;
		RedBlackTreeNode right;
		RedBlackTreeNode parent;
		String colour;

		public RedBlackTreeNode(Building building, String colour) {
			this.building = building;
			this.left = null;
			this.right = null;
			this.parent = null;
			this.colour = colour;
		}
	}

	public RBT() {
		// Default Constructor
	}

	static RedBlackTreeNode root;

	/**
	 * function to create a Node.
	 * 
	 * @param data
	 * @return
	 */
	public RedBlackTreeNode createNode(Building data) {
		RedBlackTreeNode node = new RedBlackTreeNode(data, RED_COLOUR);
		node.left = new RedBlackTreeNode(null, BLACK_COLOUR);
		node.right = new RedBlackTreeNode(null, BLACK_COLOUR);
		return node;
	}

	/**
	 * function to balance the tree after deletion.
	 * 
	 * @param node
	 * @param color
	 */
	private static void balanceRBT(RedBlackTreeNode node, String color) {

		// if node is Red-Black.
		if (node.parent == null || color.equals("BR") || color.equals("RB")) {
			node.colour = BLACK_COLOUR;
			return;
		}

		RedBlackTreeNode parent = node.parent;

		// get sibling of the given node.
		RedBlackTreeNode sibling = null;
		if (parent.left == node) {
			sibling = parent.right;
		} else {
			sibling = parent.left;
		}

		RedBlackTreeNode sibleft = sibling.left;
		RedBlackTreeNode sibright = sibling.right;

		if (isObjectNull(sibleft) && isObjectNull(sibright)) {
			return;
		}

		// left sibling and right sibling are black.
		if (sibling.colour == BLACK_COLOUR && sibleft.colour == BLACK_COLOUR && sibright.colour == BLACK_COLOUR) {
			sibling.colour = RED_COLOUR;
			node.colour = BLACK_COLOUR;
			String s = parent.colour + BLACK_COLOUR;
			balanceRBT(parent, s);
			return;
		}

		// sibling is red.
		if (sibling.colour == RED_COLOUR) {
			if (parent.right == sibling) {
				doLeftRotation(sibling);
			} else {
				doRightRotation(sibling);
			}
			balanceRBT(node, color);
			return;
		}

		// sibling is red but one of its children are red.
		if (isObjectNull(sibleft)) {
			return;
		}
		if (parent.left == sibling) {
			if (sibleft.colour == RED_COLOUR) {
				doRightRotation(sibling);
				sibleft.colour = BLACK_COLOUR;
			} else {
				doLeftRotation(sibright);
				doRightRotation(sibright);
				sibright.left.colour = BLACK_COLOUR;
			}
			return;
		} else {
			if (sibright.colour == RED_COLOUR) {
				doLeftRotation(sibling);
				sibright.colour = BLACK_COLOUR;
			} else {
				doRightRotation(sibleft);
				doLeftRotation(sibleft);
				sibleft.right.colour = BLACK_COLOUR;
			}
			return;
		}
	}

	/**
	 * function to find the next greater element than the given node.
	 * @param node
	 * @return
	 */
	private static RedBlackTreeNode findNextElement(RedBlackTreeNode node) {
		RedBlackTreeNode next = node.right;
		if (next.building == null) {
			return node;
		}
		while (next.left.building != null) {
			next = next.left;
		}
		return next;
	}

	/**
	 * function for PreOrder Traversal of the tree.
	 * @param node
	 */
	private static void preOrderTraversal(RedBlackTreeNode node) {
		if (isObjectNull(node.building)) {
			return;
		}
		System.out.print(node.building + "-" + node.colour + " ");
		preOrderTraversal(node.left);
		preOrderTraversal(node.right);
	}

	/**
	 * function to print the tree.
	 */
	public static void printRBT() {
		if (root == null) {
			return;
		}
		preOrderTraversal(root);
		System.out.println();
	}

	/**
	 * Function to search for a building number in a tree (takes logarthmic time)
	 * @param node
	 * @param buildingNumber
	 * @return
	 */
	public static Building searchBuildingNumber(RedBlackTreeNode node, int buildingNumber) {
		if (isObjectNull(node) || isObjectNull(node.building)) {
			return null;
		}
		if (node.building.getBuildingNumber() == buildingNumber) {
			return node.building;
		} else if (node.building.getBuildingNumber() < buildingNumber) {
			return searchBuildingNumber(node.right, buildingNumber);
		} else if (node.building.getBuildingNumber() > buildingNumber) {
			return searchBuildingNumber(node.left, buildingNumber);
		}
		return null;
	}

	/**
	 * Function to search buildings in the inclusive range of startBuilding & endBuilding>
	 * Returns list of building tuple in sorted order of building numbers
	 * @param list
	 * @param node
	 * @param startBuilding
	 * @param endBuilding
	 * @return
	 */
	public static ArrayList<Building> searchBuildingNumberInRange(ArrayList<Building> list, RedBlackTreeNode node,
			int startBuilding, int endBuilding) {
		// Base case
		if (isObjectNull(node) || isObjectNull(node.building)) {
			return list;
		}
		if (inRange(node.building.getBuildingNumber(), startBuilding, endBuilding)) {
			searchBuildingNumberInRange(list, node.left, startBuilding, endBuilding);
			list.add(node.building);
			searchBuildingNumberInRange(list, node.right, startBuilding, endBuilding);
		} else if (node.building.getBuildingNumber() >= startBuilding) {
			searchBuildingNumberInRange(list, node.left, startBuilding, endBuilding);
		} else if (node.building.getBuildingNumber() <= startBuilding) {
			searchBuildingNumberInRange(list, node.right, startBuilding, endBuilding);
		}
		return list;
	}

	/*
	 * Returns true if the provided building number is the inclusive range of start
	 * and end numbers
	 */
	private static boolean inRange(int buildingNum, int startBuilding, int endBuilding) {
		if (buildingNum >= startBuilding && buildingNum <= endBuilding)
			return true;
		return false;
	}

	/**
	 * Function to add node to RedBlackTree
	 * @param newBuilding
	 * @return
	 */
	public boolean addNode(Building newBuilding) {
		RedBlackTreeNode node = createNode(newBuilding);
		if (isObjectNull(root)) {
			root = node;
			root.colour = BLACK_COLOUR;
			return true;
		}

		RedBlackTreeNode tempNode = root;
		while (!isObjectNull(tempNode)) {
			if (tempNode.building.getBuildingNumber() == newBuilding.getBuildingNumber()) {
				return false;
			}
			if (tempNode.building.getBuildingNumber() > newBuilding.getBuildingNumber()) {
				if (isObjectNull(tempNode.left.building)) {
					tempNode.left = node;
					node.parent = tempNode;
					insertBalance(node); // balance the tree after insertion.
					return true;
				}
				tempNode = tempNode.left;
				continue;
			}
			if (tempNode.building.getBuildingNumber() < newBuilding.getBuildingNumber()) {
				if (isObjectNull(tempNode.right.building)) {
					tempNode.right = node;
					node.parent = tempNode;
					insertBalance(node); // balance the tree after insertion.
					return true;
				}
				tempNode = tempNode.right;
			}
		}
		return true;
	}

	/**
	 *  Function to perform Left Rotation.
	 * @param node
	 */
	private static void doLeftRotation(RedBlackTreeNode node) {
		RedBlackTreeNode parent = node.parent;
		RedBlackTreeNode left = node.left;
		node.left = parent;
		parent.right = left;
		if (left != null) {
			left.parent = parent;
		}
		String s = parent.colour;
		parent.colour = node.colour;
		node.colour = s;
		RedBlackTreeNode gp = parent.parent;
		parent.parent = node;
		node.parent = gp;

		if (gp == null) {
			root = node;
			return;
		} else {
			if (gp.left == parent) {
				gp.left = node;
			} else {
				gp.right = node;
			}
		}
	}

	/**
	 *  Function to perform Right Rotation.
	 * @param node
	 */
	private static void doRightRotation(RedBlackTreeNode node) {
		RedBlackTreeNode parent = node.parent;
		RedBlackTreeNode right = node.right;
		node.right = parent;
		parent.left = right;
		if (right != null) {
			right.parent = parent;
		}
		String s = parent.colour;
		parent.colour = node.colour;
		node.colour = s;
		RedBlackTreeNode gp = parent.parent;
		parent.parent = node;
		node.parent = gp;

		if (gp == null) {
			root = node;
			return;
		} else {
			if (gp.left == parent) {
				gp.left = node;
			} else {
				gp.right = node;
			}
		}
	}

	/**
	 *  Function to balance the tree after insertion
	 * @param node
	 */
	public static void insertBalance(RedBlackTreeNode node) {
		// if given node is root node.
		if (node.parent == null) {
			root = node;
			root.colour = BLACK_COLOUR;
			return;
		}

		// if node's parent color is black.
		if (node.parent.colour == BLACK_COLOUR) {
			return;
		}

		// get the node's parent's sibling node.
		RedBlackTreeNode sibling = null;
		if (node.parent.parent.left == node.parent) {
			sibling = node.parent.parent.right;
		} else {
			sibling = node.parent.parent.left;
		}

		// if sibling color is red.
		if (sibling.colour == RED_COLOUR) {
			node.parent.colour = BLACK_COLOUR;
			sibling.colour = BLACK_COLOUR;
			node.parent.parent.colour = RED_COLOUR;
			insertBalance(node.parent.parent);
			return;
		}

		// if sibling color is black.
		else {
			if (node.parent.left == node && node.parent.parent.left == node.parent) {
				doRightRotation(node.parent);
				insertBalance(node.parent);
				return;
			}
			if (node.parent.right == node && node.parent.parent.right == node.parent) {
				doLeftRotation(node.parent);
				insertBalance(node.parent);
				return;
			}
			if (node.parent.right == node && node.parent.parent.left == node.parent) {
				doLeftRotation(node);
				doRightRotation(node);
				insertBalance(node);
				return;
			}
			if (node.parent.left == node && node.parent.parent.right == node.parent) {
				doRightRotation(node);
				doLeftRotation(node);
				insertBalance(node);
				return;
			}
		}
	}

	/**
	 *  function to remove nodes from the tree.
	 * @param data
	 */
	public void removeNode(Building data) {
		if (root == null) {
			return;
		}

		// search for the given element in the tree.
		RedBlackTreeNode temporaryNode = root;
		while (temporaryNode.building != null) {
			if (temporaryNode.building == data) {
				break;
			}
			if (temporaryNode.building.getBuildingNumber() >= data.getBuildingNumber()) {
				temporaryNode = temporaryNode.left;
				continue;
			}
			if (temporaryNode.building.getBuildingNumber() < data.getBuildingNumber()) {
				temporaryNode = temporaryNode.right;
				continue;
			}
		}

		// if not found. then return.
		if (temporaryNode.building == null) {
			return;
		}

		// find the next greater number than the given data.
		RedBlackTreeNode next = findNextElement(temporaryNode);

		// swap the data values of given node and next greater number.
		Building t = temporaryNode.building;
		temporaryNode.building = next.building;
		next.building = t;

		// remove the next node.
		RedBlackTreeNode parent = next.parent;
		if (parent == null) {
			if (next.left.building == null) {
				root = null;
			} else {
				root = next.left;
				next.parent = null;
				root.colour = BLACK_COLOUR;
			}
			return;
		}

		if (parent.right == next) {
			parent.right = next.left;
		} else {
			parent.left = next.left;
		}
		next.left.parent = parent;
		String color = next.left.colour + next.colour;
		balanceRBT(next.left, color);
	}

	/**
	 * Utility function to check if object is null. Returns true if it is null
	 * 
	 * @param obj
	 * @return
	 */
	private static boolean isObjectNull(Object obj) {
		if (obj == null)
			return true;
		return false;
	}

}
