import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;

import java.util.Arrays;

public final class BruteCollinearPoints {
    private final Point[] points;
    private LineSegment[] segments;
    private Point[] addedPoints;
    private int number;

    // finds all line segments containing 4 points
    public BruteCollinearPoints(Point[] array) {
        if (array == null || array.length < 4 || arrayContains(array, null)) {
            throw new IllegalArgumentException();
        }

        for (int i = 0; i < array.length; i++) {
            Point value = array[i];
            array[i] = null;
            if (arrayContains(array, value)) {
                throw new IllegalArgumentException();
            }
            array[i] = value;
        }

        points = array.clone();
        segments = new LineSegment[0];
        addedPoints = new Point[0];

        calculateSegments();
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

        BruteCollinearPoints checker = new BruteCollinearPoints(points);
        LineSegment[] segments = checker.segments();
        for (LineSegment segment : segments) {
            StdOut.println(segment.toString());
        }
    }

    private void calculateSegments() {
        for (int indexP = 0; indexP < points.length - 3; indexP++) {
            for (int indexQ = indexP + 1; indexQ < points.length - 2; indexQ++) {
                for (int indexR = indexQ + 1; indexR < points.length - 1; indexR++) {
                    for (int indexS = indexR + 1; indexS < points.length; indexS++) {
                        Point p = points[indexP];
                        Point q = points[indexQ];
                        Point r = points[indexR];
                        Point s = points[indexS];

                        double slopePQ = p.slopeTo(q);
                        double slopePR = p.slopeTo(r);
                        double slopePS = p.slopeTo(s);

                        if (Double.compare(slopePQ, slopePR) == 0 && Double.compare(slopePQ, slopePS) == 0) {
                            Point[] segment = new Point[3];
                            segment[0] = p;
                            segment[1] = q;
                            segment[2] = r;

                            number = addSegment(segment, p, number);
                        }
                    }
                }
            }
        }

        for (int i = 0; i < number * 2; i += 2) {
            appendSegment(new LineSegment(addedPoints[i], addedPoints[i + 1]), i / 2);
        }
    }

    private int addSegment(Point[] segment, Point p, int segmentIndex) {
        Point min = p;
        Point max = p;

        for (int i = 0; i < segment.length; i++) {
            if (segment[i].compareTo(min) < 0) {
                min = segment[i];
            }
            if (segment[i].compareTo(max) > 0) {
                max = segment[i];
            }
        }

        boolean skip = false;
        for (int i = 0; i < segmentIndex * 2; i += 2) {
            double existentSlope = addedPoints[i].slopeTo(addedPoints[i + 1]);
            double currentSlope;

            if (p == addedPoints[i]) {
                currentSlope = p.slopeTo(addedPoints[i + 1]);
            } else {
                currentSlope = p.slopeTo(addedPoints[i]);
            }

            if (Double.compare(currentSlope, existentSlope) == 0) {
                if (min.compareTo(addedPoints[i]) < 0) {
                    addedPoints[i] = min;
                }
                if (max.compareTo(addedPoints[i + 1]) > 0) {
                    addedPoints[i + 1] = max;
                }
                skip = true;
                break;
            }
        }

        if (!skip) {
            appendPoints(min, max, 2 * segmentIndex++);
        }

        return segmentIndex;
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

    private boolean arrayContains(Point[] array, Point value) {
        for (int i = 0; i < array.length; i++) {
            Point point = array[i];
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
