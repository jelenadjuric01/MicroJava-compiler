// generated with ast extension for cup
// version 0.8
// 25/8/2024 1:50:27


package rs.ac.bg.etf.pp1.ast;

public class OneFormalPar extends FormalParamList {

    private FormalParameterDeclaration FormalParameterDeclaration;

    public OneFormalPar (FormalParameterDeclaration FormalParameterDeclaration) {
        this.FormalParameterDeclaration=FormalParameterDeclaration;
        if(FormalParameterDeclaration!=null) FormalParameterDeclaration.setParent(this);
    }

    public FormalParameterDeclaration getFormalParameterDeclaration() {
        return FormalParameterDeclaration;
    }

    public void setFormalParameterDeclaration(FormalParameterDeclaration FormalParameterDeclaration) {
        this.FormalParameterDeclaration=FormalParameterDeclaration;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
        if(FormalParameterDeclaration!=null) FormalParameterDeclaration.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(FormalParameterDeclaration!=null) FormalParameterDeclaration.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(FormalParameterDeclaration!=null) FormalParameterDeclaration.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("OneFormalPar(\n");

        if(FormalParameterDeclaration!=null)
            buffer.append(FormalParameterDeclaration.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [OneFormalPar]");
        return buffer.toString();
    }
}
