// Generated from SimpleLang.g4 by ANTLR 4.9.2
import org.antlr.v4.runtime.tree.ParseTreeVisitor;

/**
 * This interface defines a complete generic visitor for a parse tree produced
 * by {@link SimpleLangParser}.
 *
 * @param <T> The return type of the visit operation. Use {@link Void} for
 * operations with no return type.
 */
public interface SimpleLangVisitor<T> extends ParseTreeVisitor<T> {
	/**
	 * Visit a parse tree produced by {@link SimpleLangParser#program}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitProgram(SimpleLangParser.ProgramContext ctx);
	/**
	 * Visit a parse tree produced by {@link SimpleLangParser#expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExpr(SimpleLangParser.ExprContext ctx);
	/**
	 * Visit a parse tree produced by {@link SimpleLangParser#designatorstatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDesignatorstatement(SimpleLangParser.DesignatorstatementContext ctx);
	/**
	 * Visit a parse tree produced by {@link SimpleLangParser#operator}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitOperator(SimpleLangParser.OperatorContext ctx);
	/**
	 * Visit a parse tree produced by {@link SimpleLangParser#ident}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitIdent(SimpleLangParser.IdentContext ctx);
	/**
	 * Visit a parse tree produced by {@link SimpleLangParser#assignop}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAssignop(SimpleLangParser.AssignopContext ctx);
	/**
	 * Visit a parse tree produced by {@link SimpleLangParser#integer}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitInteger(SimpleLangParser.IntegerContext ctx);
}