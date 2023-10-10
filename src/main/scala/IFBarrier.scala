package FiveStage
import chisel3._
import chisel3.util.{ BitPat, MuxCase }
import chisel3.experimental.MultiIOModule


class IFBarrier extends MultiIOModule {
  val io = IO(new Bundle {
    val PC                = Input(UInt(32.W))
    val instruction       = Input(new Instruction)
    
    val delayedPC         = Output(UInt(32.W))
    val outputInstruction = Output(new Instruction)
  })

  // Register to delay the signal PC by one cycle
  val pcRegister = Reg(UInt(32.W))
  pcRegister := io.PC
  io.delayedPC := pcRegister

  // Connect the instruction without delay
  io.outputInstruction := io.instruction
}