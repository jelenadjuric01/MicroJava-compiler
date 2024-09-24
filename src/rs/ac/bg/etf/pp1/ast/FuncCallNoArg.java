// generated with ast extension for cup
// version 0.8
// 25/8/2024 1:50:27


package rs.ac.bg.etf.pp1.ast;

public class FuncCallNoArg extends DesignatorStatement {

    private FuncCallName FuncCallName;

    public FuncCallNoArg (FuncCallName FuncCallName) {
        this.FuncCallName=FuncCallName;
        if(FuncCallName!=null) FuncCallName.setParent(this);
    }

    public FuncCallName getFuncCallName() {
        return FuncCallName;
    }

    public void setFuncCallName(FuncCallName FuncCallName) {
        this.FuncCallName=FuncCallName;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
        if(FuncCallName!=null) FuncCallName.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(FuncCallName!=null) FuncCallName.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(FuncCallName!=null) FuncCallName.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("FuncCallNoArg(\n");

        if(FuncCallName!=null)
            buffer.append(FuncCallName.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [FuncCallNoArg]");
        return buffer.toString();
    }
}
