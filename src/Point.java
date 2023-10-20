public class Point {
    private double x, y;

    // Constructor por defecto
    public Point() {
        x = 0;
        y = 0;
    }

    // Constructor que toma valores iniciales de x e y
    public Point(double x, double y) {
        this.x = x;
        this.y = y;
    }

    // Métodos para acceder y modificar la coordenada x
    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    // Métodos para acceder y modificar la coordenada y
    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }
}
