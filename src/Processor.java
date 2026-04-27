import java.util.LinkedList;
import java.util.List;
import java.util.Stack;

public class Processor {
    private Memory mem;
    public List<String> output = new LinkedList<>();
    private Word32[] registers = new Word32[32];
    private Stack<Integer> callStack = new Stack<>();
    private int pc = 0;
    private boolean halt = false;
    private ALU alu = new ALU();
    private Word32 currentWord = new Word32();
    private Word16 instructionOne = new Word16();
    private Word16 instructionTwo = new Word16();
    private int opcode;
    private Word32 op1 = new Word32();
    private Word32 op2 = new Word32();
    Bit type = new Bit(false);
    private Word32 immediate = new Word32();
    private int destinationIndex;
    private int currentClockCycle = 0;
    private InstructionCache cache;
    private L2Cache l2Cache;

    public Processor(Memory m) {
        mem = m;
        l2Cache = new L2Cache(m);
        cache = new InstructionCache(m, l2Cache);
        for(int i = 0; i < 32; i++) {
            registers[i] = new Word32();
        }
    }

    public void run() {
        do {
            fetch();
            decode();
            execute();
            store();
        } while(!halt);
        System.out.println(currentClockCycle);
    }

    private void fetch() {
        if(pc % 2 == 0) {
            int wordAddress = pc / 2;
            TestConverter.fromInt(wordAddress, mem.address);
            currentClockCycle += cache.read();
            mem.value.copy(currentWord);
            currentWord.getTopHalf(instructionOne);
            currentWord.getBottomHalf(instructionTwo);
        }
    }

    private void decode() {
        Word16 current;
        if(pc % 2 == 0) {
            current = instructionOne;
        } else {
            current = instructionTwo;
        }
        this.opcode = toInt(4,0, current, null);
        current.getBitN(5, this.type);
        switch(opcode) {
            case 1: case 2: case 3: case 4: case 5: case 6: case 7: case 11: case 19: case 20: {
                if (type.getValue()) {
                    Word16 temp = copyToWord(10, 6, 5, current);
                    immediate = signExtend(5, temp);
                } else {
                    registers[toInt(10, 6, current, null)].copy(op1);
                }
                int r2Index = toInt(15, 11, current, null);
                registers[r2Index].copy(op2);
                destinationIndex = r2Index;
                break;
            }
            case 8:
                for (int i = 31; i >= 0; i--) {
                    Bit b = new Bit(false);
                    if(i >= 21) {
                        current.getBitN(i-16, b);
                        immediate.setBitN(i, b);
                    } else {
                        immediate.setBitN(i, b);
                    }
                }
                break;
            case 9: case 12: case 13: case 14: case 15: case 16: case 17: {
                Word16 temp = copyToWord(15, 5, 0, current);
                immediate = signExtend(11, temp);
                break;
            }
            case 18: {
                int r2Index = toInt(15, 11, current, null);
                destinationIndex = r2Index;
                if (type.getValue()) {
                    Word16 temp = copyToWord(10, 6, 5, current);
                    immediate = signExtend(5, temp);
                    registers[destinationIndex].copy(op1);
                } else {
                    registers[toInt(10, 6, current, null)].copy(op1);
                }
                break;
            }
        }
    }

    private void execute() {
        Word16 current;
        if(pc % 2 == 0) {
            current = instructionOne;
        } else {
            current = instructionTwo;
        }
        switch (opcode) {
            case 0:
                halt = true;
                break;
            case 3:
                currentClockCycle += 8;
            case 1: case 2: case 6: case 7:  case 11:
                if(type.getValue()) {
                    immediate.copy(alu.op1);
                } else {
                    op1.copy(alu.op1);
                }
                current.copy(alu.instruction);
                op2.copy(alu.op2);
                alu.doInstruction();
                currentClockCycle += 2;
                break;
            case 4: case 5:
                if(type.getValue()) {
                    immediate.copy(alu.op2);
                } else {
                    op1.copy(alu.op2);
                }
                current.copy(alu.instruction);
                op2.copy(alu.op1);
                alu.doInstruction();
                currentClockCycle += 2;
                break;
            case 8:
                int syscall = toInt(31,21, null, immediate);
                switch(syscall) {
                    case 0:
                        printReg();
                        break;
                    case 1:
                        printMem();
                        break;
                }
                break;
            case 9:
                callStack.push(pc+1);
                break;
            case 18:
                if(type.getValue()) {
                    Word32 address = new Word32();
                    Adder.add(op1, immediate, address);
                    address.copy(mem.address);
                } else {
                    op1.copy(mem.address);
                }
                currentClockCycle += l2Cache.readForLoad();
                mem.value.copy(alu.result);
                break;
            case 19:
                op2.copy(mem.address);
                if(type.getValue()) {
                    immediate.copy(mem.value);
                } else {
                    op1.copy(mem.value);
                }
                currentClockCycle += l2Cache.write();
                break;
            case 20:
                if(type.getValue()) {
                    immediate.copy(alu.result);
                } else {
                    op1.copy(alu.result);
                }
                break;
        }

    }

    private void printReg() {
        for (int i = 0; i < 32; i++) {
            var line = "r"+ i + ":" + "" + registers[i].toString(); // TODO: add the register value here...
            output.add(line);
            System.out.println(line);
        }
    }

    private void printMem() {
        for (int i = 0; i < 1000; i++) {
            Word32 addr = new Word32();
            Word32 value = new Word32();
            // Convert i to Word32 here...
            TestConverter.fromInt(i, addr);
            addr.copy(mem.address);
            mem.read();
            mem.value.copy(value);
            var line = i + ":" + value + "(" + TestConverter.toInt(value) + ")";
            output.add(line);
            System.out.println(line);
        }
    }

    private void store() {
        switch(opcode) {
            case 1: case 2: case 3: case 4: case 5: case 6: case 7: case 18: case 20:
                alu.result.copy(registers[destinationIndex]);
                break;
            case 9:
                pc = calculateBranch(immediate);
                return;
            case 10:
                pc = callStack.pop();
                return;
            case 12:
                if(alu.less.getValue() || alu.equal.getValue()) {
                    pc = calculateBranch(immediate);
                    return;
                }
                break;
            case 13:
                if(alu.less.getValue()) {
                    pc = calculateBranch(immediate);
                    return;
                }
                break;
            case 14:
                if(!(alu.less.getValue()) || alu.equal.getValue()) {
                    pc = calculateBranch(immediate);
                    return;
                }
                break;
            case 15:
                if(!(alu.less.getValue()) && !(alu.equal.getValue())) {
                    pc = calculateBranch(immediate);
                    return;
                }
                 break;
            case 16:
                if(alu.equal.getValue()) {
                    pc = calculateBranch(immediate);
                    return;
                }
                break;
            case 17:
                if(!alu.equal.getValue()) {
                    pc = calculateBranch(immediate);
                    return;
                }
                break;
        }
        pc++;
    }

    private int toInt(int startRange, int endRange, Word16 instruction16, Word32 instruction32) {
        int val = 1, retVal=0;
        Bit cur = new Bit(true);
        for (int i = startRange; i >= endRange; i--) {
            if(instruction16 != null) {
                instruction16.getBitN(i, cur);
            } else {
                instruction32.getBitN(i, cur);
            }
            if (cur.getValue())
                retVal+=val;
            val *=2;
        }
        return retVal;
    }

    private int calculateBranch(Word32 immediate) {
        Word32 pcWord = new Word32();
        Word32 result = new Word32();
        TestConverter.fromInt(this.pc/2, pcWord);
        Adder.add(pcWord,immediate, result);
        return TestConverter.toInt(result) * 2;
    }

    private Word32 signExtend(int originalBits, Word16 value) {
        Word32 result = new Word32();
        for (int i = 0; i < originalBits; i++) {
            Bit b = new Bit(false);
            value.getBitN(15 - i, b);
            result.setBitN(31 - i, b);
        }
        Bit signBit = new Bit(false);
        value.getBitN(15 - (originalBits - 1), signBit);
        for (int i = 0; i < 32 - originalBits; i++) {
            result.setBitN(i, signBit);
        }
        return result;
    }

    private Word16 copyToWord(int startRange, int endRange, int positionOffset, Word16 instruction) {
        Word16 result = new Word16();
        for(int i = startRange; i >= endRange; i--) {
            Bit bit = new Bit(false);
            instruction.getBitN(i, bit);
            result.setBitN(i+positionOffset, bit);
        }
        return result;
    }
}