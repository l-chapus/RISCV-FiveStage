package FiveStage

import chisel3._
import chisel3.util._
import chisel3.core.Input
import chisel3.iotesters.PeekPokeTester


/**
  * The top level module. You do not have to change anything here, 
  * however you are free to route out signals as you see fit for debugging.
  */
class Tile() extends Module{

  val io = IO(
    new Bundle {
      val DMEMWriteData          = Input(UInt(32.W))
      val DMEMAddress            = Input(UInt(32.W))
      val DMEMWriteEnable        = Input(Bool())
      val DMEMReadData           = Output(UInt(32.W))

      val regsWriteData          = Input(UInt(32.W))
      val regsAddress            = Input(UInt(5.W))
      val regsWriteEnable        = Input(Bool())
      val regsReadData           = Output(UInt(32.W))

      val regsDeviceWriteEnable  = Output(Bool())
      val regsDeviceWriteData    = Output(UInt(32.W))
      val regsDeviceWriteAddress = Output(UInt(5.W))

      val memDeviceWriteEnable   = Output(Bool())
      val memDeviceWriteData     = Output(UInt(32.W))
      val memDeviceWriteAddress  = Output(UInt(32.W))

      val IMEMWriteData          = Input(UInt(32.W))
      val IMEMAddress            = Input(UInt(32.W))

      val setup                  = Input(Bool())

      val currentPC              = Output(UInt())
   
      val ALUOut                 = Output(UInt(32.W))
      val instruRs1              = Output(UInt(5.W))
      val aluOP                  = Output(UInt(4.W))
    })

  val CPU = Module(new CPU)

  CPU.testHarness.setupSignals.IMEMsignals.address     := io.IMEMAddress
  CPU.testHarness.setupSignals.IMEMsignals.instruction := io.IMEMWriteData
  CPU.testHarness.setupSignals.IMEMsignals.setup       := io.setup

  CPU.testHarness.setupSignals.DMEMsignals.writeEnable := io.DMEMWriteEnable
  CPU.testHarness.setupSignals.DMEMsignals.dataAddress := io.DMEMAddress
  CPU.testHarness.setupSignals.DMEMsignals.dataIn      := io.DMEMWriteData
  CPU.testHarness.setupSignals.DMEMsignals.setup       := io.setup

  CPU.testHarness.setupSignals.registerSignals.readAddress  := io.regsAddress
  CPU.testHarness.setupSignals.registerSignals.writeEnable  := io.regsWriteEnable
  CPU.testHarness.setupSignals.registerSignals.writeAddress := io.regsAddress
  CPU.testHarness.setupSignals.registerSignals.writeData    := io.regsWriteData
  CPU.testHarness.setupSignals.registerSignals.setup        := io.setup

  io.DMEMReadData := CPU.testHarness.testReadouts.DMEMread
  io.regsReadData := CPU.testHarness.testReadouts.registerRead

  io.regsDeviceWriteAddress := CPU.testHarness.regUpdates.writeAddress
  io.regsDeviceWriteEnable  := CPU.testHarness.regUpdates.writeEnable
  io.regsDeviceWriteData    := CPU.testHarness.regUpdates.writeData

  io.memDeviceWriteAddress  := CPU.testHarness.memUpdates.writeAddress
  io.memDeviceWriteEnable   := CPU.testHarness.memUpdates.writeEnable
  io.memDeviceWriteData     := CPU.testHarness.memUpdates.writeData

  io.currentPC := CPU.testHarness.currentPC

  io.ALUOut := CPU.io.ALUResult
  io.instruRs1 := CPU.io.instruRs1
  io.aluOP := CPU.io.aluOp
}



