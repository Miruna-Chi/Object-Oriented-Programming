package planeBoarding;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.util.Scanner;
import java.util.ArrayList;
import java.util.Hashtable;

/**
 * Main class with main method<br>
 * Creates a Hashtable named hashy:<p>
 * 
 * 	- key: id (of a Person)<p>
 *  
 * 	- value: an ArrayList of all the Person type objects that have the same id
 * (here, it means they are all in the same family/group or the ArrayList will have 
 * only one element if they count as single passengers)<p>
 * 
 * //See comments in the code for details
 */
public class Main {

	public static void main(String[] args) throws FileNotFoundException {

		Hashtable<String, ArrayList<Person>> hashy = new Hashtable<String, ArrayList<Person>>();

		PrintStream o = new PrintStream(new File("queue.out"));
		System.setOut(o);
		/* 
		 * changes the output from console to the newly created file 
		 * so we can still use System.out.print
		*/
		
		File file = new File("queue.in");
		Scanner input = new Scanner(file);

		
		

		int noOfPassengers, priority;
		noOfPassengers = input.nextInt();

		for (int i = 0; i < noOfPassengers; i++) {
			Person p = new Person();
			
			p.setId(input.next());
			p.setName(input.next());
			p.setAge(input.nextInt());
			p.setTicket(input.next().charAt(0));
			p.setBoardingPriority(input.nextBoolean());
			p.setSpecialNeeds(input.nextBoolean());

			/* 
			 * created a new Person, set all the parameters
			 * needed to calculate the priority 
			 */
			
			if (!hashy.containsKey(p.getId())) {
				
				/* 
				 * if the id doesn't exist in the table,
				 * add it, create the array (and add the person to it)
				 */
				
				hashy.put(p.getId(), new ArrayList<Person>());
				hashy.get(p.getId()).add(p);
				
			} else {
				// if it does, only add the person to the array
				hashy.get(p.getId()).add(p);
			}

		}

		String command, id;
		int node0 = 0;
		/*
		 * set to 0 if there is no initialized node in the tree (empty heap)
		 * set to 1 if there is at least one initialized node
		 */

		PriorityHeap.Node root = new PriorityHeap.Node();
		//creates the root of the tree
		PriorityHeap.height = PriorityHeap.height(root);
		//initializes the height

		while (input.hasNext()) {

			command = input.next();

			if (command.equals("insert")) {

				PriorityHeap.height = PriorityHeap.height(root);
				//calculates heap height
				
				priority = 0;
				id = input.next();

				Passenger p = hashy.get(id).get(0);
				/*
				 * gets the first entry in the ArrayList that corresponds to
				 * the given id (we don't need the whole Person array since the heap
				 * only needs the id and the priority of the family/group/person
				 * added to the queue)
				 */
				ArrayList<Person> a = hashy.get(id);
				/*
				 * gets the array for the given id, in order to calculate the full
				 * boarding priority and add the family/group specific bonus points
				 */
				
				if (id.charAt(0) == 'f')
					priority += 10;
				else if (id.charAt(0) == 'g')
					priority += 5;

				for (int i = 0; i < a.size(); i++)
					priority += a.get(i).calculatePriority();

				if (node0 == 0) {
					/* 
					* if there is no initialized node, initialize root and increment node0
					* will go to 'else' if the heap is not empty
					*/
					root.setId(id);
					root.setPriority(priority);
					node0++;
				} else
					root.insert(p, priority);
					

			} else if (command.equals("embark")) {
				PriorityHeap.height = PriorityHeap.height(root);
				
				/*
				 * call the embark method if there is a root and 
				 * the root has at least one child that could take its place in the queue
				 * 
				 * else, create another uninitialized root, treat it as an empty heap
				 * (if there was another root, the ties to it will automatically be cut,
				 * so the last person in the queue has boarded the plane)
				 */
				if (root != null && (root.left != null || root.right != null))
					root.embark();
				else {
					root = new PriorityHeap.Node();
					node0 = 0;
				}
			} else if (command.equals("list")) {
				root.list();
				
				/* ok (static variable):
				 * - 0, don't put a new line before the first line in the file
				 * - 1, new line before every next line
				 */
				PriorityHeap.ok = 1;

			} else if (command.equals("delete")) {

				String name = null;
				id = input.nextLine();

				/*
				 * the delete method can have one or two parameters:
				 * id and (optional)name
				 *  
				 * we take the rest of the line and separate it if need be
				 * (the method can delete a group/family/single passenger
				 * or only one person from a group/family)
				 * 
				 * id.length will be 3 if there is no given name (the whitespace
				 * before the id counts), else separate the string into id and name
				 */
				if (id.length() > 3)
					name = id.substring(4);

				id = id.substring(1, 3);
				priority = 0;

				ArrayList<Person> a = hashy.get(id);
				/*
				 * get all the people who have the same id
				 * calculate the priority again
				 */
				
				if (id.charAt(0) == 'f')
					priority += 10;
				else if (id.charAt(0) == 'g')
					priority += 5;

				for (int i = 0; i < a.size(); i++)
					priority += a.get(i).calculatePriority();

				if (name != null) {

					/*
					 * if we are given a name, then we only delete one person (part of a family/group)
					 * 
					 * we look for them in the array, subtract their priority from the whole,
					 * give them as a parameter for the delete method
					 * so it will reorder the tree based on the new priority, then remove them
					 * from the array
					 */
					for (int i = 0; i < a.size(); i++) {
						if (a.get(i).getName().contentEquals(name)) {

							Person p = a.get(i);
							priority -= a.get(i).calculatePriority();
							p.setPriority(priority);

							PriorityHeap.height = PriorityHeap.height(root);

							root.delete(p);
							a.remove(i);
							break;
						}
					}
				} else {
					/*
					 * if we are not given a name, then we remove a family/group/single passenger
					 * we set the priority to 0, delete the specific node from the heap(queue),
					 * and remove the entry in the Hashtable entirely (key and array)
					 */
					Person p = a.get(0);
					p.setPriority(0);

					PriorityHeap.height = PriorityHeap.height(root);

					root.delete(p);
					hashy.remove(id);
				}
			}
		}

		input.close();
	}
}
