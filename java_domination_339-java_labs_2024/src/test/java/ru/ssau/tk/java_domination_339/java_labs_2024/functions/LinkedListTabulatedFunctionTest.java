package ru.ssau.tk.java_domination_339.java_labs_2024.functions;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class LinkedListTabulatedFunctionTest {
    double eps = 0.01;
    LinkedListTabulatedFunction clList1 = new LinkedListTabulatedFunction(new double[]{-5,-3.6,0.01,1,4}, new double[]{0,3.6,5,100,0.1});
    LinkedListTabulatedFunction clList2 = new LinkedListTabulatedFunction(new double[]{1,2,3}, new double[]{9,10,15});
    LinkedListTabulatedFunction dList1 = new LinkedListTabulatedFunction(new SqrFunction(), -10, 20, 30);
    LinkedListTabulatedFunction dList2 = new LinkedListTabulatedFunction(new SqrFunction(), 1, 1, 5);

    @Test
    void testGetCount() {
        assertEquals(clList1.getCount(),5,eps);
        assertEquals(clList2.getCount(),3,eps);
        assertEquals(dList1.getCount(),30,eps);
        assertEquals(dList2.getCount(),5,eps);
    }

    @Test
    void testRightBound() {
        assertEquals(clList1.rightBound(),4,eps);
        assertEquals(clList2.rightBound(),3,eps);
        assertEquals(dList1.rightBound(),20,eps);
        assertEquals(dList2.rightBound(),1,eps);
    }

    @Test
    void testLeftBound() {
        assertEquals(clList1.leftBound(),-5,eps);
        assertEquals(clList2.leftBound(),1,eps);
        assertEquals(dList1.leftBound(),-10,eps);
        assertEquals(dList2.leftBound(),1,eps);
    }

    @Test
    void testGetX() {
        assertEquals(clList1.getX(1),-3.6,eps);
        assertEquals(clList2.getX(2),3,eps);
        assertEquals(dList1.getX(0),-10,eps);
        assertEquals(dList2.getX(3),1,eps);
    }

    @Test
    void testGetY() {
        assertEquals(clList1.getY(1),3.6,eps);
        assertEquals(clList2.getY(2),15,eps);
        assertEquals(dList1.getY(0),100,eps);
        assertEquals(dList2.getY(3),1,eps);
    }

    @Test
    void testSetY() {
        clList1.setY(0,-100);
        clList2.setY(2,100);
        dList1.setY(1,-9.5);
        assertEquals(clList1.getY(0),-100,eps);
        assertEquals(clList2.getY(2),100,eps);
        assertEquals(dList1.getY(1),-9.5,eps);
    }

    @Test
    void testIndexOfX() {
        assertEquals(clList1.indexOfX(100),-1,eps);
        assertEquals(clList1.indexOfX(0.01),2,eps);
        assertEquals(dList1.indexOfX(20),29,eps);
    }

    @Test
    void testIndexOfY() {
        assertEquals(clList1.indexOfY(1000),-1,eps);
        assertEquals(clList1.indexOfY(5),2,eps);
        assertEquals(dList1.indexOfY(400),29,eps);
    }

    @Test
    void floorIndexOfX() {
        assertEquals(clList1.floorIndexOfX(1),3,eps);
        assertEquals(clList2.floorIndexOfX(2.5),1,eps);
        assertEquals(dList1.floorIndexOfX(25),30,eps);
        assertEquals(dList2.floorIndexOfX(0),0,eps);
    }

    @Test
    void extrapolateLeft() {
        assertEquals(clList1.extrapolateLeft(-10),-12.85,eps);
        assertEquals(clList2.extrapolateLeft(0),8,eps);
        assertEquals(dList1.extrapolateLeft(-11),118.96,eps);
        //assertEquals(dList2.extrapolateLeft(0),NaN,eps);
    }

    @Test
    void interpolate() {
        assertEquals(clList1.interpolate(3,3),33.39,eps);
        assertEquals(clList2.interpolate(2.5,1),12.5,eps);
        assertEquals(dList1.interpolate(19,28),361.03,eps);
    }

    @Test
    void extrapolateRight() {
        assertEquals(clList1.extrapolateRight(10),-199.7,eps);
        assertEquals(clList2.extrapolateRight(10),50,eps);
        assertEquals(dList1.extrapolateRight(22),477.93,eps);
    }

    @Test
    void floorNodeOfX() {
    }

    @Test
    void apply() {
        assertEquals(clList1.apply(10),-199.7,eps);
        assertEquals(clList2.apply(1.1),9.1,eps);
        assertEquals(dList1.apply(0.01),0.23,eps);
    }
}