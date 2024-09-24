// generated with ast extension for cup
// version 0.8
// 25/8/2024 1:50:27


package rs.ac.bg.etf.pp1.ast;

public class AssignFewA extends DesignatorStatementA {

    private ListForAssign ListForAssign;
    private Designator Designator;

    public AssignFewA (ListForAssign ListForAssign, Designator Designator) {
        this.ListForAssign=ListForAssign;
        if(ListForAssign!=null) ListForAssign.setParent(this);
        this.Designator=Designator;
        if(Designator!=null) Designator.setParent(this);
    }

    public ListForAssign getListForAssign() {
        return ListForAssign;
    }

    public void setListForAssign(ListForAssign ListForAssign) {
        this.ListForAssign=ListForAssign;
    }

    public Designator getDesignator() {
        return Designator;
    }

    public void setDesignator(Designator Designator) {
        this.Designator=Designator;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
        if(ListForAssign!=null) ListForAssign.accept(visitor);
        if(Designator!=null) Designator.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(ListForAssign!=null) ListForAssign.traverseTopDown(visitor);
        if(Designator!=null) Designator.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(ListForAssign!=null) ListForAssign.traverseBottomUp(visitor);
        if(Designator!=null) Designator.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("AssignFewA(\n");

        if(ListForAssign!=null)
            buffer.append(ListForAssign.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(Designator!=null)
            buffer.append(Designator.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [AssignFewA]");
        return buffer.toString();
    }
}
