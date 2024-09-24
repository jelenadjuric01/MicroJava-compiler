// generated with ast extension for cup
// version 0.8
// 25/8/2024 1:50:27


package rs.ac.bg.etf.pp1.ast;

public class NormalIf extends IfCond {

    private StartOfIf StartOfIf;
    private Condition Condition;

    public NormalIf (StartOfIf StartOfIf, Condition Condition) {
        this.StartOfIf=StartOfIf;
        if(StartOfIf!=null) StartOfIf.setParent(this);
        this.Condition=Condition;
        if(Condition!=null) Condition.setParent(this);
    }

    public StartOfIf getStartOfIf() {
        return StartOfIf;
    }

    public void setStartOfIf(StartOfIf StartOfIf) {
        this.StartOfIf=StartOfIf;
    }

    public Condition getCondition() {
        return Condition;
    }

    public void setCondition(Condition Condition) {
        this.Condition=Condition;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
        if(StartOfIf!=null) StartOfIf.accept(visitor);
        if(Condition!=null) Condition.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(StartOfIf!=null) StartOfIf.traverseTopDown(visitor);
        if(Condition!=null) Condition.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(StartOfIf!=null) StartOfIf.traverseBottomUp(visitor);
        if(Condition!=null) Condition.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("NormalIf(\n");

        if(StartOfIf!=null)
            buffer.append(StartOfIf.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(Condition!=null)
            buffer.append(Condition.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [NormalIf]");
        return buffer.toString();
    }
}
