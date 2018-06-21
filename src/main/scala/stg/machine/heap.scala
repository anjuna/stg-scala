package stg.machine

import scala.collection.mutable.Stack
import scala.collection.mutable.HashMap
import stg.language._
import stg.util._
import stg.machine.types._
import stg.machine.env._
import stg.machine.env._

object heap {
    def allocMany(newObjects: List[HeapObject], heap: Heap): (List[MemAddress], Heap) = {
        val smallestUnusedAddress : Int = {
            if (heap.heapMap.isEmpty) {
                0
            } else {
                heap.heapMap.maxBy(_._1)._1 + 1
            }
        }

        //get a list of increasing addresses from the smallest unused, as many as the number of objects we wish to allocate
        val newAddresses : List[MemAddress] = (smallestUnusedAddress to (smallestUnusedAddress + newObjects.length - 1)).toList
        val newObjectsAllocated = (newAddresses zip newObjects).map(e => e._1 -> e._2).toMap

        (newAddresses, Heap(heap.heapMap ++ newObjectsAllocated))
    }
}


