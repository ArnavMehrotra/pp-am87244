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
	 * Visit a parse tree produced by {@link SimpleLangParser#constdecl}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitConstdecl(SimpleLangParser.ConstdeclContext ctx);
	/**
	 * Visit a parse tree produced by {@link SimpleLangParser#enumdecl}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitEnumdecl(SimpleLangParser.EnumdeclContext ctx);
	/**
	 * Visit a parse tree produced by {@link SimpleLangParser#varDecl}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitVarDecl(SimpleLangParser.VarDeclContext ctx);
	/**
	 * Visit a parse tree produced by {@link SimpleLangParser#classdecl}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitClassdecl(SimpleLangParser.ClassdeclContext ctx);
	/**
	 * Visit a parse tree produced by {@link SimpleLangParser#statement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitStatement(SimpleLangParser.StatementContext ctx);
	/**
	 * Visit a parse tree produced by {@link SimpleLangParser#interfacedecl}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitInterfacedecl(SimpleLangParser.InterfacedeclContext ctx);
	/**
	 * Visit a parse tree produced by {@link SimpleLangParser#interfacemethoddecl}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitInterfacemethoddecl(SimpleLangParser.InterfacemethoddeclContext ctx);
	/**
	 * Visit a parse tree produced by {@link SimpleLangParser#methoddecl}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMethoddecl(SimpleLangParser.MethoddeclContext ctx);
	/**
	 * Visit a parse tree produced by {@link SimpleLangParser#formpars}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFormpars(SimpleLangParser.FormparsContext ctx);
	/**
	 * Visit a parse tree produced by {@link SimpleLangParser#expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExpr(SimpleLangParser.ExprContext ctx);
	/**
	 * Visit a parse tree produced by {@link SimpleLangParser#factor}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFactor(SimpleLangParser.FactorContext ctx);
	/**
	 * Visit a parse tree produced by {@link SimpleLangParser#term}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTerm(SimpleLangParser.TermContext ctx);
	/**
	 * Visit a parse tree produced by {@link SimpleLangParser#type}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitType(SimpleLangParser.TypeContext ctx);
	/**
	 * Visit a parse tree produced by {@link SimpleLangParser#designatorstatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDesignatorstatement(SimpleLangParser.DesignatorstatementContext ctx);
	/**
	 * Visit a parse tree produced by {@link SimpleLangParser#condition}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCondition(SimpleLangParser.ConditionContext ctx);
	/**
	 * Visit a parse tree produced by {@link SimpleLangParser#condterm}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCondterm(SimpleLangParser.CondtermContext ctx);
	/**
	 * Visit a parse tree produced by {@link SimpleLangParser#condfact}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCondfact(SimpleLangParser.CondfactContext ctx);
	/**
	 * Visit a parse tree produced by {@link SimpleLangParser#actparse}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitActparse(SimpleLangParser.ActparseContext ctx);
	/**
	 * Visit a parse tree produced by {@link SimpleLangParser#designator}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDesignator(SimpleLangParser.DesignatorContext ctx);
	/**
	 * Visit a parse tree produced by {@link SimpleLangParser#operator}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitOperator(SimpleLangParser.OperatorContext ctx);
	/**
	 * Visit a parse tree produced by {@link SimpleLangParser#assignop}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAssignop(SimpleLangParser.AssignopContext ctx);
	/**
	 * Visit a parse tree produced by {@link SimpleLangParser#addop}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAddop(SimpleLangParser.AddopContext ctx);
	/**
	 * Visit a parse tree produced by {@link SimpleLangParser#relop}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRelop(SimpleLangParser.RelopContext ctx);
	/**
	 * Visit a parse tree produced by {@link SimpleLangParser#mulop}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMulop(SimpleLangParser.MulopContext ctx);
	/**
	 * Visit a parse tree produced by {@link SimpleLangParser#bool}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitBool(SimpleLangParser.BoolContext ctx);
	/**
	 * Visit a parse tree produced by {@link SimpleLangParser#charConst}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCharConst(SimpleLangParser.CharConstContext ctx);
	/**
	 * Visit a parse tree produced by {@link SimpleLangParser#integer}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitInteger(SimpleLangParser.IntegerContext ctx);
}