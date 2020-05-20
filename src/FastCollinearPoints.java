import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public final class FastCollinearPoints {
    private final Point[] points;
    private final Point[] slopePoints;
    private List<LineSegment> segments;

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

        segments = new ArrayList<>();
        slopePoints = points.clone();

        if (points.length < 4) {
            return;
        }

        calculateSegments();
    }

    // the number of line segments
    public int numberOfSegments() {
        return segments.size();
    }

    // the line segments
    public LineSegment[] segments() {
        return segments.toArray(new LineSegment[segments.size()]);
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
            Arrays.sort(slopePoints, p.slopeOrder());

            int counter = 1;
            double prevSlope = Double.NaN;

            for (int i = 0; i < slopePoints.length; i++) {
                Point q = slopePoints[i];
                double slope = p.slopeTo(q);

                if (Double.compare(slope, prevSlope) == 0) {
                    counter++;
                } else {
                    if (counter > 2) {
                        addSegment(p, i - 1, counter);
                    }
                    counter = 1;
                    prevSlope = slope;
                }
            }

            if (counter > 2) {
                addSegment(p, slopePoints.length - 1, counter);
            }
        }
    }

    private void addSegment(Point p, int index, int pointsNumber) {
        Point min = p;
        Point max = p;

        for (int i = index; i > index - pointsNumber; i--) {
            Point q = slopePoints[i];
            if (q.compareTo(min) < 0) {
                min = q;
            }
            if (q.compareTo(max) > 0) {
                max = q;
            }
        }

        if (p.compareTo(min) == 0) {
            segments.add(new LineSegment(min, max));
        }
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
}
