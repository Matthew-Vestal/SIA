import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;
import java.util.Optional;

public class CacheTest {
    private Memory m;

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
        Processor p = runProgram(program, Optional.empty());
        Assertions.assertEquals("r0:0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,0,0,1,1,1,0,1,1,1,0,1,0,", p.output.getFirst());
    }

    @Test
    public void sumArray() {
        m = new Memory();
        for(int i = 0; i < 100; i++) {
            Word32 value = new Word32();
            Word32 index = new Word32();
            TestConverter.fromInt(i+200, index);
            TestConverter.fromInt(1, value);
            m.address = index;
            m.value = value;
            m.write();
        }
        String[] program = {
                "copy 10 r2",
                "copy 0 r0",
                "multiply r2 r2",
                "multiply 2 r2",
                "copy 10 r3",
                "multiply r3 r3",
                "load r2 r1", //6
                "add r1 r0",
                "add 1 r4",
                "compare r4 r3",
                "blt -2",
                "syscall 0"
        };
        Processor p = runProgram(program, Optional.of(m));
        Assertions.assertEquals("r0:0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,1,0,0,1,0,0,", p.output.getFirst());
    }

    @Test
    public void sumLinkedList() {
        m = new Memory();
        for(int i = 300; i < 800; i += 5) {
            Word32 value = new Word32();
            Word32 index = new Word32();
            TestConverter.fromInt(i, index);
            TestConverter.fromInt(1, value);
            m.address = index;
            m.value = value;
            m.write();
            Word32 linkIndex =  new Word32();
            Word32 one = new  Word32();
            TestConverter.fromInt(1, one);
            Adder.add(index, one, linkIndex);
            Word32 linkAddress =  new  Word32();
            if(i == 795) {
                TestConverter.fromInt(0, linkAddress);

            } else {
                TestConverter.fromInt(i+5, linkAddress);
            }
            m.address = linkIndex;
            m.value = linkAddress;
            m.write();
        }
        String[] program = {
                "copy 10 r3",
                "copy 0 r0",
                "multiply r3 r3",
                "multiply 3 r3",
                "load r3 r2",//4
                "add r2 r0",
                "add 1 r3",
                "load r3 r3",
                "compare 0 r3",
                "bne -2",//9
                "syscall 0"
        };
        Processor p = runProgram(program, Optional.of(m));
        Assertions.assertEquals("r0:0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,1,0,0,1,0,0,", p.output.getFirst());
    }

    private static Processor runProgram(String[] program, Optional<Memory> m) {
        var assembled = Assembler.assemble(program);
        var merged = Assembler.finalOutput(assembled);
        Processor p;
        if(!m.isPresent()) {
            Memory mem = new Memory();
            mem.load(merged);
            p = new Processor(mem);
        } else {
            m.get().load(merged);
            p = new Processor(m.get());
        }
        p.run();
        return p;
    }
}
