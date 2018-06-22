package stg

object util {
    //basically an Either type
    sealed trait Validate[+Err, +A]
    case class Failure[Err, A](err: Err) extends Validate[Err, A]
    case class Success[Err, A](x: A) extends Validate[Err, A]

    //specialised to lists, in haskell we have: traverse :: (a -> f b) -> [a] -> f [b]
    def traverser[FailType, Key, Val](keys: List[Key], lookupFun: Key => Validate[FailType, Val]): Validate[FailType, List[Val]] = {
        keys.foldRight(Success(List()): Validate[FailType, List[Val]]) {(key, acc) => 
            acc match {
                case Failure(f) => Failure(f) // ie if one of them fails then they all do
                case Success(successfulVals) => lookupFun(key) match {
                    case Failure(f) => Failure(f)
                    case Success(valFound) => Success(valFound :: successfulVals) //build up a list of the successful results
                }
            }
        }
    }
}