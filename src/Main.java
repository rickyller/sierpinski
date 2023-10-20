import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class Main extends JFrame {

    private static final long serialVersionUID = 1L;
    private JPanel mainPanel = new JPanel();

    private GridLayout gridLayout = new GridLayout(0, 1);
    private int widthSize = 800;
    private int heightSize = 600;
    private int length = 120;
    private int recursiveNumber = 5;
    private boolean sequentialMode = true; // true para secuencial y false para paralelo
    private JButton setSizeButton = new JButton("Establecer ancho, alto, longitud y número");
    private JTextField widthText = new JTextField("800", 10);
    private JTextField heightText = new JTextField("600", 10);
    private JTextField lengthText = new JTextField("120", 10);
    private JTextField recursiveNumberText = new JTextField("5", 10);
    private JRadioButton sequentialRadioButton = new JRadioButton("Secuencial", true);
    private JRadioButton forkJoinRadioButton = new JRadioButton("ForkJoin", false);
    private JRadioButton executorServiceRadioButton = new JRadioButton("Executor Service", false);

    private ButtonGroup radioGroup = new ButtonGroup();
    private JButton showBoardButton = new JButton("Mostrar el tablero");
    private JTextField text1 = new JTextField("Elija el framework: ");
    private JTextField text2 = new JTextField("Ingrese el ancho del tablero");
    private JTextField text3 = new JTextField("Ingrese la altura del tablero");
    private JTextField text4 = new JTextField("Ingrese la longitud inicial de la línea");
    private JTextField text5 = new JTextField("Ingrese el número de triángulos");

    public static void main(String args[]) {
        Main app = new Main();
        app.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        app.setSize(880, 600); // establece el tamaño del marco
        app.setLocationRelativeTo(null);
        app.setBackground(Color.GREEN);
        app.setVisible(true);
    } // Fin de main

    public Main() {
        super("Sierpinski");

        setSizeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event) {
                String mensaje = "";
                try {
                    mensaje = String.format("El tamaño ingresado es %s x %s \nLongitud inicial: %s \nNivel: %s",
                            widthText.getText(), heightText.getText(), lengthText.getText(), recursiveNumberText.getText());
                    widthSize = Integer.parseInt(widthText.getText());
                    if (widthSize <= 1) {
                        mensaje = "¡Error! Por favor, ingrese un tamaño mayor que 1\n";
                        widthSize = 0; // Haciendo que boardSize sea algo fuera de rango.
                    }

                    heightSize = Integer.parseInt(heightText.getText());
                    if (heightSize <= 1) {
                        mensaje = "¡Error! Por favor, ingrese un tamaño de tablero mayor que 1\n";
                        heightSize = 0; // Haciendo que boardSize sea algo fuera de rango.
                    }
                    length = Integer.parseInt(lengthText.getText());
                    if (length < 1 || length > 500) {
                        mensaje = "¡Error! Por favor, ingrese la longitud correctamente entre 1-500\n";
                        widthSize = 0;
                    }
                    recursiveNumber = Integer.parseInt(recursiveNumberText.getText());
                    if (recursiveNumber < 0 || recursiveNumber > 20) {
                        mensaje = "¡Error! Por favor, ingrese el número recursivo correctamente entre 0-20\n";
                        widthSize = 0;
                    }
                    JOptionPane.showMessageDialog(null, mensaje);
                } catch (NumberFormatException n) {
                    mensaje = "¡Error! Por favor, ingrese un tamaño mayor que 1\n";
                    JOptionPane.showMessageDialog(null, mensaje);
                }
            }
        });
        mainPanel.setLayout(gridLayout); // Panel utiliza un diseño de cuadrícula.

        // Solicitar valores de entrada como tamaño del tablero y cuántos jugadores hay.
        text1.setEditable(false); // Los textos de las preguntas son textos no editables.
        text2.setEditable(false);
        text3.setEditable(false);
        text4.setEditable(false);
        text5.setEditable(false);

        mainPanel.add(text1);
        mainPanel.add(sequentialRadioButton);
        mainPanel.add(forkJoinRadioButton);
        mainPanel.add(executorServiceRadioButton); // Agregar botones al JFrame

        radioGroup.add(sequentialRadioButton);
        radioGroup.add(forkJoinRadioButton); 
        radioGroup.add(executorServiceRadioButton); 

        // Estos funcionarán cuando se haga clic en los botones de radio.
        sequentialRadioButton.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent e) {
                if (e.getSource() == sequentialRadioButton)
                    sequentialMode = true;
            }
        });
        forkJoinRadioButton.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent e) {
                if (e.getSource() == forkJoinRadioButton)
                    sequentialMode = false;
            }
        });
        executorServiceRadioButton.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent e) {
                if (e.getSource() == executorServiceRadioButton)
                    sequentialMode = false;
            }
        });

        // Solo para mostrar 2 botones de radio en la misma fila; creando un panel temporal y un diseño de cuadro en el eje x.
        JPanel radiobuttonpanel = new JPanel();
        BoxLayout radiobuttonpanellayout = new BoxLayout(radiobuttonpanel, BoxLayout.X_AXIS);
        radiobuttonpanel.setLayout(radiobuttonpanellayout);
        radiobuttonpanel.add(sequentialRadioButton);
        radiobuttonpanel.add(forkJoinRadioButton);
        radiobuttonpanel.add(executorServiceRadioButton);
        mainPanel.add(radiobuttonpanel);

        mainPanel.add(text2);
        mainPanel.add(widthText);

        mainPanel.add(text3);
        mainPanel.add(heightText);

        mainPanel.add(text4);
        mainPanel.add(lengthText);
        mainPanel.add(text5);
        mainPanel.add(recursiveNumberText);

        mainPanel.add(setSizeButton);
        mainPanel.add(showBoardButton);

        showBoardButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent act) {
                if (widthSize <= 1 || heightSize <= 1) {
                    JOptionPane.showMessageDialog(null, "¡Error! Por favor, ingrese un tamaño de tablero mayor que 1\n");
                    return;
                }

                Board board = new Board(sequentialMode, length, recursiveNumber);

                board.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                board.setSize(widthSize, heightSize); // establece el tamaño del marco
                board.setLocationRelativeTo(null); // colocando la ubicación de la ventana en el centro.
                board.setBackground(new Color(251, 195, 134));
                dispose(); // Cerrar la ventana principal y abrir la ventana del tablero
                board.setVisible(true);
            }
        });
        


        add(mainPanel); // Agregando panel al marco
    }
} // Fin de la clase
