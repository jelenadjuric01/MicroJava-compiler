// generated with ast extension for cup
// version 0.8
// 25/8/2024 1:50:27


package rs.ac.bg.etf.pp1.ast;

public class FewVarDecls extends VarDeclList {

    private NotLastVarDecl NotLastVarDecl;
    private VarDeclList VarDeclList;

    public FewVarDecls (NotLastVarDecl NotLastVarDecl, VarDeclList VarDeclList) {
        this.NotLastVarDecl=NotLastVarDecl;
        if(NotLastVarDecl!=null) NotLastVarDecl.setParent(this);
        this.VarDeclList=VarDeclList;
        if(VarDeclList!=null) VarDeclList.setParent(this);
    }

    public NotLastVarDecl getNotLastVarDecl() {
        return NotLastVarDecl;
    }

    public void setNotLastVarDecl(NotLastVarDecl NotLastVarDecl) {
        this.NotLastVarDecl=NotLastVarDecl;
    }

    public VarDeclList getVarDeclList() {
        return VarDeclList;
    }

    public void setVarDeclList(VarDeclList VarDeclList) {
        this.VarDeclList=VarDeclList;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
        if(NotLastVarDecl!=null) NotLastVarDecl.accept(visitor);
        if(VarDeclList!=null) VarDeclList.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(NotLastVarDecl!=null) NotLastVarDecl.traverseTopDown(visitor);
        if(VarDeclList!=null) VarDeclList.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(NotLastVarDecl!=null) NotLastVarDecl.traverseBottomUp(visitor);
        if(VarDeclList!=null) VarDeclList.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("FewVarDecls(\n");

        if(NotLastVarDecl!=null)
            buffer.append(NotLastVarDecl.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(VarDeclList!=null)
            buffer.append(VarDeclList.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [FewVarDecls]");
        return buffer.toString();
    }
}
