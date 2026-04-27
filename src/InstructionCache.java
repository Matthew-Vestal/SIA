public class InstructionCache {

    private Word32[] cache = new Word32[8];
    private Word32 baseAddress = new Word32();
    private boolean isEmpty = true;
    private Memory memory;
    private L2Cache l2Cache;

    public InstructionCache(Memory memory, L2Cache l2Cache) {
        this.memory = memory;
        this.l2Cache = l2Cache;
        for(int i = 0; i < 8; i++){
            cache[i] = new Word32();
        }
    }

    public int read() {
        int clockCyclesToAdd = 0;
        int base = toInt(baseAddress);
        int addr = toInt(memory.address);
        if(!isEmpty && (addr >= base && addr < base + 8)) {
            //Cache hit
            clockCyclesToAdd += 10;
            cache[addr-base].copy(memory.value);
            return clockCyclesToAdd;
        } else {
            //Cache miss
            boolean l2Hit = l2Cache.read(cache);
            isEmpty = false;
            int alignedAddress = (addr/8) * 8; //Align the blocks so L1 can stay in sync with L2
            TestConverter.fromInt(alignedAddress, baseAddress);
            cache[addr-alignedAddress].copy(memory.value);
            if(l2Hit) {
                clockCyclesToAdd += 30;
            } else {
                clockCyclesToAdd += 370;
            }
            return clockCyclesToAdd;
        }
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
