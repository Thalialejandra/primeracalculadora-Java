import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class CalculadoraUI extends JFrame {

    private final JTextField pantalla;
    private final JLabel expresionLabel;
    private final CalculadoraLogica logica;

    private String numeroActual = "";
    private double num1 = 0;
    private String operador = "";
    private boolean mostrandoResultado = false;

    public CalculadoraUI() {
        logica = new CalculadoraLogica();

        setTitle("Calculadora");
        setSize(380, 560);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        JPanel fondo = new JPanel(new BorderLayout(12, 12));
        fondo.setBackground(new Color(24, 28, 36));
        fondo.setBorder(new EmptyBorder(18, 18, 18, 18));
        setContentPane(fondo);

        JPanel panelSuperior = new JPanel(new BorderLayout(5, 8));
        panelSuperior.setOpaque(false);

        expresionLabel = new JLabel(" ");
        expresionLabel.setHorizontalAlignment(SwingConstants.RIGHT);
        expresionLabel.setForeground(new Color(170, 180, 200));
        expresionLabel.setFont(new Font("SansSerif", Font.PLAIN, 18));

        pantalla = new JTextField("0");
        pantalla.setHorizontalAlignment(SwingConstants.RIGHT);
        pantalla.setEditable(false);
        pantalla.setBackground(new Color(37, 43, 54));
        pantalla.setForeground(Color.WHITE);
        pantalla.setCaretColor(Color.WHITE);
        pantalla.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(55, 63, 79), 1, true),
                new EmptyBorder(14, 14, 14, 14)
        ));
        pantalla.setFont(new Font("SansSerif", Font.BOLD, 34));
        pantalla.setPreferredSize(new Dimension(0, 90));

        panelSuperior.add(expresionLabel, BorderLayout.NORTH);
        panelSuperior.add(pantalla, BorderLayout.CENTER);

        fondo.add(panelSuperior, BorderLayout.NORTH);

        JPanel panelBotones = new JPanel(new GridBagLayout());
        panelBotones.setOpaque(false);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.BOTH;

        agregarBoton(panelBotones, "C", gbc, 0, 0, 1);
        agregarBoton(panelBotones, "←", gbc, 1, 0, 1);
        agregarBoton(panelBotones, "/", gbc, 2, 0, 1);
        agregarBoton(panelBotones, "*", gbc, 3, 0, 1);
        agregarBoton(panelBotones, "7", gbc, 0, 1, 1);
        agregarBoton(panelBotones, "8", gbc, 1, 1, 1);
        agregarBoton(panelBotones, "9", gbc, 2, 1, 1);
        agregarBoton(panelBotones, "-", gbc, 3, 1, 1);
        agregarBoton(panelBotones, "4", gbc, 0, 2, 1);
        agregarBoton(panelBotones, "5", gbc, 1, 2, 1);
        agregarBoton(panelBotones, "6", gbc, 2, 2, 1);
        agregarBoton(panelBotones, "+", gbc, 3, 2, 1);
        agregarBoton(panelBotones, "1", gbc, 0, 3, 1);
        agregarBoton(panelBotones, "2", gbc, 1, 3, 1);
        agregarBoton(panelBotones, "3", gbc, 2, 3, 1);
        agregarBoton(panelBotones, "=", gbc, 3, 3, 1);
        agregarBoton(panelBotones, "0", gbc, 0, 4, 2);
        agregarBoton(panelBotones, ".", gbc, 2, 4, 1);

        fondo.add(panelBotones, BorderLayout.CENTER);

        setVisible(true);
    }

    private void agregarBoton(JPanel panel, String texto, GridBagConstraints gbc, int x, int y, int width) {
        gbc.gridx = x;
        gbc.gridy = y;
        gbc.gridwidth = width;
        gbc.weightx = 1;
        gbc.weighty = 1;

        JButton boton = crearBoton(texto);
        boton.addActionListener(e -> manejarEntrada(texto));

        panel.add(boton, gbc);
    }

    private JButton crearBoton(String texto) {
        JButton boton = new JButton(texto);
        boton.setFocusPainted(false);
        boton.setFont(new Font("SansSerif", Font.BOLD, 22));
        boton.setPreferredSize(new Dimension(70, 70));
        boton.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        boton.setCursor(new Cursor(Cursor.HAND_CURSOR));

        if ("C".equals(texto)) {
            boton.setBackground(new Color(220, 80, 80));
            boton.setForeground(Color.WHITE);
        } else if ("=".equals(texto)) {
            boton.setBackground(new Color(70, 130, 255));
            boton.setForeground(Color.WHITE);
        } else if (esOperador(texto) || "←".equals(texto)) {
            boton.setBackground(new Color(255, 170, 60));
            boton.setForeground(new Color(30, 30, 30));
        } else {
            boton.setBackground(new Color(52, 60, 74));
            boton.setForeground(Color.WHITE);
        }

        return boton;
    }

    private void manejarEntrada(String entrada) {
        if (esNumero(entrada)) {
            ingresarNumero(entrada);
        } else if (".".equals(entrada)) {
            agregarDecimal();
        } else if (esOperador(entrada)) {
            seleccionarOperador(entrada);
        } else if ("=".equals(entrada)) {
            calcularResultado();
        } else if ("C".equals(entrada)) {
            limpiarTodo();
        } else if ("←".equals(entrada)) {
            borrarUltimo();
        }
    }

    private void ingresarNumero(String numero) {
        if (mostrandoResultado && operador.isEmpty()) {
            numeroActual = "";
            mostrandoResultado = false;
            expresionLabel.setText(" ");
        }

        if ("0".equals(numeroActual)) {
            numeroActual = numero;
        } else {
            numeroActual += numero;
        }

        actualizarPantalla();
    }

    private void agregarDecimal() {
        if (mostrandoResultado && operador.isEmpty()) {
            numeroActual = "";
            mostrandoResultado = false;
            expresionLabel.setText(" ");
        }

        if (numeroActual.isEmpty()) {
            numeroActual = "0.";
        } else if (!numeroActual.contains(".")) {
            numeroActual += ".";
        }

        actualizarPantalla();
    }

    private void seleccionarOperador(String op) {
        if (numeroActual.isEmpty()) {
            if (!operador.isEmpty()) {
                operador = op;
                expresionLabel.setText(formatearNumero(num1) + " " + operador);
            }
            return;
        }

        if (!operador.isEmpty() && !mostrandoResultado) {
            calcularResultado();
        }

        num1 = Double.parseDouble(numeroActual);
        operador = op;
        mostrandoResultado = true;
        expresionLabel.setText(formatearNumero(num1) + " " + operador);
    }

    private void calcularResultado() {
        if (operador.isEmpty() || numeroActual.isEmpty()) {
            return;
        }

        try {
            double num2 = Double.parseDouble(numeroActual);
            double resultado = logica.calcular(num1, num2, operador);

            expresionLabel.setText(
                    formatearNumero(num1) + " " + operador + " " + formatearNumero(num2) + " ="
            );

            numeroActual = formatearNumero(resultado);
            pantalla.setText(numeroActual);

            operador = "";
            mostrandoResultado = true;

        } catch (ArithmeticException ex) {
            pantalla.setText("Error");
            expresionLabel.setText("No se puede dividir por cero");
            numeroActual = "";
            operador = "";
            mostrandoResultado = true;
        }
    }

    private void limpiarTodo() {
        numeroActual = "";
        num1 = 0;
        operador = "";
        mostrandoResultado = false;
        expresionLabel.setText(" ");
        pantalla.setText("0");
    }

    private void borrarUltimo() {
        if (mostrandoResultado && operador.isEmpty()) {
            limpiarTodo();
            return;
        }

        if (!numeroActual.isEmpty()) {
            numeroActual = numeroActual.substring(0, numeroActual.length() - 1);
        }

        actualizarPantalla();
    }

    private void actualizarPantalla() {
        if (numeroActual.isEmpty()) {
            pantalla.setText("0");
        } else {
            pantalla.setText(numeroActual);
        }
    }

    private boolean esNumero(String texto) {
        return texto.matches("\\d");
    }

    private boolean esOperador(String texto) {
        return "+-*/".contains(texto);
    }

    private String formatearNumero(double numero) {
        if (numero == (long) numero) {
            return String.valueOf((long) numero);
        }
        return String.valueOf(numero);
    }
}