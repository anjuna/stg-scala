package stg

object util {
    sealed trait Validate[+Err, +A]
    case class Failure[Err, A](err: Err) extends Validate[Err, A]
    case class Success[Err, A](x: A) extends Validate[Err, A]
}