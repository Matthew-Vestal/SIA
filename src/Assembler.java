import java.util.HashMap;

public class Assembler {
    public static String[] assemble(String[] input) {
        HashMap<String, String> opcodes = new HashMap<>();
        opcodes.put("halt", "00000");
        opcodes.put("add", "00001");
        opcodes.put("and", "00010");
        opcodes.put("multiply", "00011");
        opcodes.put("leftshift", "00100");
        opcodes.put("subtract", "00101");
        opcodes.put("or", "00110");
        opcodes.put("rightshift", "00111");
        opcodes.put("syscall", "01000");
        opcodes.put("call", "01001");
        opcodes.put("return", "01010");
        opcodes.put("compare", "01011");
        opcodes.put("ble", "01100");
        opcodes.put("blt", "01101");
        opcodes.put("bge", "01110");
        opcodes.put("bgt", "01111");
        opcodes.put("beq", "10000");
        opcodes.put("bne", "10001");
        opcodes.put("load", "10010");
        opcodes.put("store", "10011");
        opcodes.put("copy", "10100");
         HashMap<String, String> registers = new HashMap<>();
        for(int i = 0; i < 32; i++){
            registers.put("r" + i, String.format("%5s", Integer.toBinaryString(i)).replace(' ', '0'));
        }
        String[] output = new String[input.length];
        for (int i = 0; i < input.length; i++) {
            String[] split = input[i].split(" ");
            String binary = "";
            if(opcodes.containsKey(split[0])){
                if(split.length < 2) {
                    binary += opcodes.get(split[0]);
                    binary += "00000000000";
                }
                else if(split.length < 3){
                    binary += opcodes.get(split[0]);
                    int value = Integer.parseInt(split[1]);
                    int mask = (1 << 11) - 1;
                    int masked = value & mask;
                    String bits = String.format("%11s", Integer.toBinaryString(masked)).replace(' ', '0');
                    binary += bits;
                } else {
                    binary += opcodes.get(split[0]);
                    if(registers.containsKey(split[1])){
                        binary += "0";
                        binary += registers.get(split[1]);
                    } else {
                        binary += "1";
                        int value = Integer.parseInt(split[1]);
                        int mask = (1 << 5) - 1;
                        int masked = value & mask;
                        String bits = String.format("%5s", Integer.toBinaryString(masked)).replace(' ', '0');
                        binary += bits;
                    }
                    if(registers.containsKey(split[2])){
                        binary += registers.get(split[2]);
                    } else {
                        int value =  Integer.parseInt(split[2]);
                        int mask = (1 << 5) - 1;
                        int masked = value & mask;
                        String bits = String.format("%5s", Integer.toBinaryString(masked)).replace(' ', '0');
                        binary += bits;
                    }
                }
            }
            output[i] = binary;
        }
        return output;
    }

    public static String[] finalOutput(String[] input) {
        String[] output = new String[input.length % 2 == 0 ? input.length/2 : input.length/2 + 1];
        int j = 0;
        for(int i = 0; i < input.length; i+=2){
            if(j < input.length && i < input.length-1) {
                output[j] = input[i] + input[i+1];
                j++;
            }
        }
        if(input.length % 2 != 0){
            output[output.length-1] = input[input.length-1] + "0000000000000000";
        }
        return output;
    }
}
