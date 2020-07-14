
/**
 * Do NOT modify.
 * This is the class with the main function
 */

import java.io.BufferedReader;
import java.io.FileReader;
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
            return root.keys.get(0);
        }
        if(root.leaf) //if *nodepointer is a leaf
            return root.values.get(0); //return recordID of this leaf 
        else{  // ELSE this is not a leaf node. don't use values[].
            
            if(studentId < root.keys.get(0)){ //go to left hand tree, compare with left-most key value
                return tree_search(root.children.get(0), studentId);              
            }
            else{ 
                int lengthOfRoot = root.keys.size(); 
                if(studentId >= root.keys.get(lengthOfRoot - 1)){  //go to right hand tree compare with right-most key value
                    return tree_search(root.children.get(1), studentId);
                }
                else{           // leaf node traversal of intermediate values of root
                    //find i such that Ki < K < Ki+1:
                    int i = 1; 
                    while(root.keys.get(i) <= studentId){
                        i++; 
                    }
                    return tree_search(root.children.get(i), studentId);
                }
            }           
        }      
    }
    //helper method to write a new student's info to the Student.vsb
    Student writeToCsv(Student student){

        BufferedReader csvWriter = new BufferedReader(new FileReader("Student.csv"));
        //TODO: fix this below 
        csvWriter.append(student.studentId, "," + student.studentName + "," + student.major+
            ","+ student.level+","+ student.age+","+ student.recordId);

    }

    BTree insert(Student student) { 
        /**
         * TODO:
         * Implement this function to insert in the B+Tree.
         * Also, insert in student.csv after inserting in B+Tree.
         */
        
        BTreeNode node = new BTreeNode(root.t, true);
        BTreeNode curr = new BTreeNode(root.t, true);
        BTreeNode prev = new BTreeNode(root.t, true);

        int maxDegree = 2*(node.t); //max number of keys for a node
        int maxChildren = 2*(node.t)+1; //max number of children allowed for node
        
        if(root == null){ //empty tree, this is the root now. 
            root.keys.add(0, student.studentId); 
            //write to Student.csv
            writeToCsv(student); 
        }
        //previous node set to leaf= false
        //set recordID for new leaf nodes, remove from internal nodes
        else{//go to leaf and insert. split if needed. grows upwards
            //choose the subtree, find i s.t. Ki <= entry's key value < J(i+1)

            //If the bucket is not full (at most b 1 entries after the insertion), add the record.
            if(node.next == null && node.keys.size() < maxDegree ) //there is space in the bucket

            if(node.keys.size() >= maxDegree){
                split(); //splitting stuff
            }
            else{ 
                prev = node;
                curr = node.next; 
                node = node.next.next;
                if(prev<curr && curr<node ){
                    
                }
            }
        }


        
         
         
         


        return this;
    }

    /**
     * helper method for insert
     * Split the node if the parent is full 
     */
    void split(){

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
