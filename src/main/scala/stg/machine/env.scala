package stg.machine

import scala.collection.mutable.HashMap
import stg.language._
import stg.machine.types._
import stg.util._

object env {
    def getAtomValue(locals: Locals, globals: Globals)(atom: Atom): Validate[NotInScope, Value] = {
        atom match {
            case AtomLit(lit) => Success(PrimInt(lit))
            case AtomVar(myVar) => locals.locals.get(myVar) match {
                case Some(v) => Success(v)
                case None => globals.vals.get(myVar) match {
                    case Some(v) => Success(v)
                    case None => Failure(NotInScope(List(myVar)))
                }
            }
        }
    }

    def getAtomsValue(locals: Locals, globals: Globals, atoms: List[Atom]): Validate[NotInScope, List[Value]] = {
        traverser[NotInScope, Atom, Value](atoms, getAtomValue(locals, globals))
    }

    def localsValue(locals: Locals)(atom: Atom): Validate[NotInScope, Value] = {
        getAtomValue(locals, Globals(HashMap.empty))(atom)
    }

    def globalsValue(globals: Globals, atom: Atom): Validate[NotInScope, Value] = {
        getAtomValue(Locals(HashMap.empty), globals)(atom)
    }
}


