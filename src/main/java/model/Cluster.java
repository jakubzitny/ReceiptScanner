package model;

import java.util.Comparator;
import java.util.List;

/**
 * Created by d_rc on 07/12/14.
 */
public class Cluster {

    private int mNumber;
    private int mSize;

    // borders
    private Coordinates mTop = null;
    private Coordinates mBottom = null;
    private Coordinates mLeft = null;
    private Coordinates mRight = null;

    public Cluster (int number) {
        mNumber = number;
        mSize = 0;
    }

    // merging constructor
    public Cluster (List<Cluster> clusters) {
        mSize = 0;
        mNumber = -1;
        mTop = clusters.get(0).getTop();
        mBottom = clusters.get(0).getBottom();
        mLeft = clusters.get(0).getLeft();
        mRight = clusters.get(0).getRight();
        for (Cluster c: clusters) {
            mSize += c.getSize();
            if (c.getTop().x < mTop.x) mTop = c.getTop();
            if (c.getBottom().x > mBottom.x) mBottom = c.getBottom();
            if (c.getLeft().y < mLeft.y) mLeft = c.getLeft();
            if (c.getRight().y > mRight.y) mRight = c.getRight();
        }
    }

    public void add (int x, int y) {
        if (mSize == 0) {
            init(x, y);
        } else {
            recalcBorders(x, y);
        }
        mSize++;
    }

    private void init(int x, int y) {
        mTop = new Coordinates(x, y);
        mBottom = new Coordinates(x, y);
        mLeft = new Coordinates(x, y);
        mRight = new Coordinates(x, y);
    }

    private void recalcBorders(int x, int y) {
        if (x < mTop.x) mTop = new Coordinates(x, y);
        if (x > mBottom.x) mBottom = new Coordinates(x, y);
        if (y < mLeft.y) mLeft = new Coordinates(x, y);
        if (y > mRight.y) mRight = new Coordinates(x, y);
    }

    public int getNumber() {
        return mNumber;
    }

    public void setNumber(int mNumber) {
        this.mNumber = mNumber;
    }

    public int getSize() {
        return mSize;
    }

    public void setSize(int mSize) {
        this.mSize = mSize;
    }

    public Coordinates getTop() {
        return mTop;
    }

    public void setTop(Coordinates mTop) {
        this.mTop = mTop;
    }

    public Coordinates getBottom() {
        return mBottom;
    }

    public void setBottom(Coordinates mBottom) {
        this.mBottom = mBottom;
    }

    public Coordinates getLeft() {
        return mLeft;
    }

    public void setLeft(Coordinates mLeft) {
        this.mLeft = mLeft;
    }

    public Coordinates getRight() {
        return mRight;
    }

    public void setRight(Coordinates mRight) {
        this.mRight = mRight;
    }

    public static Comparator getComparator() {
        return new Comparator<Cluster>() {
            public int compare(Cluster c1, Cluster c2) {
                return c2.getSize() - c1.getSize();
            }
        };
    }
}
