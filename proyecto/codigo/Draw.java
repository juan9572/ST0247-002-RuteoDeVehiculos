import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.*;
import java.io.*;

/**
 * Clase encargada de dibujar las rutas seleccionadas por el algoritmo.
 *
 * @author Julian Gomez Benitez, Juan Pablo Rincon Usma
 * @see Solution
 * @version 1
 */
public class Draw
{
    /**
     *  Método encargado de crear una imagen donde se pintan las rutas tomadas por los vehículos.
     *
     * @param s Las soluciones del algoritmo.
     * @param fileName nombre que tendrá el archivo.
     */
    public static void  drawRoutes(Solution s, String fileName) {

        int VRP_Y = 1440;
        int VRP_INFO = 560;
        int X_GAP = 2000;
        int margin = 20;
        int marginNode;


        int XXX = VRP_INFO + X_GAP;
        int YYY = VRP_Y;


        BufferedImage output = new BufferedImage(XXX, YYY, BufferedImage.TYPE_INT_RGB);
        Graphics2D g = output.createGraphics();
        Color colorFondo = new Color(231, 231, 208);
        g.setColor(colorFondo);
        g.fillRect(0, 0, XXX, YYY);
        g.setColor(Color.BLACK);


        double minX = Double.MAX_VALUE;
        double maxX = Double.MIN_VALUE;
        double minY = Double.MAX_VALUE;
        double maxY = Double.MIN_VALUE;

        for (int k = 0; k < s.Vehiculos.length ; k++)
        {
            for (int i = 0; i < s.Vehiculos[k].Route.size(); i++)
            {
                Node n = s.Vehiculos[k].Route.get(i);
                if (n.Node_X > maxX) maxX = n.Node_X;
                if (n.Node_X < minX) minX = n.Node_X;
                if (n.Node_Y > maxY) maxY = n.Node_Y;
                if (n.Node_Y < minY) minY = n.Node_Y;
            }
        }

        int mX = XXX - 2 * margin;
        int mY = VRP_Y - 2 * margin;

        int A, B;
        if ((maxX - minX) > (maxY - minY))
        {
            A = mX;
            B = (int)((double)(A) * (maxY - minY) / (maxX - minX));
            if (B > mY)
            {
                B = mY;
                A = (int)((double)(B) * (maxX - minX) / (maxY - minY));
            }
        }
        else
        {
            B = mY;
            A = (int)((double)(B) * (maxX - minX) / (maxY - minY));
            if (A > mX)
            {
                A = mX;
                B = (int)((double)(A) * (maxY - minY) / (maxX - minX));
            }
        }

        // Draw Route
        for (int i = 0; i < s.Vehiculos.length ; i++)
        {
            for (int j = 1; j < s.Vehiculos[i].Route.size() ; j++) {
                Node n;
                n = s.Vehiculos[i].Route.get(j-1);

                int ii1 = (int) ((double) (A) * ((n.Node_X - minX) / (maxX - minX) - 0.5) + (double) mX / 2) + margin;
                int jj1 = (int) ((double) (B) * (0.5 - (n.Node_Y - minY) / (maxY - minY)) + (double) mY / 2) + margin;

                n = s.Vehiculos[i].Route.get(j);
                int ii2 = (int) ((double) (A) * ((n.Node_X - minX) / (maxX - minX) - 0.5) + (double) mX / 2) + margin;
                int jj2 = (int) ((double) (B) * (0.5 - (n.Node_Y - minY) / (maxY - minY)) + (double) mY / 2) + margin;

                g.setStroke(new BasicStroke(3.5F));
                g.setColor(new Color(75,242,212));
                g.drawLine(ii1, jj1, ii2, jj2);
            }
        }

        for (int i = 0; i < s.Vehiculos.length ; i++)
        {
            for (int j = 0; j < s.Vehiculos[i].Route.size() ; j++) {
                marginNode = 2;
                Node n = s.Vehiculos[i].Route.get(j);

                int ii = (int) ((double) (A) * ((n.Node_X  - minX) / (maxX - minX) - 0.5) + (double) mX / 2) + margin;
                int jj = (int) ((double) (B) * (0.5 - (n.Node_Y - minY) / (maxY - minY)) + (double) mY / 2) + margin;
                if(n.IsDepot){
                    marginNode = 5;
                    Color color = new Color(242,191,128);
                    g.setColor(color);
                    g.fillOval(ii - 3 * marginNode, jj - 3 * marginNode, 6 * marginNode, 6 * marginNode); //2244
                    String id = Integer.toString(n.NodeId);
                    g.setColor(Color.BLACK);
                    g.drawString(id, ii + 6 * marginNode, jj + 6 * marginNode);
                }else if(n.IsStation){
                    marginNode = 3;
                    Color color = new Color(0,23,71);
                    g.setColor(color);
                    g.fillOval(ii - 3 * marginNode, jj - 3 * marginNode, 6 * marginNode, 6 * marginNode); //2244
                    String id = Integer.toString(n.NodeId);
                    g.setColor(Color.BLACK);
                    g.drawString(id, ii + 6 * marginNode, jj + 6 * marginNode);
                }else{
                    g.setColor(new Color(80,106,212));
                    g.fillOval(ii - 3 * marginNode, jj - 3 * marginNode, 6 * marginNode, 6 * marginNode); //2244
                    String id = Integer.toString(n.NodeId);
                    g.setColor(Color.BLACK);
                    g.drawString(id, ii + 6 * marginNode, jj + 6 * marginNode);
                }
            }

        }
        marginNode = 5;
        g.setFont(new Font("TimesRoman",Font.PLAIN,40));
        g.setColor(new Color(242,191,128));
        g.drawString("Nodo deposito", 10 * marginNode+5, 10 * marginNode);
        g.fillRect(10 * marginNode-30,10* marginNode-25,25,25);
        g.setColor(new Color(0,23,71));
        g.drawString("Nodo estación",10 * marginNode+5,20 * marginNode);
        g.fillRect(10* marginNode-30,20* marginNode-25,25,25);
        g.setColor(new Color(80,106,212));
        g.drawString("Nodo cliente", 10 * marginNode+5, 30 * marginNode);
        g.fillRect(10* marginNode-30,30* marginNode-25,25,25);
        fileName = fileName + ".png";
        File f = new File(fileName);
        try
        {
            ImageIO.write(output, "PNG", f);
        } catch (IOException ex) {
        }
    }
}