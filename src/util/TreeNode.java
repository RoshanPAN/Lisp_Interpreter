package util;

/*
* Binary Tree Node definition - inner class
*/
public class TreeNode {

    private TreeNode left;
    private TreeNode right;
    private String lexval;
    private Token token;

    /*
    Create inner node. (assign null as token field.)
     */
    public TreeNode(){
        this(null);
    }

    public TreeNode(Token token){
        this.token = token;
        if (token == null){
            this.lexval = "*";
        }else{
            this.lexval = token.getLexval();
        }
        this.setLeft(null);
        this.setRight(null);
    }


    public String getLexicalVal(){
            return this.lexval;
        }

    @Override
    public String toString(){
        StringBuilder builder = new StringBuilder();
        builder.append("[");
        if(getLeft() != null)  builder.append(getLeft().getLexicalVal());
        else builder.append("null");
        builder.append(" <- ");
        builder.append(this.getLexicalVal());
        builder.append(" -> ");
        if(getRight() != null) builder.append(getRight().getLexicalVal());
        else builder.append("null");
        builder.append("]");
        return builder.toString();
    }

    public boolean isLeaf() {
        return this.getLeft() == null && this.getRight() == null;
    }

    public TreeNode getLeft() {
        return left;
    }

    public void setLeft(TreeNode node){
            this.left = node;
        }

    public TreeNode getRight() {
        return right;
    }

    public void setRight(TreeNode node){
            this.right = node;
        }


    public Token getToken() {
        return token;
    }

    public void setToken(Token token) {
        this.token = token;
    }

}
