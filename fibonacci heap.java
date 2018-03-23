import java.io.*;

public class heap 
{
	//defining members of class heap 
	node min;
	int n;
	static class node //structure of a node in heap
	{
		int key;
		node p;
		node child;
		node left;
		node right;
		int degree;
		boolean mark;	
		node(int k){ key=k;}
	}
	public heap make_fib_heap(heap H) //creates and returns new heap containing no elements
	{
		H.n=0;
		H.min= null;
		return H;
	}
	public void fib_heap_insert(heap H, node x) //inserts an element in the heap
	{
		x.degree= 0;		//setting degree of new node to zero
		x.p= null;			//parent of new node is null as it is added in the root list
		x.child=null;		//new node has no child
		x.mark= false;		// mark of new node is false
		
		//making heap with only one node
		if(H.min==null)
		{
			x.left= x;
			x.right= x;
			H.min= x;
		}
		
		//adding node x to heap if heap is not empty
		else
		{
			x.left= H.min;
			x.right= H.min.right;
			H.min.right.left= x;
			H.min.right= x;
			if(x.key < H.min.key)
				H.min= x;
		}
		H.n= H.n+1;			//updating number of nodes in the heap
	}
	public heap heap_union(heap H1, heap H2)  		//function to merge two heaps and storing the resultant heap in the first heap
	{
		//if any one heap is null then return the other heap
		if(H1.min== null)
			return H2;
		else if(H2.min==null)
			return H1;
		node x,y,xleft,yleft;
		//System.out.println("Merging heap");
		x= H1.min;
		y= H2.min;
		xleft= x.left;
		yleft= y.left;
		//merging the root list of the two heaps
		x.left= yleft;
		yleft.right =x;
		y.left= xleft;
		xleft.right=y;
		//System.out.println("Merged heap");
		//finding the minimum node of the new heap
		if(H2.min.key<H1.min.key)
			H1.min= H2.min;
		//updating first heap with the new heap and deleting select heap
		H1.n= H1.n+ H2.n;
		H2.min= null;
		H2.n=0;
		//System.out.println("Updated heap"+H1.min.key);
		return H1; 			//returning merged heap
	}
	public node delete_min(heap H)		//to delete minimum node from the heap
	{
		node z= H.min;
		if(z!= null)		//if heap is not empty
		{
			if(z.child!=null)		//if the minimum node has child then adding them to root list of the heap
			{
				node c= z.child;
				node x=c;
				do					//updating parent of all child of minimum node to null
				{
					x.p= null;
					x= x.right;
				}
				while(x!=c);
				// adding all child of z in the root list of H
				
				z.left= c.left;
				c.left.right= z;
				c.left= z.left;
				z.left.right= c;
			}
			
				// removing z from root list of H
			
				node left= z.left;
				node right= z.right;
				left.right= right;
				right.left= left;
			
			// defining new H.min for the heap
			if(z==z.right)
				H.min = null;
			else
			{
				H.min= z.right;
				H.consolidate(H);		//calling consolidate method
			}
			H.n = H.n-1;			//updating number of nodes in the heap
		}
		return z;					//returning the deleted node
	}
	//consolidate method makes every node in the heap to have distinct degree value
	public void consolidate(heap H)
	{
		int dn=n, d, numr=0;
		node x= H.min;
		node y;
		node A[]= new node[dn];
		for(int i=0; i<dn; i++)			//array that will store nodes wuth different value of degree
			A[i]= null;
		x= H.min;
		do								//counting number of nodes in the root list
		{
			x=x.right;
			numr++;
		}
		while(x!=H.min);
		node w= H.min;
		for(int i=1; i<= numr; i++)
		{
			x=w;
			w= w.right;
			d= x.degree;
			while(A[d]!= null)
			{
				y= A[d];				//another node with the same degree as x
				if(x.key>y.key)			//exchanging x with y
				{
					node temp= x;
					x=y;
					y=temp;
				}
				H.fib_heap_link(H,y,x);			//the node with higher key value is made child of the node with lower key value
				
				A[d]= null;
				d=d+1;
			}
				A[d]= x;
		}
		H.min= null;
		for(int i=0; i<dn; i++)				//making new heap containing the nodes stored in the array
		{
			if(A[i]!= null)					//adding A[i] to the heap
			{
				if(H.min== null)			
				{
					A[i].left= A[i];
					A[i].right= A[i];
					H.min= A[i];
				}
				else
				{
					A[i].left= H.min;
					A[i].right= H.min.right;
					H.min.right.left= A[i];
					H.min.right=A[i];
					if(A[i].key<H.min.key)
						H.min= A[i];
				}
			}
		}
	}
	public void fib_heap_link(heap H, node y, node x)		//to make y a child of x
	{	
		
		node left, right,child;
		
		//removing y from root list of H
		left= y.left;
		right= y.right;
		left.right= right;
		right.left= left;
		
		// making y a child of x and increasing its degree
		y.p=x;
		child= x.child;
		if(child!= null)		//if x has no child
		{
			y.left= child.left;
			y.right= child;
			child.left.right= y;
			child.left=y;
				
		}
		else					//if x already has any child
		{
			x.child= y;
			y.left= y;
			y.right=y;
		}
		
		x.degree= x.degree+1;	//updating degree of x
		y.mark= false;			//updating mark of y
	}
	public void decrease_key(heap H, node x, int k)				//method to decrease the key of any particular node in the heap
	{
		if(k> x.key)					//if new key is greater than the current key
		{
			System.out.println("New key is greater than current key");
			return;
		}
		x.key=k;						//updating the key of the given node
		node y= x.p;					//checking if the property of the min heap is violated or not
		if(y!=null && x.key< y.key)		//if property of min heap is violated 
		{
			H.cut(H, x, y);				//remove the given node from its parent node and add to the root list
			H.cascade_cut(H,y);			//if 2nd child of y is removed then move y to root list
		}
		if(x.key<H.min.key)				//updating the value of minimum node
			H.min= x;
	}
	public void cut(heap H, node x, node y)		//remove x from its parent node y and adding it to the root list
	{
		node left, right;
		if(x.right!= x)							//if x is not the only child of y
		{
			y.child= x.right;					//updating child of y
			left= x.left;						//removing x from child list of y
			right= x.right;
			left.right= right;
			right.left= left;
		}
		else
			y.child= null;						//if x is only child of y then y will have no child now
		//adding x to the root list
		x.left= H.min;
		x.right= H.min.right;
		H.min.right.left= x;
		H.min.right=x;
		
		x.p= null;								//updating parent of x
		x.mark= false;							//updating mark of x
		
	}
	public void cascade_cut(heap H, node y)		//if 2nd child of y is removed then move y to root list
	{
		 node z= y.p;
		 if(z!=null)							//if y has a parent
		 {
			 if(y.mark== false)					//if first child is being removed then update mark of y
				 y.mark= true;
			 else
			 {				
				 H.cut(H, y, z);				//if second child is being removed then move y to root list
				 H.cascade_cut(H, z);			//recursive call of cascade_cut on parent of y
			 }
		 }
	}
	public void delete(heap H, node x)			//deleting any node from the heap
	{
		H.decrease_key(H, x, (H.min.key-1));	//making the node to be deleted as minimum node
		H.delete_min(H);						//deleting the minimum
	}
	public node find_key(heap H, int k)			//method to find a node with key 'k' in the heap
	{
		node x=H.min;
		node z= new node(0);					
		if(x!=null)								//if heap contains any node
		{
			do									//successively searching the required node in all trees of the heap 
			{
				z=H.find_tree(x,k);
				if(z.key==k)					//if required node is found then return it
					return z;
				
				x= x.right;
			}
			while(x!=H.min);
		}
		return z;								//if the node is not found then a node with default value will be returned
	}
	public node find_tree(node x,int k)			//finding the node in the tree
	{
		node z= new node(0);
		if(x!=null)								//if tree is not empty
		{
		if(x.key==k)							//if key is found then return the node
			return x;
		node child= x.child;
		
		if(child!=null)							//if the node has any child
		{
			do
			{
				z=find_tree(child,k);			//recursive call on child to find the key
				child= child.right;				//searching in the tree of all child
			}
			while(child!=x.child);
		}
		}
		return z;								//if the node is not found then a node with default value will be returned
	}
		
	public void print_heap(heap H)				//method to print the heap
	{
		node x=H.min;
		if(x!=null)								//if heap contains any node
		{
			do									//successively printing all trees of the heap
			{
				H.print_tree(x);
				System.out.println();
				x= x.right;
			}
			while(x!=H.min);
		}
	}
	public void print_tree(node x)				//subroutine to print individual tree of the heap
	{
		System.out.print(x.key+" ");			//printing root of tree
		node child= x.child;	
		if(child==null)							//if root has no child then traversing is complete				
			return;
		System.out.print("(");
		do										//printing all child all the root
		{
			print_tree(child);					//recursively printing the tree of the child
			child= child.right;					//printing all nodes in the child list
		}
		while(child!=x.child);
		System.out.print(")");
	}
	public static void main(String args[])throws IOException		//main function
	{
		InputStreamReader in= new InputStreamReader(System.in);		//for console input
		BufferedReader bin= new BufferedReader(in);
		heap H1 = new heap();										//declaring first heap
		heap H2= new heap();										//declaring second heap
		H1=H1.make_fib_heap(H1);									//making first heap with no node
		H2=H2.make_fib_heap(H2);									//making second heap with no node
		int ch;
		do															//performing operations on the heap		
		{
			System.out.println("1. Insert a node \n2. Get minimum key \n3. Delete minimum key node \n4. Decrease key \n5. Merge two heaps \n6. Delete a node \n7. Print heap \n8. Exit");
			ch= Integer.parseInt(bin.readLine());
			int k, h=0;
			node x=new node(0);
			if(ch==1||ch==2||ch==3||ch==4||ch==6||ch==7)
			{
				System.out.println("Heap 1 or 2?");
				h= Integer.parseInt(bin.readLine());
			}
			switch(ch)
			{
				case 1:
					System.out.println("Enter key");
					k= Integer.parseInt(bin.readLine());
					x= new node(k);
					if(h==1)
						H1.fib_heap_insert(H1,x);
					if(h==2)
						H2.fib_heap_insert(H2,x);
					break;
				case 2:
					if(h==1&& H1.min!= null)
						System.out.println("Minimum key="+H1.min.key);
					else if(h==2&& H2.min!= null)
						System.out.println("Minimum key="+H2.min.key);
					else
						System.out.println("Heap empty");
					break;
				case 3:
					if(h==1)
						x= H1.delete_min(H1);
					else if(h==2)
						x= H2.delete_min(H2);
					if(x!=null)
						System.out.println("Deleted node= "+x.key);
					else
						System.out.println("Heap empty");
					break;
				case 4:
					System.out.println("Enter old key");
					int n= Integer.parseInt(bin.readLine());
					if(h==1)
						x= H1.find_key(H1, n);
					if(h==2)
						x= H2.find_key(H2, n);
					if(x.key!=n)
					{
						System.out.println("Wrong key");
						break;
					}
					System.out.println("Enter new key");
					k= Integer.parseInt(bin.readLine());
					if(h==1)
						H1.decrease_key(H1, x, k);
					if(h==2)
						H2.decrease_key(H2, x, k);
					break;
				case 5:
					H1= H1.heap_union(H1, H2);
					//if(H1.min!= null)
						H1.print_heap(H1);
					//else
						//System.out.println("Empty heap");
					break;
				case 6:
					System.out.println("Enter the node to be deleted");
					k= Integer.parseInt(bin.readLine());
					if(h==1)
						x=H1.find_key(H1, k);
					if(h==2)
						x=H2.find_key(H2, k);
					if(x.key!=k)
					{
						System.out.println("Wrong key");
						break;
					}
					if(h==1)
						H1.delete(H1, x);
					if(h==2)
						H2.delete(H2, x);
						
					break;
				case 7:
					if(h==1)
						H1.print_heap(H1);
					if(h==2)
						H2.print_heap(H2);
					break;
				case 8:
					break;
				default:
					System.out.println("Wrong choice");
			}
			
		}
		while(ch!=8);
		
	}
}
