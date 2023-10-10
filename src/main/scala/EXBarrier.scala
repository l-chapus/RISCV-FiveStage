package FiveStage
import chisel3._
import chisel3.experimental.MultiIOModule


class EXBarrier extends MultiIOModule {
  val io = IO(new Bundle {
    val PCIn           = Input(UInt(32.W))
    val controlIn      = Input(UInt(32.W))
    val ALUResultIn    = Input(UInt(32.W))
    val readDataBIn    = Input(UInt(32.W))
    val instructionIn  = Input(new Instruction)
    val controlSignalsIn = Input(new ControlSignals)

    val PCOut           = Output(UInt(32.W))
    val controlOut      = Output(UInt(32.W))
    val ALUResultOut    = Output(UInt(32.W))
    val readDataBOut    = Output(UInt(32.W))
    val instructionOut  = Output(new Instruction)
    val controlSignalsOut = Output(new ControlSignals)
  })

  // delay every value by one cycle
  val pcRegister = Reg(UInt(32.W))
  pcRegister := io.PCIn
  io.PCOut := pcRegister

  val controlRegister = Reg(UInt(32.W))
  controlRegister := io.controlIn
  io.controlOut := controlRegister

/*
  val ALUResultRegister = Reg(UInt(32.W))
  ALUResultRegister := io.ALUResultIn
  io.ALUResultOut := ALUResultRegister

  val readDataBRegister = Reg(UInt(32.W))
  readDataBRegister := io.readDataBIn
  io.readDataBOut := readDataBRegister

  val instructionRegister = Reg(new Instruction)
  instructionRegister := io.instructionIn
  io.instructionOut := instructionRegister

  val controlSignalsRegister = Reg(new ControlSignals)
  controlSignalsRegister := io.controlSignalsIn
  io.controlSignalsOut   := controlSignalsRegister
  */
  io.ALUResultOut := io.ALUResultIn
  io.readDataBOut := io.readDataBIn
  io.instructionOut := io.instructionIn
  io.controlSignalsOut := io.controlSignalsIn
}