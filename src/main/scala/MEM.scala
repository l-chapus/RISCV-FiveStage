package FiveStage
import chisel3._
import chisel3.util._
import chisel3.experimental.MultiIOModule


class MemoryFetch() extends MultiIOModule {


  // Don't touch the test harness
  val testHarness = IO(
    new Bundle {
      val DMEMsetup      = Input(new DMEMsetupSignals)
      val DMEMpeek       = Output(UInt(32.W))

      val testUpdates    = Output(new MemUpdates)
    })

  val io = IO(
    new Bundle {
      val dataIn          = Input(UInt(32.W))
      //val dataAddress     = Input(UInt(7.W))
      //val writeEnable     = Input(Bool())
      val instruction     = Input(new Instruction)
      val controlSignals  = Input(new ControlSignals)

      val dataOut     = Output(UInt(32.W))
    })


  val DMEM = Module(new DMEM)


  /**
    * Setup. You should not change this code
    */
  DMEM.testHarness.setup  := testHarness.DMEMsetup
  testHarness.DMEMpeek    := DMEM.io.dataOut
  testHarness.testUpdates := DMEM.testHarness.testUpdates


  /**
    * Your code here.
    */
  
  DMEM.io.writeEnable  := false.B

  // LOAD VALUE
  when(io.controlSignals.memRead){
    DMEM.io.dataIn       := 0.U
    DMEM.io.dataAddress  := io.dataIn + io.instruction.immediateIType.asUInt 
    io.dataOut           := DMEM.io.dataOut
  }.otherwise{
    DMEM.io.dataIn       := 0.U
    DMEM.io.dataAddress  := 0.U
  }

  // STORE VALUE
  when(io.controlSignals.memWrite){
    DMEM.io.dataIn       := io.dataIn
    DMEM.io.dataAddress  := io.dataIn + io.instruction.immediateSType.asUInt
    DMEM.io.writeEnable  := io.controlSignals.memWrite
  }.otherwise{
    DMEM.io.dataIn       := 0.U
    DMEM.io.dataAddress  := 0.U
  }
  
  io.dataOut := DMEM.io.dataOut
}
