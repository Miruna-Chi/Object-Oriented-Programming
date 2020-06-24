package planeBoarding;

import java.util.ArrayList;

/**
 * Class that contains the Tree(here it will be a max heap)<br>
 * - has a static nested class named Node (all the nodes of the tree)
 *
 */
public class PriorityHeap {
	static int height = 0;
	static int noOfNodes = 0;
	static int ok = 0;

	static class Node {
		private String id;
		private int priority;
		protected Node parent, left, right;

		/**
		 * Default constructor<br>
		 * - creates a node without a parent or children
		 */
		public Node() {
			priority = 0;
			parent = null;
			left = null;
			right = null;
		}

		/**
		 * Constructor<br>
		 * - creates a node without a parent or children
		 * 
		 * @param id       char 'f', 'g' or 's' (family, group or single) + number
		 * @param priority boarding priority of a family/group/single passenger
		 */
		public Node(String id, int priority) {
			this.id = id;
			this.priority = priority;
			parent = null;
			left = null;
			right = null;
		}

		public void setPriority(int priority) {
			this.priority = priority;
		}

		public int getPriority() {
			return priority;
		}

		public void setId(String id) {
			this.id = id;
		}

		public String getId() {
			return id;
		}

		/**
		 * Inserts a Passenger in the heap<br>
		 * 
		 * //See comments in the code for more details
		 * 
		 * @param p        the Passenger to be inserted in the queue
		 * @param priority the Passenger's boarding priority
		 */
		public void insert(Passenger p, int priority) {

			Node root = this;
			Node auxNode = root;
			Node node = new Node(p.getId(), priority);

			/*
			 * if the last level of the heap is full, the position for the new node can by
			 * found by traversing the heap, from root to the leftmost node on the last
			 * level (go left from node to node until the end)
			 * 
			 * then, we add the node to the left of that node
			 */
			if (fullLastLevel(auxNode, height - 1) == true) {
				while (auxNode.left != null)
					auxNode = auxNode.left;

				auxNode.left = node;
				node.parent = auxNode;

				/*
				 * after addition, we swap the node with its parent while the node's priority is
				 * greater than the parent's (and we go up in the tree, towards the root)
				 */
				while (auxNode != null && auxNode.getPriority() < node.getPriority()) {
					int value;
					String id;

					value = auxNode.getPriority();
					auxNode.setPriority(node.getPriority());
					node.setPriority(value);

					id = auxNode.getId();
					auxNode.setId(node.getId());
					node.setId(id);

					auxNode = auxNode.parent;
					node = node.parent;
				}
			} else {
				int level = height - 1;
				/*
				 * if the last level isn't full, then find the spot where to add the given node
				 * and do the same type of swaps (as mentioned in the previous comment)
				 */
				addonULevel(root, node, level);
			}

		}

		/**
		 * First Passenger in queue boards the plane:<br>
		 * - remove the root<br>
		 * - replace it with the last added node<br>
		 * - restore the order in the heap
		 * <p>
		 * 
		 *  //See comments in the code for more details
		 */
		public void embark() {
			Node root = this;
			Node auxNode = root;

			/*
			 * find the last added node:
			 * 
			 * if the last level is full, then the last added node is the rightmost node on
			 * the last level
			 * 
			 * else, the method lastAddedNode will find the rightmost existing node on the
			 * last level and return it
			 */
			if (fullLastLevel(auxNode, height - 1) == true) {
				while (auxNode.right != null)
					auxNode = auxNode.right;
			} else {
				int level = height - 1;
				auxNode = lastAddedNode(auxNode, level);
			}

			// the attributes from the last added node are transfered to the root
			root.setId(auxNode.getId());
			root.setPriority(auxNode.getPriority());

			// the node is cut from the tree

			auxNode = auxNode.parent;

			if (auxNode.right != null && auxNode.right.getId() == root.getId())
				auxNode.right = null;
			else if (auxNode.left != null && auxNode.left.getId() == root.getId())
				auxNode.left = null;

			// start swapping from root to children until order is restored

			swap(root);

		}

		/**
		 * List the priority queue (preorder traversal of the heap)
		 */
		public void list() {
			Node node = this;
			ArrayList<String> print = new ArrayList<String>();
			// save all the ids in an array

			traversePreOrder(node, print);

			// if this isn't the first printed line, add new line
			if (ok != 0)
				System.out.print("\r\n");
			for (int i = 0; i < print.size() - 1; i++)
				System.out.print(print.get(i) + " ");

			System.out.print(print.get(print.size() - 1));
		}

		/**
		 * Deletes a family/group/single passenger or a person from a family/group from
		 * the heap and restores queue order
		 * 
		 * @param p Passenger to be removed
		 */
		public void delete(Passenger p) {
			Node root = this;

			// find the node with the given id
			Node node = findNode(root, height - 1, p.getId());
			Node auxNode = root;
			node.setPriority(p.getPriority());

			/*
			 * - if the given priority is 0, then a whole Passenger is deleted from the
			 * queue: we swap it with the last added node so we can remove it as a leaf,
			 * then the node that took its place will be swapped with its children (the
			 * priority will be less or equal, it can't be greater) until order is restored
			 * 
			 * Note: this case is almost the same as embark (the difference being that not
			 * the root, but the node to be removed will take the attributes of the last
			 * added node)
			 * 
			 * - else: the given priority is greater than 0, we do not remove the node, we
			 * only swap it with its children (the priority will be less or equal, it can't
			 * be greater) until order is restored
			 */
			if (p.getPriority() == 0) {

				if (fullLastLevel(auxNode, height - 1) == true) {
					while (auxNode.right != null)
						auxNode = auxNode.right;
				} else {
					int level = height - 1;
					auxNode = lastAddedNode(auxNode, level);
				}

				node.setId(auxNode.getId());
				node.setPriority(auxNode.getPriority());

				auxNode = auxNode.parent;
				if (auxNode.right != null && auxNode.right.getId() == node.getId())
					auxNode.right = null;
				else if (auxNode.left != null && auxNode.left.getId() == node.getId())
					auxNode.left = null;

				swap(node);

			} else {

				swap(node);
			}
		}
	}

	/**
	 * Finds the node that has the given id, recursively traversing the tree
	 * 
	 * @param root  current node (starts with root)
	 * @param level current level of the node (starts at height - 1)
	 * @param id    of the node we want to find in the heap
	 * @return
	 */
	public static Node findNode(Node root, int level, String id) {

		if (root != null && (root.getId()).equals(id))
			return root;

		else if (level > 0) {

			Node node = findNode(root.right, level - 1, id);

			if (node != null)
				return node;

			node = findNode(root.left, level - 1, id);

			if (node != null)
				return node;

		}
		return null;
	}

	/**
	 * Swaps the given node with its left or right child until the order in the heap
	 * is restored
	 * <p>
	 * 
	 * Swaps with:<br>
	 * - left child: if the left child has a greater priority than the right child,
	 * or in case of equal priorities<br>
	 * - right child: if the right child has a greater priority than the left child
	 * 
	 * @param auxNode
	 */
	public static void swap(Node auxNode) {
		while (auxNode != null && (auxNode.left != null && auxNode.getPriority() < auxNode.left.getPriority())
				|| (auxNode.right != null && auxNode.getPriority() < auxNode.right.getPriority())) {

			if (auxNode.left != null && auxNode.right != null
					&& auxNode.right.getPriority() <= auxNode.left.getPriority()) {
				if (auxNode.getPriority() < auxNode.left.getPriority()) {
					swap_left(auxNode);
					auxNode = auxNode.left;
				}

			} else if (auxNode.left != null && auxNode.right != null
					&& auxNode.right.getPriority() > auxNode.left.getPriority()) {
				if (auxNode.getPriority() < auxNode.right.getPriority()) {
					swap_right(auxNode);
					auxNode = auxNode.right;
				}
			} else if (auxNode.left != null && auxNode.right == null) {
				if (auxNode.getPriority() < auxNode.left.getPriority()) {
					swap_left(auxNode);
					auxNode = auxNode.left;
				}

			} else if (auxNode.right != null)
				if (auxNode.getPriority() < auxNode.right.getPriority()) {
					swap_right(auxNode);
					auxNode = auxNode.right;
				}
		}
	}

	/**
	 * Swaps node with its right child
	 * 
	 * @param node
	 */
	public static void swap_right(Node node) {
		int value;
		String id;

		value = node.getPriority();
		node.setPriority(node.right.getPriority());
		node.right.setPriority(value);

		id = node.getId();
		node.setId(node.right.getId());
		node.right.setId(id);

		node = node.right;
	}

	/**
	 * Swaps node with its left child
	 * 
	 * @param node
	 */
	public static void swap_left(Node node) {
		int value;
		String id;

		value = node.getPriority();
		node.setPriority(node.left.getPriority());
		node.left.setPriority(value);

		id = node.getId();
		node.setId(node.left.getId());
		node.left.setId(id);

		node = node.left;
	}

	/**
	 * Finds and returns the last added node on the last level of the heap
	 * 
	 * @param root current node(starts with root)
	 * @param level current level(starts with height - 1)
	 * @return
	 */
	public static Node lastAddedNode(Node root, int level) {

		if (root != null && level == 0)
			return root;

		else if (level > 0) {

			Node node = lastAddedNode(root.right, level - 1);
			if (node != null)
				return node;
			node = lastAddedNode(root.left, level - 1);
			if (node != null)
				return node;

		}
		return null;
	}

	/**
	 * Adds node on the last (and incomplete/unfinished) level<br>
	 * Swaps with parent until order in the heap is restored
	 * @param root current node(starts with root)
	 * @param auxNode node to be added
	 * @param level current level(starts at height - 1)
	 * @return
	 */
	public static boolean addonULevel(Node root, Node auxNode, int level) {

		if ((root.left == null || root.right == null) && level == 1) {
			if (root.left == null)
				root.left = auxNode;
			else
				root.right = auxNode;

			auxNode.parent = root;
			while (auxNode != null && root != null && auxNode.getPriority() > root.getPriority()) {
				int value;
				String id;

				value = auxNode.getPriority();
				auxNode.setPriority(root.getPriority());
				root.setPriority(value);

				id = auxNode.getId();
				auxNode.setId(root.getId());
				root.setId(id);

				auxNode = auxNode.parent;
				root = root.parent;
			}

			return true;

		} else if (level > 0) {

			if (addonULevel(root.left, auxNode, level - 1))
				return true;
			if (addonULevel(root.right, auxNode, level - 1))
				return true;

		}
		return false;
	}
	
	/**
	 * Checks if the last level is complete<br>
	 * Traverses the tree: if a node on the last level is null(missing), returns false
	 * @param root current node(starts with root)
	 * @param level current level(starts at height - 1)
	 * @return true if the last level is complete/full, false otherwise
	 */
	public static boolean fullLastLevel(Node root, int level) {

		if (root == null && level == 0)
			return false;

		else if (level > 0) {

			if (fullLastLevel(root.left, level - 1) == false)
				return false;
			if (fullLastLevel(root.right, level - 1) == false)
				return false;

		}

		return true;
	}

	/**
	 * Computes the height of the tree
	 * @param node starts from root
	 * @return height
	 */
	public static int height(Node node) {

		if (node == null)
			return 0;

		int left_height = height(node.left);
		int right_height = height(node.right);

		if (left_height > right_height) {
			height = left_height + 1;
			return left_height + 1;
		} else {
			height = right_height + 1;
			return right_height + 1;
		}
	}

	/**
	 * Preorder traversal of the tree<p>
	 * 
	 * - adds the id of each node to the array to be printed(needed for list)
	 * @param node
	 * @param arrayToPrint
	 */
	public static void traversePreOrder(Node node, ArrayList<String> arrayToPrint) {

		if (node != null) {
			arrayToPrint.add(node.getId());

			traversePreOrder(node.left, arrayToPrint);
			traversePreOrder(node.right, arrayToPrint);
		}
	}

}
