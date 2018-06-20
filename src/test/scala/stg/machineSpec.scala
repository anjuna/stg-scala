package stg

import scala.collection.mutable.HashMap
import scala.collection.mutable.Stack
import org.scalatest._
import stg.machine.types._
import stg.language._

class machineEntrySpec extends FlatSpec with Matchers {
//   "The initial state function" should "create the STG initial state" in {
      
//       machine.intialState() 
//   }


    "the terminated function" should "check if the StgState is terminated" in {
        val notTerm1 = StgState(
            stgInfo = Info(StateTransitionInfo(Rule9_Lit())),
            stgCode = ReturnInt(0),
            stgStack = Stack[StackFrame](),
            stgHeap = Heap(HashMap.empty[MemAddress, HeapObject]),
            stgGlobals = Globals(HashMap.empty[Var, Value]),
            stgSteps = 0,
            )

        //@todo: sdasd
        // val notTerm2 = StgState(stgInfo = Info(StateInitial()))
        // val notTerm3 = StgState(stgInfo = Info(GarbageCollection()))

        val termed1 = StgState(
            stgInfo = Info(NoRulesApply()),
            stgCode = ReturnInt(0),
            stgStack = Stack[StackFrame](),
            stgHeap = Heap(HashMap.empty[MemAddress, HeapObject]),
            stgGlobals = Globals(HashMap.empty[Var, Value]),
            stgSteps = 0,
            )

        machineEntry.terminated(notTerm1) shouldBe false
        machineEntry.terminated(termed1) shouldBe true
    }

}