package org.example.screen;

import org.example.Connection;
import org.example.screen.components.Buttons;
import org.example.screen.components.PanelResult;
import org.example.screen.components.TextField;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.datatransfer.StringSelection;

/** Ventana principal de la calculadora. */
public class Window extends JFrame {

    private static final String[][] TECLAS = {
            {"C", "⌫", "(", ")"},
            {"7", "8", "9", "÷"},
            {"4", "5", "6", "×"},
            {"1", "2", "3", "-"},
            {"0", ".", "=", "+"}
    };

    private final TextField txtEntrada = new TextField("Ingresá una operación");
    private final PanelResult panelResultado = new PanelResult();
    private final JLabel lblEstado = new JLabel(" ");
    private boolean resultadoMostrado;

    public Window(Connection motor) {
        super("Calculadora básica");

        txtEntrada.addActionListener(e -> calcular(motor));
        txtEntrada.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                resultadoMostrado = false;
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                resultadoMostrado = false;
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                resultadoMostrado = false;
            }
        });

        JPanel contenido = new JPanel();
        contenido.setLayout(new BoxLayout(contenido, BoxLayout.Y_AXIS));
        contenido.setOpaque(false);
        agregar(contenido, crearEncabezado(), 18);
        agregar(contenido, txtEntrada, 12);
        agregar(contenido, crearTeclado(motor), 14);
        agregar(contenido, panelResultado, 12);
        agregar(contenido, crearPie(), 0);

        JPanel raiz = new JPanel(new BorderLayout());
        raiz.setBackground(Theme.BG);
        raiz.setBorder(new EmptyBorder(22, 24, 20, 24));
        raiz.add(contenido, BorderLayout.CENTER);

        setContentPane(raiz);
        setMinimumSize(new Dimension(390, 570));
        pack();
        setResizable(false);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
    }

    private JPanel crearEncabezado() {
        JLabel titulo = new JLabel("Calculadora básica");
        titulo.setFont(Theme.sans(Font.BOLD, 24));
        titulo.setForeground(Theme.TEXT);

        JLabel subtitulo = new JLabel("Suma, resta, multiplica y divide");
        subtitulo.setFont(Theme.sans(Font.PLAIN, 13));
        subtitulo.setForeground(Theme.MUTED);

        JPanel p = new JPanel();
        p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
        p.setOpaque(false);
        p.add(titulo);
        p.add(Box.createVerticalStrut(4));
        p.add(subtitulo);
        return p;
    }

    private JPanel crearTeclado(Connection motor) {
        JPanel teclado = new JPanel(new GridLayout(TECLAS.length, TECLAS[0].length, 8, 8));
        teclado.setOpaque(false);

        for (String[] fila : TECLAS) {
            for (String tecla : fila) {
                Color color = tecla.equals("=") ? Theme.BLUE
                        : "+-×÷".contains(tecla) ? Theme.GREEN : Theme.CARD;
                Color colorTexto = color.equals(Theme.CARD) ? Theme.TEXT : Theme.BG;
                Buttons boton = new Buttons(tecla, color, colorTexto, 8, 8, 18);
                boton.setPreferredSize(new Dimension(70, 48));
                boton.addActionListener(e -> pulsarTecla(motor, tecla));
                teclado.add(boton);
            }
        }
        return teclado;
    }

    private JPanel crearPie() {
        lblEstado.setFont(Theme.sans(Font.PLAIN, 12));
        lblEstado.setForeground(Theme.MUTED);

        Buttons btnCopiar = new Buttons("Copiar", Theme.CARD, Theme.TEXT, 12, 6, 12);
        btnCopiar.addActionListener(e -> copiarResultado());

        JPanel p = new JPanel(new BorderLayout());
        p.setOpaque(false);
        p.add(lblEstado, BorderLayout.WEST);
        p.add(btnCopiar, BorderLayout.EAST);
        return p;
    }

    private void pulsarTecla(Connection motor, String tecla) {
        switch (tecla) {
            case "=" -> calcular(motor);
            case "C" -> limpiar();
            case "⌫" -> borrarUltimo();
            default -> agregarEntrada(tecla);
        }
    }

    private void agregarEntrada(String tecla) {
        if (resultadoMostrado && (Character.isDigit(tecla.charAt(0))
                || tecla.equals(".") || tecla.equals("("))) {
            txtEntrada.setText("");
        }
        resultadoMostrado = false;
        String valor = switch (tecla) {
            case "×" -> "*";
            case "÷" -> "/";
            default -> tecla;
        };
        txtEntrada.setText(txtEntrada.getText() + valor);
        txtEntrada.requestFocusInWindow();
    }

    private void calcular(Connection motor) {
        String entrada = txtEntrada.getText().trim();
        if (entrada.isEmpty()) {
            mostrar("", "Ingresá una operación primero.", Theme.AMBER);
            return;
        }

        try {
            String resultado = motor.calcular(entrada);
            panelResultado.setTitulo("Resultado");
            mostrar(resultado, "Listo", Theme.GREEN);
            txtEntrada.setText(resultado);
            resultadoMostrado = true;
        } catch (Exception ex) {
            mostrar("", "No se pudo calcular: " + ex.getMessage(), Theme.RED);
        }
    }

    private void limpiar() {
        txtEntrada.setText("");
        panelResultado.setTexto("");
        panelResultado.setTitulo("Resultado");
        mostrar("", " ", Theme.MUTED);
        resultadoMostrado = false;
        txtEntrada.requestFocusInWindow();
    }

    private void borrarUltimo() {
        String entrada = txtEntrada.getText();
        if (!entrada.isEmpty()) {
            txtEntrada.setText(entrada.substring(0, entrada.length() - 1));
        }
        resultadoMostrado = false;
        txtEntrada.requestFocusInWindow();
    }

    private void mostrar(String resultado, String estado, Color colorEstado) {
        panelResultado.setTexto(resultado);
        lblEstado.setText(estado);
        lblEstado.setForeground(colorEstado);
    }

    private void copiarResultado() {
        String texto = panelResultado.getTexto();
        if (texto.isEmpty()) {
            return;
        }
        Toolkit.getDefaultToolkit().getSystemClipboard()
                .setContents(new StringSelection(texto), null);
        lblEstado.setText("Copiado al portapapeles");
        lblEstado.setForeground(Theme.BLUE);
    }

    private static void agregar(JPanel panel, JComponent componente, int espacioDespues) {
        componente.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.add(componente);
        if (espacioDespues > 0) {
            panel.add(Box.createVerticalStrut(espacioDespues));
        }
    }
}
