import scala.collection.mutable.HashMap
import scala.collection.mutable.Stack
import org.scalatest._
import stg.machine.types._
import stg.machine.heap
import stg.language._

class machineEntrySpec extends FlatSpec with Matchers {
    "The allocation on the heap" should "be correct..." in {
        val initial = Heap(HashMap.empty)

        //allocate 2 blackholes to an empty heap. 
        val (allocatedAddys1, h1) = heap.allocMany(List(Blackhole(1), Blackhole(2)), initial)

        //first two addresses are taken up
        allocatedAddys1 shouldBe List(0,1)

        //throw another blackhole on there
        val (allocatedAddys2, h2) = heap.allocMany(List(Blackhole(3)), h1)

        //the highest address (2) now contains that last black hole, which we tagged with 3
        h2.heapMap(2) shouldBe Blackhole(3)
    }
}