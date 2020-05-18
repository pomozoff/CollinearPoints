import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Merge;
import edu.princeton.cs.algs4.StdOut;

import java.util.Arrays;

public class FastCollinearPoints {
    private Point[] points;
    private LineSegment[] segments;
    private Point[] addedPoints;

    // finds all line segments containing 4 or more points
    public FastCollinearPoints(Point[] points) {
        if (points == null || points.length < 4 || arrayContains(points, null)) {
            throw new IllegalArgumentException();
        }

        for (int i = 0; i < points.length; i++) {
            Point value = points[i];
            points[i] = null;
            if (arrayContains(points, value)) {
                throw new IllegalArgumentException();
            }
            points[i] = value;
        }

        this.points = points;
        segments = new LineSegment[0];
        addedPoints = new Point[0];
    }

    // the number of line segments
    public int numberOfSegments() {
        int number = 0;

        for (int indexP = 0; indexP < points.length - 3; indexP++) {
            Point p = points[indexP];

            double[] slopes = new double[points.length - indexP - 1];
            for (int i = indexP + 1; i < points.length; i++) {
                slopes[i - indexP - 1] = p.slopeTo(points[i]);
            }

            Arrays.sort(points, indexP + 1, points.length, p.slopeOrder());

            int counter = 1;
            double prevValue = Double.NaN;

            for (int i = indexP + 1; i < points.length; i++) {
                Point q = points[i];
                double slope = p.slopeTo(q);

                if (slope == prevValue) {
                    counter++;
                } else {
                    if (counter > 2) {
                        number = addSegment(p, i - 1, counter, number);
                    }
                    counter = 1;
                    prevValue = slope;
                }
            }

            if (counter > 2) {
                number = addSegment(p, points.length - 1, counter, number);
            }
        }

        return number;
    }

    // the line segments
    public LineSegment[] segments() {
        return segments;
    }

    public static void main(String[] args) {
//        Point[] points = {
//                new Point(10000, 0),
//                new Point(0, 10000),
//                new Point(3000, 7000),
//                new Point(7000, 3000),
//                new Point(20000, 21000),
//                new Point(20000, 21000),

//                new Point(19000, 10000),
//                new Point(18000, 10000),
//                new Point(32000, 10000),
//                new Point(21000, 10000),
//                new Point(1234, 5678),
//                new Point(14000, 10000),

//                new Point(30, 30),
//                new Point(20, 40),
//                new Point(10, 10),
//                new Point(10, 20),
//                new Point(30, 10),
//                new Point( 0,  0),
//                new Point(40, 10),
//                new Point(20, 20),
//                new Point(20, 10),
//                new Point(50, 10),
//                new Point(40, 40),
//        };

        In in = new In(args[0]);
        int n = in.readInt();
        Point[] points = new Point[n];
        for (int i = 0; i < n; i++) {
            int x = in.readInt();
            int y = in.readInt();
            points[i] = new Point(x, y);
        }

        FastCollinearPoints checker = new FastCollinearPoints(points);
        StdOut.println(checker.numberOfSegments());
        StdOut.println(Arrays.toString(checker.segments()));
    }

    private int addSegment(Point p, int index, int pointsNumber, int segmentIndex) {
        Point min = p;
        Point max = p;

        for (int i = index; i > index - pointsNumber; i--) {
            Point q = points[i];
            if (q.compareTo(min) < 0) {
                min = q;
            }
            if (q.compareTo(max) > 0) {
                max = q;
            }
        }

        for (int i = 0; i < segmentIndex * 2; i += 2) {
            double existentSlope = addedPoints[i].slopeTo(addedPoints[i + 1]);
            double currentSlope = p.slopeTo(addedPoints[i]);

            if (currentSlope == existentSlope) {
                return segmentIndex;
            }
        }

        appendPoints(min, max, segmentIndex * 2);
        appendSegment(new LineSegment(min, max), segmentIndex);

        return segmentIndex + 1;
    }

    private void appendSegment(LineSegment value, int index) {
        if (index == segments.length) {
            resizeSegmentsArray(index * 2 + 1);
        }
        segments[index] = value;
    }

    private void appendPoints(Point p, Point q, int index) {
        if (index == addedPoints.length) {
            resizeAddedPointsArray(index * 2 + 2);
        }
        addedPoints[index] = p;
        addedPoints[index + 1] = q;
    }

    private boolean arrayContains(Point[] points, Point value) {
        for (int i = 0; i < points.length; i++) {
            Point point = points[i];
            if (point == value) {
                return true;
            }
        }
        return false;
    }

    private void resizeSegmentsArray(int newCapacity) {
        LineSegment[] newArray = new LineSegment[newCapacity];
        for (int i = 0; i < Math.min(newCapacity, segments.length); i++) {
            newArray[i] = segments[i];
        }
        segments = newArray;
    }

    private void resizeAddedPointsArray(int newCapacity) {
        Point[] newArray = new Point[newCapacity];
        for (int i = 0; i < Math.min(newCapacity, addedPoints.length); i++) {
            newArray[i] = addedPoints[i];
        }
        addedPoints = newArray;
    }
}
