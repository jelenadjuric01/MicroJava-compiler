package rs.ac.bg.etf.pp1;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import java.util.Stack;

import rs.ac.bg.etf.pp1.ast.*;
import rs.etf.pp1.mj.runtime.Code;
import rs.etf.pp1.symboltable.Tab;
import rs.etf.pp1.symboltable.concepts.Obj;
import rs.etf.pp1.symboltable.concepts.Scope;
import rs.etf.pp1.symboltable.concepts.Struct;
import rs.etf.pp1.symboltable.structure.HashTableDataStructure;

public class CodeGenerator extends VisitorAdaptor {
	
	private int mainPc = -1;	
	
	private Obj counter = Tab.insert(Obj.Var, "#counter",Tab.intType);
	private Obj index = Tab.insert(Obj.Var, "#index",Tab.intType);

	private String namespace = null;
	
	private Stack<Integer> startCondBlockAddressForPatchingStack = new Stack<Integer>(); 
	private Stack<Integer> falseCondAddresForPatchingStack = new Stack<Integer>();
	private Stack<Integer> startUpdateBlockAddressForPatchingStack = new Stack<Integer>(); 

	
	private Stack<List<Integer>> patchingFromBreakStatementStack = new Stack<List<Integer>>(); 	
	
	private Stack<List<Integer>> patchingFromContinueStatementStack = new Stack<List<Integer>>(); 	

	private Stack<Obj> funcCallStack = new Stack<Obj>(); 
	
	private List<Obj> listToAssign = new ArrayList<Obj>();
	public int getInitPC() {
		return mainPc;
	}
	private Stack<List<Integer>> destinationAddressPatchingFromORConditionBlockStack= new Stack<List<Integer>>(); 

	private Stack<List<Integer>> destinationAddressPatchingFromANDConditionBlockStack = new Stack<List<Integer>>(); 
	
	private Stack<List<Integer>> destinationAddressPatchingFromThenBlockStack = new Stack<List<Integer>>(); 
	

@Override
	public void visit( StartOfIf s) {
		destinationAddressPatchingFromORConditionBlockStack.push(new ArrayList<Integer>());
		destinationAddressPatchingFromANDConditionBlockStack.push(new ArrayList<Integer>());
		destinationAddressPatchingFromThenBlockStack.push(new ArrayList<Integer>());
	}
	
	@Override
	public void visit(OneExpr expr) {
		
		Code.loadConst(1); 
		Code.putFalseJump(Code.eq, 0); 
		destinationAddressPatchingFromANDConditionBlockStack.peek().add(Code.pc - 2); 
	}
	
	@Override
	public void visit(HasCondFact expr) {
		Code.putJump(0); 
		falseCondAddresForPatchingStack.push(Code.pc-2);
		startUpdateBlockAddressForPatchingStack.push(Code.pc);

	}
	
	@Override
	public void visit(HasNoCondFact expr) {
		Code.putJump(0); 
		falseCondAddresForPatchingStack.push(Code.pc-2);
		startUpdateBlockAddressForPatchingStack.push(Code.pc);

	}
	
	@Override
	public void visit(FewExpr expr) {
		
		if(expr.getRelop() instanceof E) {
			Code.putFalseJump(Code.eq, 0); 			
		} else if(expr.getRelop() instanceof NE) {
			Code.putFalseJump(Code.ne, 0); 			
		} else if(expr.getRelop() instanceof LS) {
			Code.putFalseJump(Code.lt, 0); 			
		} else if(expr.getRelop() instanceof GR) {
			Code.putFalseJump(Code.gt, 0); 			
		} else if(expr.getRelop() instanceof GRE) {
			Code.putFalseJump(Code.ge, 0); 			
		} else if(expr.getRelop() instanceof LSE) {
			Code.putFalseJump(Code.le, 0); 			
		}

		destinationAddressPatchingFromANDConditionBlockStack.peek().add(Code.pc - 2);
		
	}
	
	
	@Override
	public void visit(NormalIf expr) {
		for(int placeForPatch: destinationAddressPatchingFromORConditionBlockStack.peek()) {
			Code.fixup(placeForPatch);
		}
		
		destinationAddressPatchingFromORConditionBlockStack.peek().clear();  
		
	}
	
	@Override
	public void visit(EndOfOrCond expr) {
		Code.putJump(0); 		
		destinationAddressPatchingFromORConditionBlockStack.peek().add(Code.pc - 2);
		
		
		for(int placeForPatch: destinationAddressPatchingFromANDConditionBlockStack.peek()) {
			Code.fixup(placeForPatch);
		}
		
		destinationAddressPatchingFromANDConditionBlockStack.peek().clear();  
		
	}

	@Override
	public void visit(NoElse expr) {
		for(int placeForPatch: destinationAddressPatchingFromANDConditionBlockStack.peek()) {
			Code.fixup(placeForPatch);
		}
		
		destinationAddressPatchingFromANDConditionBlockStack.peek().clear(); 
		destinationAddressPatchingFromANDConditionBlockStack.pop();
		destinationAddressPatchingFromORConditionBlockStack.pop();
		destinationAddressPatchingFromThenBlockStack.pop();
			
	}
	
	@Override
	public void visit(EndOfThenBlock expr) {
			Code.putJump(0); 		
			destinationAddressPatchingFromThenBlockStack.peek().add(Code.pc - 2); 
		
	
		for(int placeForPatch: destinationAddressPatchingFromANDConditionBlockStack.peek()) {
			Code.fixup(placeForPatch);
		}
		
		destinationAddressPatchingFromANDConditionBlockStack.peek().clear(); 
				
	}
	
	
	@Override
	public void visit(HasElse expr) {

		for(int placeForPatch: destinationAddressPatchingFromThenBlockStack.peek()) {
			Code.fixup(placeForPatch);
		}
		
		destinationAddressPatchingFromThenBlockStack.peek().clear(); 
		destinationAddressPatchingFromThenBlockStack.pop();

		destinationAddressPatchingFromANDConditionBlockStack.pop();
		destinationAddressPatchingFromORConditionBlockStack.pop();

	}
	
	public void visit(MethodTypeName methodTypeName){
		
		methodTypeName.obj.setAdr(Code.pc);
				
		if("main".equals(methodTypeName.getMethName())){
			
			mainPc = Code.pc;
		}
		
		Code.put(Code.enter);
		Code.put(methodTypeName.obj.getLevel()); 
		Code.put(methodTypeName.obj.getLocalSymbols().size()); 
		
		
	}	
	
	@Override
	public void visit(MethodDecl meth) {
		
		Code.put(Code.exit);
		Code.put(Code.return_);
	}
	
	@Override
	public void visit(DesingListDes desingListdes) {
		listToAssign.add(desingListdes.getDesignator1().obj);
	}
	

	
	@Override
	public void visit(ArrayName1 arr) {
		Code.load(arr.getDesignator1().obj);

	}
	
	@Override
	public void visit(DesingListNoDes des) {
		listToAssign.add(new Obj(1,"#comma",Tab.noType));

	}
	
	@Override
	public void visit(AssignFew ass) {
		Code.load(ass.getDesignator().obj);
		Code.put(Code.arraylength);
		Code.loadConst(listToAssign.size()+1);
		Code.putFalseJump(Code.lt,0);
		int saveAddres=Code.pc-2;
		Code.put(Code.trap);
		Code.put(2);
		Code.fixup(saveAddres);
		int i;
		for(i =listToAssign.size()-1;i>=0;i--) {
			
			if(listToAssign.get(i).getType() != Tab.noType) {
				Code.load(ass.getDesignator().obj);
				Code.loadConst(i);
				Code.put(Code.aload);
				Code.store(listToAssign.get(i));

			}
		}
		Code.loadConst(0);
		Code.store(index);
		Code.loadConst(listToAssign.size());
		Code.store(counter);
		int savTojump = Code.pc;
		Code.load(counter);
		Code.load(ass.getDesignator().obj);
		Code.put(Code.arraylength);		
		Code.putFalseJump(Code.lt,0);
		saveAddres=Code.pc-2;
		Code.load(ass.getListForAssign().getDesignator().obj);
		Code.load(index);
		Code.load(ass.getDesignator().obj);
		Code.load(counter);
		if (ass.getDesignator().obj.getType().getKind() == Struct.Char) Code.put(Code.baload);
        else Code.put(Code.aload); 
		if (ass.getListForAssign().getDesignator().obj.getType().getKind() == Struct.Char) Code.put(Code.bastore);
        else Code.put(Code.astore); 
		
		Code.load(index);
		Code.loadConst(1);
		Code.put(Code.add);
		Code.store(index);
		Code.load(counter);
		Code.loadConst(1);
		Code.put(Code.add);
		Code.store(counter);
		Code.putJump(savTojump);
		Code.fixup(saveAddres);
		
		listToAssign.clear();
	}
	

	@Override
	public void visit(AssignFewA ass) {
		Code.load(ass.getDesignator().obj);
		Code.put(Code.arraylength);
		Code.loadConst(listToAssign.size()+1);
		Code.putFalseJump(Code.lt,0);
		int saveAddres=Code.pc-2;
		Code.put(Code.trap);
		Code.put(2);
		Code.fixup(saveAddres);
		int i;
		for(i =listToAssign.size()-1;i>=0;i--) {
			
			if(listToAssign.get(i).getType() != Tab.noType) {
				Code.load(ass.getDesignator().obj);
				Code.loadConst(i);
				Code.put(Code.aload);
				Code.store(listToAssign.get(i));

			}
		}
		Code.loadConst(0);
		Code.store(index);
		Code.loadConst(listToAssign.size());
		Code.store(counter);
		int savTojump = Code.pc;
		Code.load(counter);
		Code.load(ass.getDesignator().obj);
		Code.put(Code.arraylength);		
		Code.putFalseJump(Code.lt,0);
		saveAddres=Code.pc-2;
		Code.load(ass.getListForAssign().getDesignator().obj);
		Code.load(index);
		Code.load(ass.getDesignator().obj);
		Code.load(counter);
		if (ass.getDesignator().obj.getType().getKind() == Struct.Char) Code.put(Code.baload);
        else Code.put(Code.aload); 
		if (ass.getListForAssign().getDesignator().obj.getType().getKind() == Struct.Char) Code.put(Code.bastore);
        else Code.put(Code.astore); 
		
		Code.load(index);
		Code.loadConst(1);
		Code.put(Code.add);
		Code.store(index);
		Code.load(counter);
		Code.loadConst(1);
		Code.put(Code.add);
		Code.store(counter);
		Code.putJump(savTojump);
		Code.fixup(saveAddres);
		
		listToAssign.clear();
	}
	
	@Override
	public void visit(MaxOfArray m) {
		Code.load(m.getDesignator().obj);
		Code.loadConst(0);
		Code.put(Code.aload);
		Code.load(m.getDesignator().obj);
		Code.put(Code.arraylength);
		Code.loadConst(1);
		Code.put(Code.sub);
		int startAddr = Code.pc;
		Code.put(Code.dup);
		Code.load(m.getDesignator().obj);
		Code.put(Code.dup_x1);
		Code.put(Code.pop);
		Code.put(Code.aload);
		Code.put(Code.dup_x2);
		Code.put(Code.pop);
		Code.put(Code.dup_x2);
		Code.put(Code.pop);
		Code.put(Code.dup_x2);
		Code.putFalseJump(Code.gt, 0);
		int patch1 = Code.pc - 2;
		Code.put(Code.dup_x1);
		Code.put(Code.pop);
		Code.put(Code.pop);
		Code.put(Code.dup);
		Code.load(m.getDesignator().obj);
		Code.put(Code.dup_x1);
		Code.put(Code.pop);
		Code.put(Code.aload);
		Code.put(Code.dup_x1);
		Code.put(Code.pop);
		Code.put(Code.dup);
		Code.loadConst(0);
		Code.putFalseJump(Code.eq, 0);
		int patch2 = Code.pc - 2;
		Code.loadConst(-1);
		Code.put(Code.add);
		Code.putJump( startAddr);
		Code.fixup(patch1);
		Code.put(Code.dup);
		Code.loadConst(0);
		Code.putFalseJump(Code.ne, 0);
		int patch3 = Code.pc-2;
		Code.loadConst(1);
		Code.put(Code.sub);
		Code.putJump(startAddr);
		Code.fixup(patch3);
		Code.fixup(patch2);
		Code.put(Code.pop);
	}
	
	@Override
	public void visit(NoReturnExpr expr) {
		Code.put(Code.exit);
		Code.put(Code.return_);
	}
	
	@Override
	public void visit(ReturnExpr expr) {
		Code.put(Code.exit);
		Code.put(Code.return_);
	}
	
	
	@Override
	public void visit(FuncCallName function) {
		funcCallStack.push(function.obj);
		
	}
	@Override
	public void visit(FactorFuncCallArgs function) {

		Obj obj = function.getFuncCallName().obj;

		if("len".equals(obj.getName())) {
					Code.put(Code.arraylength);
			return;
		}

		if("ord".equals(obj.getName()) || "chr".equals(obj.getName())) {
			return;
		}

		int offset=obj.getAdr() - Code.pc;

			Code.put(Code.call); 
			Code.put2(offset);
		funcCallStack.pop();
	}
		
	@Override
	public void visit(FactorFuncCallNoArg function) {

		Obj obj = function.getFuncCallName().obj;

		if("len".equals(obj.getName())) {
					Code.put(Code.arraylength);
			return;
		}

		if("ord".equals(obj.getName()) || "chr".equals(obj.getName())) {
			return;
		}

		int offset=obj.getAdr() - Code.pc;

			Code.put(Code.call); 
			Code.put2(offset);
		funcCallStack.pop();
	}
	@Override
	public void visit(FuncCallArgsA function) {
		Obj obj = function.getFuncCallName().obj;
		
		if("len".equals(obj.getName())) {
			Code.put(Code.arraylength);
			return;
		}
		int offset=obj.getAdr() - Code.pc;
	
			Code.put(Code.call); 
			Code.put2(offset); 
			if(obj.getType() != Tab.noType) {
				Code.put(Code.pop);
			}
		 
		
		funcCallStack.pop();

	}
	@Override
	public void visit(FuncCallArgs function) {

		Obj obj = function.getFuncCallName().obj;
		if("len".equals(obj.getName())) {
			Code.put(Code.arraylength);
			return;
		}

		int offset=obj.getAdr() - Code.pc;

			Code.put(Code.call); 
			Code.put2(offset); 
		
			if(obj.getType() != Tab.noType) {
				Code.put(Code.pop);
			}
		 
		
		funcCallStack.pop();

	}
	@Override
	public void visit(FuncCallNoArgA function) {

		Obj obj = function.getFuncCallName().obj;
		
		if("len".equals(obj.getName())) {
			Code.put(Code.arraylength);
			return;
		}
		int offset=obj.getAdr() - Code.pc;

		Code.put(Code.call); 
		Code.put2(offset); 
	
		if(obj.getType() != Tab.noType) {
			Code.put(Code.pop);
		}
	 
	
	funcCallStack.pop();

	}
	@Override
	public void visit(FuncCallNoArg function) {

		Obj obj = function.getFuncCallName().obj;
		
		if("len".equals(obj.getName())) {
			Code.put(Code.arraylength);
			return;
		}
		int offset=obj.getAdr() - Code.pc;
		Code.put(Code.call); 
		Code.put2(offset); 
	
		if(obj.getType() != Tab.noType) {
			Code.put(Code.pop);
		}
	 
	
	funcCallStack.pop();

	}
	
	
	
	@Override
	public void visit(PrintNoWidth stmt) {
		if(stmt.getExpr().struct == Tab.intType || stmt.getExpr().struct == SemanticAnalyzer.boolType) {
			Code.loadConst(5);
			Code.put(Code.print);
		}  else {
			Code.loadConst(1);
			Code.put(Code.bprint);
		}
	}
	
	@Override
	public void visit(PrintWidth stmt) {
	
		Code.loadConst(stmt.getWidth());
		
		if(stmt.getExpr().struct == Tab.intType || stmt.getExpr().struct == SemanticAnalyzer.boolType) {
			Code.put(Code.print);
		} else {
			Code.put(Code.bprint);
		}		
	}
	
	
	@Override
	public void visit(Read stmt) {
		
		if(stmt.getDesignator().obj.getType() == Tab.charType) {
			Code.put(Code.bread);			
		} else {
			Code.put(Code.read);
		}		

		Code.store(stmt.getDesignator().obj);
		
	}
	
	
	@Override
	public void visit(ArrayName arr) {
		Code.load(arr.getDesignator().obj);
	}
	

	@Override
	public void visit(IncDesA designator) {

		
		 if(designator.getDesignator().obj.getKind() == Obj.Elem) {
			
			Code.put(Code.dup2); 
			Code.load(designator.getDesignator().obj);	
			
		} else if(designator.getDesignator().obj.getKind() == Obj.Var) {		
			Code.load(designator.getDesignator().obj);

		}

		Code.loadConst(1); 
		Code.put(Code.add);
		
		
		Code.store(designator.getDesignator().obj);

	}
	
	@Override
	public void visit(IncDes designator) {

	
		 if(designator.getDesignator().obj.getKind() == Obj.Elem) {
		
			Code.put(Code.dup2); 
			Code.load(designator.getDesignator().obj);	
			
		} else if(designator.getDesignator().obj.getKind() == Obj.Var) {		
			
			Code.load(designator.getDesignator().obj);	

		}

		Code.loadConst(1); 
		Code.put(Code.add);
		
		Code.store(designator.getDesignator().obj);

	}
	@Override
	public void visit(DecDesA designator) {
	
	  if(designator.getDesignator().obj.getKind() == Obj.Elem) {		
			Code.put(Code.dup2); 
			Code.load(designator.getDesignator().obj);	
			
		} else 	if(designator.getDesignator().obj.getKind() == Obj.Var) {		
			
			Code.load(designator.getDesignator().obj);

		}
		Code.loadConst(1); 
		Code.put(Code.sub);
		
		Code.store(designator.getDesignator().obj);
		
	}
	@Override
	public void visit(DecDes designator) {
		  if(designator.getDesignator().obj.getKind() == Obj.Elem) {		
				Code.put(Code.dup2); 
				Code.load(designator.getDesignator().obj);	
				
			} else 	if(designator.getDesignator().obj.getKind() == Obj.Var) {		
				
				Code.load(designator.getDesignator().obj);

			}
			Code.loadConst(1); 
			Code.put(Code.sub);
			
			Code.store(designator.getDesignator().obj);
		
	}
			
	@Override
	public void visit(AssignDesA designator) {
		
			Code.store(designator.getDesignator().obj);
	
		
	}
	@Override
	public void visit(AssignDes designator) {
	
			Code.store(designator.getDesignator().obj);
		
	}

	
	
		
	
	@Override
	public void visit(FactorDes factor) {
		
			Code.load(factor.getDesignator().obj);
		
	}

	
	@Override
	public void visit(FactorNewArray factor) {

		Code.put(Code.newarray);
		
		if(factor.struct.getElemType() == Tab.charType) {
			Code.put(0);
		} else {
			Code.put(1);
		}
	}
	
	@Override
	public void visit(FactorNum factor) {
		Obj con = Tab.insert(Obj.Con, "#const", Tab.intType);
		con.setLevel(0);
		con.setAdr(factor.getConstValue());
		Code.load(con);
	}
	
	@Override
	public void visit(FactorChar factor) {
		Obj con = Tab.insert(Obj.Con, "#const", Tab.charType);
		con.setLevel(0);
		con.setAdr(factor.getConstValue());
				
		Code.load(con);
	}
	
	@Override
	public void visit(FactorBool factor) {
		Obj con = Tab.insert(Obj.Con, "#const", SemanticAnalyzer.boolType);
		con.setLevel(0);
		if(factor.getConstValue() == true) {
			con.setAdr(1);
		} else {
			con.setAdr(0);
		}

		Code.load(con);
	}
	
		
	@Override
	public void visit(MinTerm term) {
		Code.put(Code.neg); 
	}
	
	@Override
	public void visit(Summing expr) {
		if(expr.getAddop() instanceof Plus) {
			Code.put(Code.add); 
		} else {
			Code.put(Code.sub); 
		}
	}
	
	@Override
	public void visit(FewFactors factor) {
		if(factor.getMulop() instanceof Mul) {
			Code.put(Code.mul); 
		} else if(factor.getMulop() instanceof Div) {
			Code.put(Code.div);  
 		} else {
			Code.put(Code.rem);
		}
	}

	@Override 
	public void visit(StartOfCond startOfCond) {
		
		 destinationAddressPatchingFromORConditionBlockStack.push(new ArrayList<Integer>());
		destinationAddressPatchingFromANDConditionBlockStack.push(new ArrayList<Integer>()); 
		startCondBlockAddressForPatchingStack.push(Code.pc);

		patchingFromBreakStatementStack.push(new ArrayList<Integer>());
		patchingFromContinueStatementStack.push(new ArrayList<Integer>());
		
	}
	
	
	@Override
	public void visit(For forLoopStart) {
		Code.putJump(startUpdateBlockAddressForPatchingStack.peek());
		for(int placeForPatch: patchingFromContinueStatementStack.peek()) {
			 Code.put2(placeForPatch, (startUpdateBlockAddressForPatchingStack.peek()-placeForPatch + 1));

		}
		
		for(int placeForPatch: patchingFromBreakStatementStack.peek()) {
			 Code.fixup(placeForPatch);

		}
		 for(int placeForPatch: destinationAddressPatchingFromANDConditionBlockStack.peek()) {
			Code.fixup(placeForPatch);
		} 
		
		patchingFromContinueStatementStack.peek().clear();  
		patchingFromContinueStatementStack.peek().clear();
		startUpdateBlockAddressForPatchingStack.pop();
		startCondBlockAddressForPatchingStack.pop();
		patchingFromBreakStatementStack.peek().clear();	
		
		
	}
	@Override
	public void visit(ForLoopStart forLoopStart) {
		Code.putJump(startCondBlockAddressForPatchingStack.peek());

		Code.fixup(falseCondAddresForPatchingStack.pop());
		
	
	}
	
	
	
	@Override
	public void visit(Break stmt) {		
		Code.putJump(0); 		
		patchingFromBreakStatementStack.peek().add(Code.pc - 2);	
	
	}
	
	
	@Override
	public void visit(Continue stmt) {
		Code.putJump(0); 		
		patchingFromContinueStatementStack.peek().add(Code.pc - 2); 
	
	}
}


