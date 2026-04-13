public class Memory {
    public Word32 address= new Word32();
    public Word32 value = new Word32();

    private final Word32[] dram= new Word32[1000];

    public int addressAsInt() {
        int val = 1, retVal=0;
        Bit cur = new Bit(true);
        for (int i = 31; i >= 0; i--) {
            address.getBitN(i,cur);
            if (cur.getValue())
                retVal+=val;
            val *=2;
        }
        return retVal;
    }

    public Memory() {
        for(int i = 0; i < dram.length; i++) {
            dram[i] = new Word32();
        }
    }

    public void read() {
        int index = addressAsInt();
        Word32 word = dram[index];
        word.copy(value);
    }

    public void write() {
        int index = addressAsInt();
        value.copy(dram[index]);
    }

    public void load(String[] data) {
        for(int i = 0; i < data.length; i++) {
                        String wordString = data[i];
                        if(wordString.length() != 32) {
                            throw new IllegalArgumentException("Word length should be 32");
                        }
                        for(int j = 31; j >= 0; j--) {
                            if(wordString.charAt(j) == '0') {
                                Bit temp = new Bit(false);
                                value.setBitN(j, temp);
                            } else {
                                Bit temp = new Bit(true);
                    value.setBitN(j, temp);
                }
            }
            TestConverter.fromInt(i, address);
            write();
        }
    }
}
