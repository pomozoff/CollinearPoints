import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;

import javax.sound.sampled.Line;
import java.util.Arrays;

public class BruteCollinearPoints {
    private Point[] points;
    private LineSegment[] segments;
    private Point[] addedPoints;

    // finds all line segments containing 4 points
    public BruteCollinearPoints(Point[] points) {
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

                        if (slopePQ == slopePR && slopePQ == slopePS) {
                            Point[] segment = new Point[3];
                            segment[0] = p;
                            segment[1] = q;
                            segment[2] = r;

                            Point min = s;
                            Point max = s;

                            for (int i = 0; i < segment.length; i++) {
                                if (segment[i].compareTo(min) < 0) {
                                    min = segment[i];
                                }
                                if (segment[i].compareTo(max) > 0) {
                                    max = segment[i];
                                }
                            }

                            boolean skip = false;
                            for (int i = 0; i < number * 2; i += 2) {
                                double existentSlope = addedPoints[i].slopeTo(addedPoints[i + 1]);
                                double currentSlope;

                                if (p == addedPoints[i]) {
                                    currentSlope = p.slopeTo(addedPoints[i + 1]);
                                } else {
                                    currentSlope = p.slopeTo(addedPoints[i]);
                                }

                                if (currentSlope == existentSlope) {
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

                            if (skip) {
                                continue;
                            }

                            appendPoints(min, max, 2 * number++);
                        }
                    }
                }
            }
        }

        for (int i = 0; i < number * 2; i += 2) {
            appendSegment(new LineSegment(addedPoints[i], addedPoints[i + 1]), i / 2);
        }

        return number;
    }

    // the line segments
    public LineSegment[] segments() {
        return segments;
    }

    public static void main(String[] args) {
//        Point[] points = {
//                new Point(10, 40),
//                new Point(40, 40),
//                new Point(20, 10),
//                new Point(10, 20),
//                new Point(30, 10),
//                new Point(10, 40),
//                new Point(30, 30),
//                new Point(10, 10),
//                new Point(20, 20),
//                new Point(10, 30),
//        };

        In in = new In(args[0]);
        int n = in.readInt();
        Point[] points = new Point[n];
        for (int i = 0; i < n; i++) {
            int x = in.readInt();
            int y = in.readInt();
            points[i] = new Point(x, y);
        }

        BruteCollinearPoints checker = new BruteCollinearPoints(points);
        StdOut.println(checker.numberOfSegments());
        StdOut.println(Arrays.toString(checker.segments()));
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
