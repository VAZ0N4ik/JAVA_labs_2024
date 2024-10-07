package ru.ssau.tk.java_domination_339.java_labs_2024.functions;

import ru.ssau.tk.java_domination_339.java_labs_2024.exceptions.InterpolationException;

import java.util.Iterator;
import java.util.NoSuchElementException;

import static java.lang.Math.abs;

public class LinkedListTabulatedFunction extends AbstractTabulatedFunction implements Insertable, Removable {

    static class Node {
        public Node next;
        public Node prev;
        public double x;
        public double y;
    }

    private Node head;

    private void addNode(double x, double y) {
        Node temp = new Node();
        temp.x = x;
        temp.y = y;
        if (head == null) {
            head = temp;
            head.next = head;
            head.prev = head;
        } else {
            Node last = head.prev;
            last.next = temp;
            head.prev = temp;
            temp.prev = last;
            temp.next = head;
        }
        count++;
    }

    public LinkedListTabulatedFunction(double[] xValues, double[] yValues) throws IllegalArgumentException {
        if (xValues.length < 2 || yValues.length < 2)
            throw new IllegalArgumentException("Length must be >=2");
        checkLengthIsTheSame(xValues, yValues);
        checkSorted(xValues);

        for (int i = 0; i < xValues.length; i++) {
            addNode(xValues[i], yValues[i]);
        }
    }

    public LinkedListTabulatedFunction(MathFunction source, double xFrom, double xTo, int count) throws IllegalArgumentException {
        if (count < 2)
            throw new IllegalArgumentException("Length must be >=2");
        if (xFrom > xTo) {
            double temp;
            temp = xFrom;
            xFrom = xTo;
            xTo = temp;
        }

        if (xFrom == xTo) {
            for (int i = 0; i < count; i++) {
                addNode(xFrom, source.apply(xFrom));
            }
        } else {
            double step = (xTo - xFrom) / (count - 1);
            double x0 = xFrom;
            for (int i = 0; i < count; i++) {
                addNode(x0, source.apply(x0));
                x0 += step;
            }
        }
    }

    public int getCount() {
        return count;
    }

    public double leftBound() {
        return head.x;
    }

    public double rightBound() {
        return head.prev.x;
    }

    private Node getNode(int index) throws IllegalArgumentException {
        if (index < 0 || index >= count)
            throw new IllegalArgumentException("Incorrect index");
        Node temp = head;
        while (index > 0) {
            temp = temp.next;
            index--;
        }
        return temp;
    }

    public double getX(int index) throws IllegalArgumentException {
        if (index < 0 || index >= count)
            throw new IllegalArgumentException("Incorrect index");
        return getNode(index).x;
    }

    public double getY(int index) throws IllegalArgumentException {
        if (index < 0 || index >=count)
            throw new IllegalArgumentException("Incorrect index");
        return getNode(index).y;
    }

    public void setY(int index, double value) throws IllegalArgumentException {
        if (index < 0 || index >= count)
            throw new IllegalArgumentException("Incorrect index");
        getNode(index).y = value;
    }

    public int indexOfX(double x)  {

        int ans = -1;
        Node temp = head;
        for (int i = 0; i < count; i++) {
            if (abs(x - temp.x) <= 1e-8) {
                ans = i;
                break;
            }
            temp = temp.next;
        }
        return ans;
    }

    public int indexOfY(double y) {

        int ans = -1;
        Node temp = head;
        for (int i = 0; i < count; i++) {
            if (abs(y - temp.y) <= 1e-8) {
                ans = i;
                break;
            }
            temp = temp.next;
        }
        return ans;
    }

    protected int floorIndexOfX(double x) throws IllegalArgumentException {
        if (x < leftBound())
            throw new IllegalArgumentException("Argument less than left bound");
        int ans = 0;
        Node temp = head;
        for (int i = 0; i < count; i++) {
            if (x >= temp.x) {
                ans = i;
            } else {
                return ans;
            }
            temp = temp.next;
        }
        return count;
    }

    @Override
    protected double extrapolateLeft(double x) {

        return interpolate(x, head.x, head.next.x, head.y, head.next.y);
    }

    @Override
    protected double interpolate(double x, int floorIndex) {

        if (x < getNode(floorIndex).x || x > getNode(floorIndex + 1).x) {
            throw new InterpolationException();
        }

        Node temp = getNode(floorIndex);
        return interpolate(x, temp.x, temp.next.x, temp.y, temp.next.y);
    }

    @Override
    protected double extrapolateRight(double x) {
        return interpolate(x, head.prev.prev.x, head.prev.x, head.prev.prev.y, head.prev.y);
    }

    protected Node floorNodeOfX(double x) throws IllegalArgumentException {
        if (x < leftBound())
            throw new IllegalArgumentException("Argument less than left bound");
        Node temp = head;
        Node ans = head;
        while (temp.next != head && temp.x < x) {
            ans = temp;
            temp = temp.next;
            if (temp.x == x) {
                return temp;
            }
        }
        return ans;
    }

    @Override
    public double apply(double x) {
        if (x < leftBound()) {
            return extrapolateLeft(x);
        } else if (x > rightBound()) {
            return extrapolateRight(x);
        } else {
            if (indexOfX(x) != -1) {
                return getY(indexOfX(x));
            } else {
                Node floor = floorNodeOfX(x);
                return interpolate(x, floor.x, floor.next.x, floor.y, floor.next.y);
            }
        }
    }

    @Override
    public void insert(double x, double y) {
        if (head == null) {
            addNode(x, y);
        } else {
            Node cur = head;
            Node prev = head.prev;

            while (cur.x < x && cur.next != head) {
                cur = cur.next;
            }

            if (cur.x < x) {
                cur = head;
            }

            if (cur.x == x) {
                cur.y = y;
            } else {
                Node newNode = new Node();
                newNode.x = x;
                newNode.y = y;

                newNode.next = cur;
                newNode.prev = cur.prev;
                cur.prev = newNode;
                newNode.prev.next = newNode;

                if (cur == head && x < head.x)
                    head = newNode;
                ++count;
            }
        }
    }

    public void remove(int index) {
        if (index < 0 || index >= count)
            throw new IllegalArgumentException("Incorrect index");
        if (count == 1) {
            head = null;
        } else {
            Node n = getNode(index);
            Node pr = n.prev;
            n.prev.next = n.next;
            n.next.prev = pr;
            n = null;
            pr = null;
        }
        count--;
    }

    @Override
    public Iterator<Point> iterator() {
        return new Iterator<>() {

            Node node = head;

            @Override
            public boolean hasNext(){
                return node != null;
            }
            @Override
            public Point next() throws NoSuchElementException{
                if (!hasNext())
                    throw new NoSuchElementException();
                else{
                    Point curPoint = new Point(node.x,node.y);
                    node = node.next;
                    if (node == head)
                        node = null;
                    return curPoint;
                }
            }
        };

    }

}