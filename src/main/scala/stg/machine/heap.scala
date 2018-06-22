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

    def alloc(newObject: HeapObject, heap: Heap): (MemAddress, Heap) = {
        val listed = allocMany(List(newObject), heap)

        //need to descructure the list of memaddresses in the first component
        (listed._1.head, listed._2)
    }

    def update(addr: MemAddress, obj: HeapObject, heap: Heap): Heap = {
        Heap(heap.heapMap += (addr -> obj))
    }

    def updateMany(maps: List[(MemAddress, HeapObject)], heap: Heap): Heap = {
        maps.foldRight(heap){ case ((addr, ob), h) => 
            update(addr, ob, heap)
        }
    }
}


