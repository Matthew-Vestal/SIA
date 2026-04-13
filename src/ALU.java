public class ALU {
    public Word16 instruction = new Word16();
    public Word32 op1 = new Word32();
    public Word32 op2 = new Word32();
    public Word32 result = new Word32();
    public Bit less = new Bit(false);
    public Bit equal = new Bit(false);

    public void doInstruction(){
        equal.assign(false);
        less.assign(false);
        int val = 1, opcode=0;
        Bit cur = new Bit(true);
        for (int i = 4; i >= 0; i--) {
            instruction.getBitN(i,cur);
            if (cur.getValue())
                opcode+=val;
            val *=2;
        }
        if (opcode == 1) {
            Adder.add(op1, op2, result);
        } else if (opcode == 2) {
            op1.and(op2, result);
        } else if (opcode == 3) {
            Multiplier.multiply(op1, op2, result);
        } else if (opcode == 4) {
            int amount = TestConverter.toInt(op2);
            Shifter.LeftShift(op1, amount, result);
        } else if(opcode == 5){
            Adder.subtract(op1, op2, result);
        } else if(opcode == 6){
            op1.or(op2, result);
        } else if(opcode == 7){
            int amount = TestConverter.toInt(op2);
            Shifter.RightShift(op1, amount, result);
        } else if(opcode == 11){
            if(op1.equals(op2)) {
                equal.assign(true);
                less.assign(false);
                return;
            }
            for(int i = 0; i < 32; i++) {
                Bit bit1 = new Bit(false);
                Bit bit2 = new Bit(false);
                op1.getBitN(i, bit1);
                op2.getBitN(i, bit2);
                if (bit1.getValue()) {
                    if (!bit2.getValue()) {
                        less.assign(false);
                        equal.assign(false);
                        break;
                    }
                } else if (bit2.getValue()) {
                    less.assign(true);
                    equal.assign(false);
                    break;
                }
            }
        }
    }
}
