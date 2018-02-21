public class Rectangle {
  private final double x1;
  private final double y1;
  private final double x2;
  private final double y2;

  public double getHeight() {
    return height;
  }

  private final double height;

  public Rectangle(final double x1, final double y1, final double x2, final double y2) {
    this(x1, y1, x2, y2, 0.0);
  }

  public Rectangle(final double x1, final double y1, final double x2, final double y2, final
                   double h) {
    this.x1 = x1;
    this.y1 = y1;
    this.x2 = x2;
    this.y2 = y2;

    if (x2 < x1 || y2 < y1) {
      throw new IllegalArgumentException();
    }
    this.height = h;
  }

  public double getX1() {
    return x1;
  }

  public double getY1() {
    return y1;
  }

  public double getX2() {
    return x2;
  }

  public double getY2() {
    return y2;
  }

  public boolean contains(final Rectangle rectangle) {
    return x1 <= rectangle.getX1() && x2 >= rectangle.getX2() &&
        y1 <= rectangle.getY1() && y2 >= rectangle.getY2();
  }

  public boolean intersect(final Rectangle rectangle) {
    return (x1 <= rectangle.getX1() && rectangle.getX1() <= x2 || x1 <= rectangle.getX2() &&
        rectangle.getX2() <= x2) &&
        (y1 <= rectangle.getY1() && rectangle.getY1() <= y2 || y1 <= rectangle.getY2() &&
            rectangle.getY2() <= y2);
  }
}
