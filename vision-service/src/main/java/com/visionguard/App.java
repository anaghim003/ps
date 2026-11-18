package com.visionguard;

import boofcv.abst.fiducial.QrCodeDetector;
import boofcv.alg.fiducial.qrcode.QrCode;
import boofcv.factory.fiducial.FactoryFiducial;
import boofcv.io.image.ConvertBufferedImage;
import boofcv.io.image.UtilImageIO;
import boofcv.struct.image.GrayU8;
import java.awt.image.BufferedImage;
import java.util.List;

public class App {
    public static void main(String[] args) {
        BufferedImage image = UtilImageIO.loadImageNotNull("images/qr.png");
        GrayU8 gray = ConvertBufferedImage.convertFrom(image, (GrayU8) null);

        QrCodeDetector<GrayU8> detector = FactoryFiducial.qrcode(null, GrayU8.class);
        detector.process(gray);

        List<QrCode> found = detector.getDetections();
        System.out.println("QR codes trouvés : " + found.size());
        for (QrCode qr : found) {
            System.out.println("Message : " + qr.message);
        }
    }
}
