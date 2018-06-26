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
    case class Closure(form: LambdaForm, freeVarValues: List[Value]) extends HeapObject
    case class Blackhole(tag: Int) extends HeapObject

    case class Globals(vals: HashMap[Var, Value])

    sealed trait Value
    case class Addr(address: MemAddress) extends Value
    case class PrimInt(pimInt: Integer) extends Value

    // case class MemAddress(addr: Int)
    type MemAddress = Int
    
    case class Locals(locals: HashMap[Var, Value])

    sealed trait StackFrame
    case class ArgumentFrame(myVal: Value) extends StackFrame
    case class ReturnFrame(alts: Alts, locals: Locals) extends StackFrame
    case class UpdateFrame(memAddr: MemAddress) extends StackFrame

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
    case object UpdatableClosureWithArgs extends StateError
    case object ReturnIntWithEmptyReturnStack extends StateError
    case object AlgReturnToPrimAlts extends StateError
    case object PrimReturnToAlgAlts extends StateError
    case object InitialStateCreationFailed extends StateError
    case object EnterBlackhole extends StateError
    case object UpdateClosureWithPrimitive extends StateError
    case object NonAlgPrimScrutinee extends StateError
    case object DivisionByZero extends StateError
    case class BadConArity(scrutinee: Int, pattern: Int)  extends StateError

    sealed trait StateTransition
    case object Rule1_Eval_FunctionApplication extends StateTransition
    case object Rule2_Enter_NonUpdatableClosure extends StateTransition
    case class  Rule3_Eval_Let(isRecursive: Boolean) extends StateTransition
    case object Rule4_Eval_Case extends StateTransition
    case object Rule5_Eval_AppC extends StateTransition
    case object Rule6_ReturnCon_Match extends StateTransition
    case object Rule7_ReturnCon_DefUnbound extends StateTransition
    case object Rule8_ReturnCon_DefBound extends StateTransition
    case object Rule9_Lit extends StateTransition
    case object Rule10_LitApp extends StateTransition
    case object Rule11_ReturnInt_Match extends StateTransition
    case object Rule12_ReturnInt_DefBound extends StateTransition
    case object Rule13_ReturnInt_DefUnbound extends StateTransition
    case object Rule14_Eval_AppP extends StateTransition
    case object Rule15_Enter_UpdatableClosure extends StateTransition
    case object Rule16_ReturnCon_Update extends StateTransition
    case object Rule17_Enter_PartiallyAppliedUpdate extends StateTransition
    case object Rule17a_Enter_PartiallyAppliedUpdate extends StateTransition
    case object Rule1819_Eval_Case_Primop_Shortcut extends StateTransition

}



