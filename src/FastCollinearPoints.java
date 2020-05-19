import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public final class FastCollinearPoints {
    private final Point[] points;
    private List<LineSegment> segments;
    private List<Point> addedPoints;

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
        addedPoints = new ArrayList<>();

        if (points.length >= 4) {
            calculateSegments();
        }
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
                        addSegment(p, i - 1, counter);
                    }
                    counter = 1;
                    prevSlope = slope;
                }
            }

            if (counter > 2) {
                addSegment(p, points.length - 1, counter);
            }
        }
    }

    private void addSegment(Point p, int index, int pointsNumber) {
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

        for (int i = 0; i < addedPoints.size(); i += 2) {
            Point exMin = addedPoints.get(i);
            Point exMax = addedPoints.get(i + 1);

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
                return;
            }
        }

        if (pointsNumber > 3) {
            appendPoints(min, max);
        }
        segments.add(new LineSegment(min, max));
    }

    private void appendPoints(Point p, Point q) {
        addedPoints.add(p);
        addedPoints.add(q);
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
