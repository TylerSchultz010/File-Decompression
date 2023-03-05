import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

/**
 * @author Tyler Schultz
 * 11/15/22
 * We will decode a compressed file, reconstruct the huffman tree and send the decoded characters to a new file
 *
 */

public class FileDecompression {

	public static void main(String[] args) throws IOException, ClassNotFoundException {
		
		FileInputStream fileStream;
		fileStream = new FileInputStream("CompressedFile.txt");
		ObjectInputStream objStream = new ObjectInputStream(fileStream);
		HashMap codeMap = (HashMap)objStream.readObject();
		 
		objStream.close();
		fileStream.close();
		
		PrintWriter newText = new PrintWriter(new FileWriter("DecodedText.txt")); 		//the decoder output
		String code = "";
		char letter;
		
		TreeNode root = null;															//create a root for our tree, doesn't matter what the arguments are
		
		 for (Object c : codeMap.keySet())
		 {
			 code = (String) codeMap.get(c);											//cast a string to codeMap so we get our code
			 letter = (char) c;
			 TreeNode node = new TreeNode(code, letter);								//make a node with the letter and our code as arguments
			 root = createTree(root, node, 0);											//send our new node to the tree
		 }
		 
		 Scanner s = new Scanner(System.in);
		 String userString = "";
		 System.out.println("Please enter a string full of ones and zeroes and I will send the character translation to the decoded text: ");
		 userString = s.next();
		 
		 if (workable(userString) == false)												//fail safe in case the user enters something else
			 System.out.print("String is invalid. Please relaunch and enter ones or zeroes");
		 else
		 {
			 TreeNode ptr = root;														//ptr will be a temporary node for traversing
			 for (int i = 0; i < userString.length(); i++)
			 {
				 if (userString.charAt(i) == '0')										//bring ptr left or right whether its 0 or 1
					 ptr = ptr.left;
				 if (userString.charAt(i) == '1')
					 ptr = ptr.right;
				 if (ptr.left == null && ptr.right == null)
				 {
					 newText.write(ptr.c);												//when both are null the ptr should hold elements from the userString
					 ptr = root;														//reset ptr
				 }
			 }
			 newText.close();															//close our print writer when we are finished
		 }
		 
	}
	
	static TreeNode createTree(TreeNode root, TreeNode node, int index)					//index will be our position within the code
	{
		if (root == null)																	//only make a new node if its null
			root = new TreeNode("?", '?');
		
		if (index <= node.code.length() - 1)												//if the index hasn't reached the end of our code
		{
			if (node.code.charAt(index) == '0')												//if the index of the code is 0, we go left
			{
				root.left = createTree(root.left, node, index+1);
			}
			
			if (node.code.charAt(index) == '1')												//if the index of the code is 1, we go right
			{
				root.right = createTree(root.right, node, index+1);
			}
		}
		
		else
		{
			root = node;																	//node will be placed wherever root currently is
		}
		return root;
	}
	
	/*After the for loop for the key set is completed, our node tree is fully reconstructed*/
	
	static boolean workable(String userString)												//workable will make sure the strings can be translated (they're 1 or 0)
	{
		for (int i = 0; i < userString.length(); i++)
		{
			if (userString.charAt(i) != '0' && userString.charAt(i) != '1')
				return false;
		}
		return true;
	}
}

class TreeNode{																			//tree class so we can establish the different nodes and data
	
	String code;
	char c;
	
	TreeNode left,
			right;
	TreeNode(String code, char c)
	{
		this.code = code;
		this.c = c;
		left = right = null;															//when creating a new node we should establish it without children
	}
}
