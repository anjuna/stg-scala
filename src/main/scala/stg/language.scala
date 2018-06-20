package stg

import scala.collection.mutable.HashMap

object language {
    type Literal = Integer
    type Var = String


    case class Program(binds: Binds)

    sealed trait Expr 
    case class Let(isRecursive: Boolean, binds: Binds, expr: Expr) extends Expr
    case class Case(expr: Expr, alts: Alts) extends Expr
    case class AppFun(myFun: Var, args: List[Atom]) extends Expr
    case class AppConstr(constr: Constr, args: List[Atom]) extends Expr
    case class AppPrim(primOp: PrimOp, arg1: Atom, arg2: Atom) extends Expr
    case class LitE(lit: Literal) extends Expr


    case class LambdaForm(freeVars: List[Var], isUpdateable: Boolean, boundVars: List[Var], body: Expr)
    case class Binds(binds: HashMap[Var, LambdaForm])


    case class Alts(nonDefaults: NonDefaultAlts, default: DefaultAlt)

    sealed trait NonDefaultAlts
    case class NoNonDefaultAlts() extends NonDefaultAlts
    case class AlgebraicAlt(constr: Constr, vars: List[Var], expr: Expr) extends NonDefaultAlts
    case class PrimAlt(lit: Literal, expr: Expr) extends NonDefaultAlts

    sealed trait DefaultAlt
    case class DefaultNotBound(expr: Expr) extends DefaultAlt
    case class DefaultBound(myVar: Var, expr: Expr) extends DefaultAlt

    sealed trait Atom
    case class AtomVar(myVar: Var) extends Atom
    case class AtomLit(lit: Literal) extends Atom

    case class Constr(name: String)

    sealed trait PrimOp
    case class Add() extends PrimOp
    case class Sub() extends PrimOp
    case class Mul() extends PrimOp
    case class Div() extends PrimOp
    case class Mod() extends PrimOp
    case class Eq() extends PrimOp
    case class Lt() extends PrimOp
    case class Leq() extends PrimOp
    case class Gt() extends PrimOp
    case class Geq() extends PrimOp
    case class Neq() extends PrimOp
}



