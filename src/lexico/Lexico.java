package lexico;

import java.util.Arrays;
import java.util.Stack;

public class Lexico {

    class TipoSimbolo {
        public static final int ERROR = -1;
        public static final int IDENTIFICADOR = 0;
        public static final int OPADIC = 1; //10
        //public static final int OPMULT = 2;
        public static final int PESOS = 2; //1
        public static final int ENTERO = 4; //4
    }//fin de la clase Simbolo

    //atributos
    private String fuente;
    private int ind;
    private boolean continua;
    private char c;
    private int estado;
    public String simbolo;
    public int tipo;
    private int[][] tabla =
    {
        {2, 0, 0, 1},
        {0, 0, -1, 0},
        {0, 3, -3, 0},
        {2, 0, 0, 4},
        {0, 0, -2, 0}
    };
    public int idReglas[] = {3, 3};
    public int lonReglas[] ={3, 1};
    public String strReglas[] = {"E", "E"};



    public Lexico(String fuente) {
        this.fuente = fuente;
        ind = 0;
    }

    public Lexico() {
        ind = 0;
    }

    public void entrada(String fuente) {
        ind = 0;
        this.fuente = fuente;
    }

    public String tipoAcad(int tipo) {

        String cad = "";

        switch (tipo) {
            case TipoSimbolo.IDENTIFICADOR:
                cad = "Identificador";
                break;

            /*case TipoSimbolo.OPADIC:
                cad = "Op. Adicion";
                break;

           case TipoSimbolo.OPMULT:
                cad= "Op. Multiplicacion";
                break;*/
            case TipoSimbolo.ENTERO:
                cad = "Entero";
                break;

            case TipoSimbolo.PESOS:
                cad = "Fin de la Entrada";
                break;


        }

        return cad;

    }

    public int sigSimbolo() {
        estado = 0;
        continua = true;
        simbolo = "";

        //Inicio del Automata
        while (continua) {
            c = sigCaracter();

            if (esDigito(c)) aceptacion(1);
            else if (c == '+' || c == '-') aceptacion(2);
            else if (esLetra(c)) aceptacion(3);
            else if (c == '$') aceptacion(4);
            break;


        }
        //Fin del Automata

        switch (estado) {

            case 1:
                tipo = TipoSimbolo.ENTERO;
                break;
            case 2:
                tipo = TipoSimbolo.OPADIC;
                break;

            case 3:
                tipo = TipoSimbolo.IDENTIFICADOR;
                break;

            case 4:
                tipo = TipoSimbolo.PESOS;
                break;

            default:
                tipo = TipoSimbolo.ERROR;
        }

        return tipo;
    }

    public boolean terminado() {
        return ind >= fuente.length();
    }

    private char sigCaracter() {
        if (terminado()) return '$';

        return fuente.charAt(ind++);
    }

    private void sigEstado(int estado) {
        this.estado = estado;
        simbolo += c;
    }

    private void aceptacion(int estado) {
        sigEstado(estado);
        continua = false;
    }

    private boolean esLetra(char c) {
        return Character.isLetter(c) || c == '_';
    }

    private boolean esDigito(char c) {
        return Character.isDigit(c);
    }

    private boolean esEspacio(char c) {
        return c == ' ' || c == '\t';
    }

    private boolean esSimboloFinalCadena(char c) {
        return c == ' ' || c == '+' || c == '-' || c == '$';
    }

    private void retroceso() {
        if (c != '$') ind--;
        continua = false;
    }

    public void ejercicioTres()
    {
        Stack<ElementoPila> pila = new Stack<>();
        int fila, columna, accion = 0;
        boolean aceptacion = false;
        Lexico lexico = new Lexico("a+b+c");

        pila.push(new Terminal(TipoSimbolo.PESOS, "$"));
        pila.push(new Estado(0));
        while (true)
        {
            lexico.sigSimbolo();

            fila = pila.peek().getId();
            columna = lexico.tipo;
            accion = tabla[fila][columna];

            ElementoPila[] elementos = new ElementoPila[pila.size()];
            String elementosEnPila = "";
            for(int i = 0; i < elementos.length; i++){
                elementos[i] = pila.get(i);
            }

            for(int i = elementos.length - 1; i >= 0; i--){
                elementosEnPila += elementos[i].getElemento();
            }

            System.out.println(elementosEnPila + " - " + lexico.simbolo + " - " + accion);


            if(accion > 0)
            {
                pila.push(new Terminal(lexico.tipo, lexico.simbolo));
                pila.push(new Estado(accion));
            }//fin de if
            else if(accion < 0)
            {
                if(accion == -1)
                {
                    fila = pila.peek().getId();
                    columna = lexico.tipo;
                    accion = tabla[fila][columna];
                    System.out.println("Aceptacion");
                    break;
                }//fin de if

                int posicionReduccion = (accion * -1) -2;

                for(int i = 0; i < lonReglas[posicionReduccion] * 2; i++){
                    pila.pop();
                }

                fila = pila.peek().getId();
                columna = idReglas[posicionReduccion];
                accion = tabla[fila][columna];
                pila.push(new NoTerminal(idReglas[posicionReduccion], strReglas[posicionReduccion]));
                pila.push(new Estado(accion));
            }//fin de else if

            if(accion == 0){
                return;
            }

        }//fin de while


    }//fin del metodo ejercicio



}//fin de la clase Lexico
