public class Word16 {
    private Bit[] bits = new Bit[16];
    public Word16() {
        for (int i = 0; i < 16; i++) {
            bits[i] = new Bit(false);
        }
    }

    public Word16(Bit[] in) {
        for (int i = 0; i < 16; i++) {
            bits[i] = new Bit(in[i].getValue());
        }
    }

    public void copy(Word16 result) { // sets the values in "result" to be the same as the values in this instance; use "bit.assign"
        for(int i = 0; i < 16; i++) {
            if (bits[i].getValue()) {
                result.bits[i].assign(true);
            } else {
                result.bits[i].assign(false);
            }
        }
    }

    public void setBitN(int n, Bit source) { // sets the nth bit of this word to "source"
        if(source.getValue()) {
            bits[n].assign(true);
        } else {
            bits[n].assign(false);
        }
    }

    public void getBitN(int n, Bit result) { // sets result to be the same value as the nth bit of this word
        if(bits[n].getValue()) {
            result.assign(true);
        } else {
            result.assign(false);
        }
    }

    public boolean equals(Word16 other) { // is other equal to this
        for(int i = 0; i < 16; i++) {
            if(bits[i].getValue() != other.bits[i].getValue()) {
                return false;
            }
        }
        return true;
    }

    public static boolean equals(Word16 a, Word16 b) {
        for(int i = 0; i < 16; i++) {
            if(a.bits[i].getValue() != b.bits[i].getValue()) {
                return false;
            }
        }
        return true;
    }

    public void and(Word16 other, Word16 result) {
        for (int i = 0; i < 16; i++) {
            bits[i].and(other.bits[i], result.bits[i]);
        }
    }

    public static void and(Word16 a, Word16 b, Word16 result) {
        for (int i = 0; i < 16; i++) {
            a.bits[i].and(b.bits[i], result.bits[i]);
        }
    }

    public void or(Word16 other, Word16 result) {
        for(int i = 0; i < 16; i++) {
            bits[i].or(other.bits[i], result.bits[i]);
        }
    }

    public static void or(Word16 a, Word16 b, Word16 result) {
        for(int i = 0; i < 16; i++) {
            a.bits[i].or(b.bits[i], result.bits[i]);
        }
    }

    public void xor(Word16 other, Word16 result) {
        for(int i = 0; i < 16; i++) {
            bits[i].xor(other.bits[i], result.bits[i]);
        }
    }

    public static void xor(Word16 a, Word16 b, Word16 result) {
        for(int i = 0; i < 16; i++) {
            a.bits[i].xor(b.bits[i], result.bits[i]);
        }
    }

    public void not( Word16 result) {
        for(int i = 0; i < 16; i++) {
            bits[i].not(result.bits[i]);
        }
    }

    public static void not(Word16 a, Word16 result) {
        for(int i = 0; i < 16; i++) {
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