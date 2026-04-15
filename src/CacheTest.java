import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;

public class CacheTest {

    @Test
    public void sumNumbers() {
        String[] program = {
                "copy 0 r0",
                "copy 1 r1",
                "copy 12 r2",
                "add 13 r2",
                "leftshift 2 r2",
                "add 1 r2", //r2 = 101 (upper bound)

                "add r1 r0",
                "add 1 r1",
                "compare r1 r2",
                "blt -1",
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
                "multiply 3 r5", //r5 = 300 (last address for array)

                "copy 10 r6",
                "multiply r6 r6",
                "multiply 2 r6", //r6 = 200 (first address for array)
                "copy r6 r2",//r2 = 200 (pointer for later)

                "store 1 r6",
                "add 1 r6",
                "compare r6 r5",
                "blt -1",

                "copy 10 r3",
                "multiply r3 r3", //r3 = 100 (upper bound)
                "load r2 r1",
                "add 1 r2",
                "add r1 r0",
                "add 1 r4",
                "compare r4 r3",
                "blt -2",
                "syscall 0"
        };
        Processor p = runProgram(program);
        Assertions.assertEquals("r0:0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,1,0,0,1,0,0, (100)", p.output.getFirst() + " (" + Integer.parseInt(p.output.getFirst().substring(3,p.output.getFirst().length()-1).replaceAll(",",""), 2)+ ")");
    }

    @Test
    public void sumLinkedList() {
        String[] program = {
                //Linked list is structured such that addr = value, addr+1 = next pointer
                "copy 10 r4",
                "multiply r4 r4",
                "multiply 3 r4", //r4 = 300 (start of the linked list)

                "copy 10 r5",
                "multiply r5 r5",
                "multiply 8 r5", //r5 = 800 (end of linked list)

                "store 1 r4",
                "copy r4 r6",
                "add 1 r4",
                "add 5 r6",
                "store r6 r4",
                "add 4 r4",
                "compare r4 r5",
                "blt -3", //13
                "subtract 4 r4",
                "store 0 r4",//Sets last node  to 0 (end of list)

                "copy 10 r3",
                "copy 0 r0",
                "multiply r3 r3",
                "multiply 3 r3",//r3 = 300 (pointer to first entry)

                "load r3 r2",
                "add r2 r0",
                "add 1 r3",
                "load r3 r3",
                "compare 0 r3",
                "bne -2",
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
