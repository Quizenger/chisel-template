import chisel3._
import chisel3.util._
import chisel3.stage.ChiselStage

// Demonstrates waiting for n 32-bit inputs from io.in
// before sending 1 output to io.out, that is the sum of the n inputs
class Foo(n: Int) extends Module {
  val io = IO(new Bundle {
    val in = Flipped(Decoupled(UInt(32.W)))
    val out = Decoupled(UInt(32.W))
  })
  // when count == n, we have accumulated enough, and should try to send to tout
  val count = RegInit(0.U((1 + log2Ceil(n)).W))
  val acc = RegInit(0.U(32.W))


  io.out.valid := count === n.U
  io.out.bits  := acc
  io.in.ready  := count =/= n.U

  // increment the counter when we see a valid input, and add the input to the accumulator
  when (io.in.fire()) {
    count := count + 1.U
    acc := acc + io.in.bits
  }
  
  // Firing to io.out this cycle, so reset count and accumulator
  when (io.out.fire()) {
    count := 0.U
    acc := 0.U
  }
}
