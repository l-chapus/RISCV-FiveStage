package FiveStage

import chisel3._
import chisel3.core.Input
import chisel3.util._
import chisel3.experimental.MultiIOModule
import chisel3.experimental._


class CPU extends MultiIOModule {

  val testHarness = IO(
    new Bundle {
      val setupSignals = Input(new SetupSignals)
      val testReadouts = Output(new TestReadouts)
      val regUpdates   = Output(new RegisterUpdates)
      val memUpdates   = Output(new MemUpdates)
      val currentPC    = Output(UInt(32.W))
    }
  )

  /*
    You need to create the classes for these yourself
    */
  val IFBarrier  = Module(new IFBarrier).io
  val IDBarrier  = Module(new IDBarrier).io
  val EXBarrier  = Module(new EXBarrier).io
  val MEMBarrier = Module(new MEMBarrier).io

  val ID  = Module(new InstructionDecode)
  val IF  = Module(new InstructionFetch)
  val EX  = Module(new Execute)
  val MEM = Module(new MemoryFetch)
  // val WB  = Module(new Execute) (You may not need this one?)


  /**
    * Setup. You should not change this code
    */
  IF.testHarness.IMEMsetup     := testHarness.setupSignals.IMEMsignals
  ID.testHarness.registerSetup := testHarness.setupSignals.registerSignals
  MEM.testHarness.DMEMsetup    := testHarness.setupSignals.DMEMsignals

  testHarness.testReadouts.registerRead := ID.testHarness.registerPeek
  testHarness.testReadouts.DMEMread     := MEM.testHarness.DMEMpeek

  /**
    spying stuff
    */
  testHarness.regUpdates := ID.testHarness.testUpdates
  testHarness.memUpdates := MEM.testHarness.testUpdates
  testHarness.currentPC  := IF.testHarness.PC


  /**
    TODO: Your code here
    */
  // IFBARRIER
  IFBarrier.PC          := IF.io.PC
  IFBarrier.instruction := IF.io.instruction

  ID.io.PCIn            := IFBarrier.delayedPC
  ID.io.instructionIn   := IFBarrier.outputInstruction

  // IDBARRIER
  IDBarrier.readDataAIn       := ID.io.readData1
  IDBarrier.readDataBIn       := ID.io.readData2
  IDBarrier.PCIn              := ID.io.PCOut
  IDBarrier.controlIn         := 0.U
  IDBarrier.immediateIn       := ID.io.immediateOut
  IDBarrier.instructionIn     := ID.io.instructionOut
  IDBarrier.ALUOpIn           := ID.io.ALUOp
  IDBarrier.controlSignalsIn  := ID.io.controlSignals

  EX.io.readDataAIn       := IDBarrier.readDataAOut
  EX.io.readDataBIn       := IDBarrier.readDataBOut
  EX.io.PCIn              := IDBarrier.PCOut
  EX.io.controlIn         := IDBarrier.controlOut
  EX.io.immediateIn       := IDBarrier.immediateOut
  EX.io.ALUop             := IDBarrier.ALUOpOut
  EX.io.instructionIn     := IDBarrier.instructionOut
  EX.io.controlSignalsIn  := IDBarrier.controlSignalsOut

  // EXBARRIER
  EXBarrier.PCIn             := EX.io.PCOut
  EXBarrier.controlIn        := 0.U
  EXBarrier.ALUResultIn      := EX.io.ALUResult
  EXBarrier.readDataBIn      := IDBarrier.readDataBOut
  EXBarrier.instructionIn    := IDBarrier.instructionOut
  EXBarrier.controlSignalsIn := EX.io.controlSignalsOut


  // MEMBARRIER
  MEMBarrier.instructionIn  := EXBarrier.instructionOut
  MEMBarrier.dataIn         := EXBarrier.ALUResultOut
  

  // LOAD VALUE
  when(EXBarrier.controlSignalsOut.memRead){
    MEM.io.dataIn       := 0.U
    MEM.io.dataAddress  := MEMBarrier.dataOut + EXBarrier.instructionOut.immediateIType.asUInt
    MEM.io.writeEnable  := false.B
    EX.io.readDataBIn   := MEM.io.dataOut
  }.otherwise{
    MEM.io.dataIn       := 0.U
    MEM.io.dataAddress  := 0.U
    MEM.io.writeEnable  := 0.U
  }

  // STORE VALUE
  when(EXBarrier.controlSignalsOut.memWrite){
    MEM.io.dataIn       := MEMBarrier.dataOut
    MEM.io.dataAddress  := MEMBarrier.dataOut + EXBarrier.instructionOut.immediateSType.asUInt
    MEM.io.writeEnable  := EXBarrier.controlSignalsOut.memWrite
  }.otherwise{
    MEM.io.dataIn       := 0.U
    MEM.io.dataAddress  := 0.U
    MEM.io.writeEnable  := 0.U
  }
  
  when(EXBarrier.instructionOut.registerRd === 0.U) {
    ID.io.writeEnable     := false.B
  }.otherwise {
    ID.io.writeEnable     := EXBarrier.controlSignalsOut.regWrite
  }
  ID.io.writeData       := EXBarrier.ALUResultOut
  ID.io.writeAddress    := EXBarrier.instructionOut.registerRd
  ID.io.writeEnable     := EXBarrier.controlSignalsOut.regWrite


  // DEBUG
   val io = IO(new Bundle {
    val ALUResult         = Output(UInt(32.W))
    val instruRs1         = Output(UInt(5.W))
    val aluOp             = Output(UInt(4.W))
  })

  io.ALUResult := EX.io.ALUResult
  io.instruRs1 := EXBarrier.instructionOut.immediateIType.asUInt
  io.aluOp     := ID.io.ALUOp
}
