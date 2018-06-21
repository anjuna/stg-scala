package stg

import scala.collection.mutable.HashMap
import scala.collection.mutable.Stack
import stg.machine.types._
import stg.language._
import stg.machine.evaluate._

object machineEntry {
    def initialState(mainVar: Var, prog: Program): StgState = {
        val dummyEval = Eval(
            expr = Let(isRecursive = true, binds = prog.binds, expr = AppFun(mainVar, List())), 
            locals = Locals(HashMap.empty[Var, Value])
            )
        
        val dummyInit = StgState(
          stgCode    = dummyEval,
          stgStack   = Stack[StackFrame](),
          stgHeap    = Heap(HashMap.empty[MemAddress, HeapObject]),
          stgGlobals = Globals(HashMap.empty[Var, Value]),
          stgSteps   = 1,
          stgInfo    = Info(StateInitial())
          )

          evalStep(dummyInit) match {
              case ss if terminated(ss) => ss
              case s => s.stgCode match {
                 case Eval(AppFun(mainVar, List()), Locals(locals)) => s.copy(
                    stgCode    = Eval(AppFun(mainVar, List()), Locals(HashMap.empty[Var, Value])),
                    stgGlobals = Globals(vals = locals), //copying over locals to globals
                    stgSteps   = 0,
                    stgInfo    = Info(StateInitial()),
                 )
                 case _ => s.copy(
                    stgInfo = Info(StateErrorInfo(InitialStateCreationFailed()))
                 )
              }
          }
    } 

    def terminated(s: StgState): Boolean = { 
        s.stgInfo.info match {
            case StateTransitionInfo(_)   => false //how to not have to specify the number of parameters here??
            case StateInitial()      => false
            case GarbageCollection() => false
            case _ => true
        }
    }
}