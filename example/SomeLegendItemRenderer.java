package example;

import com.itextpdf.text.pdf.PdfContentByte;
import org.gephi.preview.api.*;
import org.gephi.preview.spi.ItemBuilder;
import org.openide.util.NbBundle;
import org.openide.util.lookup.ServiceProvider;
import org.w3c.dom.Element;
import processing.core.PGraphics;

@ServiceProvider(service = SomeLegendItemRenderer.class, position = 10)
public class SomeLegendItemRenderer implements org.gephi.preview.spi.Renderer {

    private static final String LEGEND_TOP_ELEMENT = "legends";

    public String getDisplayName() {
        return NbBundle.getMessage(SomeLegendItemRenderer.class, "localized.name.key");
    }

    public void preProcess(PreviewModel previewModel) {
        //Optionally pre process items or properties before each rendering
    }

    public void render(Item item, RenderTarget target, PreviewProperties properties) {
        SomeLegendItem someLegendItem = (SomeLegendItem) item;
        if (target instanceof ProcessingTarget) {
            renderProcessing(someLegendItem, (ProcessingTarget) target, properties);
        } else if (target instanceof SVGTarget) {
            renderSVG(someLegendItem, (SVGTarget) target, properties);
        } else if (target instanceof PDFTarget) {
            renderPDF(someLegendItem, (PDFTarget) target, properties);
        }
    }

    private void renderProcessing(SomeLegendItem item, ProcessingTarget target, PreviewProperties properties) {
        PGraphics graphics = target.getGraphics();
        //Render here with processing graphics
    }

    private void renderSVG(SomeLegendItem someLegendItem, SVGTarget target, PreviewProperties properties) {
        Element topLegend = target.getTopElement(LEGEND_TOP_ELEMENT);//Create your own top element for legend items, if it does not exist already
        Element someLegendItemElement = target.createElement("some-svg-element");
        //Set attributes to this new element...

        topLegend.appendChild(someLegendItemElement);//Add to the svg document under the proper top element
    }

    private void renderPDF(SomeLegendItem someLegendItem, PDFTarget target, PreviewProperties properties) {
        PdfContentByte cb = target.getContentByte();
        //Render here with PdfContentByte in a similar way as processing and svg
    }

    public PreviewProperty[] getProperties() {
        return new PreviewProperty[0];//Return empty array if you don't need properties for this kind of legend item
        //Use PreviewProperty.createProperty static methods to build your properties if needed.
        
        //You can also implement a PreviewUI if you need a more complex UI to let users
        //configure your legend item properties for rendering.
        //In that case, use PreviewProperties.putValue whenever it is possible so these preferences are automatically saved in a .gephi project.
    }

    public boolean isRendererForitem(Item item, PreviewProperties properties) {
        return item instanceof SomeLegendItem;
        //Note that here, you can use properties to decide if the item should be rendered at the moment or not
        //For example return false while the user is moving the canvas if your item takes a long time to be rendered
    }

    public boolean needsItemBuilder(ItemBuilder itemBuilder, PreviewProperties properties) {
        return itemBuilder instanceof SomeLegendItemItemBuilder;
        //Same possibility with properties here, you can make the preview API not to use some builder
        //by not having any renderer to return true when passed that builder to it
    }
}
