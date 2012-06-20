/*
 * System.out.println("@Var: /*
 * : "+/*
 * );
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.gephi.legend.api;


import com.itextpdf.text.PageSize;
import com.itextpdf.text.pdf.DefaultFontMapper;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfWriter;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import org.gephi.preview.api.Item;
import org.gephi.preview.api.PreviewProperties;
import org.gephi.preview.api.ProcessingTarget;
import processing.core.PGraphicsJava2D;


/**
 *
 * @author edubecks
 */
public class Table{

    private ArrayList<String> labels;
    private float[][] tableValues;
    private boolean isCellColoring;
    private int cellSizeWidth;
    private int cellSizeHeight;
    private Color BACKGROUND;
    private ArrayList<Color> listOfColors;
    private Table.Direction horizontalAlignment;
    private Table.Direction horizontalTextAlignment;
    private Table.Direction verticalAlignment;
    private Table.Direction cellColoring;
    private Table.VerticalTextDirection verticalTextDirection;
    //font
    private int fontSize;
    private String fontType;
    private int fontStyle;
    private Font font;
    //margins
    private int verticalExtraMargin;
    private int horizontalExtraAlignment;
    private int MINIMUM_MARGIN = 3;
    
    //Item properties
    public static final String TYPE = "Table Legend";
    
    
    public Table() {
    }


    

    public void renderProcessing(Item item, ProcessingTarget target, PreviewProperties properties) {
        PGraphicsJava2D graphics = (PGraphicsJava2D) target.getGraphics();
        Graphics2D g2 = graphics.g2;

        BufferedImage processingImage = createProcessingImage();
        g2.drawImage(processingImage, 20, 20, null);
    }

    public enum Direction {

        UP("Up"),
        BOTTOM("Bottom"),
        RIGHT("Right"),
        LEFT("Left");
        private final String direction;

        private Direction(String direction) {
            this.direction = direction;
        }

        @Override
        public String toString() {
            return this.direction;
        }

    }

    public enum VerticalTextDirection {

        // anti clockwise
        UP(-90d),
        DOWN(-90d),
        DIAGONAL(-45d);
        private final double rotationAngle;

        private VerticalTextDirection(double rotationAngle) {
            this.rotationAngle = rotationAngle;
        }

        public double rotationAngle() {
            return Math.toRadians(rotationAngle);
        }

    }


    public static class Builder {
        // Required parameters

        private ArrayList<String> labels;
        private float[][] tableValues;
        // Optional parameters - initialized to default values
        private int cellSizeWidth = 5;
        private int cellSizeHeight = 5;
        // processing
        private boolean isCellColoring = false;
        private Table.Direction cellColoring = Table.Direction.UP;
        // color
        private Color BACKGROUND = null;
        private ArrayList<Color> listOfColors;
        //alignment
        private Table.Direction horizontalAlignment = Table.Direction.LEFT;
        private Table.Direction horizontalTextAlignment = Table.Direction.LEFT;
        private Table.Direction verticalAlignment = Table.Direction.UP;
        private Table.VerticalTextDirection verticalTextDirection = Table.VerticalTextDirection.UP;
        //font
        private int fontSize = 5;
        private String fontType = Font.SANS_SERIF;
        private int fontStyle = Font.PLAIN;
        // bug, dont know why
        private int verticalExtraMargin = 0;
        private int horizontalExtraAlignment = 0;

        public Builder(ArrayList<String> labels) {
            this.labels = labels;

            //default colors
        }

        public Table.Builder cellSizeHeight(int value) {
            cellSizeHeight = value;
            return this;
        }

        public Table.Builder cellSizeWidth(int value) {
            cellSizeWidth = value;
            return this;
        }

        public Table.Builder fontSize(int value) {
            fontSize = value;
            return this;
        }

        public Table.Builder background(Color value) {
            BACKGROUND = value;
            return this;
        }

        public Table.Builder verticalAlignment(Table.Direction value) {
            verticalAlignment = value;
            return this;
        }

        public Table.Builder verticalTextDirection(Table.VerticalTextDirection value) {
            verticalTextDirection = value;
            return this;
        }

        public Table.Builder horizontalAlignment(Table.Direction value) {
            horizontalAlignment = value;
            return this;
        }

        public Table.Builder horizontalTextAlignment(Table.Direction value) {
            horizontalTextAlignment = value;
            return this;
        }

        public Table.Builder fontType(String value) {
            fontType = value;
            return this;
        }

        public Table.Builder fontStyle(int value) {
            fontStyle = value;
            return this;
        }

        public Table.Builder tableValues(float[][] value) {
            tableValues = value;
            return this;
        }

        public Table.Builder isCellColoring(boolean value) {
            isCellColoring = value;
            return this;
        }

        public Table.Builder cellColoring(Table.Direction value) {
            cellColoring = value;
            return this;
        }

        public Table.Builder listOfColors(ArrayList<Color> value) {
            listOfColors = value;
            return this;
        }

        public Table.Builder verticalExtraMargin(int value) {
            verticalExtraMargin = value;
            return this;
        }

        public Table.Builder horizontalExtraAlignment(int value) {
            horizontalExtraAlignment = value;
            return this;
        }

        public Table build() {
            return new Table(this);
        }

    }

    private Table(Table.Builder builder) {
        labels = builder.labels;
        fontSize = builder.fontSize;
        cellSizeHeight = builder.cellSizeHeight;
        cellSizeWidth = builder.cellSizeWidth;
        BACKGROUND = builder.BACKGROUND;
        horizontalAlignment = builder.horizontalAlignment;
        horizontalTextAlignment = builder.horizontalTextAlignment;
        verticalAlignment = builder.verticalAlignment;
        verticalTextDirection = builder.verticalTextDirection;
        fontSize = builder.fontSize;
        fontStyle = builder.fontStyle;
        tableValues = builder.tableValues;
        isCellColoring = builder.isCellColoring;
        cellColoring = builder.cellColoring;
        listOfColors = builder.listOfColors;
        verticalExtraMargin = builder.verticalExtraMargin;
        horizontalExtraAlignment = builder.horizontalExtraAlignment;
    }

//    private int maxLabelLength() {
//
//        assert (labels.size() > 0) : "labels is empty";
//
//        int maxLength = Integer.MIN_VALUE;
//        for (String label : labels) {
//            maxLength = Math.max(maxLength, label.length());
//        }
//
//        return maxLength;
//    }
    private String longestLabel() {

        assert (labels.size() > 0) : "labels is empty";

        String maxLabel = labels.get(0);
        for (int i = 1; i < labels.size(); i++) {
            if (labels.get(i).length() > maxLabel.length()) {
                maxLabel = labels.get(i);
            }
        }
        return maxLabel;
    }

    //options
    public void normalize(float[][] table, float minValue, float maxValue) {
        float range = maxValue - minValue;
        for (int i = 0; i < table.length; i++) {
            for (int j = 0; j < table[i].length; j++) {
                table[i][j] = (table[i][j] - minValue) / (range);
            }
        }
    }
    //options

    public void normalize(float[][] table) {
        float minValue = Float.MAX_VALUE;
        float maxValue = Float.MIN_VALUE;
        for (int i = 0; i < table.length; i++) {
            for (int j = 0; j < table[i].length; j++) {
                maxValue = Math.max(maxValue, table[i][j]);
                minValue = Math.min(minValue, table[i][j]);
            }
        }
        normalize(table, minValue, maxValue);
    }

//    public BufferedImage createProcessingVerticalText(){
//        
//    }
    public void createVerticalText(Graphics2D graphics, AffineTransform affineTransform, Integer width, Integer height) {

        int diagonalShift = (int) (cellSizeWidth * Math.cos(verticalTextDirection.rotationAngle()));


        //margin
        int centerDistance = (cellSizeWidth - fontSize) / 2;

//        System.out.printf("New Image:%d,%d\n", widthSub, heightSub);


//        AffineTransform affineTransform = new AffineTransform();
//        BufferedImage tempImage = new BufferedImage(widthSub, heightSub, BufferedImage.TYPE_INT_RGB);
//        Graphics2D graphics = (Graphics2D) tempImage.getGraphics();

//        //drawing background 
//        if (BACKGROUND != null) {
//            imageGraphics.setColor(BACKGROUND);
//            imageGraphics.fillRect(0, 0, image.getWidth(), image.getHeight());
//        }
//
//        if (BACKGROUND != null) {
//            tempImageGraphics.setColor(BACKGROUND);
//            tempImageGraphics.fillRect(0, 0, image.getWidth(), image.getHeight());
//        }

        //font
        graphics.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
                                  RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        graphics.setFont(font);
        graphics.setColor(Color.MAGENTA);

        //metrics
        FontMetrics metrics = graphics.getFontMetrics();


        switch (verticalTextDirection) {
            case UP: {
                //rotating
                affineTransform.translate(0, height - verticalExtraMargin);
                affineTransform.rotate(verticalTextDirection.rotationAngle());
//                fontAfineTransform.translate(-widthSub+verticalExtraMargin, 0);
                graphics.setTransform(affineTransform);


                for (int i = 0; i < labels.size(); i++) {
                    String label = labels.get(i);
                    graphics.drawString(label,
                                        MINIMUM_MARGIN,
                                        i * cellSizeWidth + fontSize + centerDistance);

                }


                break;
            }
            case DOWN: {




                //rotating
                affineTransform.translate(0, height - verticalExtraMargin);
                affineTransform.rotate(verticalTextDirection.rotationAngle());
//                fontAfineTransform.translate(-widthSub+verticalExtraMargin, 0);
                graphics.setTransform(affineTransform);

//                graphics.setColor(Color.GREEN);
//                graphics.drawLine(0, 0, 0, heightSub);
//                graphics.setColor(Color.PINK);
//                graphics.drawLine(0, 0, widthSub, 0);

                for (int i = 0; i < labels.size(); i++) {
                    String label = labels.get(i);
                    graphics.drawString(label,
                                        height - metrics.stringWidth(label) - MINIMUM_MARGIN,
                                        (int) (i * cellSizeWidth) + fontSize + centerDistance);

                }


                break;
            }
            case DIAGONAL: {

                //overriding centerdistance
                centerDistance = (int) ((cellSizeWidth - fontSize * Math.cos(verticalTextDirection.rotationAngle())) / 2);

                //vertical shift for Diagonal case
                int verticalShift = -(int) (height * Math.sin(verticalTextDirection.rotationAngle()));
                System.out.printf("verticalShift: %d centerDistance:%d\n", verticalShift, centerDistance);
//                fontAfineTransform.translate(diagonalShift, -diagonalShift);
//                fontAfineTransform.translate(centerDistance, verticalShift + diagonalShift - verticalExtraMargin);
                affineTransform.translate(0, verticalShift + diagonalShift - verticalExtraMargin);
                affineTransform.rotate(verticalTextDirection.rotationAngle());
                graphics.setTransform(affineTransform);


                for (int i = 0; i < labels.size(); i++) {
                    String label = labels.get(i);
//                    tempImageGraphics.fillRect(margin + ((i + 1) * diagonalShift),
//                                               (int) (i * cellSizeWidth) + fontSize - centerDistance, 3, 3);
                    graphics.drawString(label,
                                        MINIMUM_MARGIN + ((i) * diagonalShift) + centerDistance + horizontalExtraAlignment,
                                        (int) (i * diagonalShift) + fontSize - centerDistance + horizontalExtraAlignment);
//                    System.out.printf("Drawing at: (%d,%d)\n",
//                                      margin + (i * diagonalShift),
//                                      (int) (i * cellSizeWidth) + fontSize + centerDistance);
                }

                //rotating




//                fontAfineTransform.translate(width, 0);
                break;
            }
        }

        //drawing into image
//        imageGraphics.drawImage(tempImage, fontAfineTransform, null);

//        imageGraphics.drawImage(tempImage, 0, 0, null);

    }

    public BufferedImage createVerticalText() {

        assert (cellSizeWidth >= fontSize) : "Cell Size Height must be greater than font Size";







        //creating small image to find out the text size
        BufferedImage image = new BufferedImage(10, 10,
                                                BufferedImage.TYPE_INT_RGB);
        Graphics2D imageGraphics = (Graphics2D) image.getGraphics();
        imageGraphics.setFont(font);
        FontMetrics metrics = imageGraphics.getFontMetrics();



        //compute the width and height for the new image
        int maxLength = metrics.stringWidth(longestLabel());
//        Integer height = (metrics.charWidth('w') + 3) * maxLength + margin;
        Integer height = maxLength + 2 * MINIMUM_MARGIN;
        Integer width = cellSizeWidth * labels.size() + height;
        image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        imageGraphics = (Graphics2D) image.getGraphics();








        Integer heightSub = 0, widthSub = 0;





        return image;
    }

    public void createHorizontalText(Graphics2D graphics, AffineTransform arrangeTranslation, Integer width, Integer height) {


        //arrange
        graphics.setTransform(arrangeTranslation);

        int horizontalCenterDistance = (cellSizeHeight - fontSize) / 2;



        FontMetrics metrics = graphics.getFontMetrics(font);
        graphics.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        graphics.setColor(Color.WHITE);
        for (int i = 0; i < labels.size(); i++) {
            String label = labels.get(i);
            if (isCellColoring) {
                graphics.setColor(listOfColors.get(i));
            }

            switch (horizontalTextAlignment) {
                case RIGHT: {
                    graphics.drawString(label,
                                        width - metrics.stringWidth(label) - MINIMUM_MARGIN,
                                        (int) (i * cellSizeHeight) + fontSize + horizontalCenterDistance);
                    break;
                }
                case LEFT: {
                    graphics.drawString(label,
                                        MINIMUM_MARGIN,
                                        (int) (i * cellSizeHeight) + fontSize + horizontalCenterDistance);
                    break;
                }
            }
        }
    }

    public BufferedImage createProcessingHorizontalText() {


        assert (cellSizeHeight >= fontSize) : "Cell Size Height must be greater than font Size";

        BufferedImage image = new BufferedImage(1, 1,
                                                BufferedImage.TYPE_INT_RGB);
        Integer width = image.getGraphics().getFontMetrics().stringWidth(longestLabel()) + 2 * MINIMUM_MARGIN;
        Integer height = cellSizeHeight * labels.size();
        image = new BufferedImage(width, height,
                                  BufferedImage.TYPE_INT_RGB);
        Graphics2D g2d = (Graphics2D) image.getGraphics();

        //drawing background 
        if (BACKGROUND != null) {
            g2d.setColor(BACKGROUND);
            g2d.fillRect(0, 0, image.getWidth(), image.getHeight());
        }



        return image;
    }

    public void createTableImage(Graphics2D graphics, AffineTransform arrangeTranslation) {
        //drawing background 

        //arrange
        graphics.setTransform(arrangeTranslation);


        graphics.setColor(Color.WHITE);
        for (int i = 0; i < tableValues.length; i++) {
            if (isCellColoring) {
                graphics.setColor(listOfColors.get(i));
            }
            for (int j = 0; j < tableValues[i].length; j++) {
//                System.out.printf("[%d][%d] -> (%f) -> Direction %s\n", i, j,
//                                  tableValues[i][j], cellColoring.toString());
                switch (cellColoring) {
                    case UP:
                        int x1 = j * cellSizeWidth;
                        int y2 = (int) (cellSizeHeight * tableValues[i][j]);
                        int y1 = ((i + 1) * cellSizeHeight) - y2;
                        int x2 = cellSizeWidth;
//                        System.out.printf("(%d,%d)->(%d,%d)\n", x1, y1, x1 + x2, y1 + y2);
                        graphics.fillRect(x1, y1, x2, y2);
                        break;
                }

            }
        }

    }

    public BufferedImage createProcessingTableImage(float[][] tableValues) {
        Integer width = (tableValues[0].length * cellSizeWidth);
        Integer height = (tableValues.length * cellSizeHeight);
        BufferedImage image = new BufferedImage(width, height,
                                                BufferedImage.TYPE_INT_RGB);
        Graphics2D graphics = (Graphics2D) image.getGraphics();
        if (BACKGROUND != null) {
            graphics.setColor(BACKGROUND);
            graphics.fillRect(0, 0, image.getWidth(), image.getHeight());
        }

//        createTableImage(graphics);

        return image;
    }

    public BufferedImage createProcessingImage() {

        assert (labels.size() == tableValues.length) : "dimensions do not match";


        BufferedImage image = null;

        if (isCellColoring) {
            normalize(tableValues);






            BufferedImage verticalText = createVerticalText();
            BufferedImage horizontalText = createProcessingHorizontalText();
            BufferedImage table = createProcessingTableImage(tableValues);




//            Integer width = verticalText.getWidth() + horizontalText.getWidth();
//            Integer height = verticalText.getHeight() + horizontalText.getHeight();


//        Integer height = (metrics.charWidth('w') + 3) * maxLength + margin;

            image = new BufferedImage(1, 1, BufferedImage.TYPE_INT_RGB);
            //font
            font = new Font(fontType, fontStyle, fontSize);
            Graphics2D graphics = (Graphics2D) image.getGraphics();
            graphics.setFont(font);
            FontMetrics fontMetrics = graphics.getFontMetrics();



            int maxLength = fontMetrics.stringWidth(longestLabel());
            System.out.println("@Var: maxLength: " + maxLength);



            int diagonalShift = (int) (cellSizeWidth * Math.cos(verticalTextDirection.rotationAngle()));




            Integer height = maxLength + cellSizeHeight * labels.size() + 2 * MINIMUM_MARGIN;
            Integer width = maxLength + cellSizeWidth * labels.size() + 2 * MINIMUM_MARGIN;


            int horizontalTextWidth = maxLength + 2 * MINIMUM_MARGIN;
            int horizontalTextHeight = cellSizeHeight * labels.size();
            int verticalTextHeight = maxLength + 2 * MINIMUM_MARGIN;
            int verticalTextWidth = cellSizeWidth * labels.size();

            if (verticalTextDirection == Table.VerticalTextDirection.DIAGONAL) {
                width += horizontalTextWidth;
            }

            image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
            graphics = (Graphics2D) image.getGraphics();

            //drawing background 
            if (BACKGROUND != null) {
                graphics.setColor(BACKGROUND);
                graphics.fillRect(0, 0, image.getWidth(), image.getHeight());
            }

            AffineTransform origin = new AffineTransform();
            origin.setToTranslation(0, 0);

            createImage(graphics, origin, horizontalTextWidth, horizontalTextHeight, verticalTextWidth, verticalTextHeight);


//            graphics.drawImage(table, horizontalText.getWidth(),
//                               verticalText.getHeight(), null);

        }

        return image;

    }

    public void createImage(Graphics2D graphics, AffineTransform origin, int horizontalTextWidth, int horizontalTextHeight, int verticalTextWidth, int verticalTextHeight) {

        AffineTransform arrangeTranslation = new AffineTransform();
        arrangeTranslation.setTransform(origin);
        arrangeTranslation.translate(horizontalTextWidth, 0);
//            graphics.setTransform(translateTransform);
        createVerticalText(graphics, arrangeTranslation, verticalTextWidth, verticalTextHeight);


        arrangeTranslation.setTransform(origin);
        arrangeTranslation.translate(0, verticalTextHeight);
        createHorizontalText(graphics, arrangeTranslation, horizontalTextWidth, horizontalTextHeight);


        arrangeTranslation.setTransform(origin);
        arrangeTranslation.translate(horizontalTextWidth, verticalTextHeight);
        createTableImage(graphics, arrangeTranslation);

    }

    public String SVGFillRect(String color, int x, int y, Integer width, Integer height) {
        String colorToRGB = color;
        String image = "<rect "
                       + "x=\"" + x + "\" "
                       + "y=\"" + y + "\" "
                       + "width=\"" + width + "\" "
                       + "height=\"" + height + "\" "
                       + "fill=\"" + colorToRGB + "\" "
                       + "/>";
        return image;
    }

    public String createSVGTableImage(float[][] tableValues, int begin, int end) {
        StringBuilder image = new StringBuilder();
        Integer width = (tableValues[0].length * cellSizeWidth);
        Integer height = (tableValues.length * cellSizeHeight);

        String color = "";

        for (int i = 0; i < tableValues.length; i++) {
            if (isCellColoring) {
                color = listOfColors.get(i).toString();
            }
            for (int j = 0; j < tableValues[i].length; j++) {
//                System.out.printf("[%d][%d] -> (%f) -> Direction %s\n", i, j,
//                                  tableValues[i][j], cellColoring.toString());
                switch (cellColoring) {
                    case UP:
                        int x1 = j * cellSizeWidth;
                        int y2 = (int) (cellSizeHeight * tableValues[i][j]);
                        int y1 = ((i + 1) * cellSizeHeight) - y2;
                        int x2 = cellSizeWidth;

                        //rectangle
                        String rectangle = "";
                        image.append("");
                        break;
                }

            }
        }
        return image.toString();

    }

    public void paint(Graphics2D g2d) {
        g2d.setPaint(Color.red);
        g2d.fill(new Rectangle(10, 10, 100, 100));
        g2d.setPaint(Color.BLUE);
        g2d.fill(new Rectangle(0, 0, 10, 10));
    }

//    public String createSVGImage() {
//        String image = "";
//
//        // Get a DOMImplementation.
//        DOMImplementation domImpl =
//                GenericDOMImplementation.getDOMImplementation();
//
//        // Create an instance of org.w3c.dom.Document.
//        String svgNS = "http://www.w3.org/2000/svg";
//        org.w3c.dom.Document document = domImpl.createDocument(svgNS, "svg", null);
//
//        // Create an instance of the SVG Generator.
//        SVGGraphics2D graphics = new SVGGraphics2D(document);
//
//        // Ask the test to render into the SVG Graphics2D implementation.
////        paint(svgGenerator);
//
//
//
//        int maxLength = 92;
//        System.out.println("@Var: maxLength: " + maxLength);
//
//
//
//        int diagonalShift = (int) (maxLength * Math.cos(verticalTextDirection.rotationAngle()));
//
//
//        int horizontalTextWidth = maxLength + 2 * MINIMUM_MARGIN;
//        int horizontalTextHeight = cellSizeHeight * labels.size();
//        int verticalTextHeight = maxLength + 2 * MINIMUM_MARGIN;
//        int verticalTextWidth = cellSizeWidth * labels.size();
//
//        Integer height = maxLength + cellSizeHeight * labels.size() + 2 * MINIMUM_MARGIN;
//        Integer width = maxLength + cellSizeWidth * labels.size() + 2 * MINIMUM_MARGIN;
//
//
//        if (verticalTextDirection == Table.VerticalTextDirection.DIAGONAL) {
//
//            System.out.println("diagonalShift" + diagonalShift);
//            width += diagonalShift;
//
//
//
//        }
//
//
//
//        AffineTransform origin = new AffineTransform();
//        origin.setToTranslation(0, 0);
//
//        createImage(graphics, origin, horizontalTextWidth, horizontalTextHeight, verticalTextWidth, verticalTextHeight);
//
//
//        // Finally, stream out SVG to the standard output using
//        // UTF-8 encoding.
//        boolean useCSS = true; // we want to use CSS style attributes
//        try {
//            File file = new File("test.svg");
//            OutputStream fos = new FileOutputStream(file);
//            Writer out = new OutputStreamWriter(fos, "UTF-8");
//            graphics.stream(out, useCSS);
//
//        } catch (Exception e) {
//        }
//
//
//        return image;
//    }

    public void createPDFImage() {

        try {
            
            System.out.printf("creating pdf\n");

            com.itextpdf.text.Document pdfDocument = new com.itextpdf.text.Document(PageSize.A4);
            FileOutputStream pdfFile = new FileOutputStream(new File("test.pdf"));

            PdfWriter writer = PdfWriter.getInstance(pdfDocument, pdfFile);
//            writer.setStrictImageSequence(true);
            pdfDocument.open();
//            pdfDocument.close();

            PdfContentByte cb = writer.getDirectContent();
//            cb.saveState();
//            DefaultFontMapper mapper = new DefaultFontMapper();
            DefaultFontMapper mapper = new DefaultFontMapper();
            Graphics2D graphics = cb.createGraphics(pdfDocument.getPageSize().getWidth(),pdfDocument.getPageSize().getHeight());
            
            
            


            int maxLength = 92;
            
            int horizontalTextWidth = maxLength + 2 * MINIMUM_MARGIN;
            int horizontalTextHeight = cellSizeHeight * labels.size();
            int verticalTextHeight = maxLength + 2 * MINIMUM_MARGIN;
            int verticalTextWidth = cellSizeWidth * labels.size();
            
            Integer height = maxLength + cellSizeHeight * labels.size() + 2 * MINIMUM_MARGIN;
            Integer width = maxLength + cellSizeWidth * labels.size() + 2 * MINIMUM_MARGIN;

            if (verticalTextDirection == Table.VerticalTextDirection.DIAGONAL) {
                width += horizontalTextWidth;
            }


            AffineTransform origin = new AffineTransform();
            origin.setToTranslation(0, 0);

            createImage(graphics, origin, horizontalTextWidth, horizontalTextHeight, verticalTextWidth, verticalTextHeight);
            
            graphics.dispose();

//            cb.restoreState();
            pdfDocument.close();
            pdfFile.close();
            

        } catch (Exception e) {
        }
    }

}
