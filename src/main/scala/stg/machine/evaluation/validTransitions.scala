package stg.machine.evaluation

import scala.collection.mutable.Stack
import scala.collection.mutable.HashMap
import stg.language._
import stg.util._
import stg.machine.types._
import stg.machine.env._
import stg.machine.heap._

object validTransitions {
    def rule1_functionApp(s: StgState): Option[StgState] = {
        def buildNewState(fun: Var, args: List[Atom], locals: Locals, st: StgState): Option[StgState] = {
            
            //get a for comprehension in here?
            
            //need to find functions address and its args address from the local and global addresses
            getAtomValue(locals, st.stgGlobals)(AtomVar(fun)) match {
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
        def buildNewState(isRecursive: Boolean, letBinds: Binds, letExpr: Expr, locals: Locals, stgState: StgState): StgState = {
            val (letVars, letLambdaForms) = letBinds.binds.toList.unzip

            //alloc some blackholes on the heap, to be overriden when we later find the let vars
            val (newAddrs, heapWithPreallocations) = allocMany(letVars.map(_ => Blackhole(0)), stgState.stgHeap)

            //set up new local environment by appending the letvars mapped to new addresses on the heap to the original
            val newLocals = Locals(locals.locals ++ (letVars zip newAddrs).map(e => e._1 -> Addr(e._2)).toMap)

            //new locals are invisible to 
            val localsRhs = if (isRecursive) newLocals else locals
       
            traverser[NotInScope, LambdaForm, Closure](letLambdaForms, liftLambdaToClosure(localsRhs)) match {
                case Success(closures) => {
                    val updatedHeap = updateMany((newAddrs zip closures), heapWithPreallocations)

                    stgState.copy(
                        stgCode = Eval(letExpr, localsRhs),
                        stgHeap = updatedHeap,
                        stgInfo = Info(StateTransitionInfo(Rule3_Eval_Let(isRecursive)))
                    )
                }   
                case Failure(f) => stgState.copy(stgInfo = Info(StateTransitionInfo(Rule3_Eval_Let(isRecursive))))
            }
        }

        s.stgCode match {
            case Eval(Let(isRecursive, letBinds, letExpr), locals) => Some(buildNewState(isRecursive, letBinds, letExpr, locals, s))
            case _ => None
        }
    }

    protected def liftLambdaToClosure(locals: Locals)(lambdaForm: LambdaForm): Validate[NotInScope, Closure] = {
        
        val atoms = lambdaForm.freeVars.map(v => AtomVar(v))
        
        //all free vars in the lambda form must be present in the local environment
        traverser[NotInScope, Atom, Value](atoms, localsValue(locals)) match {
            case Success(vals) => Success(Closure(form = lambdaForm, freeVarValues = vals))
            case Failure(missingVars) => Failure(missingVars)
        }   
    }
}






