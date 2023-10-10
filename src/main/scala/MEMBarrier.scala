package FiveStage
import chisel3._
import chisel3.experimental.MultiIOModule


class MEMBarrier extends MultiIOModule {
  val io = IO(new Bundle {
    val instructionIn  = Input(new Instruction)
    val dataIn         = Input(UInt(32.W))

    val instructionOut  = Output(new Instruction)
    val dataOut         = Output(UInt(32.W))
  })

  // delay every value by one cycle
  val instructionRegister = Reg(new Instruction)
  instructionRegister := io.instructionIn
  io.instructionOut := instructionRegister

  val dataRegister = Reg(UInt(32.W))
  dataRegister := io.dataIn
  io.dataOut := dataRegister
}