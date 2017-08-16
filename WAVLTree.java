package project_m;

import java.util.Arrays;

import javax.management.openmbean.KeyAlreadyExistsException;
import javax.swing.text.TabableView;

/**
*
* WAVLTree
*
* An implementation of a WAVL Tree with
* distinct integer keys and info
*
*/

public class WAVLTree {
	

	private int size;
	
//	public static void main(String[] args){ 
//		WAVLTree t = new WAVLTree();
//		t.insert(58, "a");		
//		t.insert(7, "b");
//		t.insert(160, "d");
//		t.insert(899, "c");
//		t.insert(56, "e");
//		t.insert(2, "j");
//		t.insert(1, "i");
//		t.insert(90, "h");
//		t.insert(89, "g");
//		t.insert(7, "f");
//		t.insert(45, "k");
//		t.insert(3443534, "l");
//		t.insert(100, "hh");
//		t.delete(20);
//		t.delete(45);
////		WAVLNode test = t.findNode(64);
////		test = t.replaceInnerNode(test);
////		t.deleteLeaf(test);
//		
//		t.delete(64);
//		t.delete(15);
//		t.delete(100);
//		t.delete(89);
//		t.delete(90);
//		t.delete(7);
//		t.delete(5);
//		t.delete(1);
//		t.delete(132);
//		t.delete(2);
//		t.insert(132,"h");
////		t.delete(100);
//		WAVLNode x = t.findNode(3443534);
//		//System.out.println(x.key);
//
//		t.delete(3443534);
//		//System.out.println(t.root == t.extLeaf);
//		printBinaryTree(t.root, 0);
//		System.out.println(Arrays.toString(t.infoToArray()));
//		System.out.println(t.size);
//		
//	}	
	
	WAVLNode root; // here we DON'T have a sentinel - I had some doubts about this, and if you feel like we're better off with one - do tell me, it's fairly easy to create.
	WAVLNode extLeaf; // External Leaf - only one
	WAVLNode sentinel;
	//-------------------------TREE BUILDER 1- empty-------------------------//
	public WAVLTree(){   
		root = this.new WAVLNode();
		extLeaf = this.new WAVLNode();
		sentinel = this.new WAVLNode();
		root.parent = sentinel;
		sentinel.rank = -1005;
		sentinel.right = root;
		sentinel.left = root;
		root.rank = -2;
		extLeaf.rank = -1;
		size=0;
	}
	
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~ Printing ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~//
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
	
	
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~ Is Empty ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~//

 /**
  * public boolean empty()
  *
  * returns true if and only if the tree is empty
  *
  */
 public boolean empty() {   //O(1)
   return root.rank == -2; 
 }

 
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~ Search ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~//
/**
  * public String search(int k)   //
  *
  * returns the info of an item with key k if it exists in the tree
  * otherwise, returns null
  */
 public String search(int k)   //O(logn)
 {
	if (this.empty())
		return null;
	WAVLNode current = root;
	while ((current != null)&&(current != extLeaf)){
		if (current.key == k){
			return current.info;
 }
		if (current.key > k)
			current = current.left;
		if (current.key < k)
			current = current.right;
			}
	return null;
 }
 
//-------------------------Help method- Insert to an empty tree-------------------------//
 private void InsertFirst(int key, String info){ //O(1)
	  root.rank = 0; 
	  root.info = info;
	  root.key = key;
	  root.parent = null;
	  root.left = extLeaf;
	  root.right = extLeaf;
	  
 }
	//-------------------------Help method- insert to the place-------------------------//
 private void insertInPlace(WAVLNode newnode){ 
		 WAVLNode current = root;	 
		 while(true)
		 { 
			 if (current.key > newnode.key)
			 { // go left
				 if (current.left == extLeaf)
				 { // if this is a leaf;
					 current.left = newnode;
					 newnode.parent = current;
					 break;
				 }	 
				 else
					 current = current.left; // if we still have something to the left
			 }
			 if (current.key < newnode.key)
			 {
				 if (current.right == extLeaf)
				 {
					 current.right = newnode;
					 newnode.parent = current;
					 break;
				 }
				 else
					 current = current.right; 
			 }
		 }
		 newnode.rank=0;
		// newnode.parent.rank+=1;
		 newnode.left=newnode.right= extLeaf;   //the node we added is a LEAF now! 
	 }

 //-------------------------Help method- A Leaf? -------------------------//
private boolean IsLeaf(WAVLNode n){ 
	 if (n.rank==0)
	 {
		 return true;
	 }
	 return false;
}


//-------------------------Help method- Which Node ("0-1", "1-1"...)- left first, right after? -------------------------//
private String DiffType(WAVLNode n){ 
	 String Nodetype="";

	 int leftdif=n.rank-n.left.rank;
	 int rightdif=n.rank-n.right.rank;
	 Nodetype+= Integer.toString(leftdif)+ Integer.toString(rightdif);

	 return Nodetype;
}
  
//-------------------------Help method- Legal-------------------------//
private boolean Islegal(WAVLNode n){ 

	 if((DiffType(n).equals("22"))||(DiffType(n).equals("11"))||(DiffType(n).equals("21"))||(DiffType(n).equals("12")))
		 {
	 		return true;
		 }
	 return false;
}


	//-------------------------Help method- Rotation right-------------------------//
 private WAVLNode RotateRight(WAVLNode origin){ 
	 boolean check=false;
	 WAVLNode temp = null;
	 if(origin==root)
	 {
		 check=true;
	 }
	 else
		 temp = origin.parent;
	 WAVLNode other=origin.left;
	 origin.left=other.right;
	 other.right=origin;
	 Demote(origin);  
	 if(origin.left.parent != extLeaf)
		 origin.left.parent = origin;
	 other.parent=origin.parent; 
	 origin.parent=other;
	 if(check==true)
	 {
		 root=other;
		 root.parent = sentinel;
	 }
	 else{ 
		 if (temp.left == origin)
			 temp.left = other;
		 if (temp.right == origin)
			 temp.right = other;
	 }		 
	 return other;
 }
 
 
	//-------------------------Help method- Rotation left-------------------------//
 private WAVLNode RotateLeft(WAVLNode origin){ 
	 boolean check=false; 
	 WAVLNode temp = null;
	 if(origin==root)
	 {
		 check=true;
		 temp=sentinel;
	 }
	 else{
		  temp = origin.parent;
	 }
	 WAVLNode other=origin.right;
	 origin.right=other.left;
	 other.left=origin;
     Demote(origin); 
	 origin.right.parent = origin;
	 other.parent=origin.parent; 
	 origin.parent=other;
	 if(check==true)
	 {		 
		 root=other;
		 root.parent = sentinel;
	 }
	 else{
		 if (temp.left == origin)
			 temp.left = other;
		 if (temp.right == origin)
			 temp.right = other;
	 }	
	 return other;
 }
 
 
 
//-------------------------Help method- Double Rotate Left-Right-------------------------//
private WAVLNode RotateLeftRight(WAVLNode origin){ 
	boolean check = false;
	WAVLNode temp = extLeaf;
	if (origin == root)
	{
		check = true;
		origin.parent = sentinel;
	}
	temp = origin.parent;
	origin.left = RotateLeft(origin.left);
	WAVLNode newNode = (RotateRight(origin));
	if (temp.left == origin)
		temp.left = newNode;
	if (temp.right == origin)
		temp.right = newNode;
	 Promote(newNode);  
	 newNode.parent = temp;
	 if (check)
		 root = newNode;
	 return newNode; 
}

//-------------------------Help method- Double Rotate Right-Left-------------------------//
private WAVLNode RotateRightLeft(WAVLNode origin){
	boolean check = false;
	WAVLNode temp  = extLeaf;
	if (origin == root)
	{
		check = true;
		origin.parent = sentinel;
	}
	temp = origin.parent;
	origin.right = RotateRight(origin.right);	
	WAVLNode newNode= (RotateLeft(origin));
	if (temp.left == origin)
		temp.left = newNode;
	if (temp.right == origin)
		temp.right = newNode;
	Promote(newNode);  
	newNode.parent = temp;
	 if (check)
		 root = newNode;
	return newNode;
}

	//-------------------------Help method- Promote-------------------------//
private void Promote(WAVLNode n){ 
	n.rank+=1;
}

//-------------------------Help method- Demote-------------------------//
private void Demote(WAVLNode n){ 
n.rank-=1;
}


	//-------------------------Help method- INSERT Cases-------------------------//
private int insertCases(WAVLNode n){ 
	WAVLNode pointer=n;
	int counter=0;
	while( ((pointer!=root)) && ( (DiffType(pointer.parent).equals("01") )||( DiffType(pointer.parent).equals("10"))))  //Case1
	{
		Promote(pointer.parent);
		pointer=pointer.parent;
		counter++;
	}
	if((pointer==root)||(Islegal(pointer.parent)))//Case1 solve the problem
	{
		return counter;
	}
	else	
	{
		while(pointer!=root)
		{
			if((DiffType(pointer.parent).equals("02")) & (DiffType(pointer).equals("12"))) // Case2
			{
				pointer= RotateRight(pointer.parent);
				counter+=1; //1 rotate, without- 1 demote
				return counter;
			}
			else if(DiffType(pointer.parent).equals("20") & DiffType(pointer).equals("12")) // Case2 symetric
			{
				pointer= RotateRightLeft(pointer.parent);	
				counter+=2;  //2 rotates, without the inner- 2 demotes, 1 promote
				return counter;
			}
			else if(DiffType(pointer.parent).equals("02") & DiffType(pointer).equals("21")) // Case3 
			{
				pointer = RotateLeftRight(pointer.parent);
				counter+=2;   //2 rotates, without- 2 demotes, 1 promote
				return counter;
			}
			else if(DiffType(pointer.parent).equals("20") & DiffType(pointer).equals("21")) // Case3 symetric
			{
				pointer= RotateLeft(pointer.parent);		
				counter+=1;  //1 rotate, without- 1 demote
				return counter;
			}
		}
		return counter;
	}
}

	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~ Insert ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~//

 /**
  * public int insert(int k, String i)
  *
  * inserts an item with key k and info i to the WAVL tree.
  * the tree must remain valid (keep its invariants).
  * returns the number of rebalancing operations, or 0 if no rebalancing operations were necessary.
  * returns -1 if an item with key k already exists in the tree.
  */
  public int insert(int k, String i) {
	  size+=1;
	  int numOfBalance=0;
	  if(this.empty())
		  this.InsertFirst(k, i);
	  else if (this.search(k) != null)  //already in the tree
	  {
		  size-=1;
		  return -1;
	  }
	  else
	  {
		  WAVLNode newnode= new WAVLNode(k,i);  //insert to the tree- after this we'll update ranks	  
		  insertInPlace(newnode);
		  if (newnode.parent == root){
			  Promote(newnode.parent);
			 // System.out.println("when insert  "+  k + "   num of balance operations was 1");
			  return 1;
		  }
		  if(newnode.parent.rank!=0){  //No-Leaf Parent- you jast need to Insert. the ranks are update! 	
			 // System.out.println("when insert  "+  k + "   num of balance operations was 0");
			  return 0; 
		  }	  
		  if(newnode.parent.rank-newnode.rank==0)
			  numOfBalance=insertCases(newnode);
	  }	 
	 // System.out.println("when insert  "+  k + "   num of balance operations was  "+ numOfBalance);
	  return numOfBalance;	
  }

  
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~ Delete ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~//

 /**
  * public int delete(int k)
  *
  * deletes an item with key k from the binary tree, if it is there;
  * the tree must remain valid (keep its invariants).
  * returns the number of rebalancing operations, or 0 if no rebalancing operations were needed.
  * returns -1 if an item with key k was not found in the tree.
  */
  public int delete(int k)
  {
	  if (size == 0){ // empty tree
		  System.out.println("Deleting from empty WAVLTree!");
		  return -1;
	  }
	  WAVLNode current = findNode(k);  
	  if (current == null){ 
		  System.out.println("No such key in WAVLTree!");
		  return -1;
	  }
	  if ((this.size == 2) && (current == root)){
		  if(current.left == extLeaf)
			  root = current.right;
		  if(current.right == extLeaf)
			  root = current.left;
		  root.parent = sentinel;
		  size = 1;
		  return 0;
	  }
	  if (size == 1) { // delete root. 
		  System.out.println("WAVLTree is now empty.");
		  root = this.new WAVLNode();
		  root.rank = -2;	  
		  root.left = extLeaf;
		  root.right = extLeaf;
		  size = 0;
		  return 0;
	  } 

	  if (!((current.left.rank == -1) && (current.right.rank == -1))) {
		  current = replaceInnerNode(current);
		  }
	  
	  
	  
	  WAVLNode parent = current.parent;
	  if (((current.left.rank == -1) && (current.right.rank == -1))) {
		  deleteLeaf(current);
	  }
	  if (((current.left.rank == -1) || (current.right.rank == -1))) {
		  
		  deleteUnary(current);
	  }
	  size--;
	  int rot = 0; // number of rotations
	  rot = deleteCases(parent);    
	  return rot;
  }
  
//-------------------------Help method-  DELETE cases-------------------------//

  private int deleteCases(WAVLNode current){
	  int reb = 0;
	  while (true)
	  {
		  if((DiffType(current).equals("22")) && (current.right==extLeaf) && (current.left==extLeaf))// if 2-2 && extleaf rec != root
				{
					Demote(current);
					if(current==root)
						return reb++;
					else{
						current = current.parent;
						continue;
					}
					
				}
		  if(Islegal(current)) // if legal - return  
			  return reb;
		  if((DiffType(current).equals("32"))||(DiffType(current).equals("23"))) // if 3-2/2-3 demote rec != root
		  {
			  Demote(current);
			  if(current==root)
					return reb++;
				else{
					current = current.parent;
					continue;
				}
		  }
		  if((DiffType(current).equals("31"))&&(DiffType(current.right).equals("22")))		  	// if 3-1&&R2-2 DD rec notroot
		  {
			  Demote(current);
			  Demote(current.right);
			  if(current==root)
					return reb+2;
				else{
					current = current.parent;
					continue;
				}
		  }
		  if((DiffType(current).equals("13"))&&(DiffType(current.left).equals("22")))		  	// // if 1-3&&L2-2 DD rec notroot- symetric
		  {
			  Demote(current);
			  Demote(current.left);
			  if(current==root)
					return reb+2;
				else{
					current = current.parent;
					continue;
				}
		  }
		  if((DiffType(current).equals("31"))&&((DiffType(current.right).equals("21"))||(DiffType(current.right).equals("11"))))	// if 3-1&&R$-1 RL ret
		  {
			  current = RotateLeft(current);
			  Promote(current);
			  WAVLNode temp=current.left;
			  if((DiffType(temp).equals("22")) && (temp.right==extLeaf) && (temp.left==extLeaf))
			  {
				  Demote(temp);
			  }
			  
				  
			  reb++;   
			  return reb;
		  }  
		  if((DiffType(current).equals("13"))&&((DiffType(current.left).equals("12"))||(DiffType(current.left).equals("11"))))	  // if 1-3&&L1-$ RR ret symetric
		  {
			  current = RotateRight(current);
			  Promote(current);
			  WAVLNode temp=current.right;
			  if((DiffType(temp).equals("22")) && (temp.right==extLeaf) && (temp.left==extLeaf))
			  {
				  Demote(temp);
			  }
			  reb++;  
			  
			  return reb;
		  } 
		  if((DiffType(current).equals("31"))&&(DiffType(current.right).equals("12")))  // if 3-1&&R1-2 RRL ret
		  {
			  Demote(current);
			  current= RotateRightLeft(current);
			  Promote(current);			  
			  reb+=2;   
			  return reb;
		  }  
		  if((DiffType(current).equals("13"))&&(DiffType(current.left).equals("21")))  // if 1-3&&L2-1 RLR ret symetric
		  {
			  Demote(current);
			  current= RotateLeftRight(current);
			  Promote(current);		
			  reb+=2;  
			  return reb;
		  }  
 		
		  System.out.println("CASES EROR" );  
	  }
	
	 
  }
  
  
//-------------------------Help method- like search, but returns a pointer.-------------------------//
  private WAVLNode findNode(int k)  
  {
		WAVLNode current = root;
		while ((current != null)&&(current != extLeaf)){
			if (current.key == k)
			{	
				return current;
			}
			if (current.key > k)
				current = current.left;
			if (current.key < k)
				current = current.right;
				}
		return null;
  }
  
  
//-------------------------Help method- deletes a given leaf, no re-balancing-------------------------//

  private void deleteLeaf(WAVLNode current)  
  {											
	  if (current.parent.left == current)
		  current.parent.left = extLeaf;
	  if (current.parent.right == current)
		  current.parent.right = extLeaf;
  }
  private void deleteUnary(WAVLNode current){

	  if (current.parent.left == current){
		  if (current.left != extLeaf){
			  current.parent.left = current.left;
			  current.left.parent = current.parent;
		  }
		  if (current.right != extLeaf){
			  current.parent.left = current.right;
			  current.right.parent = current.parent;
		  }
	  }
	  if (current.parent.right == current){
		  if (current.left != extLeaf){
			  current.parent.right = current.left;
			  current.left.parent = current.parent;
		  }
		  if (current.right != extLeaf){
			  current.parent.right = current.right;
			  current.right.parent = current.parent;
		  }
	  } 
	  
	  
  }
  
//-------------------------Help method- "rolling down" the problem-------------------------//

  private WAVLNode replaceInnerNode(WAVLNode current){ // @pre - node isn't a leaf. 
	  WAVLNode origin = current;
	  if ((current.right.rank != -1) && (current.left.rank != -1)){     //Successor
		  current = current.right;
		  while (current.left.rank != -1)
			  current = current.left;	  
	  }
	 swapData(origin, current);   
	 

	  return current;
  }
  
//-------------------------Help method- swap Data-------------------------//

  private void swapData(WAVLNode a, WAVLNode b){
	  String s = a.info;
	  int k = a.key;
	  a.info = b.info;
	  a.key = b.key;
	  b.info = s;
	  b.key = k;
  }

  
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~ Min ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~//

  /**
   * public String min()
   *
   * Returns the ifo of the item with the smallest key in the tree,
   * or null if the tree is empty
   */
  public String min()
  {
	 if (this.empty()) // if empty
		 return null;
	  WAVLNode current = root;
	  while (current.left != extLeaf){ // go left all the way.
		  current = current.left;
	  }
	  return current.info;
	   
  }

	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~ Max ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~//

  /**
   * public String max()
   *
   * Returns the info of the item with the largest key in the tree,
   * or null if the tree is empty
   */
  public String max()
  {
	if (this.empty()) // if empty
		return null;
	  WAVLNode current = root;
	  while (current.right != extLeaf){ // go right all the way.
		  current = current.right;
	  }
	  return current.info;
  }

  
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~ Keys -> array ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~//

 /**
  * public int[] keysToArray()
  *
  * Returns a sorted array which contains all keys in the tree,
  * or an empty array if the tree is empty.
  */
 public int[] keysToArray()
 {
       WAVLNode[] arr = new WAVLNode[size]; 
       int[] resultKey=new int[size];
       if(empty())
       {
    	   return resultKey;
       }
       else
       {
    	   WAVLNode n=root;
    	   int i=0;
    	   i=Sort(i,n,arr);
       }
       for(int i=0; i<size; i++)
       {
    	   resultKey[i]= arr[i].key;  
       }
       return resultKey;           
 }

 
	//-------------------------Help method- Sort-------------------------//

	private int Sort(int i, WAVLNode n, WAVLNode[] arr) {
		if(IsLeaf(n))
		{
			arr[i]=n;
			return (i+1);
		}
		else if(n.rank==-1)
		{
			return i;
		}
		else
		{
			i=Sort(i,n.left,arr);
			arr[i]=n;
			i=Sort(i+1,n.right,arr);
		}
		return i;
	}

	
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~ Info -> array ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~//

 /**
  * public String[] infoToArray()
  *
  * Returns an array which contains all info in the tree,
  * sorted by their respective keys,
  * or an empty array if the tree is empty.
  */
 public String[] infoToArray()
 {
     WAVLNode[] arr = new WAVLNode[size]; 
     String[] resultinfo=new String[size];
     if(empty())
     {
  	   return resultinfo;
     }
     else
     {
  	   WAVLNode n=root;
  	   int i=0;
  	   i=Sort(i,n,arr);
     }
     for(int i=0; i<size; i++)
     {
  	   resultinfo[i]= arr[i].info;  
     }
     return resultinfo;           
}

 
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~ Size ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~//

  /**
   * public int size()
   *
   * Returns the number of nodes in the tree.
   *
   * precondition: none
   * postcondition: none
   */
  public int size()
  {
	   return size; 
  }
  
 /**
  * public class WAVLNode
  *
  * If you wish to implement classes other than WAVLTree
  * (for example WAVLNode), do it in this file, not in 
  * another file.
  * This is an example which can be deleted if no such classes are necessary.
  */
  
	 //-------------------------WAVL NODE CLASS-------------------------//
 public class WAVLNode{ // inner class node - currently just one type. morWAVLNode, alexWAVLNode and arnonEvronWAVLNode are currently in developing and will be introduced in v2.0.
	 
	 public WAVLNode left = null;
	 public WAVLNode right = null;
	 public WAVLNode parent = null;
	 public int rank;
	 public int key;
	 public String info;
	 
	 //-------------------------NODE BUILDER 1- empty-------------------------//
	 public WAVLNode(){ 
	 }
	 
	 //-------------------------NODE BUILDER 2- key & info-------------------------//
	 public WAVLNode(int key, String info){ 
		this.rank = 0;
		this.left = this.right = extLeaf;
		this.key = key;
		this.info = info;
		
	 }
 }

}





