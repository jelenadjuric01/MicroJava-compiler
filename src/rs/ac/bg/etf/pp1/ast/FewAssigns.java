// generated with ast extension for cup
// version 0.8
// 25/8/2024 1:50:27


package rs.ac.bg.etf.pp1.ast;

public class FewAssigns extends OtherConstDecl {

    private OtherConstDecl OtherConstDecl;
    private FirstValueAssignment FirstValueAssignment;

    public FewAssigns (OtherConstDecl OtherConstDecl, FirstValueAssignment FirstValueAssignment) {
        this.OtherConstDecl=OtherConstDecl;
        if(OtherConstDecl!=null) OtherConstDecl.setParent(this);
        this.FirstValueAssignment=FirstValueAssignment;
        if(FirstValueAssignment!=null) FirstValueAssignment.setParent(this);
    }

    public OtherConstDecl getOtherConstDecl() {
        return OtherConstDecl;
    }

    public void setOtherConstDecl(OtherConstDecl OtherConstDecl) {
        this.OtherConstDecl=OtherConstDecl;
    }

    public FirstValueAssignment getFirstValueAssignment() {
        return FirstValueAssignment;
    }

    public void setFirstValueAssignment(FirstValueAssignment FirstValueAssignment) {
        this.FirstValueAssignment=FirstValueAssignment;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
        if(OtherConstDecl!=null) OtherConstDecl.accept(visitor);
        if(FirstValueAssignment!=null) FirstValueAssignment.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(OtherConstDecl!=null) OtherConstDecl.traverseTopDown(visitor);
        if(FirstValueAssignment!=null) FirstValueAssignment.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(OtherConstDecl!=null) OtherConstDecl.traverseBottomUp(visitor);
        if(FirstValueAssignment!=null) FirstValueAssignment.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("FewAssigns(\n");

        if(OtherConstDecl!=null)
            buffer.append(OtherConstDecl.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(FirstValueAssignment!=null)
            buffer.append(FirstValueAssignment.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [FewAssigns]");
        return buffer.toString();
    }
}
