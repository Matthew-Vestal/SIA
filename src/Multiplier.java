public class Multiplier {
    public static void multiply(Word32 a, Word32 b, Word32 result) {
        Word32 sum = new Word32();
        for(int i = 31; i >= 0; i--) {
            Bit bBit = new Bit(false);
            Word32 tempSum = new Word32();
            b.getBitN(i, bBit);
            for(int j = 31; j >= 0; j--) {
                Bit aBit = new Bit(false);
                Bit tempBit = new Bit(false);
                a.getBitN(j, aBit);
                bBit.and(aBit, tempBit);
                tempSum.setBitN(j, tempBit);
            }
            Word32 shifted = new Word32();
            Shifter.LeftShift(tempSum,31-i, shifted);
            Adder.add(shifted, sum, sum);
        }
        sum.copy(result);
    }
}
