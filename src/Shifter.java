public class Shifter {
    public static void LeftShift(Word32 source, int amount, Word32 result) {
        for(int i = 31; i >= 0; i--) {
            Bit bit = new Bit(false);
            source.getBitN(i, bit);
            if(i-amount >= 0) {
                result.setBitN(i-amount, bit);
            }
        }
    }

    public static void RightShift(Word32 source, int amount, Word32 result) {
        for(int i = 0; i < 32; i++) {
            Bit bit = new Bit(false);
            source.getBitN(i, bit);
            if(i+amount < 32) {
                result.setBitN(i+amount, bit);
            }
        }
    }
}
