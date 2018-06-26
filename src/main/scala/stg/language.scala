package stg

import scala.collection.mutable.HashMap

object language {
    type Literal = Integer
    type Var = String


    case class Program(binds: Binds)

    sealed trait Expr 
    case class Let(isRecursive: Boolean, binds: Binds, expr: Expr) extends Expr
    case class Case(expr: Expr, alts: Alts) extends Expr
    case class AppFun(fun: Var, args: List[Atom]) extends Expr
    case class AppConstr(constr: Constr, args: List[Atom]) extends Expr
    case class AppPrim(primOp: PrimOp, arg1: Atom, arg2: Atom) extends Expr
    case class LitE(lit: Literal) extends Expr

    case class Binds(binds: HashMap[Var, LambdaForm])
    case class LambdaForm(freeVars: List[Var], isUpdateable: Boolean, boundVars: List[Var], body: Expr)

    case class Alts(nonDefaults: NonDefaultAlts, defaults: DefaultAlt)

    sealed trait NonDefaultAlts
    case object NoNonDefaultAlts extends NonDefaultAlts
    case class AlgebraicAlts(alts: List[AlgebraicAlt]) extends NonDefaultAlts
    case class PrimAlts(alts: List[PrimAlt]) extends NonDefaultAlts

    case class AlgebraicAlt(constr: Constr, vars: List[Var], expr: Expr)
    case class PrimAlt(lit: Literal, expr: Expr)

    sealed trait DefaultAlt
    case class DefaultNotBound(expr: Expr) extends DefaultAlt
    case class DefaultBound(myVar: Var, expr: Expr) extends DefaultAlt

    sealed trait Atom
    case class AtomVar(myVar: Var) extends Atom
    case class AtomLit(lit: Literal) extends Atom

    case class Constr(name: String)

    sealed trait PrimOp
    case object Add extends PrimOp
    case object Sub extends PrimOp
    case object Mul extends PrimOp
    case object Div extends PrimOp
    case object Mod extends PrimOp
    case object Eq extends PrimOp
    case object Lt extends PrimOp
    case object Leq extends PrimOp
    case object Gt extends PrimOp
    case object Geq extends PrimOp
    case object Neq extends PrimOp
}



