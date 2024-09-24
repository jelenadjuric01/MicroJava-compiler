// generated with ast extension for cup
// version 0.8
// 25/8/2024 1:50:27


package rs.ac.bg.etf.pp1.ast;

public class VarDeclarList extends DeclarList {

    private DeclarList DeclarList;
    private VarDecl VarDecl;

    public VarDeclarList (DeclarList DeclarList, VarDecl VarDecl) {
        this.DeclarList=DeclarList;
        if(DeclarList!=null) DeclarList.setParent(this);
        this.VarDecl=VarDecl;
        if(VarDecl!=null) VarDecl.setParent(this);
    }

    public DeclarList getDeclarList() {
        return DeclarList;
    }

    public void setDeclarList(DeclarList DeclarList) {
        this.DeclarList=DeclarList;
    }

    public VarDecl getVarDecl() {
        return VarDecl;
    }

    public void setVarDecl(VarDecl VarDecl) {
        this.VarDecl=VarDecl;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
        if(DeclarList!=null) DeclarList.accept(visitor);
        if(VarDecl!=null) VarDecl.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(DeclarList!=null) DeclarList.traverseTopDown(visitor);
        if(VarDecl!=null) VarDecl.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(DeclarList!=null) DeclarList.traverseBottomUp(visitor);
        if(VarDecl!=null) VarDecl.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("VarDeclarList(\n");

        if(DeclarList!=null)
            buffer.append(DeclarList.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(VarDecl!=null)
            buffer.append(VarDecl.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [VarDeclarList]");
        return buffer.toString();
    }
}
