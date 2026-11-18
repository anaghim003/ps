package com.visionguard;

import boofcv.gui.image.ShowImages;
import boofcv.io.image.UtilImageIO;
import java.awt.image.BufferedImage;

public class App {
    public static void main(String[] args) {
        BufferedImage image = UtilImageIO.loadImageNotNull("images/piece.png");
        ShowImages.showWindow(image, "Ma première image");
    }
}
