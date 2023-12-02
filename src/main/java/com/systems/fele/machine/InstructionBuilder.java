package com.systems.fele.machine;

import java.util.ArrayList;
import java.util.List;

import com.systems.fele.machine.instr.AddInstr;
import com.systems.fele.machine.instr.CallInstr;
import com.systems.fele.machine.instr.CastInstr;
import com.systems.fele.machine.instr.DivInstr;
import com.systems.fele.machine.instr.LoadCInstr;
import com.systems.fele.machine.instr.LoadLInstr;
import com.systems.fele.machine.instr.MulInstr;
import com.systems.fele.machine.instr.StoreLInstr;
import com.systems.fele.machine.instr.SubInstr;
import com.systems.fele.syntax.AbstractSyntaxTree;
import com.systems.fele.syntax.Context;
import com.systems.fele.syntax.ParseContext;
import com.systems.fele.syntax.function.FunctionSymbol;
import com.systems.fele.syntax.tree.AbstractSyntaxTreeNode;
import com.systems.fele.syntax.tree.AssignmentNode;
import com.systems.fele.syntax.tree.BinaryOperatorNode;
import com.systems.fele.syntax.tree.FunctionCallNode;
import com.systems.fele.syntax.tree.IdentifierReferenceNode;
import com.systems.fele.syntax.tree.NumberLiteralNode;
import com.systems.fele.syntax.tree.ParenthesisNode;
import com.systems.fele.syntax.tree.ReturnNode;
import com.systems.fele.syntax.variable.LocalVariableSymbol;

public class InstructionBuilder {

	private final List<AbstractMachineInstruction> instructions = new ArrayList<>();
	
	private InstructionBuilder() {}
	
	public static AbstractMachineInstruction[] build(AbstractMachineFunction amContext, AbstractSyntaxTree tree) {
		var builder = new InstructionBuilder();
		builder.buildNextNode(amContext, tree.root, tree.globalContext);
		return builder.instructions.toArray(AbstractMachineInstruction[]::new);
	}
	
	public static AbstractMachineInstruction[] buildNodes(AbstractMachineFunction amContext, AbstractSyntaxTreeNode nodes[], Context context) {
		var builder = new InstructionBuilder();
		for (var node : nodes) {
			builder.buildNextNode(amContext, node, context);
		}
		return builder.instructions.toArray(AbstractMachineInstruction[]::new);
	}
	
	/**
	 * Converts one node into more or more instructions
	 * @param amContext
	 * @param node
	 * @param context
	 */
	private void buildNextNode(AbstractMachineFunction amContext, AbstractSyntaxTreeNode node, Context context) {
		if (node instanceof BinaryOperatorNode binaryOperator)
		{
			var amType = binaryOperator.evaluateType(context);
			var instrType = amType.instrType();
			
			var instr = switch(binaryOperator.getOperatorType()) {
				case PLUS -> new AddInstr(binaryOperator, instrType);
				case MINUS -> new SubInstr(binaryOperator, instrType);
				case STAR -> new MulInstr(binaryOperator, instrType);
				case SLASH -> new DivInstr(binaryOperator, instrType);
				default -> throw new IllegalArgumentException("Unexpected value: " + binaryOperator.getOperatorType());
			};
			
			var lhsType = binaryOperator.getLhs().evaluateType(context);
			var rhsType = binaryOperator.getRhs().evaluateType(context);
			
			// As always, push arguments onto the stack from right to left

			buildNextNode(amContext, binaryOperator.getRhs(), context);
			if (rhsType != amType) instructions.add(new CastInstr(null, rhsType.instrType(), instrType));
			
			buildNextNode(amContext, binaryOperator.getLhs(), context);
			if (lhsType != amType) instructions.add(new CastInstr(null, lhsType.instrType(), instrType));

			instructions.add(instr);
		}	
		else if (node instanceof NumberLiteralNode numberLiteral)
		{
			var instr = new LoadCInstr(numberLiteral.evaluateType(context).instrType(), numberLiteral);
			instructions.add(instr);
		}
		else if (node instanceof ParenthesisNode parenthesis)
		{
			buildNextNode(amContext, parenthesis.getInnerNode(), context);
		}
		else if (node instanceof IdentifierReferenceNode identifierRef)
		{
			var sym = context.findSymbol(identifierRef.getSourceToken().text());
			var instr = new LoadLInstr(identifierRef.evaluateType(context).instrType(), identifierRef, amContext.getLocalIndex(sym.getName()));
			instructions.add(instr);
		}
		else if (node instanceof  AssignmentNode assign)
		{
			var sym = (LocalVariableSymbol) context.defineVariable(assign.getLhs().getSourceToken().text(), assign.evaluateType(context));
			if (sym == null) {
				throw new RuntimeException("Could load find symbol: " + assign.toString() + " in context: " + context.getContextName());
			}
			
			buildNextNode(amContext, assign.getRhs(), context);
		
			var storeInstr = new StoreLInstr(assign, sym.getAbstractMachineType().instrType(), amContext.getLocalIndex(sym.getName()));
			instructions.add(storeInstr);
		}
		else if (node instanceof ReturnNode returnNode)
		{
			if (returnNode.getExpression() != null) {
				buildNextNode(amContext, returnNode.getExpression(), context);
				var fSymbol = (FunctionSymbol) context.getAsSymbol();
				var returnActualType = returnNode.getExpression().evaluateType(context);
				var returnExpectedType = fSymbol.getReturnType();
				
				if (returnActualType != returnExpectedType) instructions.add(new CastInstr(null, returnActualType.instrType(), returnExpectedType.instrType()));
			}
		}
		else if (node instanceof FunctionCallNode functionCall)
		{
			var fSymbol = (FunctionSymbol) context.findSymbol(functionCall.getFuncitonName());
			var args = functionCall.getArguments();
			
			if (args.size() != fSymbol.getParameters().size()) {
				throw new RuntimeException("Wrong number of arguments for function call: " + functionCall.getFuncitonName() + ". Expected: " + fSymbol.getParameters().size() + " but got: " + args.size()); 
			}
			
			for (int i = args.size()-1; i >= 0; i--) {
				if (args.get(i).evaluateType(context) != fSymbol.getParameters().get(i).getType())
					throw new RuntimeException("Wrong type!");
				buildNextNode(amContext, args.get(i), context);
			}
		
			if (fSymbol.getAbstractMachineContext().getParseContext() instanceof ParseContext parseContext
					&& parseContext.getType() == ParseContext.ContextType.EXTERN_FUNCTION) {
				instructions.add( fSymbol.getAbstractMachineContext().instr[0] );
			} else {			
				instructions.add(new CallInstr(functionCall, fSymbol));
			}
		}
		else {
			throw new IllegalArgumentException("Unexpected value: " + node);
		}
	}

}
