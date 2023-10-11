package FiveStage
import chisel3._
import chisel3.util.{ BitPat, MuxCase }
import chisel3.experimental.MultiIOModule


class InstructionDecode extends MultiIOModule {

  // Don't touch the test harness
  val testHarness = IO(
    new Bundle {
      val registerSetup = Input(new RegisterSetupSignals)
      val registerPeek  = Output(UInt(32.W))

      val testUpdates   = Output(new RegisterUpdates)
    })


  val io = IO(
    new Bundle {
      /**
        * TODO: Your code here.
        */
      val instructionIn  = Input(new Instruction)
      val PCIn           = Input(UInt(32.W))
      val writeEnable    = Input(Bool())
      val writeAddress   = Input(UInt(5.W))
      val writeData      = Input(UInt(32.W))

      val PCOut          = Output(UInt(32.W))
      val instructionOut = Output(new Instruction)
      val readData1      = Output(UInt(32.W))
      val readData2      = Output(UInt(32.W))
      val ALUOp          = Output(UInt(4.W))
      val controlSignals = Output(new ControlSignals)
      val immediateOut   = Output(UInt(3.W))
    }
  )


  val registers = Module(new Registers)
  val decoder   = Module(new Decoder).io


  /**
    * Setup. You should not change this code
    */
  registers.testHarness.setup := testHarness.registerSetup
  testHarness.registerPeek    := registers.io.readData1
  testHarness.testUpdates     := registers.testHarness.testUpdates


  /**
    * TODO: Your code here.
    */
  registers.io.readAddress1 := io.instructionIn.registerRs1
  registers.io.readAddress2 := io.instructionIn.registerRs2
  registers.io.writeEnable  := io.writeEnable
  registers.io.writeAddress := io.writeAddress
  registers.io.writeData    := io.writeData

  decoder.instruction := io.instructionIn

  io.PCOut          := io.PCIn
  io.instructionOut := io.instructionIn


  io.ALUOp          := decoder.ALUop
  io.controlSignals := decoder.controlSignals

  io.immediateOut   := decoder.immType


  val data1 = Wire(UInt(32.W))
  val data2 = Wire(UInt(32.W))

  data1 := registers.io.readData1
  data2 := registers.io.readData2

  // For the ITYPE
  when(decoder.immType === 0.U && decoder.controlSignals.memRead === 0.U){
    data1     := registers.io.readData1
    data2     := io.instructionIn.immediateIType.asUInt
  }

  // For the STYPE
  when(decoder.immType === 1.U){
    data1     := registers.io.readData1
    data2     := 0.U
  }
  // For LW
  when(decoder.immType === 0.U && decoder.controlSignals.memRead === 1.U){
    registers.io.readAddress1 := io.instructionIn.registerRs1 + io.instructionIn.immediateIType.asUInt
    data1     := registers.io.readData1
    data2     := io.instructionIn.immediateIType.asUInt
  }
  io.readData1 := data1
  io.readData2 := data2
}
