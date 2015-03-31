/**
 * AnimalCosa
 *
 * Modela la definición de todos los objetos de tipo
 * <code>AnimalCosa</code>
 *
 * @author Antonio Mejorado y Mario Sergio Fuentes
 * @version 2.1 2015/1/24
 */
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.image.ImageObserver;
import javax.swing.ImageIcon;

public class AnimalCosa {

    private int iX;     //posicion en x.       
    private int iY;     //posicion en y.
    private ImageIcon imiIcono;	//icono.

    /**
     * AnimalCosa
     * 
     * Metodo constructor usado para crear el objeto AnimalCosa
     * creando el icono a partir de una imagen
     * 
     * @param iX es la <code>posicion en x</code> del objeto.
     * @param iY es la <code>posicion en y</code> del objeto.
     * @param imaImagen es la <code>imagen</code> del objeto.
     * 
     */
    public AnimalCosa(int iX, int iY ,Image imaImagen) {
        this.iX = iX;
        this.iY = iY;
        imiIcono = new ImageIcon(imaImagen);
    }

    /**
     * AnimalCosa
     * 
     * Metodo constructor usado para crear el objeto AnimalCosa
     * creando el icono de imagen de un objeto igual
     * 
     * @param iX es la <code>posicion en x</code> del objeto.
     * @param iY es la <code>posicion en y</code> del objeto.
     * @param imiIcono es la <code>imagen tipo icono</code> del objeto.
     * 
     */
    public AnimalCosa(int iX, int iY ,ImageIcon imiIcono) {
        this.iX = iX;
        this.iY = iY;
        this.imiIcono = imiIcono;
    }
    
    /**
     * setX
     * 
     * Metodo modificador usado para cambiar la posicion en x del objeto
     * 
     * @param iX es la <code>posicion en x</code> del objeto.
     * 
     */
    public void setX(int iX) {
        this.iX = iX;
    }

    /**
     * getX
     * 
     * Metodo de acceso que regresa la posicion en x del objeto 
     * 
     * @return iX es la <code>posicion en x</code> del objeto.
     * 
     */
    public int getX() {
            return iX;
    }

    /**
     * setY
     * 
     * Metodo modificador usado para cambiar la posicion en y del objeto 
     * 
     * @param iY es la <code>posicion en y</code> del objeto.
     * 
     */
    public void setY(int iY) {
            this.iY = iY;
    }

    /**
     * getY
     * 
     * Metodo de acceso que regresa la posicion en y del objeto 
     * 
     * @return posY es la <code>posicion en y</code> del objeto.
     * 
     */
    public int getY() {
        return iY;
    }

    /**
     * setImageIcon
     * 
     * Metodo modificador usado para cambiar el icono del objeto
     * 
     * @param imiIcono es el <code>icono</code> del objeto.
     * 
     */
    public void setImageIcon(ImageIcon imiIcono) {
        this.imiIcono = imiIcono;
    }

    /**
     * getImageIcon
     * 
     * Metodo de acceso que regresa el icono del objeto 
     * 
     * @return imiIcono es el <code>icono</code> del objeto.
     * 
     */
    public ImageIcon getImageIcon() {
        return imiIcono;
    }

    /**
     * setImagen
     * 
     * Metodo modificador usado para cambiar el icono de imagen del objeto
     * tomandolo de un objeto imagen
     * 
     * @param imaImagen es la <code>imagen</code> del objeto.
     * 
     */
    public void setImagen(Image imaImagen) {
        this.imiIcono = new ImageIcon(imaImagen);
    }

    /**
     * getImagen
     * 
     * Metodo de acceso que regresa la imagen que representa el icono del objeto
     * 
     * @return la imagen a partide del <code>icono</code> del objeto.
     * 
     */
    public Image getImagen() {
        return imiIcono.getImage();
    }

    /**
     * getAncho
     * 
     * Metodo de acceso que regresa el ancho del icono 
     * 
     * @return un <code>entero</code> que es el ancho del icono.
     * 
     */
    public int getAncho() {
        return imiIcono.getIconWidth();
    }

    /**
     * getAlto
     * 
     * Metodo que  da el alto del icono 
     * 
     * @return un <code>entero</code> que es el alto del icono.
     * 
     */
    public int getAlto() {
        return imiIcono.getIconHeight();
    }
    
    /**
     * paint
     * 
     * Metodo para pintar el AnimalCosa
     * 
     * @param graGrafico    objeto de la clase <code>Graphics</code> para graficar
     * @param imoObserver  objeto de la clase <code>ImageObserver</code> es el 
     *    Applet donde se pintara
     * 
     */
    public void paint(Graphics graGrafico, ImageObserver imoObserver) {
        graGrafico.drawImage(getImagen(), getX(), getY(), imoObserver);
    }
    
    /**
     * intersecta
     * 
     * Metodo que checa si un objeto AnimalCosa intersecta a otro AnimalCosa
     * 
     * @param objAnimalCosa es un objeto de la clase <code>Object</code>
     * @return un valor <code>boolean</code> indicando si se intersecta con este
     *  
     */
    public boolean intersecta(Object objAnimalCosa) {
        // valido si el objecto es AnimalCosa
        if (objAnimalCosa instanceof AnimalCosa) {
            // creo los rectangulos de ambos
            Rectangle rctEste = new Rectangle(this.getX(), this.getY(), 
                    this.getAncho(), this.getAlto());
            AnimalCosa aniTemp = (AnimalCosa) objAnimalCosa;
            Rectangle rctParam = new Rectangle(aniTemp.getX(), aniTemp.getY(), 
                    aniTemp.getAncho(), aniTemp.getAlto());
            return rctEste.intersects(rctParam);
        }
        // si no entro entonces no es un AnimalCosa
        return false;
    }
    
    /**
     * estaAdentro
     * 
     * Metodo que checa si una coordenada esta contenida
     * en el area abarcada por un AnimalCosa
     * 
     * @param x es un <code>int</code> que indica la coordenada en X
     * @param y es un <code>int</code> que indica la coordenada en Y
     * @return un valor <code>boolean</code> indicando si la coordenada esta
     * dentro del area del AnimalCosa
     *  
     */
    public boolean estaAdentro(int x, int y) {
        
        // se crea rectángulo del objeto
        Rectangle rctEste = new Rectangle(this.getX(), this.getY(), 
                this.getAncho(), this.getAlto());
        // Devuelve si la coordenada (x,y) esta contenida en el rectangulo
        // que representa la imagen del objeto AnimalCosa
        return rctEste.contains(x,y);
            
    }
    
    /**
     * intersectaPorAbajoCompleto
     * 
     * Metodo que checa si el objeto AnimalCosa intersecta a otro AnimalCosa
     * en la parte inferior de este ultimo, en una rectangulo ubicado en la  
     * parte inferior de este con un ancho igual a su ancho real y una altura
     * igual a 20 pixeles; cheaca que el Animal cosa no choque con el rectangulo
     * superior al rectangulo antes mencionado (para verificar que el choque
     * no sea de arriba hacia abajo);
     * y que ademas que los limites de las orillas del
     * objeto AnimalCosa recibido como parametro sean abarcadas totalmente por
     * las orillas del objeto AnimalCosa this.
     * 
     * @param objAnimalCosa es un objeto de la clase <code>Object</code>
     * @return un valor <code>boolean</code> indicando si se intersecta con este
     *  
     */
    public boolean intersectaPorAbajoCompleto(Object objAnimalCosa) {
        // valido si el objecto es AnimalCosa
        if (objAnimalCosa instanceof AnimalCosa) {
            // creo los rectangulos de ambos
            Rectangle rctEste = new Rectangle(this.getX(), this.getY(), 
                    this.getAncho(), this.getAlto());
            AnimalCosa aniTemp = (AnimalCosa) objAnimalCosa;
            // Rectangulo auxiliar inferior
            Rectangle rctParamInf = new Rectangle(aniTemp.getX(), aniTemp.getY()
                    + aniTemp.getAlto() - 20, aniTemp.getAncho(), 20);
            // Rectangulo auxiliar superior
            Rectangle rctParamSup = new Rectangle(aniTemp.getX(), 
                    aniTemp.getY(), aniTemp.getAncho(), aniTemp.getAlto() - 20);
            // checa interseccion inferior
            return rctEste.intersects(rctParamInf) 
                    // checa que no haya interseccion con rectangulo superior
                    && !rctEste.intersects(rctParamSup)
                    // checa rango de orilla izquierda
                    && this.getX() <= aniTemp.getX() 
                    && aniTemp.getX() <= this.getX() + this.getAncho()
                    // checa rango de orilla derecha
                    && this.getX() <= aniTemp.getX() + aniTemp.getAncho()
                    && aniTemp.getX() + aniTemp.getAncho() 
                    <= this.getX() + this.getAncho();
        }
        // si no entro entonces no es un AnimalCosa
        return false;
    }
    
    
}