package FiveStage
import chisel3._
import chisel3.experimental.MultiIOModule


class IDBarrier extends MultiIOModule {
  val io = IO(new Bundle {
    val readDataAIn    = Input(UInt(32.W))
    val readDataBIn    = Input(UInt(32.W))
    val PCIn           = Input(UInt(32.W))
    val controlIn      = Input(UInt(32.W))
    val immediateIn    = Input(UInt(3.W))
    val instructionIn  = Input(new Instruction)
    val ALUOpIn        = Input(UInt(4.W))
    val controlSignalsIn = Input(new ControlSignals)

    val readDataAOut    = Output(UInt(32.W))
    val readDataBOut    = Output(UInt(32.W))
    val PCOut           = Output(UInt(32.W))
    val controlOut      = Output(UInt(32.W))
    val immediateOut    = Output(UInt(3.W))
    val instructionOut  = Output(new Instruction)
    val ALUOpOut        = Output(UInt(4.W))
    val controlSignalsOut = Output(new ControlSignals)
  })
  /*
  // delay every value by one cycle
  val dataARegister = Reg(UInt(32.W))
  dataARegister   := io.readDataAIn
  io.readDataAOut := dataARegister

  val dataBRegister = Reg(UInt(32.W))
  dataBRegister   := io.readDataBIn
  io.readDataBOut := dataBRegister

  val pcRegister = Reg(UInt(32.W))
  pcRegister := io.PCIn
  io.PCOut   := pcRegister

  val controlRegister = Reg(UInt(32.W))
  controlRegister := io.controlIn
  io.controlOut   := controlRegister

  val immediateRegister = Reg(UInt(32.W))
  immediateRegister := io.immediateIn
  io.immediateOut   := immediateRegister

  val instructionRegister = Reg(new Instruction)
  instructionRegister := io.instructionIn
  io.instructionOut   := instructionRegister

  val ALURegister = Reg(UInt(4.W))
  ALURegister := io.ALUOpIn
  io.ALUOpOut := ALURegister

  val controlSignalsRegister = Reg(new ControlSignals)
  controlSignalsRegister := io.controlSignalsIn
  io.controlSignalsOut   := controlSignalsRegister
  */
  
  io.readDataAOut        := io.readDataAIn
  io.readDataBOut        := io.readDataBIn
  io.PCOut               := io.PCIn
  io.controlOut          := io.controlIn
  io.immediateOut        := io.immediateIn
  io.instructionOut      := io.instructionIn 
  io.ALUOpOut            := io.ALUOpIn
  io.controlSignalsOut   := io.controlSignalsIn
  
}