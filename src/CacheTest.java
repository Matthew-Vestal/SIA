import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;

public class CacheTest {

    @Test
    public void sumNumbers() {
        String[] program = {
                "copy 1 r1",
                "copy 10 r2",
                "multiply r2 r2",
                "add 1 r2",
                "add r1 r0",//4
                "add 1 r1",
                "compare r1 r2",
                "blt -1", //7
                "syscall 0"
        };
        Processor p = runProgram(program);
        Assertions.assertEquals("r0:0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,0,0,1,1,1,0,1,1,1,0,1,0, (5050)", p.output.getFirst() + " (" + Integer.parseInt(p.output.getFirst().substring(3,p.output.getFirst().length()-1).replaceAll(",",""), 2)+ ")");
    }

    @Test
    public void sumArray() {
        String[] program = {
                "copy 10 r5",
                "multiply r5 r5",
                "multiply 3 r5",
                "copy 10 r6",
                "multiply r6 r6",
                "multiply 2 r6",
                "store 1 r6", //6
                "add 1 r6",
                "compare r6 r5",
                "blt -1", //9

                "copy 10 r2",
                "copy 0 r0",
                "multiply r2 r2",
                "multiply 2 r2",
                "copy 10 r3",
                "multiply r3 r3",
                "load r2 r1", //16
                "add r1 r0",
                "add 1 r4",
                "compare r4 r3",
                "blt -2", //20
                "syscall 0"
        };
        Processor p = runProgram(program);
        Assertions.assertEquals("r0:0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,1,0,0,1,0,0, (100)", p.output.getFirst() + " (" + Integer.parseInt(p.output.getFirst().substring(3,p.output.getFirst().length()-1).replaceAll(",",""), 2)+ ")");
    }

    @Test
    public void sumLinkedList() {
        String[] program = {
                "copy 10 r4",
                "multiply r4 r4",
                "multiply 3 r4",
                "copy 10 r5",
                "multiply r5 r5",
                "multiply 8 r5",
                "store 1 r4", //6
                "copy r4 r6",
                "add 1 r4",
                "add 5 r6",
                "store r6 r4",
                "add 4 r4",
                "compare r4 r5",
                "blt -3", //13
                "subtract 4 r4",
                "store 0 r4",

                "copy 10 r3",
                "copy 0 r0",
                "multiply r3 r3",
                "multiply 3 r3",
                "load r3 r2",//20
                "add r2 r0",
                "add 1 r3",
                "load r3 r3",
                "compare 0 r3",
                "bne -2",//25
                "syscall 0"
        };
        Processor p = runProgram(program);
        Assertions.assertEquals("r0:0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,1,0,0,1,0,0, (100)", p.output.getFirst() + " (" + Integer.parseInt(p.output.getFirst().substring(3,p.output.getFirst().length()-1).replaceAll(",",""), 2)+ ")");
    }

    private static Processor runProgram(String[] program) {
        var assembled = Assembler.assemble(program);
        var merged = Assembler.finalOutput(assembled);
        Processor p;
        Memory mem = new Memory();
        mem.load(merged);
        p = new Processor(mem);
        p.run();
        return p;
    }
}
