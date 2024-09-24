// generated with ast extension for cup
// version 0.8
// 25/8/2024 1:50:27


package rs.ac.bg.etf.pp1.ast;

public class FewDesStmtsU extends UpdateStmt {

    private UpdateStmt UpdateStmt;
    private DesignatorStatement DesignatorStatement;

    public FewDesStmtsU (UpdateStmt UpdateStmt, DesignatorStatement DesignatorStatement) {
        this.UpdateStmt=UpdateStmt;
        if(UpdateStmt!=null) UpdateStmt.setParent(this);
        this.DesignatorStatement=DesignatorStatement;
        if(DesignatorStatement!=null) DesignatorStatement.setParent(this);
    }

    public UpdateStmt getUpdateStmt() {
        return UpdateStmt;
    }

    public void setUpdateStmt(UpdateStmt UpdateStmt) {
        this.UpdateStmt=UpdateStmt;
    }

    public DesignatorStatement getDesignatorStatement() {
        return DesignatorStatement;
    }

    public void setDesignatorStatement(DesignatorStatement DesignatorStatement) {
        this.DesignatorStatement=DesignatorStatement;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
        if(UpdateStmt!=null) UpdateStmt.accept(visitor);
        if(DesignatorStatement!=null) DesignatorStatement.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(UpdateStmt!=null) UpdateStmt.traverseTopDown(visitor);
        if(DesignatorStatement!=null) DesignatorStatement.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(UpdateStmt!=null) UpdateStmt.traverseBottomUp(visitor);
        if(DesignatorStatement!=null) DesignatorStatement.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("FewDesStmtsU(\n");

        if(UpdateStmt!=null)
            buffer.append(UpdateStmt.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(DesignatorStatement!=null)
            buffer.append(DesignatorStatement.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [FewDesStmtsU]");
        return buffer.toString();
    }
}
