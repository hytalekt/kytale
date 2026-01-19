package io.github.hytalekt.kytale.extension

import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.nulls.shouldBeNull
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.collections.shouldContain
import io.kotest.matchers.collections.shouldHaveSize

class CollectionExtensionsTest : FunSpec({
    
    context("randomOrNull") {
        test("returns null for empty collection") {
            val result = emptyList<Int>().randomOrNull()
            
            result.shouldBeNull()
        }
        
        test("returns element from non-empty collection") {
            val list = listOf(1, 2, 3)
            
            val result = list.randomOrNull()
            
            result.shouldNotBeNull()
            list shouldContain result
        }
        
        test("returns only element from single-element list") {
            val list = listOf(42)
            
            val result = list.randomOrNull()
            
            result shouldBe 42
        }
    }
    
    context("randomElements") {
        test("returns empty list when count is 0") {
            val list = listOf(1, 2, 3)
            
            val result = list.randomElements(0)
            
            result shouldHaveSize 0
        }
        
        test("returns requested number of elements") {
            val list = listOf(1, 2, 3, 4, 5)
            
            val result = list.randomElements(3)
            
            result shouldHaveSize 3
        }
        
        test("returns all elements when count exceeds size") {
            val list = listOf(1, 2, 3)
            
            val result = list.randomElements(10)
            
            result shouldHaveSize 3
        }
    }
    
    context("allMatch") {
        test("returns true when all match") {
            val list = listOf(2, 4, 6, 8)
            
            val result = list.allMatch { it % 2 == 0 }
            
            result shouldBe true
        }
        
        test("returns false when any does not match") {
            val list = listOf(2, 3, 6, 8)
            
            val result = list.allMatch { it % 2 == 0 }
            
            result shouldBe false
        }
        
        test("returns true for empty collection") {
            val list = emptyList<Int>()
            
            val result = list.allMatch { it > 0 }
            
            result shouldBe true
        }
    }
    
    context("anyMatch") {
        test("returns true when any matches") {
            val list = listOf(1, 2, 3)
            
            val result = list.anyMatch { it == 2 }
            
            result shouldBe true
        }
        
        test("returns false when none match") {
            val list = listOf(1, 3, 5)
            
            val result = list.anyMatch { it == 2 }
            
            result shouldBe false
        }
        
        test("returns false for empty collection") {
            val list = emptyList<Int>()
            
            val result = list.anyMatch { it > 0 }
            
            result shouldBe false
        }
    }
    
    context("noneMatch") {
        test("returns true when none match") {
            val list = listOf(1, 3, 5)
            
            val result = list.noneMatch { it == 2 }
            
            result shouldBe true
        }
        
        test("returns false when any matches") {
            val list = listOf(1, 2, 3)
            
            val result = list.noneMatch { it == 2 }
            
            result shouldBe false
        }
        
        test("returns true for empty collection") {
            val list = emptyList<Int>()
            
            val result = list.noneMatch { it > 0 }
            
            result shouldBe true
        }
    }
    
    context("countBy") {
        test("counts occurrences by key") {
            val list = listOf("a", "b", "a", "c", "a")
            
            val result = list.countBy { it }
            
            result["a"] shouldBe 3
            result["b"] shouldBe 1
            result["c"] shouldBe 1
        }
        
        test("returns empty map for empty collection") {
            val list = emptyList<String>()
            
            val result = list.countBy { it }
            
            result.size shouldBe 0
        }
    }
    
    context("duplicates") {
        test("returns duplicate elements") {
            val list = listOf("a", "b", "a", "c", "b", "a")
            
            val result = list.duplicates()
            
            result shouldContain "a"
            result shouldContain "b"
        }
        
        test("does not include non-duplicates") {
            val list = listOf("a", "b", "a", "c")
            
            val result = list.duplicates()
            
            result shouldContain "a"
            result.contains("c") shouldBe false
        }
        
        test("returns empty set for no duplicates") {
            val list = listOf("a", "b", "c")
            
            val result = list.duplicates()
            
            result.size shouldBe 0
        }
    }
    
    context("secondOrNull and thirdOrNull") {
        test("secondOrNull returns second element") {
            val list = listOf(1, 2, 3)
            
            list.secondOrNull() shouldBe 2
        }
        
        test("secondOrNull returns null for single element") {
            val list = listOf(1)
            
            list.secondOrNull().shouldBeNull()
        }
        
        test("thirdOrNull returns third element") {
            val list = listOf(1, 2, 3)
            
            list.thirdOrNull() shouldBe 3
        }
        
        test("thirdOrNull returns null for two elements") {
            val list = listOf(1, 2)
            
            list.thirdOrNull().shouldBeNull()
        }
    }
    
    context("replaceFirst") {
        test("replaces first matching element") {
            val list = listOf(1, 2, 3, 2, 4)
            
            val result = list.replaceFirst({ it == 2 }, 99)
            
            result shouldBe listOf(1, 99, 3, 2, 4)
        }
        
        test("returns original list if no match") {
            val list = listOf(1, 2, 3)
            
            val result = list.replaceFirst({ it == 5 }, 99)
            
            result shouldBe listOf(1, 2, 3)
        }
    }
    
    context("replaceAll") {
        test("replaces all matching elements") {
            val list = listOf(1, 2, 3, 2, 4)
            
            val result = list.replaceAll({ it == 2 }, 99)
            
            result shouldBe listOf(1, 99, 3, 99, 4)
        }
        
        test("returns original list if no match") {
            val list = listOf(1, 2, 3)
            
            val result = list.replaceAll({ it == 5 }, 99)
            
            result shouldBe listOf(1, 2, 3)
        }
    }
    
    context("swap") {
        test("swaps elements at given indices") {
            val list = mutableListOf(1, 2, 3, 4, 5)
            
            list.swap(1, 3)
            
            list shouldBe listOf(1, 4, 3, 2, 5)
        }
    }
    
    context("removeFirstOrNull") {
        test("removes and returns first element") {
            val list = mutableListOf(1, 2, 3)
            
            val removed = list.removeFirstOrNull()
            
            removed shouldBe 1
            list shouldBe listOf(2, 3)
        }
        
        test("returns null for empty list") {
            val list = mutableListOf<Int>()
            
            val removed = list.removeFirstOrNull()
            
            removed.shouldBeNull()
        }
    }
    
    context("removeLastOrNull") {
        test("removes and returns last element") {
            val list = mutableListOf(1, 2, 3)
            
            val removed = list.removeLastOrNull()
            
            removed shouldBe 3
            list shouldBe listOf(1, 2)
        }
        
        test("returns null for empty list") {
            val list = mutableListOf<Int>()
            
            val removed = list.removeLastOrNull()
            
            removed.shouldBeNull()
        }
    }
})
