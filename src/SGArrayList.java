import java.util.*;

// Clase que extiende ArrayList
public class SGArrayList<T> extends ArrayList<Point[]> {
    private static final long serialVersionUID = 1L;

    // Método personalizado para agregar un conjunto de puntos a la lista
    public void add(Point point1, Point point2, Point point3) {
        // Crear un arreglo de puntos con los puntos proporcionados
        Point[] array = { point1, point2, point3 };
        // Agregar el arreglo a la lista
        this.add(array);
    }
}