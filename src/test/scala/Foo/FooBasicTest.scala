import chisel3._
import chiseltest._
import org.scalatest.flatspec.AnyFlatSpec

class BasicTest extends AnyFlatSpec with ChiselScalatestTester {
  behavior of "Foo"

  it should "not fire multiple times" in {
    test(new Foo(1)).withAnnotations(Seq(WriteVcdAnnotation)) { c =>
        c.io.in.valid.poke(true.B)
        println("Init ready value :" + c.io.in.ready.peek().litValue)
        c.io.in.bits.poke(5.U)
        c.clock.step()
        c.io.out.bits.expect(5.U)
        // c.io.in.ready.expect(true.B)
        println("Cycle 1 value :" + c.io.in.ready.peek().litValue)
        c.clock.step()
        // c.io.in.ready.expect(false.B)
        println("Cycle 2 value :" + c.io.in.ready.peek().litValue)
        c.clock.step()
        println("Cycle 3 value :" + c.io.in.ready.peek().litValue)

}
  
}
}