// generated with ast extension for cup
// version 0.8
// 25/8/2024 1:50:27


package rs.ac.bg.etf.pp1.ast;

public class For extends FinalStatement {

    private InitStmt InitStmt;
    private StartOfCond StartOfCond;
    private CondStmt CondStmt;
    private UpdateStmt UpdateStmt;
    private ForLoopStart ForLoopStart;
    private Statement Statement;

    public For (InitStmt InitStmt, StartOfCond StartOfCond, CondStmt CondStmt, UpdateStmt UpdateStmt, ForLoopStart ForLoopStart, Statement Statement) {
        this.InitStmt=InitStmt;
        if(InitStmt!=null) InitStmt.setParent(this);
        this.StartOfCond=StartOfCond;
        if(StartOfCond!=null) StartOfCond.setParent(this);
        this.CondStmt=CondStmt;
        if(CondStmt!=null) CondStmt.setParent(this);
        this.UpdateStmt=UpdateStmt;
        if(UpdateStmt!=null) UpdateStmt.setParent(this);
        this.ForLoopStart=ForLoopStart;
        if(ForLoopStart!=null) ForLoopStart.setParent(this);
        this.Statement=Statement;
        if(Statement!=null) Statement.setParent(this);
    }

    public InitStmt getInitStmt() {
        return InitStmt;
    }

    public void setInitStmt(InitStmt InitStmt) {
        this.InitStmt=InitStmt;
    }

    public StartOfCond getStartOfCond() {
        return StartOfCond;
    }

    public void setStartOfCond(StartOfCond StartOfCond) {
        this.StartOfCond=StartOfCond;
    }

    public CondStmt getCondStmt() {
        return CondStmt;
    }

    public void setCondStmt(CondStmt CondStmt) {
        this.CondStmt=CondStmt;
    }

    public UpdateStmt getUpdateStmt() {
        return UpdateStmt;
    }

    public void setUpdateStmt(UpdateStmt UpdateStmt) {
        this.UpdateStmt=UpdateStmt;
    }

    public ForLoopStart getForLoopStart() {
        return ForLoopStart;
    }

    public void setForLoopStart(ForLoopStart ForLoopStart) {
        this.ForLoopStart=ForLoopStart;
    }

    public Statement getStatement() {
        return Statement;
    }

    public void setStatement(Statement Statement) {
        this.Statement=Statement;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
        if(InitStmt!=null) InitStmt.accept(visitor);
        if(StartOfCond!=null) StartOfCond.accept(visitor);
        if(CondStmt!=null) CondStmt.accept(visitor);
        if(UpdateStmt!=null) UpdateStmt.accept(visitor);
        if(ForLoopStart!=null) ForLoopStart.accept(visitor);
        if(Statement!=null) Statement.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(InitStmt!=null) InitStmt.traverseTopDown(visitor);
        if(StartOfCond!=null) StartOfCond.traverseTopDown(visitor);
        if(CondStmt!=null) CondStmt.traverseTopDown(visitor);
        if(UpdateStmt!=null) UpdateStmt.traverseTopDown(visitor);
        if(ForLoopStart!=null) ForLoopStart.traverseTopDown(visitor);
        if(Statement!=null) Statement.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(InitStmt!=null) InitStmt.traverseBottomUp(visitor);
        if(StartOfCond!=null) StartOfCond.traverseBottomUp(visitor);
        if(CondStmt!=null) CondStmt.traverseBottomUp(visitor);
        if(UpdateStmt!=null) UpdateStmt.traverseBottomUp(visitor);
        if(ForLoopStart!=null) ForLoopStart.traverseBottomUp(visitor);
        if(Statement!=null) Statement.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("For(\n");

        if(InitStmt!=null)
            buffer.append(InitStmt.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(StartOfCond!=null)
            buffer.append(StartOfCond.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(CondStmt!=null)
            buffer.append(CondStmt.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(UpdateStmt!=null)
            buffer.append(UpdateStmt.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(ForLoopStart!=null)
            buffer.append(ForLoopStart.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(Statement!=null)
            buffer.append(Statement.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [For]");
        return buffer.toString();
    }
}
