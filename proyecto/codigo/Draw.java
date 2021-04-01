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

        int VRP_Y = 800;
        int VRP_INFO = 200;
        int X_GAP = 600;
        int margin = 30;
        int marginNode = 1;


        int XXX = VRP_INFO + X_GAP;
        int YYY = VRP_Y;


        BufferedImage output = new BufferedImage(XXX, YYY, BufferedImage.TYPE_INT_RGB);
        Graphics2D g = output.createGraphics();
        g.setColor(Color.WHITE);
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


                g.drawLine(ii1, jj1, ii2, jj2);
            }
        }

        for (int i = 0; i < s.Vehiculos.length ; i++)
        {
            for (int j = 0; j < s.Vehiculos[i].Route.size() ; j++) {

                Node n = s.Vehiculos[i].Route.get(j);

                int ii = (int) ((double) (A) * ((n.Node_X  - minX) / (maxX - minX) - 0.5) + (double) mX / 2) + margin;
                int jj = (int) ((double) (B) * (0.5 - (n.Node_Y - minY) / (maxY - minY)) + (double) mY / 2) + margin;
                if (i != 0) {
                    g.fillOval(ii - 3 * marginNode, jj - 3 * marginNode, 6 * marginNode, 6 * marginNode); //2244
                    String id = Integer.toString(n.NodeId);
                    g.drawString(id, ii + 6 * marginNode, jj + 6 * marginNode); //88
                } else {
                    g.fillRect(ii - 3 * marginNode, jj - 3 * marginNode, 6 * marginNode, 6 * marginNode);  //4488
                    String id = Integer.toString(n.NodeId);
                    g.drawString(id, ii + 6 * marginNode, jj + 6 * marginNode); //88
                }
            }

        }

        String cst = "VRP solución para "+s.numeroClientes + " clientes con un costo de: " + s.Costo;
        g.drawString(cst, 10, 10);

        fileName = fileName + ".png";
        File f = new File(fileName);
        try
        {
            ImageIO.write(output, "PNG", f);
        } catch (IOException ex) {
        }
    }
}