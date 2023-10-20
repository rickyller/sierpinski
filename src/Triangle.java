import java.awt.*;
import java.awt.geom.Path2D;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveTask;

public class Triangle extends Path2D.Double {

    private static final long serialVersionUID = 1L;
    SGArrayList<Point> arrayList = new SGArrayList<Point>();

    public Triangle(Point p1, Point p2, Point p3, Graphics2D g2d, int numberOfTimes, int longitud, boolean esSecuencial) {
        // Creación de puntos temporales para pintarlos de negro.
        Point p1Temp = new Point(p1.getX(), p1.getY());
        Point p2Temp = new Point(p2.getX(), p2.getY());
        Point p3Temp = new Point(p3.getX(), p3.getY());

        for (int i = 0; i < numberOfTimes; i++) {
            // Agregar puntos a la estructura de datos recursivamente
            agregadorDePuntosRecursivo(p1Temp, p2Temp, p3Temp, g2d, i + 1);
            // Configurar nuevos puntos del triángulo grande
            p1Temp.setX(p1Temp.getX() + longitud + 10);
            p2Temp.setX(p2Temp.getX() + longitud + 10);
            p3Temp.setX(p3Temp.getX() + longitud + 10);
        }

        // Todos los puntos se han agregado a la estructura de datos, por lo que se pinta de blanco cada punto del triángulo que se obtiene recursivamente mediante agregadorDePuntosRecursivo
        dibujarLinea(arrayList, g2d, numberOfTimes, esSecuencial);
    }

    public void agregadorDePuntosRecursivo(Point p1, Point p2, Point p3, Graphics2D g2d, int conteoRecursivo) {
        if (conteoRecursivo == 0)
            return; // Detener la adición de puntos

        // Creación de puntos temporales para pintarlos de negro.
        Point p1Temp = new Point();
        Point p2Temp = new Point();
        Point p3Temp = new Point();

        p1Temp.setX((p1.getX() + p2.getX()) / 2.0);
        p1Temp.setY((p1.getY() + p2.getY()) / 2.0);

        p2Temp.setX((p2.getX() + p3.getX()) / 2.0);
        p2Temp.setY((p2.getY() + p3.getY()) / 2.0);

        p3Temp.setX((p1.getX() + p3.getX()) / 2.0);
        p3Temp.setY((p1.getY() + p3.getY()) / 2.0);

        arrayList.add(p1Temp, p2Temp, p3Temp);
        // Pintar el interior de los triángulos recursivamente
        agregadorDePuntosRecursivo(p3Temp, p2Temp, p3, g2d, conteoRecursivo - 1);
        arrayList.add(p1Temp, p2Temp, p3Temp);
        agregadorDePuntosRecursivo(p1Temp, p2, p2Temp, g2d, conteoRecursivo - 1);
        arrayList.add(p1Temp, p2Temp, p3Temp);
        agregadorDePuntosRecursivo(p1, p1Temp, p3Temp, g2d, conteoRecursivo - 1);
        arrayList.add(p1Temp, p2Temp, p3Temp);

        // Cambio al siguiente triángulo grande de manera recursiva
        agregadorDePuntosRecursivo(p1, p2, p3, g2d, conteoRecursivo - 1);
    }

    public double[] arrayCreator(double p1, double p2, double p3) {
        double[] array;
        array = new double[3];
        array[0] = p1;
        array[1] = p2;
        array[2] = p3;
        return array;
    }
    

    public void dibujarLinea(SGArrayList<Point> arrayList, Graphics2D g2d, int numberOfTimes, boolean esSecuencial) {
        if (esSecuencial) {
            long tiempoInicio = System.currentTimeMillis();
            for (int k = 0; k < 20 - numberOfTimes; k++)
                for (int i = 0; i < arrayList.size(); i++) {
                    Point p1 = arrayList.get(i)[0];
                    Point p2 = arrayList.get(i)[1];
                    Point p3 = arrayList.get(i)[2];

                    double[] arrayX = arrayCreator(p1.getX(), p2.getX(), p3.getX());
                    double[] arrayY = arrayCreator(p1.getY(), p2.getY(), p3.getY());

                    // Dibuja el triángulo basado en los arreglos.
                    Path2D path = new Path2D.Double();
                    path.moveTo(arrayX[0], arrayY[0]);
                    for (int j = 1; j < arrayX.length; ++j)
                        path.lineTo(arrayX[j], arrayY[j]);
                    path.closePath();

                    // Dibuja el triángulo con líneas negras y rellena el interior de blanco.
                    g2d.setStroke(new BasicStroke(0.0f)); // Configuración del tamaño del trazo en 0.0 para que no sea muy grueso.
                    g2d.setColor(Color.BLACK);
                    g2d.draw(path);
                    g2d.setColor(Color.WHITE);
                    g2d.fill(path);
                }

            long tiempoFinal = System.currentTimeMillis();
            System.out.println("Tiempo Secuencial (ms): " + (tiempoFinal - tiempoInicio));
        } else {
            long tiempoInicio = System.currentTimeMillis();
            ParallelDrawingTask task = new ParallelDrawingTask(arrayList, 0, arrayList.size(), g2d);
            ForkJoinPool pool = new ForkJoinPool();
            pool.invoke(task);
            long tiempoFinal = System.currentTimeMillis();
            System.out.println("Tiempo Forkjoin (ms): " + (tiempoFinal - tiempoInicio));
        }
    }
}

class ParallelDrawingTask extends RecursiveTask<Void> {
    private SGArrayList<Point> arrayList;
    private int start;
    private int end;
    private Graphics2D g2d;

    public ParallelDrawingTask(SGArrayList<Point> arrayList, int start, int end, Graphics2D g2d) {
        this.arrayList = arrayList;
        this.start = start;
        this.end = end;
        this.g2d = g2d;
    }
    
    

    @Override
    protected Void compute() {
        for (int i = start; i < end; i++) {
            Point p1 = arrayList.get(i)[0];
            Point p2 = arrayList.get(i)[1];
            Point p3 = arrayList.get(i)[2];

            double[] arrayX = {p1.getX(), p2.getX(), p3.getX()};
            double[] arrayY = {p1.getY(), p2.getY(), p3.getY()};

            // Dibuja el triángulo basado en los arreglos.
            Path2D path = new Path2D.Double();
            path.moveTo(arrayX[0], arrayY[0]);
            for (int j = 1; j < arrayX.length; ++j)
                path.lineTo(arrayX[j], arrayY[j]);
            path.closePath();

            g2d.setStroke(new BasicStroke(0.0f));
            g2d.setColor(Color.BLACK);
            g2d.draw(path);
            g2d.setColor(Color.WHITE);
            g2d.fill(path);
        }
        return null;
    }
}


/*Cuando el programa se ejecuta en modo paralelo (esSecuencial es false), 
se crea una tarea principal (ParallelDrawingTask) que toma una lista de puntos 
arrayList y una región del índice de inicio al índice final a procesar. 
El ForkJoinPool se encarga de dividir esta tarea principal en sub-tareas más pequeñas, 
y los hilos de Fork-Join ejecutan estas sub-tareas en paralelo.*/