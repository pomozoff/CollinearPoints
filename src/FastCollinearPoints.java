import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;

import java.util.Arrays;

public final class FastCollinearPoints {
    private final Point[] points;
    private LineSegment[] segments;
    private Point[] addedPoints;
    private int number;
    private int addedNumber = 0;

    // finds all line segments containing 4 or more points
    public FastCollinearPoints(Point[] array) {
        if (array == null || arrayHasNull(array)) {
            throw new IllegalArgumentException();
        }
        points = array.clone();

        Point prev = null;
        Arrays.sort(points);
        for (Point p : points) {
            if (prev != null && prev.compareTo(p) == 0) {
                throw new IllegalArgumentException();
            }
            prev = p;
        }

        segments = new LineSegment[0];
        addedPoints = new Point[0];

        if (points.length >= 4) {
            calculateSegments();
        }
    }

    // the number of line segments
    public int numberOfSegments() {
        return number;
    }

    // the line segments
    public LineSegment[] segments() {
        return Arrays.copyOf(segments, number);
    }

    public static void main(String[] args) {
        In in = new In(args[0]);
        int n = in.readInt();
        Point[] points = new Point[n];
        for (int i = 0; i < n; i++) {
            int x = in.readInt();
            int y = in.readInt();
            points[i] = new Point(x, y);
        }

        FastCollinearPoints checker = new FastCollinearPoints(points);
        LineSegment[] segments = checker.segments();
        for (LineSegment segment : segments) {
            StdOut.println(segment.toString());
        }
    }

    private void calculateSegments() {
        for (int indexP = 0; indexP < points.length - 3; indexP++) {
            Point p = points[indexP];

            int start = indexP + 1;
            Arrays.sort(points, start, points.length, p.slopeOrder());

            /*
            double[] slopes = new double[points.length - start];
            for (int i = start; i < points.length; i++) {
                slopes[i - start] = p.slopeTo(points[i]);
            }
             */

            int counter = 1;
            double prevSlope = Double.NaN;

            for (int i = start; i < points.length; i++) {
                Point q = points[i];
                double slope = p.slopeTo(q);

                if (Double.compare(slope, prevSlope) == 0) {
                    counter++;
                } else {
                    if (counter > 2) {
                        number = addSegment(p, i - 1, counter, number);
                    }
                    counter = 1;
                    prevSlope = slope;
                }
            }

            if (counter > 2) {
                number = addSegment(p, points.length - 1, counter, number);
            }
        }
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

        for (int i = 0; i < addedNumber; i += 2) {
            Point exMin = addedPoints[i];
            Point exMax = addedPoints[i + 1];

            if (min.compareTo(exMin) < 0 || max.compareTo(exMax) > 0) {
                continue;
            }

            double existentSlope = exMin.slopeTo(exMax);
            double currentSlope = min.slopeTo(max);
            double compareSlope;

            if (min.compareTo(exMin) == 0) {
                compareSlope = min.slopeTo(exMax);
            } else {
                compareSlope = min.slopeTo(exMin);
            }

            if (Double.compare(currentSlope, existentSlope) == 0
                        && Double.compare(currentSlope, compareSlope) == 0
            ) {
                return segmentIndex;
            }
        }

        if (pointsNumber > 3) {
            appendPoints(min, max);
        }
        appendSegment(new LineSegment(min, max), segmentIndex);

        return segmentIndex + 1;
    }

    private void appendSegment(LineSegment value, int index) {
        if (index == segments.length) {
            resizeSegmentsArray(index * 2 + 1);
        }
        segments[index] = value;
    }

    private void appendPoints(Point p, Point q) {
        if (addedNumber >= addedPoints.length) {
            resizeAddedPointsArray(addedNumber * 2 + 2);
        }
        addedPoints[addedNumber] = p;
        addedPoints[addedNumber + 1] = q;

        addedNumber += 2;
    }

    private boolean arrayHasNull(Point[] array) {
        for (int i = 0; i < array.length; i++) {
            Point point = array[i];
            if (point == null) {
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
