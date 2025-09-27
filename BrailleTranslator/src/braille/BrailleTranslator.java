package braille;

import java.util.ArrayList;

/**
 * Contains methods to translate Braille to English and English to Braille using
 * a BST.
 * Reads encodings, adds characters, and traverses tree to find encodings.
 * 
 * @author Seth Kelley
 * @author Kal Pandit
 */
public class BrailleTranslator {

    private TreeNode treeRoot;

    /**
     * Default constructor, sets symbols to an empty ArrayList
     */
    public BrailleTranslator() {
        treeRoot = null;
    }

    /**
     * Reads encodings from an input file as follows:
     * - One line has the number of characters
     * - n lines with character (as char) and encoding (as string) space-separated
     * USE StdIn.readChar() to read character and StdIn.readLine() after reading
     * encoding
     * 
     * @param inputFile the input file name
     */
    public void createSymbolTree(String inputFile) {

        /* PROVIDED, DO NOT EDIT */

        StdIn.setFile(inputFile);
        int numberOfChars = Integer.parseInt(StdIn.readLine());
        for (int i = 0; i < numberOfChars; i++) {
            Symbol s = readSingleEncoding();
            addCharacter(s);
        }
    }

    /**
     * Reads one line from an input file and returns its corresponding
     * Symbol object
     * 
     * ONE line has a character and its encoding (space separated)
     * 
     * @return the symbol object
     */
    public Symbol readSingleEncoding() {
        // WRITE YOUR CODE HERE
        char c = StdIn.readChar();
        String encode = StdIn.readString();
        StdIn.readLine();
        Symbol bob = new Symbol(c, encode);
        return bob; // Replace this line, it is provided so your code compiles
    }

    /**
     * Adds a character into the BST rooted at treeRoot.
     * Traces encoding path (0 = left, 1 = right), starting with an empty root.
     * Last digit of encoding indicates position (left or right) of character within
     * parent.
     * 
     * @param newSymbol the new symbol object to add
     */
    public void addCharacter(Symbol newSymbol) {
        // WRITE YOUR CODE HERE

        String encode = newSymbol.getEncoding();
        String partialEncoding = "";
        Symbol work = new Symbol(partialEncoding);
        if (treeRoot == null) {
            treeRoot = new TreeNode(work, null, null);
        }
        TreeNode ptr = treeRoot;
        for (int i = 0; i < encode.length(); i++) {
            char direction = encode.charAt(i);
            partialEncoding += direction;
            if (direction == 'L') {
                if (ptr != null && ptr.getLeft() == null) {
                    if (i == encode.length() - 1) {
                        Symbol pE = newSymbol;
                        ptr.setLeft(new TreeNode(pE, null, null));
                    } else {
                        Symbol pE = new Symbol(partialEncoding);
                        ptr.setLeft(new TreeNode(pE, null, null));
                    }
                }
                ptr = ptr.getLeft();
            } else if (direction == 'R') {
                if (ptr != null && ptr.getRight() == null) {
                    if (i == encode.length() - 1) {
                        Symbol pE = newSymbol;
                        ptr.setRight(new TreeNode(pE, null, null));
                    } else {
                        Symbol pE = new Symbol(partialEncoding);
                        ptr.setRight(new TreeNode(pE, null, null));
                    }
                }
                ptr = ptr.getRight();
            }
            /*
             * if direction is left, go left:
             * if there's no node here, make a new one
             * if direction is right, go right:
             * if there's no node here, make a new one
             * 
             */
        }
    }

    /**
     * Given a sequence of characters, traverse the tree based on the characters
     * to find the TreeNode it leads to
     * 
     * @param encoding Sequence of braille (Ls and Rs)
     * @return Returns the TreeNode of where the characters lead to, or null if
     *         there is no path
     */
    public TreeNode getSymbolNode(String encoding) {
        // WRITE YOUR CODE HERE
        TreeNode ptr = treeRoot;
        for (int i = 0; i < encoding.length(); i++) {
            char direction = encoding.charAt(i);
            if (direction == 'L') {
                if (ptr.getLeft() == null) {
                    return null;
                }
                ptr = ptr.getLeft();
            } else if (direction == 'R') {
                if (ptr.getRight() == null) {
                    return null;
                }
                ptr = ptr.getRight();
            }
        }
        return ptr; // Replace this line, it is provided so your code compiles
    }

    /**
     * Given a character to look for in the tree will return the encoding of the
     * character
     * 
     * @param character The character that is to be looked for in the tree
     * @return Returns the String encoding of the character
     */

    public String findBrailleEncoding(char character) {
        // WRITE YOUR CODE HERE
        TreeNode ptr = treeRoot;
        // check if ptr is a leaf node
        // then check if character matches
        // else return null1
        return help(ptr, character);
        // Replace this line, it is provided so your code compiles
    }

    public String help(TreeNode curr, char c) {
        if (curr == null) {
            return null;
        }
        if (curr.getLeft() == null && curr.getRight() == null) {
            if (curr.getSymbol().getCharacter() == c) {
                return curr.getSymbol().getEncoding();
            }
        }
        String left = help(curr.getLeft(), c);
        if (left != null) {
            return left;
        }

        String right = help(curr.getRight(), c);
        return right;

    }

    /**
     * Given a prefix to a Braille encoding, return an ArrayList of all encodings
     * that start with
     * that prefix
     * 
     * @param start the prefix to search for
     * @return all Symbol nodes which have encodings starting with the given prefix
     */

    /*
     * Use getSymbolNode to find the TreeNode that corresponds to the starting
     * encoding (call it startNode). If this method is implemented correctly, it
     * should work for partial encoding nodes as well (ex: those that hold partial
     * encodings but not characters; non-leaf nodes).
     * Nothing happens if the node doesn’t exist in the tree – return an empty
     * ArrayList in this case.
     * Conduct a preorder traversal (ex: preorder(startNode)) from the start node to
     * find all leaves under it, populate an ArrayList of Symbols that store all
     * characters under the startNode, and return an ArrayList of Symbols
     * representing characters.
     * Traverse the root of a subtree, then recursively visit its left and right
     * subtrees.
     * This is similar to the preOrder() method in your BST traversal lab.
     */
    public ArrayList<Symbol> encodingsStartWith(String start) {
        // WRITE YOUR CODE HERE
        TreeNode startNode = getSymbolNode(start);
        ArrayList<Symbol> list = new ArrayList<>();
        if (startNode == null) {
            return list;
        }
        preOrderHelper(startNode, list);
        return list;
        // Replace this line, it is provided so your code compiles
    }

    public void preOrderHelper(TreeNode start, ArrayList<Symbol> list) {
        if (start == null) {
            return;
        }
        if (start.getLeft() == null && start.getRight() == null) {
            list.add(start.getSymbol());
        }
        preOrderHelper(start.getLeft(), list);
        preOrderHelper(start.getRight(), list);
    }

    /**
     * Reads an input file and processes encodings six chars at a time.
     * Then, calls getSymbolNode on each six char chunk to get the
     * character.
     * 
     * Return the result of all translations, as a String.
     * 
     * @param input the input file
     * @return the translated output of the Braille input
     */
    public String translateBraille(String input) {
        // WRITE YOUR CODE HERE
        StdIn.setFile(input);
        String result = "";
        String StringI = StdIn.readString();
        for (int i = 0; i < StringI.length(); i += 6) {
            String temp = StringI.substring(i, i + 6);
            TreeNode node = getSymbolNode(temp);
            result += node.getSymbol().getCharacter();
        }
        return result; // Replace this line, it is provided so your code compiles
    }

    /**
     * Given a character, delete it from the tree and delete any encodings not
     * attached to a character (ie. no children).
     * 
     * @param symbol the symbol to delete
     */
    /*
     * To complete this method:
     * 
     * Find the parent and target nodes to delete.
     * As stated above, you can traverse the tree similarly to how you traversed in
     * getSymbolNode, and keep track of both variables.
     * OR you can use getSymbolNode(String encoding) to find the parent and target –
     * remember that you can find the parent encoding by removing the last character
     * from the longer one.
     * If the target node is a leaf, unhook it from its parent.
     * Repeat this for each partial encoding (ex: if deleting s – LRRLRL, check
     * LRRLR, LRRL, etc)
     * Make sure that if treeRoot (the instance variable) is a leaf, that it’s also
     * deleted as well.
     */
    public void deleteSymbol(char symbol) {
        // WRITE YOUR CODE HERE
        String encode = findBrailleEncoding(symbol);
        for (int i = encode.length(); i > 0; i--) {
            TreeNode target = getSymbolNode(encode.substring(0, i));
            TreeNode parent = getSymbolNode(encode.substring(0, i - 1));
            if (target.getLeft() == null && target.getRight() == null) {
                char direction = encode.charAt(i - 1);
                if (direction == 'L') {
                    parent.setLeft(null);
                } else if (direction == 'R') {
                    parent.setRight(null);
                }
            }
        }
        if (treeRoot.getLeft() == null && treeRoot.getRight() == null) {
            treeRoot = null;
        }
    }

    public TreeNode getTreeRoot() {
        return this.treeRoot;
    }

    public void setTreeRoot(TreeNode treeRoot) {
        this.treeRoot = treeRoot;
    }

    public void printTree() {
        printTree(treeRoot, "", false, true);
    }

    private void printTree(TreeNode n, String indent, boolean isRight, boolean isRoot) {
        StdOut.print(indent);

        // Print out either a right connection or a left connection
        if (!isRoot)
            StdOut.print(isRight ? "|+R- " : "--L- ");

        // If we're at the root, we don't want a 1 or 0
        else
            StdOut.print("+--- ");

        if (n == null) {
            StdOut.println("null");
            return;
        }
        // If we have an associated character print it too
        if (n.getSymbol() != null && n.getSymbol().hasCharacter()) {
            StdOut.print(n.getSymbol().getCharacter() + " -> ");
            StdOut.print(n.getSymbol().getEncoding());
        } else if (n.getSymbol() != null) {
            StdOut.print(n.getSymbol().getEncoding() + " ");
            if (n.getSymbol().getEncoding().equals("")) {
                StdOut.print("\"\" ");
            }
        }
        StdOut.println();

        // If no more children we're done
        if (n.getSymbol() != null && n.getLeft() == null && n.getRight() == null)
            return;

        // Add to the indent based on whether we're branching left or right
        indent += isRight ? "|    " : "     ";

        printTree(n.getRight(), indent, true, false);
        printTree(n.getLeft(), indent, false, false);
    }

}
