// generated with ast extension for cup
// version 0.8
// 25/8/2024 1:50:27


package rs.ac.bg.etf.pp1.ast;

public class OneVarDecl extends VarDeclList {

    private LastVarDecl LastVarDecl;

    public OneVarDecl (LastVarDecl LastVarDecl) {
        this.LastVarDecl=LastVarDecl;
        if(LastVarDecl!=null) LastVarDecl.setParent(this);
    }

    public LastVarDecl getLastVarDecl() {
        return LastVarDecl;
    }

    public void setLastVarDecl(LastVarDecl LastVarDecl) {
        this.LastVarDecl=LastVarDecl;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
        if(LastVarDecl!=null) LastVarDecl.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(LastVarDecl!=null) LastVarDecl.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(LastVarDecl!=null) LastVarDecl.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("OneVarDecl(\n");

        if(LastVarDecl!=null)
            buffer.append(LastVarDecl.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [OneVarDecl]");
        return buffer.toString();
    }
}
