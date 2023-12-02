package com.systems.fele.syntax;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import com.systems.fele.machine.AbstractMachineFunction;
import com.systems.fele.machine.AbstractMachineType;
import com.systems.fele.syntax.ParseContext.ContextType;
import com.systems.fele.syntax.function.FunctionParameter;
import com.systems.fele.syntax.tree.AbstractSyntaxTreeNode;
import com.systems.fele.syntax.tree.AssignmentNode;
import com.systems.fele.syntax.tree.BinaryOperatorNode;
import com.systems.fele.syntax.tree.FunctionCallNode;
import com.systems.fele.syntax.tree.IdentifierReferenceNode;
import com.systems.fele.syntax.tree.NumberLiteralNode;
import com.systems.fele.syntax.tree.ParameterNode;
import com.systems.fele.syntax.tree.ParenthesisNode;
import com.systems.fele.syntax.tree.ReturnNode;
import com.systems.fele.syntax.tree.TypeExpressionNode;

public class Parser {
	
	private final List<Token> tokens = new ArrayList<>();
	private int tokenIndex = 0;
	private final Lexer lexer;
	private final String fileName;


	public Parser(String fileName, String source) {
		this.lexer = new Lexer(source);
		this.fileName = fileName;
	}

	public Parser(String source) {
		this("inline", source);
	}
	
	public Program parseExpression() {
		var program = new Program();
		var compileUnit = new CompileUnit("console", program);
		var main = new ParseContext("main", compileUnit, ContextType.FUNCTION);
		var ast =  new AbstractSyntaxTree(parseNext(), main);
		var symbol = compileUnit.defineFunction("main",
				Arrays.asList(new FunctionParameter(0, "args", AbstractMachineType.I32, compileUnit)),
				AbstractMachineType.I32,
				new AbstractMachineFunction(program.getMachine(), ast, ast.globalContext));
		main.setSymbol(symbol);
		return program;
	}

	public void parseSource(Program program) {
		var compileUnit = new CompileUnit(fileName, program);
		program.addCompileUnit(compileUnit);
		while(current().kind() != TokenKind.EOF) {
			if (current().kind() == TokenKind.IDENTIFIER && next().kind() == TokenKind.IDENTIFIER) {
				// It may be a function declaration!
				var returnType = fetchToken();
				var functionName = fetchToken();
				var functionContext = new ParseContext(functionName.text(), compileUnit, ParseContext.ContextType.FUNCTION);
				
				if (current().kind() == TokenKind.OPEN_PARENTHESIS) {
					parseNextFunctionDeclaration(program, compileUnit, returnType, functionName, functionContext);
				} else {
					throw new RuntimeException("Expected open parenthesis at function declaration. Got: %s".formatted(current()));
				}
				
			}
			
		}
	}

	private void parseNextFunctionDeclaration(Program program, CompileUnit compileUnit, Token returnType, Token functionName,
			ParseContext functionContext) {
		advance();
		
		var parameterList = new ArrayList<FunctionParameter>();
		int i = 0;
		while (current().kind() != TokenKind.CLOSE_PARENTHESIS) {
			var parameterNode = parseNextParameter();

			var functionParameter = new FunctionParameter(i++, parameterNode.getName(), parameterNode.getType().evaluateType(compileUnit), compileUnit);
			functionContext.defineSymbol(functionParameter);
			parameterList.add(functionParameter);

			// FIXME: Does not throw a error if there's no comma
			if (current().kind() == TokenKind.COMMA) advance();
		}
		expectCurrent(TokenKind.CLOSE_PARENTHESIS); advance(); // Close parenthesis
		expectCurrent(TokenKind.OPEN_BRACES); advance(); // Open braces
		
		List<AbstractSyntaxTreeNode> statements = new ArrayList<>();
		while (current().kind() != TokenKind.CLOSE_BRACES) {
			statements.add(parseNextStatement(functionContext));
		}
		
		expectCurrent(TokenKind.CLOSE_BRACES); advance(); // Close braces
		
		AbstractMachineFunction amc = new AbstractMachineFunction(program.getMachine(), (List<AbstractSyntaxTreeNode>) null, functionContext);

		// parseContext is the CompileUnit. functionContext, the actual function. Do not mix those two
		functionContext.setSymbol(compileUnit.defineFunction(functionName.text(),
				parameterList,
				program.typeManager.get(returnType.text()),
				amc));
		amc.setStatements(statements);
	}
	
	private TypeExpressionNode parseNextTypeExpression() {
		expectCurrent(TokenKind.IDENTIFIER);
		var type = current(); advance();
	
		if (current().kind() == TokenKind.OPEN_BRACKETS) {
			// Is a array type expression
			expectCurrent(TokenKind.CLOSE_BRACKETS);
			advance(); advance();
			return new TypeExpressionNode(type, TypeExpressionNode.Modifier.ARRAY);
		}

		return new TypeExpressionNode(type, TypeExpressionNode.Modifier.NONE);
	}
	
	private ParameterNode parseNextParameter() {
		var type = parseNextTypeExpression();
		expectCurrent(TokenKind.IDENTIFIER);
		var parameterName = current(); advance();
		return new ParameterNode(type, parameterName);
	}

	private AbstractSyntaxTreeNode parseNextStatement(Context context) {
		if (current().kind() == TokenKind.KEYWORD) {
			switch (current().text()) {
			case "let":
			{
				advance(); // discard the "let" token
				expectCurrent(TokenKind.IDENTIFIER);
				var variableName = new IdentifierReferenceNode(fetchToken());
				expectCurrent(Token::isAssignment, "Operator must be assignment. Got: %s");
				var assignmentOperator = fetchToken();
				var expression = parseNext();
				expectCurrent(TokenKind.SEMICOLON); advance();
				context.defineVariable(variableName.getSourceToken().text(), expression.evaluateType(context));
				return new AssignmentNode(variableName, assignmentOperator, expression);
			}
			case "ret":
				var returnToken = fetchToken(); // discard the "ret" token
				
				if (current().kind() == TokenKind.SEMICOLON) {
					return new ReturnNode(returnToken, null);
				} else {
					var returnExpression = parseNextExpression();
					var returnNode = new ReturnNode(returnToken, returnExpression);
					expectCurrent(TokenKind.SEMICOLON); advance();
					return returnNode;
				}
			default:
				throw new RuntimeException("Unhandled keyword: %s".formatted(current()));
			}
		} else {
			var nextExpression = parseNextExpression();
			expectCurrent(TokenKind.SEMICOLON); advance();
			return nextExpression;
		}
	}
	
	private void expectCurrent(TokenKind kind) {
		if (current().kind() != kind) {
			List<Token> debugTokens = new ArrayList<>();
			for (int i = -4 ; i < 4; i++) {
				debugTokens.add(peek(i));
			}
			
			throw new RuntimeException("Unexpected token kind: %s. Expected: %s. Parsing: %s".formatted(current(), kind, debugTokens.stream()
					.filter(token -> token.kind() != TokenKind.EOF)
					.map(Token::text)
					.collect(Collectors.joining(" "))));
		}
	}
	
	private void expectCurrent(Predicate<Token> kind, String message) {
		if (!kind.test(current())) {
			throw new RuntimeException(message.formatted(current()));
		}
	}
	
	public AbstractSyntaxTreeNode parseNextExpression() {
	 	if (TokenKind.isOperand(current().kind())) {
	 		var operand = parseNext();
	 		
	 		if (current().kind() == TokenKind.OPERATOR) {
	 			var operatorToken = current();
	 			advance();
	 			var rhs = parseNext();
	 			return new BinaryOperatorNode(operand, rhs, operatorToken);
	 		} else {
	 			return operand;
	 		}
	 	} else {
	 		throw new IllegalArgumentException("Unexpected value: " + current().kind());
	 	}
	}
	
	/**
	 * This function expects the curret token to be pointed at the OPEN_PARENTHESIS token
	 * of the function call. The callee should've been parsed by another of the parseNext
	 * functions.
	 * @param callee The callee of this function call expression
	 * @return Object of type {@link FunctionCallNode}
	 */
	private AbstractSyntaxTreeNode parseNextFunctionCall(AbstractSyntaxTreeNode callee) {

		// Discards the open parenthesis
		expectCurrent(TokenKind.OPEN_PARENTHESIS);
		advance();

		List<AbstractSyntaxTreeNode> arguments = new ArrayList<>();

		// Parse arguments until close parenthesis
		while (current().kind() != TokenKind.CLOSE_PARENTHESIS) {
			arguments.add(parseNextExpression());
			if (current().kind() == TokenKind.COLON)
				advance();
		 	else
		 		expectCurrent(TokenKind.CLOSE_PARENTHESIS);
		}

		advance(); // Close parenthesis

		var functionCallNode = new FunctionCallNode(callee, arguments);

		if (current().kind() == TokenKind.OPERATOR) {
 			var operatorToken = current();
		 	advance();
			var rhs = parseNext();
	 		return new BinaryOperatorNode(functionCallNode, rhs, operatorToken);
		}

	 	return functionCallNode; 
	}

	private AbstractSyntaxTreeNode parseNext() {
		
		AbstractSyntaxTreeNode node = null;
		if (TokenKind.isOperand(current().kind())) {
			node = parseNextOperand();
			if (current().kind() == TokenKind.OPERATOR) {
				node = parseNextBinaryOperator(node);
			} else if(current().kind() == TokenKind.OPEN_PARENTHESIS) {
		 		node = parseNextFunctionCall(node);
			}
		} else if (current().kind() == TokenKind.OPEN_PARENTHESIS) {
			node = parseNextParenthesis();
			if (current().kind() == TokenKind.OPERATOR) {
				node = parseNextBinaryOperator(node);
			} 
		} else if (current().kind() == TokenKind.OPERATOR) {
			if (current().isAssignment()) {
				node = parseAssignmentOperator(node);
			} else {
				node = parseNextBinaryOperator(node);				
			}
		} else {
			throw new RuntimeException("Unexpected token: %s".formatted(current()));
		}
		
		return node;
	}
	
	private AbstractSyntaxTreeNode parseAssignmentOperator(AbstractSyntaxTreeNode node) {
	 	if (current().kind() == TokenKind.OPERATOR && current().isAssignment()) {
	 		var operatorToken = current();
	 		advance();
	 		var rhs = parseNext();

	 		return new AssignmentNode(node, operatorToken, rhs);
	 	} else {
	 		throw new IllegalArgumentException("Unexpected value: " + current().kind());
	 	}
	}

	private AbstractSyntaxTreeNode parseNextParenthesis() {
		assert(current().kind() == TokenKind.OPEN_PARENTHESIS);
		var open = current();
		advance();
		var innerNode = parseNext();
		if(current().kind() != TokenKind.CLOSE_PARENTHESIS) {
			throw new RuntimeException("Expected end-of-expression. Got: %s".formatted(current().kind()));
		}
		advance();
		return new ParenthesisNode(innerNode, open);
	}
	
	private AbstractSyntaxTreeNode parseNextOperand() {
		var node = switch (current().kind()) {
			case FLOAT_LITERAL, INTEGER_LITERAL -> new NumberLiteralNode(current());
			case IDENTIFIER -> new IdentifierReferenceNode(current());
			default -> throw new IllegalArgumentException("Unexpected value: " + current().kind());
		};
		advance();
		return node;
	}
	
	private AbstractSyntaxTreeNode parseNextBinaryOperator(AbstractSyntaxTreeNode lhs) {
	 	if (current().kind() == TokenKind.OPERATOR) {
	 		var operatorToken = current();
	 		advance();
	 		var rhs = parseNext();
	 		
	 		var operatorType = BinaryOperatorNode.OperatorType.fromSymbol(operatorToken.text());
	 		if (rhs instanceof BinaryOperatorNode nextOperator && nextOperator.getOperatorType().getPrecedence() <= operatorType.getPrecedence()) {
	 			
	 			var thisOperator = new BinaryOperatorNode(lhs, nextOperator.getLhs(), operatorToken);
	 			return new BinaryOperatorNode(thisOperator, nextOperator.getRhs(), nextOperator.getSourceToken());
	 		}
	 		return new BinaryOperatorNode(lhs, rhs, operatorToken);
	 	} else {
	 		throw new IllegalArgumentException("Unexpected value: " + current().kind());
	 	}
	}
	
	private void advance() {
		if (tokenIndex < tokens.size()) {
			tokenIndex++;
			peek(0);
		}
	}
	
	private Token fetchToken() {
		var token = current();
		advance();
		return token;
	}
	
	private Token current() {
		return peek(0);
	}
	
	private Token previous() {
		return peek(-1);
	}
	
	private Token next() {
		return peek(1);
	}
	
	private Token peek(int i) {
		if(!tokens.isEmpty() && tokenIndex+i < tokens.size()) {
			return tokens.get(tokenIndex+i);
		} else {
			for (int j = 0; j <= i; j++) {
				var nextToken = lexer.nextToken();
				while(nextToken.kind() == TokenKind.WHITESPACE) {
					nextToken = lexer.nextToken();
				}
				if (nextToken.kind() == TokenKind.EOF) {
					return nextToken;
				} else {
					tokens.add(nextToken);
				}
			}
			return tokens.get(tokenIndex+i);
		}
	}

	
}
