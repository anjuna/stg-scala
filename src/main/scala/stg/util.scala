package stg

object util {
    //basically an Either type
    sealed trait Validate[+Err, +A]
    case class Failure[Err, A](err: Err) extends Validate[Err, A]
    case class Success[Err, A](x: A) extends Validate[Err, A]

    def traverser[A, B, C](xs: List[C], fun: C => Validate[A, B]): Validate[A, List[B]] = {
        xs.foldRight(Success(List()): Validate[A, List[B]]) {(x, acc) => 
            acc match {
                case Failure(f) => Failure(f) // ie if one of them fails then they all do
                case Success(l) => fun(x) match {
                    case Failure(f) => Failure(f)
                    case Success(s) => Success(s :: l)
                }
            }
        }
    }
}