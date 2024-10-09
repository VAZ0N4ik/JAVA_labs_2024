package ru.ssau.tk.java_domination_339.java_labs_2024.operations;
import org.junit.jupiter.api.Test;

import ru.ssau.tk.java_domination_339.java_labs_2024.functions.ArrayTabulatedFunction;
import ru.ssau.tk.java_domination_339.java_labs_2024.functions.LinkedListTabulatedFunction;
import ru.ssau.tk.java_domination_339.java_labs_2024.functions.TabulatedFunction;
import ru.ssau.tk.java_domination_339.java_labs_2024.functions.factory.ArrayTabulatedFunctionFactory;
import ru.ssau.tk.java_domination_339.java_labs_2024.functions.factory.LinkedListTabulatedFunctionFactory;

import static org.junit.jupiter.api.Assertions.assertEquals;

class TabulatedDifferentialOperatorTest {

        double[] xValues = {1.0, 2.0, 3.0};
        double[] yValues = {2.0, 4.0, 6.0};

        TabulatedFunction arrayFunction = new ArrayTabulatedFunction(xValues, yValues);
        TabulatedFunction linkedListFunction = new LinkedListTabulatedFunction(xValues, yValues);

        @Test
        public void testGetFactory() {

            TabulatedDifferentialOperator operator = new TabulatedDifferentialOperator();
            assertEquals(ArrayTabulatedFunctionFactory.class, operator.getFactory().getClass());

            TabulatedDifferentialOperator operatorWithParam = new TabulatedDifferentialOperator(new LinkedListTabulatedFunctionFactory());
            assertEquals(LinkedListTabulatedFunctionFactory.class, operatorWithParam.getFactory().getClass());
        }

        @Test
        public void testSetFactory() {
            TabulatedDifferentialOperator operator = new TabulatedDifferentialOperator();
            assertEquals(ArrayTabulatedFunctionFactory.class, operator.getFactory().getClass());

            operator.setFactory(new LinkedListTabulatedFunctionFactory());
            assertEquals(LinkedListTabulatedFunctionFactory.class, operator.getFactory().getClass());


            operator.setFactory(new ArrayTabulatedFunctionFactory());
            assertEquals(ArrayTabulatedFunctionFactory.class, operator.getFactory().getClass());
        }

        @Test
        public void testConstructors() {

            TabulatedDifferentialOperator operatorDefault = new TabulatedDifferentialOperator();
            assertEquals(ArrayTabulatedFunctionFactory.class, operatorDefault.getFactory().getClass());


            TabulatedDifferentialOperator operatorWithFactory = new TabulatedDifferentialOperator(new LinkedListTabulatedFunctionFactory());
            assertEquals(LinkedListTabulatedFunctionFactory.class, operatorWithFactory.getFactory().getClass());
        }

        @Test
        public void testDeriveWithDifferentFactories() {

            TabulatedDifferentialOperator operatorArray = new TabulatedDifferentialOperator(new ArrayTabulatedFunctionFactory());
            TabulatedFunction derivedArrayFunction = operatorArray.derive(arrayFunction);
            assertEquals(arrayFunction.getCount(), derivedArrayFunction.getCount());


            TabulatedDifferentialOperator operatorLinkedList = new TabulatedDifferentialOperator(new LinkedListTabulatedFunctionFactory());
            TabulatedFunction derivedLinkedListFunction = operatorLinkedList.derive(linkedListFunction);
            assertEquals(linkedListFunction.getCount(), derivedLinkedListFunction.getCount());
        }


}