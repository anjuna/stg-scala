package stg.machine.evaluation

import scala.collection.mutable.Stack
import scala.collection.mutable.HashMap
import stg.language._
import stg.util._
import stg.machine.types._
import stg.machine.env._
import stg.machine.env._

object validTransitions {
    def rule1_functionApp(s: StgState): Option[StgState] = {
        def buildNewState(fun: Var, args: List[Atom], locals: Locals, st: StgState): Option[StgState] = {
            
            //get a for comprehension in here?
            
            //need to find functions address and its args address from the local and global addresses
            getAtomValue(locals, st.stgGlobals, AtomVar(fun)) match {
                case Success(Addr(address)) => getAtomsValue(locals, st.stgGlobals, args) match {
                    case Success(vals) => {
                        //actually build new state here

                        val framedValues = vals.map(v => ArgumentFrame(v))
                        val newStack = st.stgStack.pushAll(framedValues); 

                        Some(st.copy(
                            stgCode = Enter(address), //entering function's address
                            stgStack = newStack,
                            stgInfo = Info(StateTransitionInfo(Rule1_Eval_FunctionApplication()))
                        ))
                    }
                    case _ => None
                }
                case _ => None
            }
        }

        s.stgCode match {
            case Eval(exp @ AppFun(fun, args), locals) => buildNewState(fun, args, locals, s)
            case _ => None
        }
    }

    //rule2_enterNonUpdatable

    def rule3_let(s: StgState): Option[StgState] = {
        def buildNewState(letBinds: Binds, letExpr: Expr, locals: Locals, st: StgState): StgState = {
            val (letVars, letLambdaForms) = letBinds.binds.toList.unzip
        }

        s.stgCode match {
            case Eval(Let(isRecursive, letBinds, letExpr), locals) => Some(buildNewState(letBinds, letExpr, locals, s))
            case _ => None
        }
    }
}