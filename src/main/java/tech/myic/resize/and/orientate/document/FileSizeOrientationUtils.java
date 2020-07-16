package tech.myic.resize.and.orientate.document;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import javax.imageio.ImageIO;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.graphics.image.LosslessFactory;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;
import org.apache.pdfbox.rendering.ImageType;
import org.apache.pdfbox.rendering.PDFRenderer;

public class FileSizeOrientationUtils {

    private FileSizeOrientationUtils() {
    }

    private static void rotateAndResizeImage(PDDocument pdfDocument, BufferedImage image)
            throws FileNotFoundException, IOException {
        PDPage page = new PDPage(new PDRectangle(image.getWidth(), image.getHeight()));
        PDRectangle pageSize = PDRectangle.A4;

        int initialWidth = image.getWidth();
        int initialHeight = image.getHeight();
        float a4PageWidth = pageSize.getWidth();
        float a4PageHeight = pageSize.getHeight();
        float ratio = Math.min(a4PageWidth / initialWidth, a4PageHeight / initialHeight);
        float scaledWidth = initialWidth * ratio;
        float scaledHeight = initialHeight * ratio;
        float x = (a4PageWidth - scaledWidth) / 2;
        float y = (a4PageHeight - scaledHeight) / 2;

        PDRectangle mediaBox = page.getMediaBox();
        if ((mediaBox.getWidth() <= mediaBox.getHeight()) && (page.getRotation() == 90 || page.getRotation() == 270)) {
            page.setRotation(0);
        }

        page.setMediaBox(PDRectangle.A4);

        pdfDocument.addPage(page);

        PDImageXObject pdImage = LosslessFactory.createFromImage(pdfDocument, image);

        try (PDPageContentStream contentStream = new PDPageContentStream(pdfDocument, page)) {
            contentStream.drawImage(pdImage, x, y, scaledWidth, scaledHeight);
        }
    }

    public static void rotateAndResizeImageFileToPortraitPDF(File file, File out)
            throws IOException {
        try (PDDocument pdfDocument = new PDDocument(); OutputStream os = new FileOutputStream(out)) {
            try (InputStream in = new FileInputStream(file)) {
                rotateAndResizeImage(pdfDocument, ImageIO.read(in));
            }
            pdfDocument.save(os);
        }
    }

    public static void rotateAndResizeImageFileToPortraitPDF(InputStream in, OutputStream os)
            throws IOException {
        try (PDDocument pdfDocument = new PDDocument()) {
            rotateAndResizeImage(pdfDocument, ImageIO.read(in));
            pdfDocument.save(os);
        }
    }

    public static void rotateAndResizeImageFileToPortraitPDF(InputStream in, File out)
            throws IOException {
        try (PDDocument pdfDocument = new PDDocument(); OutputStream os = new FileOutputStream(out)) {
            rotateAndResizeImage(pdfDocument, ImageIO.read(in));
            pdfDocument.save(os);
        }
    }

    public static void rotateAndResizePDFFileToPortraitPDF(File file, File out)
            throws IOException {
        try (PDDocument pdfDocument = new PDDocument(); OutputStream os = new FileOutputStream(out)) {
            try (PDDocument document = PDDocument.load(file)) {
                PDFRenderer pdfr = new PDFRenderer(document);
                BufferedImage image;
                for (int page = 0; page < document.getNumberOfPages(); page++) {
                    image = pdfr.renderImageWithDPI(page, 300, ImageType.RGB);
                    rotateAndResizeImage(pdfDocument, image);
                }
            }
            pdfDocument.save(os);
        }
    }

    public static void rotateAndResizePDFFileToPortraitPDF(InputStream in, OutputStream os)
            throws IOException {
        try (PDDocument pdfDocument = new PDDocument()) {
            try (PDDocument document = PDDocument.load(in)) {
                PDFRenderer pdfr = new PDFRenderer(document);
                BufferedImage image;
                for (int page = 0; page < document.getNumberOfPages(); page++) {
                    image = pdfr.renderImageWithDPI(page, 300, ImageType.RGB);
                    rotateAndResizeImage(pdfDocument, image);
                }
            }
            pdfDocument.save(os);
        }
    }
}
