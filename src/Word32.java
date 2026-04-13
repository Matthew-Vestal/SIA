public class Word32 {
    private Bit[] bits = new Bit[32];
    public Word32() {
        for (int i = 0; i < 32; i++) {
            bits[i] = new Bit(false);
        }
    }

    public Word32(Bit[] in) {
        for(int i = 0; i < 32; i++) {
            bits[i] = new Bit(in[i].getValue());
        }
    }

    public void getTopHalf(Word16 result) { // sets result = bits 0-15 of this word. use bit.assign
        for(int i = 0; i < 16; i++) {
            Bit temp = new Bit(false);
            if(bits[i].getValue()) {
                temp.assign(true);
            } else {
                temp.assign(false);
            }
            result.setBitN(i, temp);
        }
    }

    public void getBottomHalf(Word16 result) { // sets result = bits 16-31 of this word. use bit.assign
        int counter = 16;
        for(int i = 0; i < 16; i++) {
            Bit temp = new Bit(false);
            if(bits[counter+i].getValue()) {
                temp.assign(true);
            } else {
                temp.assign(false);
            }
            result.setBitN(i, temp);
        }
    }

    public void copy(Word32 result) { // sets result's bit to be the same as this. use bit.assign
        for(int i = 0; i < 32; i++) {
            if (bits[i].getValue()) {
                result.bits[i].assign(true);
            } else {
                result.bits[i].assign(false);
            }
        }
    }

    public boolean equals(Word32 other) {
        for(int i = 0; i < 32; i++) {
            if(bits[i].getValue() != other.bits[i].getValue()) {
                return false;
            }
        }
        return true;
    }

    public static boolean equals(Word32 a, Word32 b) {
        for(int i = 0; i < 32; i++) {
            if(a.bits[i].getValue() != b.bits[i].getValue()) {
                return false;
            }
        }
        return true;
    }

    public void getBitN(int n, Bit result) {
        // use bit.assign
        if(bits[n].getValue()) {
            result.assign(true);
        } else {
            result.assign(false);
        }
    }

    public void setBitN(int n, Bit source) { //  use bit.assign
        if(source.getValue()) {
            bits[n].assign(true);
        } else {
            bits[n].assign(false);
        }
    }

    public void and(Word32 other, Word32 result) {
        for (int i = 0; i < 32; i++) {
            bits[i].and(other.bits[i], result.bits[i]);
        }
    }

    public static void and(Word32 a, Word32 b, Word32 result) {
        for (int i = 0; i < 32; i++) {
            a.bits[i].and(b.bits[i], result.bits[i]);
        }
    }

    public void or(Word32 other, Word32 result) {
        for(int i = 0; i < 32; i++) {
            bits[i].or(other.bits[i], result.bits[i]);
        }
    }

    public static void or(Word32 a, Word32 b, Word32 result) {
        for(int i = 0; i < 32; i++) {
            a.bits[i].or(b.bits[i], result.bits[i]);
        }
    }

    public void xor(Word32 other, Word32 result) {
        for(int i = 0; i < 32; i++) {
            bits[i].xor(other.bits[i], result.bits[i]);
        }
    }

    public static void xor(Word32 a, Word32 b, Word32 result) {
        for(int i = 0; i < 32; i++) {
            a.bits[i].xor(b.bits[i], result.bits[i]);
        }
    }

    public void not( Word32 result) {
        for(int i = 0; i < 32; i++) {
            bits[i].not(result.bits[i]);
        }
    }

    public static void not(Word32 a, Word32 result) {
        for(int i = 0; i < 32; i++) {
            a.bits[i].not(result.bits[i]);
        }
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (Bit bit : bits) {
            sb.append(bit.toString());
            sb.append(",");
        }
        return sb.toString();
    }
}
