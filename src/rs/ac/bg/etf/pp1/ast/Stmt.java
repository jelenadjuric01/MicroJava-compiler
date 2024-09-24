// generated with ast extension for cup
// version 0.8
// 25/8/2024 1:50:27


package rs.ac.bg.etf.pp1.ast;

public class Stmt extends Statement {

    private FinalStatement FinalStatement;

    public Stmt (FinalStatement FinalStatement) {
        this.FinalStatement=FinalStatement;
        if(FinalStatement!=null) FinalStatement.setParent(this);
    }

    public FinalStatement getFinalStatement() {
        return FinalStatement;
    }

    public void setFinalStatement(FinalStatement FinalStatement) {
        this.FinalStatement=FinalStatement;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
        if(FinalStatement!=null) FinalStatement.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(FinalStatement!=null) FinalStatement.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(FinalStatement!=null) FinalStatement.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("Stmt(\n");

        if(FinalStatement!=null)
            buffer.append(FinalStatement.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [Stmt]");
        return buffer.toString();
    }
}
