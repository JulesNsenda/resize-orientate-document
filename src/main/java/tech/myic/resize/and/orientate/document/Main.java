/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tech.myic.resize.and.orientate.document;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

/**
 *
 * @author jules
 */
public class Main {

    public static void main(String[] args) throws FileNotFoundException, IOException {
        System.out.println("Porcessing documet....");
        File inPdf = new File("/home/jules/Downloads/all2.pdf");
        if (!inPdf.exists()) {
            throw new FileNotFoundException("No file found");
        }

        File outPdfFile = new File("/tmp/all2-pd.pdf");
        FileSizeOrientationUtils.rotateAndResizePDFFileToPortraitPDF(inPdf, outPdfFile);
        System.out.println("Done!");

        System.out.println("Porcessing documet image file....");
        File img = new File("/home/jules/Downloads/in2.png");
        if (!img.exists()) {
            throw new FileNotFoundException("No file found");
        }

        File outImg = new File("/tmp/in2.pdf");
        FileSizeOrientationUtils.rotateAndResizeImageFileToPortraitPDF(img, outImg);
        System.out.println("Done!");
    }
}
