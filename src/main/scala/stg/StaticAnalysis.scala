package stg

import stg.language._

object StaticAnalysis {
    trait HasFreeVariables[A] {
        def getFreeVariables(x: A): Set[Var]
    }

    implicit object ExprHasFreeVariables extends HasFreeVariables[Expr] {
        def getFreeVariables(e: Expr) = {
            e match {
                case Let(isRecursive, binds, expr) => (expr.getFreeVariables &~ binds.binds.keySet) ++ binds.getFreeVariables
                case Case(expr, alts) => expr.getFreeVariables ++ alts.getFreeVariables
                case AppFun(fun, args) => Set(fun) ++ args.getFreeVariables
                case AppConstr(constr, args) => args.getFreeVariables
                case AppPrim(primOp, arg1, arg2) => arg1.getFreeVariables ++ arg2.getFreeVariables
                case LitE(lit) => Set.empty
            }
        }
    }


    implicit object BindsHasFreeVariables extends HasFreeVariables[Binds] {
        def getFreeVariables(binds: Binds) = {
            binds.binds.foldRight(Set.empty){ case (acc, (v, l)) =>
                acc
            }
        }
    }

    implicit object AtomHasFreeVariables extends HasFreeVariables[Atom] {
        def getFreeVariables(atom: Atom) = {
            atom match {
                case AtomVar(v) => Set(v)
                case _ => Set.empty
            }
        }
    }

    implicit object AltsHasFreeVariables extends HasFreeVariables[Alts] {
        def getFreeVariables(alts: Alts) = alts.nonDefaults.getFreeVariables ++ alts.defaults.getFreeVariables
    }

    implicit object NonDefaultAltsHasFreeVariables extends HasFreeVariables[NonDefaultAlts] {
        def getFreeVariables(nonDefaultAlts: NonDefaultAlts) = {
            nonDefaultAlts match {
                case NoNonDefaultAlts   => Set.empty
                case AlgebraicAlts(alts) => Set.empty
                case PrimAlts(alts) => Set.empty
            }
        } 
    }

    implicit object AlgebraicAltHasFreeVariables extends HasFreeVariables[AlgebraicAlt] where
        freeVariables (AlgebraicAlt _con patVars expr)
        = freeVariables expr -<> freeVariables patVars

    implicit object PrimitiveAltHasFreeVariables extends HasFreeVariables[PrimAlt] {
        def getFreeVariables(prim: PrimAlt) = prim.expr.getFreeVariables
    }

    implicit object DefaultAltHasFreeVariables extends HasFreeVariables[DefaultAlt] {
        def getFreeVariables(d: DefaultAlt) = {
            d match {
                case DefaultNotBound(expr) => expr.getFreeVariables
                case DefaultBound(myVar, expr) =>expr.getFreeVariables &~ myVar
            }
        }
    }


    //easy peasy
    implicit object LambdaFormHasFreeVariables extends HasFreeVariables[LambdaForm] {
        def getFreeVariables(lf: LambdaForm) = lf.freeVars.toSet
    }

    //literals are just that, they have no variables inside
    implicit object LiteralHasFreeVariables extends HasFreeVariables[Literal] {
        def getFreeVariables(l: Literal) = Set.empty
    }

    //we assume for a single variable that it's free
    implicit object VarHasFreeVariables extends HasFreeVariables[Var] {
        def getFreeVariables(v: Var) = Set(v)
    }

    


    //glue it all together
    implicit class FreeVariableUtil[A](x: A) {
        def getFreeVariables(implicit hasFreeVars: HasFreeVariables[A]) = {
            hasFreeVars.getFreeVariables(x)
        }
    }

}