package stg.machine 

import stg.machine.types._
import stg.machine.evaluation.validTransitions

object evaluate {
    def evalStep(state: StgState): StgState = {
        val stepped = stgRule(state)

        stepped.copy(stgSteps = stepped.stgSteps + 1)
    }

    def stgRule(state: StgState): StgState = {

        def applyRules(s: StgState, rulesLeft: List[StgState => Option[StgState]]): StgState = {
            rulesLeft match {
                case h :: tail => h(s) match {
                    case None => applyRules(s, tail)
                    case Some(s) => s
                }
                case _ => noRulesApply(s)
            }
        }

        applyRules(state, rules)
    }

    def rules(): List[StgState => Option[StgState]] = {
        List(
            validTransitions.rule1_functionApp
            // validTransitions.rule2_enterNonUpdatable, 
            // validTransitions.rule3_let, 
            // validTransitions.rule1819_casePrimopShortcut
            // validTransitions.rule4_case, 
            // validTransitions.rule5_constructorApp, 
            // validTransitions.rule6_algebraicNormalMatch, 
            // validTransitions.rule7_algebraicUnboundDefaultMatch, 
            // validTransitions.rule8_algebraicBoundDefaultMatch, 
            // validTransitions.rule9_primitiveLiteralEval, 
            // validTransitions.rule10_primitiveLiteralApp, 
            // validTransitions.rule11_primitiveNormalMatch, 
            // validTransitions.rule12_primitiveBoundDefaultMatch, 
            // validTransitions.rule13_primitiveUnboundDefaultMatch, 
            // validTransitions.rule14_primop, 
            // validTransitions.rule15_enterUpdatable, 
            // validTransitions.rule16_missingReturnUpdate, 
            // validTransitions.rule17a_missingArgUpdate
        )
    }

    def noRulesApply(s: StgState): StgState = {
        s.copy(
            stgInfo = Info(NoRulesApply())
        )
    }
}


