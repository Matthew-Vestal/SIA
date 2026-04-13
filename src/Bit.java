public class Bit {
    private boolean bit;

    public Bit(boolean value) {
        bit = value;
    }

    public boolean getValue() {
        return bit;
    }

    public void assign(boolean value) {
        bit = value;
    }

    public void and(Bit b2, Bit result) {
        and(this, b2, result);
    }

    public static void and(Bit b1, Bit b2, Bit result) {
        if (b1.bit) {
            if(b2.bit) {
                result.bit = true;
                return;
            }
        }
        result.bit = false;
    }

    public void or(Bit b2, Bit result) {
        or(this, b2, result);
    }

    public static void or(Bit b1, Bit b2, Bit result) {
        if(b1.bit) {
            result.bit = true;
            return;
        }
        else if(b2.bit) {
            result.bit = true;
            return;
        }
        result.bit = false;
    }

    public void xor(Bit b2, Bit result) {
        xor(this, b2, result);
    }

    public static void xor(Bit b1, Bit b2, Bit result) {
        if(b1.bit) {
            if(b2.bit == false) {
                result.bit = true;
                return;
            }
            result.bit = false;
            return;
        } else if(b2.bit) {
            result.bit = true;
            return;
        }
        result.bit = false;
    }

    public static void not(Bit b2, Bit result) {
        if(b2.bit) {
            result.bit = false;
            return;
        }
        result.bit = true;
    }

    public void not(Bit result) {
        not(this, result);
    }

    public String toString() {
        return bit ? "1" : "0";
    }
}
