// generated with ast extension for cup
// version 0.8
// 25/8/2024 1:50:27


package rs.ac.bg.etf.pp1.ast;

public class AssignStmt extends FinalStatement {

    private DesignatorStatementA DesignatorStatementA;

    public AssignStmt (DesignatorStatementA DesignatorStatementA) {
        this.DesignatorStatementA=DesignatorStatementA;
        if(DesignatorStatementA!=null) DesignatorStatementA.setParent(this);
    }

    public DesignatorStatementA getDesignatorStatementA() {
        return DesignatorStatementA;
    }

    public void setDesignatorStatementA(DesignatorStatementA DesignatorStatementA) {
        this.DesignatorStatementA=DesignatorStatementA;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
        if(DesignatorStatementA!=null) DesignatorStatementA.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(DesignatorStatementA!=null) DesignatorStatementA.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(DesignatorStatementA!=null) DesignatorStatementA.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("AssignStmt(\n");

        if(DesignatorStatementA!=null)
            buffer.append(DesignatorStatementA.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [AssignStmt]");
        return buffer.toString();
    }
}
