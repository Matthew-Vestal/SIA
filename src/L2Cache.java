public class L2Cache {
   private Word32[][] cache = new Word32[4][8];
    private Word32[] baseAddresses = new Word32[4];
    private boolean[] isEmpty = {true, true, true, true};
    private int replaceIndex = 0; //Next cache line to be replaced
    private Memory memory;

    public L2Cache (Memory memory) {
        this.memory = memory;
        for(int i = 0; i < 4; i++){
            baseAddresses[i] = new Word32();
            for(int j = 0; j < 8; j++){
                cache[i][j] = new Word32();
            }
        }
    }

    public boolean read( Word32[] l1Cache) {
        int address = toInt(memory.address);
        for(int i = 0; i < 4; i++){
            int base = toInt(baseAddresses[i]);
            if(!isEmpty[i] && (address >= base && address < base + 8)) {
                for(int j = 0; j < 8; j++){
                    cache[i][j].copy(l1Cache[j]);
                }
                return true;
            }
        }
        isEmpty[replaceIndex] = false;
        int alignedAddress = (address/8) * 8;
        TestConverter.fromInt(alignedAddress, baseAddresses[replaceIndex]);
        Word32 tempAddress = new Word32();
        memory.address.copy(tempAddress);
        Word32 one = new Word32();
        one.setBitN(31, new Bit(true));
        baseAddresses[replaceIndex].copy(memory.address);
        for(int i = 0; i < 8; i++) {
            memory.read();
            memory.value.copy(cache[replaceIndex][i]);
            cache[replaceIndex][i].copy(l1Cache[i]); //Filling the l1 cache
            Adder.add(memory.address,one,memory.address);
        }
        tempAddress.copy(memory.address);
        replaceIndex = (replaceIndex + 1) % 4; //Circular block filling (loops back to 0 when at 4)
        return false;
    }

    public int write() {
        int address = toInt(memory.address);
        boolean hit = false;
        for(int i = 0; i < 4; i++) {
            int base = toInt(baseAddresses[i]);
            if(!isEmpty[i] && (address >= base && address < base + 8)) {
                memory.value.copy(cache[i][address - base]);
                hit = true;
                break;
            }
        }
        memory.write();
        if(hit) {
            return 20;
        } else {
            return 360;
        }
    }

    public int readForLoad() {
        int address = toInt(memory.address);
        for(int i = 0; i < 4; i++) {
            int base = toInt(baseAddresses[i]);
            if(!isEmpty[i] && (address >= base && address < base + 8)) {
                cache[i][address - base].copy(memory.value);
                return 20;
            }
        }
        isEmpty[replaceIndex] = false;
        int alignedAddress = (address/8) * 8;
        TestConverter.fromInt(alignedAddress, baseAddresses[replaceIndex]);
        Word32 tempAddress = new Word32();
        memory.address.copy(tempAddress);
        Word32 one = new Word32();
        one.setBitN(31, new Bit(true));
        baseAddresses[replaceIndex].copy(memory.address);
        for(int i = 0; i < 8; i++) {
            memory.read();
            memory.value.copy(cache[replaceIndex][i]);
            Adder.add(memory.address,one,memory.address);
        }
        int offset = address - alignedAddress; //Makes it so the correct word is grabbed from cache
        cache[replaceIndex][offset].copy(memory.value);
        tempAddress.copy(memory.address);
        replaceIndex = (replaceIndex + 1) % 4;
        return 360;
    }

    public int toInt(Word32 word) {
        int val = 1, retVal=0;
        Bit cur = new Bit(true);
        for (int i = 31; i >= 0; i--) {
            word.getBitN(i,cur);
            if (cur.getValue())
                retVal+=val;
            val *=2;
        }
        return retVal;
    }
}
