package FiveStage
import chisel3._
import chisel3.util._
import chisel3.util.{ BitPat, MuxCase, MuxLookup }


class ALU extends Module {

  val io = IO(new Bundle {
      val op1            = Input(UInt(32.W))
      val op2            = Input(UInt(32.W))
      val aluOp          = Input(UInt(4.W))
      val immType        = Input(UInt(3.W))

      val Result         = Output(UInt(32.W))
   })
  
  
  
  import lookup._
  
  val op1convert    = io.op1.asSInt()
  val op2convert    = io.op2.asSInt()
  
  io.Result     := 0.U

  // For the ADD operation
  when(io.aluOp === 0.U && io.immType === 6.U) {
    io.Result := io.op1 + io.op2
  }
  // For the SUB operation
  when(io.aluOp === 1.U) {
    io.Result := io.op1 - io.op2
  }
  // For the AND operation
  when(io.aluOp === 2.U && io.immType === 6.U) {
    io.Result := io.op1 & io.op2
  }
  // For the OR operation
  when(io.aluOp === 3.U && io.immType === 6.U) {
    io.Result := io.op1 | io.op2
  }
  // For the XOR operation
  when(io.aluOp === 4.U && io.immType === 6.U) {
    io.Result := io.op1 ^ io.op2
  }
  // For the SLT operation
  when(io.aluOp === 5.U && io.immType === 6.U) {
    io.Result := op1convert < op2convert
  }
  // For the SLL operation
  when(io.aluOp === 6.U && io.immType === 6.U) {
    val shiftAmount = io.op2(4, 0) // Use only the bottom 5 bits of rs2
    io.Result := io.op1 << shiftAmount
  }
  // For the SLTU operation
  when(io.aluOp === 7.U && io.immType === 6.U) {
    io.Result := io.op1 < io.op2
  }
  // For the SRL operation
  when(io.aluOp === 8.U) {
    val shiftAmount = io.op2(4, 0) // Use only the bottom 5 bits of rs2
    io.Result := io.op1 >> shiftAmount
  }
  // For the SRA operation
  when(io.aluOp === 9.U) {
    val shiftAmount = io.op2(4, 0) // Use only the bottom 5 bits of rs2
    val resultConvert = Wire(SInt(32.W))
    resultConvert := op1convert >> shiftAmount
    io.Result := resultConvert.asUInt
  }

  // IMMEDIATE OPERATION

  val op2With12Bits   = io.op2.asTypeOf(UInt(12.W))
  val op2ConvertImm   = Cat(Fill(32 - 12, op2With12Bits(11)), op2With12Bits).asSInt()
  

  // For the ADDI operation
  when(io.aluOp === 0.U && io.immType === 0.U) {
    val resultADDI    = Wire(SInt(32.W))
    resultADDI := op1convert + op2ConvertImm
    io.Result := resultADDI.asUInt()
  }
  // For the ANDI operation
  when(io.aluOp === 2.U && io.immType === 0.U) {
    io.Result := io.op1 & op2ConvertImm.asUInt()
  }
  // For the ORI operation
  when(io.aluOp === 3.U && io.immType === 0.U) {
    io.Result := io.op1 | op2ConvertImm.asUInt()
  }
  // For the XORI operation
  when(io.aluOp === 4.U && io.immType === 0.U) {
    io.Result := io.op1 ^ op2ConvertImm.asUInt()
  }
  // For the SLTI operation
  when(io.aluOp === 5.U && io.immType === 0.U) {
    io.Result := op1convert < op2ConvertImm
  }
  // For the SLLI operation
  when(io.aluOp === 6.U && io.immType === 0.U) {
    io.Result := io.op1 << op2With12Bits
  }
  // For the SLTIU operation
  when(io.aluOp === 7.U && io.immType === 0.U) {
    io.Result := io.op1 < op2ConvertImm.asUInt()
  }


  // For the LUI operation
  when(io.aluOp === 6.U && io.immType === 3.U) {
    io.Result := io.op1
  }
}
