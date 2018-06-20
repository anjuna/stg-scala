package stg.machine

import scala.collection.mutable.HashMap
import stg.language._
import stg.machine.types._
import stg.util._

object env {
    def getAtomValue(locals: Locals, globals: Globals, atom: Atom): Validate[NotInScope, Value] = {
        atom match {
            case AtomLit(lit) => Success(PrimInt(lit))
            case AtomVar(myVar) => locals.vals.get(myVar) match {
                case Some(v) => Success(v)
                case None => globals.vals.get(myVar) match {
                    case Some(v) => Success(v)
                    case None => Failure(NotInScope(List(myVar)))
                }
            }
        }
    }

    //how to do a traverse in scala?? can't quite figure how to accumulate failures..
    def getAtomsValue(locals: Locals, globals: Globals, atoms: List[Atom]): Validate[NotInScope, List[Value]] = {
        atoms.foldRight(Success(List()): Validate[NotInScope, List[Value]]) {(atom, acc) => 
            acc match {
                case Failure(f) => Failure(f) // ie if one of them fails then they all do
                case Success(l) => getAtomValue(locals, globals, atom) match {
                    case Failure(f) => Failure(f)
                    case Success(s) => Success(s :: l)
                }
            }
        }
    }
}


