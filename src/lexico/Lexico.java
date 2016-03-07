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

    private void esPalabraReservada(char c) {

    }

    /*
    public void ejemplo(){
        Pila pila = new Pila();
        int fila, columna, accion;
        boolean aceptacion = false;
        Lexico lexico = new Lexico("a");

        //Iniciacion de la pila
        pila.push(Lexico.TipoSimbolo.PESOS);
        pila.push(0);
        lexico.sigSimbolo();

        //El primer paso del analisis es encontrar la accion a realizar eso se consigue con:
        fila = pila.top();
        columna = lexico.tipo;
        accion = lexico.tablaLR[fila][columna];

        //Finalmente mostramos el contenido de la pila, la entrada actual y la accion que se va a realizar:
        pila.muestra();
        System.out.println("Entrada: " + lexico.simbolo);
        System.out.println("Accion: " + accion);

        pila.push(lexico.tipo);
        pila.push(accion);

        lexico.sigSimbolo();

        fila = pila.top();

        columna = lexico.tipo;
        //System.out.println("CUAJO: " + fila);
        accion = lexico.tablaLR[fila][columna];

        pila.muestra();

        System.out.println("Entrada: " + lexico.simbolo);
        System.out.println("Accion: " + accion);


        Ahora, Obtenemos una reduccion porque el entero es -2, lo que
        nos indica que es la reduccion 1, como la regla 1 es: E -> <id>

        //tenemos que sacar 2 elementos de la pila
        pila.pop();
        pila.pop();

        fila = pila.top();
        columna = 2; //el no terminal que representa a E
        accion = lexico.tablaLR[fila][columna];

        //transicion
        pila.push(2); //el 2 es el entero que representa el no terminal E
        pila.push(accion);
        pila.muestra();

        System.out.println("Entrada: " + lexico.simbolo);
        System.out.println("Accion: " + accion);

        fila = pila.top();
        columna = lexico.tipo;
        accion = lexico.tablaLR[fila][columna];

        pila.muestra();
        System.out.println("Entrada: " + lexico.simbolo);
        System.out.println("Accion: " + accion);
        aceptacion = accion == -1;
        if(aceptacion)
            System.out.println("Aceptacion");
    }

    public void ejercicioUno()
    {
        //variables locales
        Pila pila = new Pila();
        int fila, columna, accion;
        boolean aceptacion = false;
        Lexico lexico = new Lexico("a+b");

        //iniciacion de la pila
        pila.push(TipoSimbolo.PESOS);
        pila.push(0);
        lexico.sigSimbolo();

        fila = pila.top();
        columna = lexico.tipo;
        accion = lexico.tablaLRUno[fila][columna];

        pila.muestra();
        System.out.println("Entrada: " + lexico.simbolo);
        System.out.println("Accion: " + accion);
        //******Primera pasada

        pila.push(lexico.tipo);
        pila.push(accion);
        lexico.sigSimbolo();

        fila = pila.top();
        columna = lexico.tipo;
        accion = lexico.tablaLRUno[fila][columna];

        pila.muestra();
        System.out.println("Entrada: " + lexico.simbolo);
        System.out.println("Accion: " + accion);
        //******Segunda pasada

        pila.push(lexico.tipo);
        pila.push(accion);
        lexico.sigSimbolo();

        fila = pila.top();
        columna = lexico.tipo;
        accion = lexico.tablaLRUno[fila][columna];

        pila.muestra();
        System.out.println("Entrada: " + lexico.simbolo);
        System.out.println("Accion: " + accion);
        //****Tercera pasada

        pila.push(lexico.tipo);
        pila.push(accion);
        lexico.sigSimbolo();

        fila = pila.top();
        columna = lexico.tipo;
        accion = lexico.tablaLRUno[fila][columna];

        pila.muestra();
        System.out.println("Entrada: " + lexico.simbolo);
        System.out.println("Accion: " + accion);
        //***cuarta pasada

        //como la accion es una reduccion tenemos que sacar seis elementos de la pila
        pila.pop();
        pila.pop();
        pila.pop();
        pila.pop();
        pila.pop();
        pila.pop();

        fila = pila.top();
        columna = 3; //el no terminal que representa a E
        accion = lexico.tablaLRUno[fila][columna];

        //transicion
        pila.push(3); //el 2 es el entero que representa el no terminal E
        pila.push(accion);

        pila.muestra();
        System.out.println("Entrada: " + lexico.simbolo);
        System.out.println("Accion: " + accion);
        //*****Quinta pasada

        fila = pila.top();
        columna = lexico.tipo;
        accion = lexico.tablaLRUno[fila][columna];

        pila.muestra();
        System.out.println("Entrada: " + lexico.simbolo);
        System.out.println("Accion: " + accion);

        aceptacion = accion == -1;
        if(aceptacion)
            System.out.println("Aceptacion");

    }//fin del metodo ejercicioUno

    public void ejercicioDos()
    {
        //variables locales
        Pila pila = new Pila();
        int fila, columna, accion = 0;
        boolean aceptacion = false;
        Lexico lexico = new Lexico("a+b+c+d+e+f+");

        //iniciacion de la pila
        pila.push(TipoSimbolo.PESOS);
        pila.push(0);

        while(true)
        {
            lexico.sigSimbolo();

            //siguiente accion
            fila = pila.top();
            columna = lexico.tipo;
            accion = lexico.tablaLRDos[fila][columna];

            //mostrar salida
            pila.muestra();
            System.out.println("Entrada: " + lexico.simbolo);
            System.out.println("Accion: " + accion);

            if(accion > 0) //desplazamiento
            {

                pila.push(lexico.tipo);
                pila.push(accion);

            }
            else if(accion < 0) //reduccion
            {
                if(accion == -1)
                {
                    fila = pila.top();
                    columna = lexico.tipo;
                    accion = lexico.tablaLRDos[fila][columna];
                    System.out.println("Aceptacion");
                    break;
                }

                int posicionReduccion = (accion * -1) - 2;
                //System.out.println("Magia: " + posicionReduccion);

                for(int i = 0; i < lonReglas[posicionReduccion] * 2; i++){
                    pila.pop();
                }

                fila = pila.top();
                columna = idReglas[posicionReduccion];
                accion = tablaLRDos[fila][columna];
                pila.push(idReglas[posicionReduccion]);
                pila.push(accion);


            }
            if(accion == 0)
                return;
        }//fin de while


    }//fin del metodo ejercicioDos
    */


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
