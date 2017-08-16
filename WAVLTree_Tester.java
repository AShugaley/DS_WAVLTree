package project_m;

import java.util.Arrays;
import java.util.Random;

import project_m.WAVLTree.WAVLNode;


public class WAVLTree_Tester {
	
	public static void main(String[] args) {
		//create a random tree and test if there are any error while inserting the items.
		WAVLTree test_tree = new WAVLTree();
		test_tree = example_tree_creator2();
//		for(int i= 0; i<155 ; i++){
//			example_random_tree_deletor(test_tree,20,20);
//		}
		insDelTest(5000,500,500,500);
		
		
		
		System.out.println("__________________________________________________________________");
		System.out.println("");

		// creating a tree and testing it for min, max, search, keysToArray, infoToArray, size.
		test_tree = example_tree_creator();
		if(!test_tree.min().equals("1")){
			System.out.println("error 1: in min function");
		}
		if(!test_tree.max().equals("9")){
			System.out.println("error 2: in max function");
		}
		if(!test_tree.search(3).equals("3")){
			System.out.println("error 3: in search function");
		}
		if(!(test_tree.search(4) == null)){
			System.out.println("error 4: in search function");
		}
		int [] sorted_keys_array = {1,2,3,5,6,7,9};
		if(!Arrays.equals(test_tree.keysToArray(),sorted_keys_array)){
			System.out.println("error 5: in keys to array function");
		}
		String [] sorted_info_array = {"1","2","3","5","6","7","9"};
		if(!Arrays.equals(test_tree.infoToArray(),sorted_info_array)){
			System.out.println("error 6: in info to array function");
		}
		WAVLTree empty_tree = new WAVLTree();
		if(empty_tree.empty() != true){
			System.out.println("error 7: in empty function");
		}
		if(test_tree.empty() == true){
			System.out.println("error 8: in empty function");
		}
		if(empty_tree.size() != 0){
			System.out.println("error 9: in size function");
		}
		if(test_tree.size() != 7){
			System.out.println("error 10: in size function");
		}
		System.out.println("Done!");
	}
	
	public static WAVLTree example_tree_creator(){
		WAVLTree tree = new WAVLTree();
		
		tree.insert(5, "5");
		tree.insert(3, "3");
		tree.insert(6, "6");
		tree.insert(7, "7");
		tree.insert(1, "1");
		tree.insert(9, "9");
		tree.insert(2, "2");
		return tree;
	}
	public static void insDelTest(int iter, int startTreeSize, int numOfTests, int maxNum){
		Random random = new Random();
		int in;
		for (int i = 0; i<iter; i++){
			WAVLTree tree = new WAVLTree();
			for(int j =0; j<startTreeSize; j++){
				in = random.nextInt(maxNum);
				tree.insert(in, "" + in);
			}
			
			//printBinaryTree(tree.root, 0);
			for(int j = 0; j < numOfTests; j++){
				in = random.nextInt(maxNum);
				tree.insert(in, "" + in);
				int[] array = tree.keysToArray();
				int rnd = new Random().nextInt(array.length);
				in =  array[rnd];
				tree.delete(in);
				if(!testTreeRanks(tree.root)){
					System.out.println("Error in rebalancing after inserting item key " + in + " " + i);
					printBinaryTree(tree.root, 0);
				}
//				else
//					//System.out.println("delete!");
//					
			}
			System.out.println("DDone!");
		}
	}
	
	public static WAVLTree example_tree_creator2(){
		WAVLTree tree = new WAVLTree();
		
		tree.insert(1, "1");

		tree.insert(13, "13");
		tree.insert(14, "14");
		tree.insert(15, "15");
		tree.insert(7, "7");
		tree.insert(8, "8");
		tree.insert(16, "16");
		tree.insert(17, "17");
		tree.insert(18, "18");
		tree.insert(19, "19");
		tree.insert(3, "3");
		tree.insert(4, "4");
		
		tree.insert(6, "6");

		tree.insert(9, "9");
		tree.insert(10, "10");
		tree.insert(11, "11");
		tree.insert(2, "2");
		tree.insert(12, "12");
		tree.insert(5, "5");
	
		tree.insert(20, "20");
		return tree;
	}
	public static WAVLTree example_random_tree_insertor(WAVLTree tree,int randomInsertionNumber, int randomMaxInteger){
		Random random = new Random();
		int lastKey;
		int lastTreeSize =tree.size();
		tree = example_tree_creator2();
		for(int i = 0; i < randomInsertionNumber; i++){
			tree.insert(lastKey = random.nextInt(randomMaxInteger), "info");
		    
			if(!testTreeRanks(tree.root)){
				System.out.println("Error in rebalancing after inserting item key " + lastKey + " " + i);
				printBinaryTree(tree.root, 0);
				return tree;
			}
			
		}
		System.out.println("Finished inserting 	" + (tree.size()-lastTreeSize) + "	 random items to tree without any errors.");
		System.out.println("DDone!");

		return tree;
	}
	
	public static WAVLTree example_random_tree_deletor(WAVLTree tree,int randomDeletionNumber, int randomMaxInteger){
		Random random = new Random();
		int lastKey;
		int lastTreeSize =tree.size();
//		printBinaryTree(tree.WAVL_root, 0,tree.WAVL_emptyNode);

		for(int i = 0; i < randomDeletionNumber; i++){
			tree = example_tree_creator2();
			if(tree.delete(lastKey = random.nextInt(randomMaxInteger))!=-1){
				
//				System.out.println("__________________________________________________________________");
//				System.out.println("");
//				System.out.println("deleting item " + lastKey);
//				System.out.println("__________________________________________________________________");
//				System.out.println("");
				//printBinaryTree(tree.WAVL_root, 0,tree.WAVL_emptyNode);
				if(!testTreeRanks(tree.root)){
					System.out.println("Error in rebalancing after deleting item key " + lastKey);
					printBinaryTree(tree.root, 0);

					return tree;
				}
			}
			System.out.println("DDone!");
		}
//		System.out.println("Finished deleting	" + (lastTreeSize-tree.size()) + "	 random items to tree without any errors.");
		return tree;
	}
//	public static void printBinaryTree(WAVLTree.WAVLNode root, int level, WAVLTree.WAVLNode empty){
//	    if(root==null || root == empty)
//	         return;
//	    printBinaryTree(root.right, level+1,empty);
//	    if(level!=0){
//	        for(int i=0;i<level-1;i++)
//	            System.out.print("|\t");
//	            System.out.println("|---" + root.rank + "---"+root.key);
//	    }
//	    else
//	        System.out.println(root.key);
//	    printBinaryTree(root.left, level+1,empty);
//	}
	public static void printBinaryTree(WAVLNode root, int level){
	    if(root==null)
	         return;
	    printBinaryTree(root.right, level+1);
	    if(level!=0){
	        for(int i=0;i<level-1;i++)
	            System.out.print("|\t");
	            System.out.println("|-------"+root.key + "(" + root.rank + ")");
	    }
	    else
	        System.out.println(root.key + "(" + root.rank + ")");
	    printBinaryTree(root.left, level+1);
	}    
	public static boolean testTreeRanks(WAVLTree.WAVLNode root){
		int rank = root.rank;
		int left_rank = root.left.rank;
		int right_rank = root.right.rank;
		if (root.left.key > root.right.key)
			return false;
		if (rank - left_rank > 2)
			return false;
		if (rank - left_rank < 1)
			return false;
		if (rank - right_rank < 1)
			return false;
		if (rank - right_rank > 2)
			return false;
		if (root.right.rank != -1)
			testTreeRanks(root.right);
		if (root.left.rank != -1)
			testTreeRanks(root.left);
		return true;
		
	}
}

