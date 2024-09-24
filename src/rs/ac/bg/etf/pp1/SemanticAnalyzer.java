package rs.ac.bg.etf.pp1;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Stack;

import org.apache.log4j.Logger;

import rs.ac.bg.etf.pp1.ast.*;
import rs.etf.pp1.symboltable.Tab;
import rs.etf.pp1.symboltable.concepts.Obj;
import rs.etf.pp1.symboltable.concepts.Scope;
import rs.etf.pp1.symboltable.concepts.Struct;
import rs.etf.pp1.symboltable.visitors.DumpSymbolTableVisitor;
import rs.etf.pp1.symboltable.visitors.SymbolTableVisitor;

public class SemanticAnalyzer extends VisitorAdaptor{
	

	private Logger log = Logger.getLogger(getClass());

	public static Struct boolType = new Struct(Struct.Bool);

	
	
	
	private boolean errorDetected = false;	
	
	private static int programVariablesNumber = 0;
	private boolean mainMethodFound = false;
	
	private Struct currentType = null; 
	
	private boolean isVariableArray = false;	
	
	private Obj currentMethod = null;
	private boolean returnFound = false;
	private int methodFormParamsCount = 0;
	
	private int forCounter=0;
	
	private String namespace=null;

	private List<Obj> listOfDes = new ArrayList<Obj>(); 
	
	private Stack<List<Struct>> stackOfActPars = new Stack<List<Struct>>(); 
	
	
	public void report_error(String message, SyntaxNode info) {
		errorDetected = true;
		StringBuilder msg = new StringBuilder("Error");
		int line = (info == null) ? 0: info.getLine();
		if (line != 0)
			msg.append(" on the line ").append(line).append(": ");
		else 
			msg.append(": ");
		msg.append(message);
		log.error(msg.toString());
	}

	public void dump(SymbolTableVisitor stv) {
		System.out.println("=====================SYMBOL TABLE DUMP=========================");
		if (stv == null)
			stv = new DumpSymbolTableVisitor();
		for (Scope s = Tab.currentScope(); s != null; s = s.getOuter()) {
			s.accept(stv);
		}
		log.info(stv.getOutput());
	}
	
	public void tsdump() {
		dump(null);
	}

	enum RelOpEnum { EQ, NE, LS, GR, GRE, LSE }
	private RelOpEnum relationalOperation = null;

	enum AddOpEnum { PLUS, MINUS }
	
	enum MulOpEnum { MUL, DIV, MOD }
	
		
	public String structName(Struct s) {
		switch (s.getKind()) {
			case Struct.None: return "none";
			case Struct.Int: return "int";
			case Struct.Char: return "char";
			case Struct.Array: return "array";
			case Struct.Class: return "class";
			case Struct.Bool: return "bool";
			default: return "";
		}
	
	}

  private boolean constantConstraintName(String name, SyntaxNode info) {
    	
    	Obj obj = Tab.find((namespace!=null?namespace+"::"+name:name));
    	if(obj != Tab.noObj) {
			report_error("Name " + name + " is already declared!", info);    		
			return false;
    	}
    	return true; 
}

 private boolean constantConstraintType(String name, Struct type, SyntaxNode info) {
    	
    	if(!type.equals(currentType)) {
			report_error("Declared type of constant '" + structName(currentType) + " is not the same as type " + structName(type) + "'!", info);    		    		
    		return false;
    	}
    	
    	return true; 
    }

 private boolean variableConstraintName(String name, SyntaxNode info) {
    	
    	
    	Obj obj = Tab.find(namespace!=null?namespace+"::"+name:name);
    	if(obj != Tab.noObj) {
    		if(Tab.currentScope.findSymbol(name) != null) {
    			
				report_error("Name " + name + "is already declared!", info);    		
				return false;
    		}
    	}
    	return true; 
    }
    
 @Override
    public void visit(ReturnExpr expr) {
    	returnFound = true;
    	Struct type = currentMethod.getType();
    	if(type == Tab.noType) {
			report_error("Function with return type void can not have type expression (" + structName(expr.getExpr().struct) + ")!", expr);
			return;    		  
    	}
    	if(!type.equals(expr.getExpr().struct)){
			report_error("Function return type (" + structName(expr.getExpr().struct) + ") is not compatible with return expression " + structName(type), expr);
			return;
    	}
    }

private List<Obj> argumentsNumberConstraint(Obj function, int numOfActualPar, SyntaxNode info) {
    	
    	List<Obj> formalArgs = new ArrayList<Obj>(); 
    	
    	int index = 0;
    	
    	for (Iterator<Obj> it = function.getLocalSymbols().iterator(); 
    			index < function.getLevel() && it.hasNext();) {

    	    Obj formalParameter = it.next();
    	    
    	    formalArgs.add(formalParameter);    	    
    	    index++;
    	}
    	
    	int numOfForPar = function.getLevel();
    	
    	
    	if(numOfActualPar == numOfForPar) {
    		
    	
			return formalArgs;    		
    	}
    	else {
		report_error("Number of real (" + numOfActualPar + ") and formal (" + (numOfForPar) + ") has to be the same", info);        	    		
    	}
    	
    	return null;
    	
    }
    
    private Struct argumentConstraint(Obj function, SyntaxNode info) {
    	
    	if(function.getKind() != Obj.Meth) {
    		report_error(function.getName() + " is not a function", info);        	    		
    		return Tab.noType;
    	}
    	
    	List<Struct> actArgs = stackOfActPars.pop();
    	int numOfActArgs = actArgs.size();
    	
    	List<Obj> formalArgumentsForCalledFunction = argumentsNumberConstraint(function, numOfActArgs, info);
    			
    	if(formalArgumentsForCalledFunction == null)	{
    		return Tab.noType;
    	}  	
    	
    	for(int i = 0; i < numOfActArgs; i++) {
    		
    		if(!actArgs.get(i).assignableTo(formalArgumentsForCalledFunction.get(i).getType())){
    			report_error("Formal argument on the position " + (i + 1) + " (type: " + structName(formalArgumentsForCalledFunction.get(i).getType()) + ") can not be assigned real parameter (type: " + structName(actArgs.get(i)) + ")!", info);
    			return Tab.noType;
    		}    		
    	}
    	
    
    	
    	return function.getType();
	}

  private boolean rightValueConstraint(Obj dst, SyntaxNode info, int errorType) {
    	
    	if(dst.getKind() != Obj.Var && dst.getKind() != Obj.Elem) {
    		switch(errorType) {
    		case 0:
    			report_error("Left side of assignment has to be variable or array element!", info);        	
    			break;
    		case 1:
    			report_error("Increment needs to be preformed on variables or array elements!", info);        	    			
    			break;
    		case 2:
    			report_error("Decrement has to be performed on variables or array elements!", info);        	    			
    			break;
    		default:
    			report_error("Reading can only be done in types int, char or bool!", info);        	    			
    			break;
    		}
			return false;
		}
		return true;
	}
    
     
    @Override
    public void visit(E op) {
    	relationalOperation = RelOpEnum.EQ;
    }
    
    @Override
    public void visit(NE op) {
    	relationalOperation = RelOpEnum.NE;
    }
    
    @Override
    public void visit(LS op) {
    	relationalOperation = RelOpEnum.LS;
    }
    
    @Override
    public void visit(LSE op) {
    	relationalOperation = RelOpEnum.LSE;
    }
    
    @Override
    public void visit(GR op) {
    	relationalOperation = RelOpEnum.GR;
    }
    
    @Override
    public void visit(GRE op) {
    	relationalOperation = RelOpEnum.GRE;
    }
	
	public SemanticAnalyzer() {
		Tab.currentScope.addToLocals(new Obj(Obj.Type, "bool", boolType));
	}	
	


	public static int getProgramVariablesNumber() {
		return programVariablesNumber;
	}

	public static void setProgramVariablesNumber(int num) {
		programVariablesNumber = num;
	}

	
	

	
	
    @Override
    public void visit(ProgName progName) {
    	progName.obj = Tab.insert(Obj.Prog, progName.getProgName(), Tab.noType);
    	Tab.openScope(); 
    }
    
    @Override
    public void visit(Program program) {
    	
    	programVariablesNumber = Tab.currentScope.getnVars();
    	
		if(mainMethodFound == false){
			report_error("Main method is not found!", null);
		}

    	
    	Tab.chainLocalSymbols(program.getProgName().obj); 
    	Tab.closeScope();
    }
    
    
    @Override
    public void visit(HasNoNamespace type) {
    	
    	Obj typeNode = Tab.find(type.getTypeName()); 
    	
    	if(typeNode == Tab.noObj) {
    		report_error("Type " + type.getTypeName() + " not declared!", type);
    		type.struct = Tab.noType; 
    	} else {
			
    		if(Obj.Type == typeNode.getKind()) {
    			type.struct = typeNode.getType();
    		} else {
    			report_error("Name " + type.getTypeName() + " is not a type!", type);
        		type.struct = Tab.noType;
    		}
    	}
    	
    	currentType = type.struct; 
    	
    }
    
   

    

    
   
    private void putConstantToSymbolTable(String name, Struct type, int value, SyntaxNode info) {

    	Obj obj = Tab.insert(Obj.Con,(namespace!=null?namespace+"::"+ name:name), type);
		obj.setAdr(value);
	}
    
    @Override
    public void visit(AssignBool bool) {
    	if(!constantConstraintName(bool.getConstName(), bool)) {
    		    		return;
    	}
    	if(!constantConstraintType(bool.getConstName(), boolType, bool)) {
    		return;
    	}

		if(bool.getBoolConstValue())
			putConstantToSymbolTable(bool.getConstName(), boolType, 1, bool);
		else 
	    	putConstantToSymbolTable(bool.getConstName(), boolType, 0, bool);

    	
    }
    
    @Override
    public void visit(AssignConst integer) {
    	if(!constantConstraintName(integer.getConstName(), integer)) {
    		return;
    	}
    	if(!constantConstraintType(integer.getConstName(), Tab.intType, integer)) {
    		return;
    	}
    	
    	putConstantToSymbolTable(integer.getConstName(), Tab.intType, integer.getNumberConstValue(), integer);

    }
    
    @Override
    public void visit(AssignChar charV) {
    	if(!constantConstraintName(charV.getConstName(), charV)) {
    		return;
    	}
    	if(!constantConstraintType(charV.getConstName(), Tab.charType, charV)) {
    		return;
    	}
    	
    	putConstantToSymbolTable(charV.getConstName(), Tab.charType, charV.getCharConstValue(), charV);

    }
    
   
    
   
    private Obj putVariableToSymbolTable(String name, SyntaxNode info) {
    	
    	
    	Struct variableType = currentType;
    	if(isVariableArray) {
    		variableType = new Struct(Struct.Array, currentType);
    	}

    	return Tab.insert(Obj.Var, (namespace!=null?namespace+"::"+name:name), variableType);
    	
    	 
	}
	
    
    @Override
    public void visit(IsArray isArray) {
    	isVariableArray = true;
    }
    
    @Override
    public void visit(VarDeclClass var) {
    	if(!variableConstraintName(var.getVarName(), var)) {
    		return;
    	}
    	
    	putVariableToSymbolTable(var.getVarName(), var);
    	
    	isVariableArray = false;

    }
    
    @Override
    public void visit(FewVarDeclClass var) {
    	if(!variableConstraintName(var.getVarName(), var)) {
    		return;
    	}
    	
    	putVariableToSymbolTable(var.getVarName(), var);
    	
    	isVariableArray = false;

    }
    

     
    @Override
    public void visit(HasReturnType type) {
    	type.struct = currentType;
    }
    
    @Override
    public void visit(HasNoReturnType type) {
    	type.struct = Tab.noType;
    }
    
    @Override
    public void visit(MethodTypeName name) {
    	
    	currentMethod = Tab.insert(Obj.Meth,(namespace!=null?namespace+"::":"")+ name.getMethName(), name.getMethodReturnType().struct);

    	name.obj = currentMethod;
    	
    	Tab.openScope(); 
				

    }

    @Override
    public void visit(FormalParameterDeclaration decl) {
		methodFormParamsCount++;
		
		if(Tab.find(decl.getParameterName()) != Tab.noObj){

			if (Tab.currentScope.findSymbol(decl.getParameterName()) != null){
				report_error("Formal parameter " + decl.getParameterName() + " is already declared!", decl);    		
				return;
			}
		}
	
    	
    	Struct type = currentType;
    	if(isVariableArray == true) {
    		type = new Struct(Struct.Array, currentType);
    	}
    	
    	Tab.insert(Obj.Var, decl.getParameterName(), type);
    	isVariableArray = false; 
    	
    }

   
    @Override
    public void visit(AssignFewA assignFewA) {
    	if(assignFewA.getDesignator().obj.getType().getKind() != Struct.Array) {
			report_error("Variable has to be array  ", assignFewA);

    	}
    	if(!assignFewA.getDesignator().obj.getType().getElemType().assignableTo(assignFewA.getListForAssign().getDesignator().obj.getType().getElemType())) {
    		report_error("Left side (" + structName(assignFewA.getDesignator().obj.getType().getElemType()) + ") is not compatible with right side (" + structName(assignFewA.getListForAssign().getDesignator().obj.getType().getElemType()) + ") pri dodeli!", assignFewA);        	    		

    	}
    	for(Obj obj:listOfDes) {
    		if(!assignFewA.getDesignator().obj.getType().getElemType().assignableTo(obj.getType())) {
        		report_error("Left side (" + structName(assignFewA.getDesignator().obj.getType().getElemType()) + ") is not compatible with right side (" + structName(obj.getType().getElemType()) + ") pri dodeli!", assignFewA);        	    		

   
    	}
    	
    }
    	listOfDes.clear();

    }
    
    @Override
    public void visit(AssignFew assignFewA) {
    	if(assignFewA.getDesignator().obj.getType().getKind() != Struct.Array) {
			report_error("Variable has to be array  ", assignFewA);

    	}
    	if(!assignFewA.getDesignator().obj.getType().getElemType().assignableTo(assignFewA.getListForAssign().getDesignator().obj.getType().getElemType())) {
    		report_error("Left side (" + structName(assignFewA.getDesignator().obj.getType().getElemType()) + ") is not compatible with right side (" + structName(assignFewA.getListForAssign().getDesignator().obj.getType().getElemType()) + ") pri dodeli!", assignFewA);        	    		

    	}
    	for(Obj obj:listOfDes) {
    		if(!assignFewA.getDesignator().obj.getType().getElemType().assignableTo(obj.getType())) {
        		report_error("Left side (" + structName(assignFewA.getDesignator().obj.getType().getElemType()) + ") is not compatible with right side (" + structName(obj.getType().getElemType()) + ") pri dodeli!", assignFewA);        	    		

   
    	}
    		
    	
    }
    	listOfDes.clear();
    }
    
    @Override
    public void visit(ListForAssign listForAssign) {
    	if(listForAssign.getDesignator().obj.getType().getKind() != Struct.Array) {
			report_error("Variable has to be array  ", listForAssign);

    	}
    }
    
    @Override
    public void visit(DesingListDes desinglistdes) {
    	if(!rightValueConstraint(desinglistdes.getDesignator1().obj, desinglistdes, 0)) {
    		return;
    	}
    	listOfDes.add(desinglistdes.getDesignator1().obj);
    }
    
    @Override
    public void visit(MethodDecl decl) {
    	
    	if(!returnFound && currentMethod.getType() != Tab.noType) {
			report_error("Function " + currentMethod.getName() + " does not have neded return expression!", decl);    		
	    
    	}

    	currentMethod.setLevel(methodFormParamsCount);

		if("main".equals(currentMethod.getName()) && methodFormParamsCount ==0){
			mainMethodFound = true;
		}

    	
    	Tab.chainLocalSymbols(currentMethod);
    	
    	Tab.closeScope();
    	
    	methodFormParamsCount = 0;
    	returnFound = false;
    	currentMethod = null;
    }

    
    @Override
    public void visit(FactorBool bool) {
    	bool.struct = boolType;
    }
    
    @Override
    public void visit(FactorNum num) {
    	num.struct = Tab.intType;
    }

    @Override
    public void visit(FactorChar charV) {
    	charV.struct = Tab.charType;
    }
        
    @Override
    public void visit(FactorHasBrackets expr) {
    	expr.struct = expr.getExpr().struct;
    }
        
    @Override
    public void visit(FactorDes fact) {
    	
    	fact.struct = fact.getDesignator().obj.getType();
    }
    
    
  @Override
  public void visit(MaxOfArray fact) {
  	
  	fact.struct = fact.getDesignator().obj.getType().getElemType();
  }
    

    @Override
    public void visit(FactorNewArray fact) {
    	
    	if(fact.getExpr().struct != Tab.intType) {
    		fact.struct = Tab.noType;
			report_error("Number of array size has to be int, not " + structName(fact.getExpr().struct) + "!", fact);    		    		
    		return;
    	}
    	    	
    	fact.struct = new Struct(Struct.Array, fact.getType().struct);
    
    }
    
    
  
    @Override
    public void visit(FuncCallName func) {
    	func.obj = func.getDesignator().obj;
    	stackOfActPars.push(new ArrayList<Struct>());
    }
    
    @Override
    public void visit(FewActPars par) {
    	stackOfActPars.peek().add(par.getExpr().struct);
    }
    
    @Override
    public void visit(OneActPars par) {
    	stackOfActPars.peek().add(par.getExpr().struct);
    }
    
    
    @Override
    public void visit(FactorFuncCallNoArg function) {
    	
    	function.struct = argumentConstraint(function.getFuncCallName().obj, function);
    	
    }
    
    
    @Override
    public void visit(FactorFuncCallArgs function) {
    	
    	function.struct = argumentConstraint(function.getFuncCallName().obj, function);
    	
    }
    
    @Override
    public void visit(FuncCallArgs function) {
    	
    	argumentConstraint(function.getFuncCallName().obj, function);
     	
    }

    @Override
    public void visit(FuncCallArgsA function) {
    	
    	argumentConstraint(function.getFuncCallName().obj, function);
     	
    }
    
    @Override
    public void visit(Read read) {
    	if(!rightValueConstraint(read.getDesignator().obj, read, 3)) {
    		return;
    	}

    	if(read.getDesignator().obj.getType() != Tab.intType && read.getDesignator().obj.getType() != Tab.charType &&read.getDesignator().obj.getType() != boolType) {
			report_error("You can only read types int, char and bool, not " + structName(read.getDesignator().obj.getType()) + "!", read);        	    			
		}
    }
    
    
    @Override
    public void visit(OneFactor fact) {
    	fact.struct = fact.getFactor().struct;
    }
    @Override
    public void visit(NamespaceName namespacename) {
    	namespace = namespacename.getName();
    }
    
    @Override
    public void visit(Namespace namespacename) {
    	namespace = null;
    }
    @Override
    public void visit(FewFactors fact) {
    	if(fact.getFactor().struct != Tab.intType || fact.getFactorList().struct != Tab.intType) {
    		report_error("Type needs to be int", fact);        	
    		fact.struct = Tab.noType;
    		return;
    	}
    	fact.struct = Tab.intType;
    }
    
    @Override
    public void visit(Term term) {
    	term.struct = term.getFactorList().struct;
    } 
    
    @Override
    public void visit(OneTerm term) {
    	term.struct = term.getTerm().struct;
    }
    
    @Override
    public void visit(Summing expr) {
    	if(!expr.getExpr().struct.compatibleWith(expr.getTerm().struct)) {
    		report_error("Types of all needs to be compatible (" + structName(expr.getExpr().struct) + "," + structName(expr.getTerm().struct) + ")", expr);        	
    		expr.struct = Tab.noType;
    		return;
    	}
    	if(expr.getExpr().struct != Tab.intType || expr.getTerm().struct != Tab.intType) {
    		report_error("Type of all needs to be int", expr);        	
    		expr.struct = Tab.noType;
    		return;
    	}
    	expr.struct = Tab.intType;
    }
        
    @Override
    public void visit(MinTerm term) {
    	if(term.getTerm().struct != Tab.intType) {
    		report_error("Type needs to be int", term);        	
    		term.struct = Tab.noType;
    		return;
    	}
    	term.struct = Tab.intType;    	
    }
    @Override
    public void visit(UsingStmt u) {
    	namespace = u.getName();
    }
    
    @Override
    public void visit(HasNoNamespaceDesNoArray1 designator) {
    	
    
    	Obj obj = Tab.find((namespace!=null?namespace+"::":"")+designator.getName()); 
    	   
    	if(obj == Tab.noObj) {
    		obj = Tab.find(designator.getName()); 
    	}
    	if(obj == Tab.noObj) {
    		report_error("Variable " + designator.getName() + " is not declared!", designator);
    	} 
    	
    	designator.obj = obj;
    }
    

    
    @Override
    public void visit(HasNamespaceDesNoArray1 designator) {
    	
    	Obj obj = Tab.find(designator.getNamespaceName()+"::"+designator.getName()); 
   
    	
    	if(obj == Tab.noObj) {
    		report_error("Variable " + designator.getName() + " is not declared!", designator);
    	} 
    	
    	designator.obj = obj;
    }
    @Override
    public void visit(HasNoNamespaceDesNoArray designator) {
    	
    
    	Obj obj = Tab.find((namespace!=null?namespace+"::":"")+designator.getName()); 
    	   
    	if(obj == Tab.noObj) {
    		obj = Tab.find(designator.getName()); 
    	}
    	if(obj == Tab.noObj) {
    		report_error("Variable " + designator.getName() + " is not declared!", designator);
    	} 
    	
    	designator.obj = obj;
    }
    @Override
    public void visit(HasNamespaceDesNoArray designator) {
    	Obj obj = Tab.find(designator.getNamespaceName()+"::"+ designator.getName()); 
    	if(obj == Tab.noObj) {
    		report_error("Variable " + designator.getName() + " is not declared!", designator);
    	} 
    	
    	designator.obj = obj;
    }
    
    
    
    
    
    @Override
    public void visit(DesArray designator) {
    	if(designator.getArrayName().getDesignator().obj.getType().getKind() != Struct.Array) {
    		report_error("Variable " + designator.getArrayName().getDesignator().obj.getName() + " has to be array" , designator);        	
    		designator.obj = Tab.noObj;
        	return;
    	}
    	if(designator.getExpr().struct != Tab.intType) {
    		report_error("Array index has to be int, not " + structName(designator.getExpr().struct), designator);        	
    		designator.obj = Tab.noObj;
        	return;
    	}

    	designator.obj = new Obj(Obj.Elem, designator.getArrayName().getDesignator().obj.getName(), designator.getArrayName().getDesignator().obj.getType().getElemType());
    
    }
    
    @Override
    public void visit(DesArray1 designator) {
    	if(designator.getArrayName1().getDesignator1().obj.getType().getKind() != Struct.Array) {
    		report_error("Variable " + designator.getArrayName1().getDesignator1().obj.getName() + " has to be array" , designator);        	
    		designator.obj = Tab.noObj;
        	return;
    	}
    	if(designator.getExpr().struct != Tab.intType) {
    		report_error("Array index has to be int, not " + structName(designator.getExpr().struct), designator);        	
    		designator.obj = Tab.noObj;
        	return;
    	}

    	designator.obj = new Obj(Obj.Elem, designator.getArrayName1().getDesignator1().obj.getName(), designator.getArrayName1().getDesignator1().obj.getType().getElemType());
    
    }
    

  
    
    @Override
    public void visit(AssignDes desingator) {
    	
    	Obj dst = desingator.getDesignator().obj;
    	Struct src = desingator.getExpr().struct;
    	
    	if(!rightValueConstraint(dst, desingator, 0)) {
    		return;
    	}
    	
    	if(!src.assignableTo( dst.getType())) {
    		report_error("Left side (" + structName(src) + ") is not compatible with right side (" + structName(dst.getType()) , desingator);        	    		
    	}

    }
    @Override
    public void visit(AssignDesA designator) {
    	
    	Obj dst = designator.getDesignator().obj;
    	Struct src = designator.getExpr().struct;
    	
    	if(!rightValueConstraint(dst, designator, 0)) {
    		return;
    	}
    	if(!src.assignableTo( dst.getType())) {
    		report_error("Left side (" + structName(src) + ") is not compatible with right side (" + structName(dst.getType()) , designator);        	    		
    	}

    }

    
    @Override
    public void visit(IncDesA designator) {

    	if(!rightValueConstraint(designator.getDesignator().obj, designator, 1)) {
    		return;
    	}
    	 if(designator.getDesignator().obj.getType() != Tab.intType) {
			report_error("Inrement has to be done on int type, not " + (structName(designator.getDesignator().obj.getType())) + "!", designator);        	    			
    	}

    	
    }
    @Override
    public void visit(IncDes designator) {

    	if(!rightValueConstraint(designator.getDesignator().obj, designator, 1)) {
    		return;
    	}
    	
    	if(designator.getDesignator().obj.getType() != Tab.intType) {
			report_error("Inrement has to be done on int type, not " + (structName(designator.getDesignator().obj.getType())) + "!", designator);        	    			
    	}

    	
    }
    
    @Override
    public void visit(DecDesA designator) {
    	if(!rightValueConstraint(designator.getDesignator().obj, designator, 2)) {
    		return;
    	}
    	
    	if(designator.getDesignator().obj.getType() != Tab.intType) {
			report_error("Decrement has to be done on int type, not " + (structName(designator.getDesignator().obj.getType())) + "!", designator);        	    			
    	}
    }
    @Override
    public void visit(DecDes designator) {
    	if(!rightValueConstraint(designator.getDesignator().obj, designator, 2)) {
    		return;
    	}
    	
    	if(designator.getDesignator().obj.getType() != Tab.intType) {
			report_error("Decrement has to be done on int type, not " + (structName(designator.getDesignator().obj.getType())) + "!", designator);        	    			
    	}
    }
    
     
   
    @Override
	public void visit(ForLoopStart forL) {
    	forCounter++;
    }
    
    @Override
    public void visit(For stmt) {
    	forCounter--;
    }
    
    
    @Override
    public void visit(Break stmt) {
    	if(forCounter == 0) {
			report_error("Break has to be inside of for loop!", stmt);        	
		}
    }
        

    @Override
    public void visit(Continue stmt) {
    	if(forCounter == 0) {
			report_error("Continue has to be inside of for loop!", stmt);        	
		}
    	
    }


    
    @Override
    public void visit(PrintNoWidth stmt) {
    	if(stmt.getExpr().struct != Tab.intType && stmt.getExpr().struct != Tab.charType && stmt.getExpr().struct != boolType) {
    		report_error("Print argument has to be int type", stmt);        	
    	}
    }
    
    @Override
    public void visit(PrintWidth stmt) {
    	if(stmt.getExpr().struct != Tab.intType && stmt.getExpr().struct != Tab.charType && stmt.getExpr().struct != boolType) {
    		report_error("Print argument has to be int type", stmt);        	
    	}    
    	
    }
               
    @Override
    public void visit(OneExpr expression) {
    	if(expression.getExpr().struct != boolType) {
    		report_error("Type of expression in if has to be bool!", expression);        	
			return;    	
    	}
    }
    
    @Override
    public void visit(FewExpr expression) {
    	
    	Expr exprLeft = expression.getExpr();
    	Expr exprRight = expression.getExpr1();
    	
    	if(!exprLeft.struct.compatibleWith(exprRight.struct)) {
        	report_error("Expression have to be compatible!", expression);  	    		
    	}
    	
    	if(exprLeft.struct.getKind() == Struct.Array || exprRight.struct.getKind() == Struct.Array) {
    		
    		if(!relationalOperation.equals(RelOpEnum.EQ) && !relationalOperation.equals(RelOpEnum.NE)) {    		
	        	report_error("References can only be compared with = and != operators ", expression);  	    		
    		}    		
    	}
    	
    }
    
   
   
    
	public boolean passed() {
    	return !errorDetected;
    }
    
}
