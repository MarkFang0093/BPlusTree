
/**
 * Do NOT modify.
 * This is the class with the main function
 */

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.io.BufferedWriter;

/**
 * B+Tree Structure Key - StudentId Leaf Node should contain [ key,recordId ]
 */
class BTree {

    /**
     * Pointer to the root node.
     */
    private BTreeNode root;
    /**
     * Number of key-value pairs allowed in the tree/the minimum degree of B+Tree
     **/
    private int t;

    BTree(int t) {
        this.root = null;
        this.t = t;
    }

    long search(long studentId) {
        /**
         * TODO: Implement this function to search in the B+Tree. Return recordID for
         * the given StudentID. Otherwise, print out a message that the given studentId
         * has not been found in the table and return -1.
         */

        return tree_search(root, studentId);
    }

    /**
     * helper method recursion
     */
    long tree_search(BTreeNode root, long studentId) {
        if (root == null) {
            return root.keys.get(0);
        }
        if (root.leaf) // if *nodepointer is a leaf
            return root.values.get(0); // return recordID of this leaf
        else { // ELSE this is not a leaf node. don't use values[].

            if (studentId < root.keys.get(0)) { // go to left hand tree, compare with left-most key value
                return tree_search(root.children.get(0), studentId);
            } else {
                int lengthOfRoot = root.keys.size();
                if (studentId >= root.keys.get(lengthOfRoot - 1)) { // go to right hand tree compare with right-most key
                                                                    // value
                    return tree_search(root.children.get(1), studentId);
                } else { // leaf node traversal of intermediate values of root
                    // find i such that Ki < K < Ki+1:
                    int i = 1;
                    while (root.keys.get(i) <= studentId) {
                        i++;
                    }
                    return tree_search(root.children.get(i), studentId);
                }
            }
        }
    }

    // helper method to write a new student's info to the Student.vsb
    void writeToCsv(Student student) throws IOException {

        
            try{
                FileWriter csvWriter = new FileWriter("Student.csv");
                csvWriter.append(student.studentId + "," + student.studentName + "," + student.major+
                    ","+ student.level+","+ student.age+","+ student.recordId);
        
                /*^^^^^^^*/
            }
            catch(IOException ex){
                System.out.println("FILE EXCEPTION");
            }
    }   

    BTree insert(Student student) throws IOException { 
        /**
         * Implement this function to insert in the B+Tree.
         * Also, insert in student.csv after inserting in B+Tree.
         */
        
        BTreeNode curr = new BTreeNode(t, true);
        int maxDegree = 2*t; //max number of keys for a node
        //int maxChildren = (2*t)+1; //max number of children allowed for node
        
        // Case 1: Inserting to an Empty B Plus Tree
        if(root == null){  //empty tree, this is the root now. 
            root = new BTreeNode(t, false); 
            root.keys.add(student.studentId); 
        }
       
        // Case 2: Only one node that is not full
        else if(root.children.isEmpty() && root.keys.size() < maxDegree) {//go to leaf and insert. split if needed. grows upwards
            // For all insertions until the root gets overfull for the first
            // time, we just update the root node, adding the new keys:
                insertLeafNode(student, this.root);
        }

        // Case 3: Normal insert
        else{
            // traverse to the last level 
            while (!(curr.children.isEmpty())) {
				curr = curr.children.get(searchInternalNode(student, curr));
            }
            insertLeafNode(student, curr);
			if (curr.keys.size() == maxDegree) {
				// If the external node becomes full, we split it
                splitLeafNodes(curr, maxDegree);
            }
        }

        writeToCsv(student); //write to Student.csv
        return this;
    }

    /**
     * helper method to insert the key
     */
    void insertLeafNode(Student student, BTreeNode node){
        int index;
         
        index = searchInternalNode(student, node); 
        if(index < node.keys.size()) { 
            node.keys.set(index, student.studentId);
            node.leaf = true; 
        }
    }

    /**
     * split the leaf nodes if maxKeys(int t) is reached
     * | 1 | 2 | 3 | 5 | with insert 4 becomes :
     * 
     *              | 3 | | (internal) 
     * | 1 | 2 | - | - | and | 3 | 4 | 5 | - | (leaf) 
     * @param node
     * @param m
     */
    void splitLeafNodes(BTreeNode node, int m){
        // Find the middle index
        int middleIndex = m / 2;
        BTreeNode middle = new BTreeNode(t, false);
        BTreeNode rightPart = new BTreeNode(t, true);
        BTreeNode leftPart = new BTreeNode(t, true);

        // Set the right part to have middle element and the elements right to the middle element
        for(int i = middleIndex; i <node.keys.size() ; i++){
            for(int j = 0; j < 2*t ; j++){
                rightPart.keys.set(j, node.keys.get(i));
            } 
        }
        middle.children.add(rightPart);

        // Set the left part to have middle element and the elements left to the middle element
        for(int i = 0; i <middleIndex ; i++){
            for(int j = 0; j < 2*t ; j++){
                leftPart.keys.set(j, node.keys.get(i));
            } 
        }
        middle.children.add(rightPart);
        
    }
    
    /**
     *  look throughout the nodes for  a specific value 
     * @param student
     * @return int the first index of the list at which the key which is greater
	 *         than the input key
     */
    int searchInternalNode(Student student, BTreeNode node)
    {
        long key = student.recordId; //the key to be searched
        int start = 0; 
        int end = node.keys.size()- 1;
        int middle; 
        int index = -1;
        
        // Return first index if key is less than the first element
        if(key < node.keys.get(start)) {
            return 0; 
        }
        // Return array size + 1 as the new position of the key if greater than the last element
        if(key >= node.keys.get(end)) {
			return node.keys.size();
        }
        //otherwise it belongs in the middle of the node 
        while(start <= end){
            middle = (start+end)/2; 
            if(key<node.keys.get(middle) && key>= node.keys.get(middle-1) ){
                index = middle;
                break;
            }
            else if(key>=node.keys.get(middle)){
                start = middle + 1;
            }
            else {
				end = middle - 1;
			}
        }
        
        return index; 
    }


    boolean delete(long studentId) {
        /**
         * TODO:
         * Implement this function to delete in the B+Tree.
         * Also, delete in student.csv after deleting in B+Tree, if it exists.
         * Return true if the student is deleted successfully otherwise, return false.
         */
        return true;
    }    

    List<Long> print() {

        List<Long> listOfRecordID = new ArrayList<>();
        /**
         * TODO:
         * Implement this function to print the B+Tree.
         * Return a list of recordIDs from left to right of leaf nodes.
         *
         */
        if(root == null){
            return listOfRecordID; 
        }

        // traverse to the last level 
        while (!(root.children.isEmpty())) {
            root = root.children.get(0);
        }
        if(root.leaf){
            while(root.next != null){
                for(int i = 0; i<root.values.size(); i++){
                    listOfRecordID.add(root.values.get(i)); //add this key value of the leaf to the arraylist 
                }
                root = root.next; //inc. ptr to next leaf
            }
        }
        return listOfRecordID;
    } 
}
