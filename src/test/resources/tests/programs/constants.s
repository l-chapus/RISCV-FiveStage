main:
  li x0, 0x0
  nop
  lui x1, 0xABCDE
  addi x1, x1, 0xF0 
  nop
  li x1, 32
  li x1, 0x800
  li x1, 0x7FF
  nop
  nop
  done
#regset t0,10
#regset t1,23
#regset t2,43
#regset t3,-11
