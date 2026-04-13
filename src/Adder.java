public class Adder {
    public static void subtract(Word32 a, Word32 b, Word32 result) {
        Word32 notB = new  Word32();
        Word32 one = new Word32();
        Bit trueBit = new Bit(true);
        b.not(notB);
        one.setBitN(31, trueBit);
        add(a, notB, result);
        add(result, one, result);
    }

    public static void add(Word32 a, Word32 b, Word32 result) {
        Bit carry = new Bit(false);
        for(int i = 31; i >= 0; i--) {
            Bit bitA = new Bit(false);
            Bit bitB = new Bit(false);
            Bit aXorB = new Bit(false);
            Bit aXorBAndCarry = new Bit(false);
            Bit aAndB = new Bit(false);
            Bit sum = new Bit(false);
            a.getBitN(i, bitA);
            b.getBitN(i, bitB);
            bitA.xor(bitB, aXorB);
            aXorB.xor(carry, sum);
            result.setBitN(i, sum);
            bitA.and(bitB, aAndB);
            aXorB.and(carry, aXorBAndCarry);
            aAndB.or(aXorBAndCarry, carry);
       }
    }
}
