/**
 * Tarea3
 *
 * Applet en el que se implementa un juego en el cual se debe dar clic a un 
 * objeto de tipo AnimalCosa (una canasta) y arrastrarlo para chocar con 
 * otros objetos de tipo AnimalCosa (pelotas de basquetbol).
 * Las pelotas aparecen al azar cayendo desde la parte superior del applet.
 * 
 * Para poder destruir a las pelotas es necesario golpearlas por abajo y de
 * forma completa: que el ancho de la canasta abarque todo el ancho de la pelota
 * El juego tiene 5 vidas, cada 10 peloas que caen al suelo eliminan una vida.
 * Cada vida que se elimina del juego, las pelotas avanzan más rápido.
 * Cada pelota que es eliminada da 100 puntos.
 * Cada pelota que llega abajo del applet quita 20 puntos.
 * 
 * NOTA: se recomienda desplazar la canasta a una velocidad moderada
 * para dar tiempo suficiente al applet para detectar la colision con 
 * una pelota. Este detalle ocurre a raiz de la validacion estrticta del choque
 * entre canasta y pelota.
 * 
 * @author Mario Sergio Fuentes Juárez
 * @version 1.0 2015/2/4
 */
 
import java.applet.Applet;
import java.applet.AudioClip;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.net.URL;
import java.util.LinkedList;

public class JuegoApplet extends Applet implements Runnable, 
                                    MouseListener, MouseMotionListener {
       
    private boolean bClic; // booleano para indicar un clic
    /**
     * diferencia de distancia en el eje X entre la coordenada de la canasta
     * y la coordenada del clic del mouse
     */
    private int iDiferenciaY;
    /**
     * diferencia de distancia en el eje X entre la coordenada de la canasta
     * y la coordenada del clic del mouse
     */
    private int iDiferenciaX; 
    private int iMouseX; // posicion en X del clic del mouse
    private int iMouseY; // posicion en Y del clic del mouse
    private int iPuntos; // puntos ganados
    private int iPelotasPerdidas; // pelotas que han caido fuera del applet
    private int iVel; // velocidad de caida de las pelotas
    private int iVidasPerdidas; // contador de vidas perdidas
    // arreglo que indica si hay contacto entre una pelota y la canasta
    private boolean [] boolArrContacto; 
    // constante con total de vidas del jugador
    final private int iTOTAL_VIDAS = 5; 
    private int iPosX; // guardo un valor de coordenada x
    private int iPosY; // guardo un valor de coordenada y
    private int iPosYAnt; // guardo un valor de coordenada y, del step previo
    private AudioClip aucSonidoPared;// Objeto AudioClip de golpe con pared
    private AudioClip aucSonidoChoque; // Objeto AudioClip de choque con canasta
    private AnimalCosa aniCanasta; // Objeto de canasta manipulado por jugador         
    private Image imaImagenApplet;   // Imagen a proyectar en Applet
    private Image imaImagenGameOver; // imagen de juego terminado
    private Graphics graGraficaApplet;  // Objeto grafico de la Imagen
    private LinkedList<AnimalCosa> lklPelotas; // lista encadenada con pelotas
	
    /** 
     * init
     * 
     * Metodo sobrescrito de la clase <code>Applet</code>.<P>
     * En este metodo se inizializan las variables o se crean los objetos
     * a usarse en el <code>Applet</code> y se definen funcionalidades.
     * 
     */
    public void init() {
        // se define el applet con tamaño 500,500
        setSize(600, 400);
        
        // inicializa booleano de clic del mouse
        bClic = false;
        
        // se declara arreglo de contacto
        boolArrContacto = new boolean [25];
        
        // inicializacion de estadisticas de juego
        iPuntos = 0;
        iPelotasPerdidas = 0;
        iVel = 1;
        /*
         * se inicializa la cantidad de vidas perdidas en 0
         * (no se ha perdido ninguna)
        */
        iVidasPerdidas = 0;
        
        // inicializacion de lista de pelotas
        URL urlImagenPelota = this.getClass().getResource("pelota.png");
        lklPelotas = new LinkedList();
        int iNumeroPelotas = 25; // total de pelotas en el juego
        // genero cada pelota y la meto a la lista
        for(int iI = 0; iI < iNumeroPelotas; iI ++){
            AnimalCosa aniPelota = new AnimalCosa(0, 0,
                    Toolkit.getDefaultToolkit().getImage(urlImagenPelota));
            iPosX = (int) (Math.random() * 
                    (getWidth() - aniPelota.getAncho()) );    
            iPosY = (int) (Math.random() * 
                    (3 * getHeight() - aniPelota.getAlto()) ) - 3 * getHeight();    
            aniPelota.setX(iPosX);
            aniPelota.setY(iPosY);
            lklPelotas.add(aniPelota);
            // se actualiza arreglo de contacto
            boolArrContacto[iI] = false; 
        }
        
        // se inicializa las coordenadas del mouse con valores arbitrarios
        iMouseX = 0;
        iMouseY = 0;
                      
        // se crea el objeto canasta 
        URL urlImagenCanasta = this.getClass().getResource("canasta.png");
	aniCanasta = new AnimalCosa(0,0,
                Toolkit.getDefaultToolkit().getImage(urlImagenCanasta));
        
        // se posiciona la canasta en alguna parte al azar del applet
	iPosX = (int) (Math.random() * 
                (getWidth() - aniCanasta.getAncho()));    
        iPosY = (int) (Math.random() * 
                (getHeight() - aniCanasta.getAlto()));   
        // inicialmente la posicion en el step previo es igual
        // a la posicion en el step actual
	iPosYAnt = iPosY; 
        aniCanasta.setX(iPosX);
        aniCanasta.setY(iPosY);
               
        // se crea el sonido de choque con pared
	URL urlSonidoPared = this.getClass().getResource("sonidoPared.wav");
        aucSonidoPared = getAudioClip (urlSonidoPared);
        // se crea el sonido de choque con canasta
	URL urlSonidoChoque = this.getClass().getResource("sonidoCanasta.wav");
        aucSonidoChoque = getAudioClip (urlSonidoChoque);
        
        // se crea la imagen de juego terminado
        URL urlImagenGameOver = this.getClass().getResource("gameOver.jpg");
        imaImagenGameOver = 
                Toolkit.getDefaultToolkit().getImage(urlImagenGameOver);

        // se define el background en color naranja
	setBackground(Color.orange);
        /* se le añade la opcion al applet de ser escuchado por los eventos
           del mouse  */
	addMouseListener(this);
        /* se le añade la opcion al applet de ser escuchado por los eventos
           del movimento del mouse  */
	addMouseMotionListener(this);
    }
	
    /** 
     * start
     * 
     * Metodo sobrescrito de la clase <code>Applet</code>.<P>
     * En este metodo se crea e inicializa el hilo
     * para la animacion este metodo es llamado despues del init o 
     * cuando el usuario visita otra pagina y luego regresa a la pagina
     * en donde esta este <code>Applet</code>
     * 
     */
    public void start() {
        // Declaras un hilo
        Thread th = new Thread(this);
        // Empieza el hilo
        th.start();
    }
	
    /** 
     * run
     * 
     * Metodo sobrescrito de la clase <code>Thread</code>.<P>
     * En este metodo se ejecuta el hilo, que contendrá las instrucciones
     * de nuestro juego.
     * 
     */
    public void run() {
        /* mientras dure el juego (es decir, mientras se hayan perdido menos
           de 5 vidas), se actualizan posiciones de objetos
           se checa si hubo colisiones para corregir movimientos de objetos
           y se vuelve a pintar todo
        */ 
        while (iVidasPerdidas < iTOTAL_VIDAS) {
            // El parametro booleano indica si solamente se debe actualizar a 
            // la canasta. Dado que se deben actualizar tanto canastas como
            // pelotas, toma valor falso
            actualiza(false); 
            checaColision();
            repaint();
            try	{
                // El thread se duerme.
                Thread.sleep(30);
            }
            catch (InterruptedException iexError) {
                System.out.println("Hubo un error en el juego " + 
                        iexError.toString());
            }
	}
      
    }
	
    /** 
     * actualiza
     * 
     * Metodo que actualiza la posicion de las pelotas y la canasta 
     * @param soloCanasta es un <code>boolean</code> que indica si
     * solamente se debe actualizar la posicion de la canasta.
     */
    public void actualiza(boolean soloCanasta) {
        
        // si la bandera de clic esta encendida, las coordenadas de
        // la canasta deben acualizarse a partir del mouse y 
        // la diferencia de coordenadas entre el mouse y la imagen
        if (bClic) {
            aniCanasta.setX(iPosX);
            aniCanasta.setY(iPosY);
        } else {
            // si no hubo clic, la posicion permanecio igual
            // con respecto al step anterior
            iPosYAnt = iPosY;
        }
        // Si tambien se va a actualizar la posicion de las pelotas
        if (!soloCanasta) {
            for (AnimalCosa aniPelota: lklPelotas) {
                //actualizo posicion de pelotas 
                aniPelota.setY(aniPelota.getY() + iVel);

                // si la pelota se sale del applet
                if (aniPelota.getY() > getHeight()) {
                    // se le asigna una posicion random en la parte superior
                    // del applet
                    iPosX = (int) (Math.random() * 
                            (getWidth() - aniPelota.getAncho()));    
                    iPosY = (int) (Math.random() * (3 * getHeight() - 
                            aniPelota.getAlto()) ) - 3 * getHeight();    
                    aniPelota.setX(iPosX);
                    aniPelota.setY(iPosY);
                    // se actualiza puntaje y numero de pelotas perdidas
                    iPuntos = iPuntos - 20;
                    iPelotasPerdidas ++;
                    // se ejecuta sonido de choque con pared
                    aucSonidoPared.play();
                }

            }
        }
        // Se actualiza numero de vidas perdidas
        iVidasPerdidas = iPelotasPerdidas / 10;
        // La velocidad se actualiza a partir del numero de vidas perdidas
        iVel = iVidasPerdidas + 1;
        
    }
	
    /**
     * checaColision
     * 
     * Metodo usado para checar la colision del objeto canasta con las pelotas
     * o con las orillas del <code>Applet</code>.
     * 
     */
    public void checaColision() {
        
        int iI = 0; // contador para navegar el arreglo de booleanos de contacto
        for (AnimalCosa aniPelota: lklPelotas) {
            
            // si se identifica colision inferior entre pelota y canasta
            // y la canasta se esta moviendo hacia arriba o permanece inmovil
            // y se verifica que no hay contacto previo (arreglo de booleanos)
            if (aniCanasta.intersectaPorAbajoCompleto(aniPelota) 
                    && iPosYAnt >= iPosY && !boolArrContacto[iI]) {
               
                // se actualiza la pelota en una posicion random
                iPosX = (int) (Math.random() * (getWidth() 
                        - aniPelota.getAncho()));    
                iPosY = (int) (Math.random() * (3 * getHeight() 
                        - aniPelota.getAlto())) - 3 * getHeight();    
                aniPelota.setX(iPosX);
                aniPelota.setY(iPosY);
                // se incrementa puntaje
                iPuntos = iPuntos + 100;
                // se ejecuta sonido de choque ente canasta y pelota
                aucSonidoChoque.play();
                
                // se actualiza arreglo de contacto
            }
            else {
                boolArrContacto[iI] = aniPelota.intersecta(aniCanasta);
            } 
            
        }
        
    }
	
    /**
     * update
     * 
     * Metodo sobrescrito de la clase <code>Applet</code>,
     * heredado de la clase Container.<P>
     * En este metodo lo que hace es actualizar el contenedor y 
     * define cuando usar ahora el paint
     * 
     * @param graGrafico es el <code>objeto grafico</code> usado para dibujar.
     * 
     */
    public void update(Graphics graGrafico) {
        // Inicializan el DoubleBuffer
        if (imaImagenApplet == null) {
                imaImagenApplet = createImage (this.getSize().width, 
                        this.getSize().height);
                graGraficaApplet = imaImagenApplet.getGraphics ();
        }

        // Actualiza la imagen de fondo.
        graGraficaApplet.setColor(getBackground());
        graGraficaApplet.fillRect(0, 0, this.getSize().width, 
                this.getSize().height);

        // Actualiza el Foreground.
        graGraficaApplet.setColor(getForeground());
        paint(graGraficaApplet);

        // Dibuja la imagen actualizada
        graGrafico.drawImage(imaImagenApplet, 0, 0, this);
    }

    /**
     * paint
     * 
     * Metodo sobrescrito de la clase <code>Applet</code>,
     * heredado de la clase Container.<P>
     * En este metodo se dibuja la imagen con la posicion actualizada,
     * ademas que cuando la imagen es cargada te despliega una advertencia.
     * 
     * @param graDibujo es el objeto de <code>Graphics</code> usado para dibujar.
     * 
     */
    public void paint(Graphics graDibujo) {
        // si la imagen de canasta y la lista encadenada de pelotas se cargaron
        if (aniCanasta != null && lklPelotas != null) {
                // si no se ha rebasado el limite de vidas
            if (iTOTAL_VIDAS - iVidasPerdidas > 0) {
                // pinto la imagen de la canasta
                aniCanasta.paint(graDibujo, this);
                // pinto cada pelota de la lista
                for (AnimalCosa aniPelota : lklPelotas) {
                    aniPelota.paint(graDibujo, this);
                }
                // pinto estadisticas de juego
                graDibujo.drawString("Puntos: " +
                        String.valueOf(iPuntos),
                        getWidth() - 150, 10);
                graDibujo.drawString("Vidas: " +
                        String.valueOf(iTOTAL_VIDAS - iVidasPerdidas),
                        getWidth() - 150, 25); 
                graDibujo.drawString("Pelotas perdidas: " +
                        String.valueOf(iPelotasPerdidas),
                        getWidth() - 150, 40);
            }
            else {
                // pinto estadisticas de fin de juego
                graDibujo.drawString("Puntos: " +
                        String.valueOf(iPuntos),
                        250, 50);

                graDibujo.drawImage(imaImagenGameOver, 100, 100, this);
            }
                
        } // sino se ha cargado se dibuja un mensaje 
        else {
            //Da un mensaje mientras se carga el dibujo	
            graDibujo.drawString("No se cargo la imagen..", 20, 20);
        }
    }

    /**
     * mouseClicked
     * Metodo sobrescrito de la interface <code>MouseListener</code>.<P>
     * En este metodo se maneja el evento que se genera al hacer click 
     * derecho en el mouse.
     * 
     * @param MouseEvent es el <code>MouseEvent</code> que se genera al soltar.
     * 
     */
    public void mouseClicked(MouseEvent mseEvent) {
        // no hay codigo pero se debe escribir el metodo
    }
    
    /**
     * mousePressed
     * Metodo sobrescrito de la interface <code>MouseListener</code>.<P>
     * En este metodo se maneja el evento que se genera al mantener presionado
     * el botón de click izquierdo en el mouse.
     * 
     * @param MouseEvent es el <code>MouseEvent</code> que se genera al soltar.
     * 
     */
    public void mousePressed(MouseEvent mseEvent) {
        // Se checa si el click se hizo dentro del área de la canasta
        if (aniCanasta.estaAdentro(mseEvent.getX(), mseEvent.getY())) {
            // Se actualiza la variable que almacena la diferencia
            // de distancia entre la coordenada de click y la coordenada
            // de la esquina superior izquierda del objeto canasta
            
            // coordenadas del clic del mouse
            iMouseX = mseEvent.getX();
            iMouseY = mseEvent.getY();
            // se calcula diferencia de distancia entre coordenada de clic
            // y coordenada de la imagen
            iDiferenciaX = iMouseX - aniCanasta.getX();
            iDiferenciaY = iMouseY - aniCanasta.getY();
            // la posicion del objeto canasta se mantiene igual
            iPosX = aniCanasta.getX();
            iPosY = aniCanasta.getY();
            bClic = true;
        }
    }

    /**
     * mouseReleased
     * Metodo sobrescrito de la interface <code>MouseListener</code>.<P>
     * En este metodo se maneja el evento que se genera al liberar
     * el botón de click izquierdo en el mouse.
     * 
     * @param MouseEvent es el <code>MouseEvent</code> que se genera al soltar.
     * 
     */
    public void mouseReleased(MouseEvent mseEvent) {
        // Cuando el click del mouse se libera, la bandera de clic
        // se limpia
        bClic = false;
        
    }

    /**
     * mouseEntered
     * Metodo sobrescrito de la interface <code>MouseListener</code>.<P>
     * En este metodo se maneja el evento que se genera al entrar el cursor
     * en la ventana del applet.
     * 
     * @param MouseEvent es el <code>MouseEvent</code> que se genera al soltar.
     * 
     */
    public void mouseEntered(MouseEvent mseEvent) {
        // no hay codigo pero se debe escribir el metodo
    }

    /**
     * mouseExited
     * Metodo sobrescrito de la interface <code>MouseListener</code>.<P>
     * En este metodo se maneja el evento que se genera al salir el cursor
     * de la ventana del applet.
     * 
     * @param MouseEvent es el <code>MouseEvent</code> que se genera al soltar.
     * 
     */
    public void mouseExited(MouseEvent mseEvent) {
        // no hay codigo pero se debe escribir el metodo
    }

    /**
     * mouseDragged
     * Metodo sobrescrito de la interface <code>MouseMotionListener</code>.<P>
     * En este metodo se maneja el evento que se genera arrastrar el cursor
     * manteniendo presionado el botón de click izquierdo.
     * 
     * @param MouseEvent es el <code>MouseEvent</code> que se genera al soltar.
     * 
     */
    public void mouseDragged(MouseEvent mseEvent) {
        // Se verifica que la bandera de click está liberada.
        // En caso de estarlo, esto significa que el click se hizo dentro
        // del área del objeto canasta
        if (bClic) {
            
            iMouseX = mseEvent.getX(); // Se captura la posición en X del cursor
            iMouseY = mseEvent.getY(); // Se captura la posición en Y del cursor
            // Si la posición del mouse causa que la imagen se desborde
            // en Y negativo
            if (iMouseY - iDiferenciaY < 0) {
                // Se actualiza la posición dando un margen igual a la distancia
                // entre la coordenada del mouse y la esquina superior izquierda
                // del objeto
                iMouseY = iDiferenciaY; 
            } 
            // Si la posición del mouse causa que la imagen se desborde
            // en Y positivo
            else if (iMouseY - iDiferenciaY + aniCanasta.getAlto()
                    > getHeight()) {
                // Se actualiza la posición dando un margen igual a la distancia
                // entre la coordenada del mouse y la esquina superior izquierda
                // del objeto
                iMouseY = getHeight() - aniCanasta.getAlto() + iDiferenciaY;
            }
            // Si la posición del mouse causa que la imagen se desborde
            // en X negativo
            if (iMouseX - iDiferenciaX < 0) { 
                // Se actualiza la posición dando un margen igual a la distancia
                // entre la coordenada del mouse y la esquina superior izquierda
                // del objeto
                iMouseX = iDiferenciaX;
            } 
            // Si la posición del mouse causa que la imagen se desborde
            // en X positivo
            else if (iMouseX - iDiferenciaX + aniCanasta.getAncho()
                    > getWidth()) { 
                // Se actualiza la posición dando un margen igual a la distancia
                // entre la coordenada del mouse y la esquina superior izquierda
                // del objeto
                iMouseX = getWidth() - aniCanasta.getAncho() + iDiferenciaX;
            }
            // se calcula la nueva de la posicion de la imagen como 
            // la diferencia entre la coordenada del mouse y la distancia 
            // entre coordenada de clic y la esquina de la imagen
            iPosYAnt = iPosY;
            iPosX = iMouseX - iDiferenciaX;
            iPosY = iMouseY - iDiferenciaY;
        }
        // Se invocan actualizaciones (solamente de posicion de la canasta)
        // para mejorar el tiempo de respuesta de identificacion de colision
        actualiza(true);
        checaColision();
    }

    /**
     * mouseMoved
     * Metodo sobrescrito de la interface <code>MouseMotionListener</code>.<P>
     * En este metodo se maneja el evento que se genera al mover el cursor
     * en la ventana del applet.
     * 
     * @param MouseEvent es el <code>MouseEvent</code> que se genera al soltar.
     * 
     */
    public void mouseMoved(MouseEvent mseEvent) {
        // no hay codigo pero se debe escribir el metodo
    }
}