package ru.ssau.tk.java_domination_339.java_labs_2024.functions;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;


class CompositeFunctionTest {
    CompositeFunction cF, secondCF;
    IdentityFunction iF1,iF2,iF3;
    @Test
    void test(){
        iF1 = new IdentityFunction();
        iF2 = new IdentityFunction();
        iF3 = new IdentityFunction();
        secondCF = new CompositeFunction(iF1,iF2);
        cF = new CompositeFunction(iF3, secondCF);
        Assertions.assertEquals(10, secondCF.apply(10), 0.00000000001);
        Assertions.assertEquals(23434.00001, cF.apply(23434.00001), 0.00000000001);
    }
}