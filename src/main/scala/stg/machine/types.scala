package stg.machine

import scala.collection.mutable.HashMap
import scala.collection.mutable.Stack
import stg.language._

object types {
    case class StgState(
        stgCode : Code,
        stgStack: Stack[StackFrame],
        stgHeap: Heap,
        stgGlobals: Globals,
        stgInfo: Info,
        stgSteps: Integer,
        )

    sealed trait Code
    case class Eval(expr: Expr, locals: Locals) extends Code
    case class Enter(memAddress: MemAddress) extends Code
    case class ReturnCon(constr: Constr, vals: List[Value]) extends Code
    case class ReturnInt(int: Int) extends Code

    case class Heap(heapMap: HashMap[MemAddress, HeapObject])

    sealed trait HeapObject
    case class HClosure(closure: Closure) extends HeapObject
    case class Blackhole(tag: Int) extends HeapObject

    case class Globals(vals: HashMap[Var, Value])

    sealed trait Value
    case class Addr(address: MemAddress) extends Value
    case class PrimInt(pimInt: Integer) extends Value

    // case class MemAddress(addr: Int)
    type MemAddress = Int
    
    case class Locals(vals: HashMap[Var, Value])

    sealed trait StackFrame
    case class ArgumentFrame(myVal: Value) extends StackFrame
    case class ReturnFrame(alts: Alts, locals: Locals) extends StackFrame
    case class UpdateFrame(memAddr: MemAddress) extends StackFrame

    case class Closure(form: LambdaForm, freeVars: List[Var])

    case class Info(info: InfoShort)
 
    sealed trait InfoShort
    case class NoRulesApply() extends InfoShort
    case class MaxStepsExceeded() extends InfoShort
    case class HaltedByPredicate() extends InfoShort
    case class StateErrorInfo(error: StateError) extends InfoShort
    case class StateTransitionInfo(transition: StateTransition) extends InfoShort
    case class StateInitial() extends InfoShort
    case class GarbageCollection() extends InfoShort

    case class NotInScope(vars: List[Var])

    sealed trait StateError
    case class VariablesNotInScope(vars: List[Var]) extends StateError
    case class UpdatableClosureWithArgs() extends StateError
    case class ReturnIntWithEmptyReturnStack() extends StateError
    case class AlgReturnToPrimAlts() extends StateError
    case class PrimReturnToAlgAlts() extends StateError
    case class InitialStateCreationFailed() extends StateError
    case class EnterBlackhole() extends StateError
    case class UpdateClosureWithPrimitive() extends StateError
    case class NonAlgPrimScrutinee() extends StateError
    case class DivisionByZero() extends StateError
    case class BadConArity(scrutinee: Int, pattern: Int)  extends StateError

    sealed trait StateTransition
    case class Rule1_Eval_FunctionApplication() extends StateTransition
    case class Rule2_Enter_NonUpdatableClosure() extends StateTransition
    case class Rule3_Eval_Let(isRecursive: Boolean) extends StateTransition
    case class Rule4_Eval_Case() extends StateTransition
    case class Rule5_Eval_AppC() extends StateTransition
    case class Rule6_ReturnCon_Match() extends StateTransition
    case class Rule7_ReturnCon_DefUnbound() extends StateTransition
    case class Rule8_ReturnCon_DefBound() extends StateTransition
    case class Rule9_Lit() extends StateTransition
    case class Rule10_LitApp() extends StateTransition
    case class Rule11_ReturnInt_Match() extends StateTransition
    case class Rule12_ReturnInt_DefBound() extends StateTransition
    case class Rule13_ReturnInt_DefUnbound() extends StateTransition
    case class Rule14_Eval_AppP() extends StateTransition
    case class Rule15_Enter_UpdatableClosure() extends StateTransition
    case class Rule16_ReturnCon_Update() extends StateTransition
    case class Rule17_Enter_PartiallyAppliedUpdate() extends StateTransition
    case class Rule17a_Enter_PartiallyAppliedUpdate() extends StateTransition
    case class Rule1819_Eval_Case_Primop_Shortcut() extends StateTransition

}



