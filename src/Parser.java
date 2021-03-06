import exception.UndefinedBehaviorException;
import functions.BuiltInFunctions;
import util.Token;
import util.TokenType;
import util.TreeNode;
import util.TreeUtil;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;

/**
 * 
 */

/**
 * @author lenovo1
 *
 */
//public class Parser implements BuiltInFunctions{
public class Parser implements BuiltInFunctions{
	/**
	 * Context-free Gramma:
	 * <Start> ::= <Expr> <Start> | <Expr> eof
	 * <Expr> ::= atom | ( <List> )
	 * <List> ::= <Expr> <List> | ε
	 */
	private LexicalScanner scn;
	private ArrayList<TreeNode> rootList;
	private ArrayList<TreeNode> resultList;

	public Parser(LexicalScanner scn) {
		// Initialize parser
		this.scn = scn;
		this.rootList = new ArrayList<>(); // root of parse tree
		this.resultList = new ArrayList<>(); // root of parse tree
	}
	
	private void moveToNextToken(){
		scn.moveToNextToken();
	}

	/*
	 * Parse <Start>
	 */
	public void  parseStart(boolean isPrint) {
		if (scn.getCurrentToken().getType() == TokenType.EOF)
			return;
		
		StringBuilder builder = new StringBuilder();
		TreeNode root = null;
		
		do{
			root = parseExpression(); // parse Top level expression
			if (isPrint) builder.append(TreeUtil.printDotNotation(root));
			this.rootList.add(root);
		}while(scn.getCurrentToken().getType() != TokenType.EOF);

		if (! this.scn.checker.isWholeStreamValid()){
			System.out.println();
			System.out.printf("Error: Invalid Parenthesis. @[parseStart] INFO: %s\n", this.scn.checker.printCheckerStatus());
			System.exit(-1);
		}
		System.out.print(builder.toString());
	}


	public void eval(boolean isPrint)
			throws UndefinedBehaviorException, NoSuchMethodException,
			IllegalAccessException, InvocationTargetException, ClassNotFoundException {
		for (TreeNode node : this.rootList){
			TreeNode evalRes = this.Eval(node);
			this.resultList.add(evalRes);
			if(isPrint)
				System.out.println(TreeUtil.printListNotation(evalRes));
		}
	}

	private TreeNode parseExpression(){
		TreeNode node = new TreeNode(); // node for current terminal/non-terminal
		Token curToken = scn.getCurrentToken();
		
		// <Expr> ::= (<List>)
		if (curToken.getType() == TokenType.OPEN_PARENTHESIS){
			moveToNextToken(); // consume OPEN_PARENTHESES
			TreeNode rightMost = node;
			while (scn.getCurrentToken().getType() != TokenType.CLOSING_PARENTHESIS) {
				if (scn.getCurrentToken().getType() == TokenType.EOF) {
					System.out.printf("Error: Invalid Parenthesis. @[parseExpression] INFO: %s\n", 
							this.scn.checker.printCheckerStatus());
					System.exit(-1);
				}
				
				rightMost.setLeft(parseExpression());
				if (scn.getCurrentToken().getType() == TokenType.CLOSING_PARENTHESIS){
					// reach the end of the LISP "list" -> NIL node
					rightMost.setRight(new TreeNode(new Token("NIL", TokenType.LITERAL_ATOM)));
				}
				else{
					// create inner node
					rightMost.setRight(new TreeNode());
				}
				rightMost = rightMost.getRight();
			}
			moveToNextToken(); // consume CLOSING_PARENTHESES
			if(rightMost.isLeaf()){
				rightMost.setToken(new Token("NIL", TokenType.LITERAL_ATOM));
			}
		}
		// <Expr> ::= Atom
		else if(curToken.getType() == TokenType.LITERAL_ATOM ||
					curToken.getType() == TokenType.NUMERIC_ATOM){
			node.setToken(curToken);
			moveToNextToken(); // consume atom
		}
		// Other -> Error
		else{
			System.out.println("Error:  Not Atom, Not Open Parenthesis@[parseExpression].");
			System.exit(-1);
		}
		return node;
	}

}
