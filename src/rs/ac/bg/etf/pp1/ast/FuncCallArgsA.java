// generated with ast extension for cup
// version 0.8
// 25/8/2024 1:50:27


package rs.ac.bg.etf.pp1.ast;

public class FuncCallArgsA extends DesignatorStatementA {

    private FuncCallName FuncCallName;
    private ActPars ActPars;

    public FuncCallArgsA (FuncCallName FuncCallName, ActPars ActPars) {
        this.FuncCallName=FuncCallName;
        if(FuncCallName!=null) FuncCallName.setParent(this);
        this.ActPars=ActPars;
        if(ActPars!=null) ActPars.setParent(this);
    }

    public FuncCallName getFuncCallName() {
        return FuncCallName;
    }

    public void setFuncCallName(FuncCallName FuncCallName) {
        this.FuncCallName=FuncCallName;
    }

    public ActPars getActPars() {
        return ActPars;
    }

    public void setActPars(ActPars ActPars) {
        this.ActPars=ActPars;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
        if(FuncCallName!=null) FuncCallName.accept(visitor);
        if(ActPars!=null) ActPars.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(FuncCallName!=null) FuncCallName.traverseTopDown(visitor);
        if(ActPars!=null) ActPars.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(FuncCallName!=null) FuncCallName.traverseBottomUp(visitor);
        if(ActPars!=null) ActPars.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("FuncCallArgsA(\n");

        if(FuncCallName!=null)
            buffer.append(FuncCallName.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(ActPars!=null)
            buffer.append(ActPars.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [FuncCallArgsA]");
        return buffer.toString();
    }
}
