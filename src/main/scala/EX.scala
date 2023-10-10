package FiveStage
import chisel3._
import chisel3.util.{ BitPat, MuxCase }
import chisel3.experimental.MultiIOModule


class Execute extends MultiIOModule {
  val io = IO(new Bundle {
    val readDataAIn      = Input(UInt(32.W))
    val readDataBIn      = Input(UInt(32.W))
    val PCIn             = Input(UInt(32.W))
    val controlIn        = Input(UInt(32.W))
    val immediateIn      = Input(UInt(3.W))
    val ALUop            = Input(UInt(4.W))
    val instructionIn    = Input(new Instruction)
    val controlSignalsIn = Input(new ControlSignals)

    val ALUResult         = Output(UInt(32.W))
    val controlSignalsOut = Output(new ControlSignals)
    val PCOut             = Output(UInt(32.W))
  })

 
  val alu = Module(new ALU).io
  alu.op1     := io.readDataAIn
  alu.op2     := io.readDataBIn
  alu.aluOp   := io.ALUop
  alu.immType := io.immediateIn

  io.ALUResult := alu.Result

  io.controlSignalsOut := io.controlSignalsIn
  io.PCOut := io.PCIn
}
