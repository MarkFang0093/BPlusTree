/**
 * Do NOT modify.
 * This is the class with the main function
 */

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * B+Tree Structure
 * Key - StudentId
 * Leaf Node should contain [ key,recordId ]
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
         * TODO:
         * Implement this function to search in the B+Tree.
         * Return recordID for the given StudentID.
         * Otherwise, print out a message that the given studentId has not been found in the table and return -1.
         */

            return tree_search(root, studentId);
    }

    /**
     * helper method recursion
     */
    long tree_search(BTreeNode root, long studentId){
        if(root == null){
            return root.values[0];
        }
        if(root.leaf) //if *nodepointer is a leaf
            return root.values[0]; //return recordID of this leaf 
        else{  // ELSE this is not a leaf node. don't use values[].
            
            if(studentId < root.keys[0]){ //go to left hand tree, compare with left-most key value
                return tree_search(root.children[0], studentId);              
            }
            else{ 
                int lengthOfRoot = root.keys.length; 
                if(studentId >= root.keys[lengthOfRoot - 1]){  //go to right hand tree compare with right-most key value
                    return tree_search(root.children[1], studentId);
                }
                else{           // leaf node traversal of intermediate values of root
                    //find i such that Ki < K < Ki+1:
                    int i = 1; 
                    while(root.keys[i] <= studentId){
                        i++; 
                    }
                    return tree_search(root.children[i], studentId);
                }
            }           
        }      
    }

    BTree insert(Student student) {
        /**
         * TODO:
         * Implement this function to insert in the B+Tree.
         * Also, insert in student.csv after inserting in B+Tree.
         */
        return this;
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
            return null; 
        }
        while(root.children != null ){ //as long as this node has children
            root = root.children[0]; //point down to its far left child
        }
        listOfRecordID.add(root.values[0]); //add the left most leaf to arraylist
        
        while(root.next != null){
            root = root.next; //inc. ptr to next leaf
            listOfRecordID.add(root.values[0]); //add this key value of the leaf to the arraylist 
        }
        return listOfRecordID;
    } 
}
