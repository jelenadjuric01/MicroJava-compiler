// generated with ast extension for cup
// version 0.8
// 25/8/2024 1:50:27


package rs.ac.bg.etf.pp1.ast;

public class ConstDecl implements SyntaxNode {

    private SyntaxNode parent;
    private int line;
    private ConstDeclType ConstDeclType;
    private FirstValueAssignment FirstValueAssignment;
    private OtherConstDecl OtherConstDecl;

    public ConstDecl (ConstDeclType ConstDeclType, FirstValueAssignment FirstValueAssignment, OtherConstDecl OtherConstDecl) {
        this.ConstDeclType=ConstDeclType;
        if(ConstDeclType!=null) ConstDeclType.setParent(this);
        this.FirstValueAssignment=FirstValueAssignment;
        if(FirstValueAssignment!=null) FirstValueAssignment.setParent(this);
        this.OtherConstDecl=OtherConstDecl;
        if(OtherConstDecl!=null) OtherConstDecl.setParent(this);
    }

    public ConstDeclType getConstDeclType() {
        return ConstDeclType;
    }

    public void setConstDeclType(ConstDeclType ConstDeclType) {
        this.ConstDeclType=ConstDeclType;
    }

    public FirstValueAssignment getFirstValueAssignment() {
        return FirstValueAssignment;
    }

    public void setFirstValueAssignment(FirstValueAssignment FirstValueAssignment) {
        this.FirstValueAssignment=FirstValueAssignment;
    }

    public OtherConstDecl getOtherConstDecl() {
        return OtherConstDecl;
    }

    public void setOtherConstDecl(OtherConstDecl OtherConstDecl) {
        this.OtherConstDecl=OtherConstDecl;
    }

    public SyntaxNode getParent() {
        return parent;
    }

    public void setParent(SyntaxNode parent) {
        this.parent=parent;
    }

    public int getLine() {
        return line;
    }

    public void setLine(int line) {
        this.line=line;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
        if(ConstDeclType!=null) ConstDeclType.accept(visitor);
        if(FirstValueAssignment!=null) FirstValueAssignment.accept(visitor);
        if(OtherConstDecl!=null) OtherConstDecl.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(ConstDeclType!=null) ConstDeclType.traverseTopDown(visitor);
        if(FirstValueAssignment!=null) FirstValueAssignment.traverseTopDown(visitor);
        if(OtherConstDecl!=null) OtherConstDecl.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(ConstDeclType!=null) ConstDeclType.traverseBottomUp(visitor);
        if(FirstValueAssignment!=null) FirstValueAssignment.traverseBottomUp(visitor);
        if(OtherConstDecl!=null) OtherConstDecl.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("ConstDecl(\n");

        if(ConstDeclType!=null)
            buffer.append(ConstDeclType.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(FirstValueAssignment!=null)
            buffer.append(FirstValueAssignment.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(OtherConstDecl!=null)
            buffer.append(OtherConstDecl.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [ConstDecl]");
        return buffer.toString();
    }
}
