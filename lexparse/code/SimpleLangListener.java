// Generated from SimpleLang.g4 by ANTLR 4.9.2
import org.antlr.v4.runtime.tree.ParseTreeListener;

/**
 * This interface defines a complete listener for a parse tree produced by
 * {@link SimpleLangParser}.
 */
public interface SimpleLangListener extends ParseTreeListener {
	/**
	 * Enter a parse tree produced by {@link SimpleLangParser#program}.
	 * @param ctx the parse tree
	 */
	void enterProgram(SimpleLangParser.ProgramContext ctx);
	/**
	 * Exit a parse tree produced by {@link SimpleLangParser#program}.
	 * @param ctx the parse tree
	 */
	void exitProgram(SimpleLangParser.ProgramContext ctx);
	/**
	 * Enter a parse tree produced by {@link SimpleLangParser#expr}.
	 * @param ctx the parse tree
	 */
	void enterExpr(SimpleLangParser.ExprContext ctx);
	/**
	 * Exit a parse tree produced by {@link SimpleLangParser#expr}.
	 * @param ctx the parse tree
	 */
	void exitExpr(SimpleLangParser.ExprContext ctx);
	/**
	 * Enter a parse tree produced by {@link SimpleLangParser#factor}.
	 * @param ctx the parse tree
	 */
	void enterFactor(SimpleLangParser.FactorContext ctx);
	/**
	 * Exit a parse tree produced by {@link SimpleLangParser#factor}.
	 * @param ctx the parse tree
	 */
	void exitFactor(SimpleLangParser.FactorContext ctx);
	/**
	 * Enter a parse tree produced by {@link SimpleLangParser#term}.
	 * @param ctx the parse tree
	 */
	void enterTerm(SimpleLangParser.TermContext ctx);
	/**
	 * Exit a parse tree produced by {@link SimpleLangParser#term}.
	 * @param ctx the parse tree
	 */
	void exitTerm(SimpleLangParser.TermContext ctx);
	/**
	 * Enter a parse tree produced by {@link SimpleLangParser#type}.
	 * @param ctx the parse tree
	 */
	void enterType(SimpleLangParser.TypeContext ctx);
	/**
	 * Exit a parse tree produced by {@link SimpleLangParser#type}.
	 * @param ctx the parse tree
	 */
	void exitType(SimpleLangParser.TypeContext ctx);
	/**
	 * Enter a parse tree produced by {@link SimpleLangParser#designatorstatement}.
	 * @param ctx the parse tree
	 */
	void enterDesignatorstatement(SimpleLangParser.DesignatorstatementContext ctx);
	/**
	 * Exit a parse tree produced by {@link SimpleLangParser#designatorstatement}.
	 * @param ctx the parse tree
	 */
	void exitDesignatorstatement(SimpleLangParser.DesignatorstatementContext ctx);
	/**
	 * Enter a parse tree produced by {@link SimpleLangParser#actparse}.
	 * @param ctx the parse tree
	 */
	void enterActparse(SimpleLangParser.ActparseContext ctx);
	/**
	 * Exit a parse tree produced by {@link SimpleLangParser#actparse}.
	 * @param ctx the parse tree
	 */
	void exitActparse(SimpleLangParser.ActparseContext ctx);
	/**
	 * Enter a parse tree produced by {@link SimpleLangParser#designator}.
	 * @param ctx the parse tree
	 */
	void enterDesignator(SimpleLangParser.DesignatorContext ctx);
	/**
	 * Exit a parse tree produced by {@link SimpleLangParser#designator}.
	 * @param ctx the parse tree
	 */
	void exitDesignator(SimpleLangParser.DesignatorContext ctx);
	/**
	 * Enter a parse tree produced by {@link SimpleLangParser#operator}.
	 * @param ctx the parse tree
	 */
	void enterOperator(SimpleLangParser.OperatorContext ctx);
	/**
	 * Exit a parse tree produced by {@link SimpleLangParser#operator}.
	 * @param ctx the parse tree
	 */
	void exitOperator(SimpleLangParser.OperatorContext ctx);
	/**
	 * Enter a parse tree produced by {@link SimpleLangParser#ident}.
	 * @param ctx the parse tree
	 */
	void enterIdent(SimpleLangParser.IdentContext ctx);
	/**
	 * Exit a parse tree produced by {@link SimpleLangParser#ident}.
	 * @param ctx the parse tree
	 */
	void exitIdent(SimpleLangParser.IdentContext ctx);
	/**
	 * Enter a parse tree produced by {@link SimpleLangParser#assignop}.
	 * @param ctx the parse tree
	 */
	void enterAssignop(SimpleLangParser.AssignopContext ctx);
	/**
	 * Exit a parse tree produced by {@link SimpleLangParser#assignop}.
	 * @param ctx the parse tree
	 */
	void exitAssignop(SimpleLangParser.AssignopContext ctx);
	/**
	 * Enter a parse tree produced by {@link SimpleLangParser#addop}.
	 * @param ctx the parse tree
	 */
	void enterAddop(SimpleLangParser.AddopContext ctx);
	/**
	 * Exit a parse tree produced by {@link SimpleLangParser#addop}.
	 * @param ctx the parse tree
	 */
	void exitAddop(SimpleLangParser.AddopContext ctx);
	/**
	 * Enter a parse tree produced by {@link SimpleLangParser#relop}.
	 * @param ctx the parse tree
	 */
	void enterRelop(SimpleLangParser.RelopContext ctx);
	/**
	 * Exit a parse tree produced by {@link SimpleLangParser#relop}.
	 * @param ctx the parse tree
	 */
	void exitRelop(SimpleLangParser.RelopContext ctx);
	/**
	 * Enter a parse tree produced by {@link SimpleLangParser#mulop}.
	 * @param ctx the parse tree
	 */
	void enterMulop(SimpleLangParser.MulopContext ctx);
	/**
	 * Exit a parse tree produced by {@link SimpleLangParser#mulop}.
	 * @param ctx the parse tree
	 */
	void exitMulop(SimpleLangParser.MulopContext ctx);
	/**
	 * Enter a parse tree produced by {@link SimpleLangParser#bool}.
	 * @param ctx the parse tree
	 */
	void enterBool(SimpleLangParser.BoolContext ctx);
	/**
	 * Exit a parse tree produced by {@link SimpleLangParser#bool}.
	 * @param ctx the parse tree
	 */
	void exitBool(SimpleLangParser.BoolContext ctx);
	/**
	 * Enter a parse tree produced by {@link SimpleLangParser#integer}.
	 * @param ctx the parse tree
	 */
	void enterInteger(SimpleLangParser.IntegerContext ctx);
	/**
	 * Exit a parse tree produced by {@link SimpleLangParser#integer}.
	 * @param ctx the parse tree
	 */
	void exitInteger(SimpleLangParser.IntegerContext ctx);
}