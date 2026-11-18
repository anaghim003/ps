package com.visionguard;

import boofcv.alg.filter.binary.BinaryImageOps;
import boofcv.alg.filter.binary.Contour;
import boofcv.alg.filter.binary.GThresholdImageOps;
import boofcv.io.image.ConvertBufferedImage;
import boofcv.io.image.UtilImageIO;
import boofcv.struct.ConnectRule;
import boofcv.struct.image.GrayU8;
import georegression.struct.point.Point2D_I32;
import java.awt.image.BufferedImage;
import java.util.List;

public class App {
    // Taille réelle de l'objet de référence, en millimètres
    static final double TAILLE_REELLE_MM = 50.0;

    public static void main(String[] args) {
        BufferedImage image = UtilImageIO.loadImageNotNull("images/calibration.png");
        GrayU8 gray = ConvertBufferedImage.convertFrom(image, (GrayU8) null);

        // 1. Séparer l'objet sombre du fond clair
        double seuil = GThresholdImageOps.computeOtsu(gray, 0, 255);
        GrayU8 binaire = new GrayU8(gray.width, gray.height);
        GThresholdImageOps.threshold(gray, binaire, seuil, true);

        // 2. Nettoyer le bruit
        GrayU8 propre = BinaryImageOps.erode8(binaire, 1, null);
        propre = BinaryImageOps.dilate8(propre, 1, null);

        // 3. Trouver les contours et garder le plus grand
        List<Contour> contours = BinaryImageOps.contourExternal(propre, ConnectRule.EIGHT);
        Contour plusGrand = null;
        for (Contour c : contours) {
            if (plusGrand == null || c.external.size() > plusGrand.external.size()) {
                plusGrand = c;
            }
        }
        if (plusGrand == null) {
            System.out.println("Aucun objet trouvé");
            return;
        }

        // 4. Boîte englobante en pixels
        int minX = Integer.MAX_VALUE, maxX = 0, minY = Integer.MAX_VALUE, maxY = 0;
        for (Point2D_I32 p : plusGrand.external) {
            minX = Math.min(minX, p.x); maxX = Math.max(maxX, p.x);
            minY = Math.min(minY, p.y); maxY = Math.max(maxY, p.y);
        }
        int largeurPx = maxX - minX + 1;
        int hauteurPx = maxY - minY + 1;

        // 5. Calibration
        double mmParPixel = TAILLE_REELLE_MM / largeurPx;
        System.out.println("Seuil automatique : " + seuil);
        System.out.println("Largeur en pixels : " + largeurPx + ", hauteur : " + hauteurPx);
        System.out.println("mm par pixel : " + mmParPixel);
        System.out.println("Hauteur mesurée : " + (hauteurPx * mmParPixel) + " mm");
    }
}
