package com.EugeneStudio.ChatAnalyzer.testDrawPicture;

import com.EugeneStudio.ChatAnalyzer.algorithm.PeopleAttribute;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.SpiderWebPlot;
import org.jfree.chart.title.LegendTitle;
import org.jfree.chart.title.TextTitle;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.ui.RectangleEdge;

import javax.swing.*;
import java.awt.*;
import java.awt.font.FontRenderContext;
import java.awt.font.LineMetrics;
import java.awt.geom.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Array;
import java.text.NumberFormat;
import java.util.ArrayList;

class MySpiderWebPlot extends SpiderWebPlot {
    /**
     *
     */
    private static final long serialVersionUID = 4005814203754627127L;
    private int ticks = DEFAULT_TICKS;
    private static final int DEFAULT_TICKS = 5;
    private NumberFormat format = NumberFormat.getInstance();
    private static final double PERPENDICULAR = 90;
    private static final double TICK_SCALE = 0.015;
    private int valueLabelGap = DEFAULT_GAP;
    private static final int DEFAULT_GAP = 10;
    private static final double THRESHOLD = 15;



    MySpiderWebPlot(CategoryDataset createCategoryDataset) {
        super(createCategoryDataset);
    }
    @Override
    protected void drawLabel(final Graphics2D g2, final Rectangle2D plotArea, final double value,
                             final int cat, final double startAngle, final double extent) {
        super.drawLabel(g2, plotArea, value, cat, startAngle, extent);
        final FontRenderContext frc = g2.getFontRenderContext();
        final double[] transformed = new double[2];
        final double[] transformer = new double[2];
        final Arc2D arc1 = new Arc2D.Double(plotArea, startAngle, 0, Arc2D.OPEN);
        for (int i = 1; i <= ticks; i++) {
            final Point2D point1 = arc1.getEndPoint();
            final double deltaX = plotArea.getCenterX();
            final double deltaY = plotArea.getCenterY();
            double labelX = point1.getX() - deltaX;
            double labelY = point1.getY() - deltaY;
            final double scale = ((double) i / (double) ticks);
            final AffineTransform tx = AffineTransform.getScaleInstance(scale, scale);
            final AffineTransform pointTrans = AffineTransform.getScaleInstance(scale + TICK_SCALE, scale + TICK_SCALE);
            transformer[0] = labelX;
            transformer[1] = labelY;
            pointTrans.transform(transformer, 0, transformed, 0, 1);
            final double pointX = transformed[0] + deltaX;
            final double pointY = transformed[1] + deltaY;
            tx.transform(transformer, 0, transformed, 0, 1);
            labelX = transformed[0] + deltaX;
            labelY = transformed[1] + deltaY;
            double rotated = (PERPENDICULAR);
            AffineTransform rotateTrans = AffineTransform.getRotateInstance(Math.toRadians(rotated), labelX, labelY);
            transformer[0] = pointX;
            transformer[1] = pointY;
            rotateTrans.transform(transformer, 0, transformed, 0, 1);
            final double x1 = transformed[0];
            final double y1 = transformed[1];
            rotated = (-PERPENDICULAR);
            rotateTrans = AffineTransform.getRotateInstance(Math.toRadians(rotated), labelX, labelY);
            rotateTrans.transform(transformer, 0, transformed, 0, 1);
            final Composite saveComposite = g2.getComposite();
            g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1.0f));
            g2.draw(new Line2D.Double(transformed[0], transformed[1], x1, y1));
            if (startAngle == this.getStartAngle()) {
                final String label = format.format(((double) i / (double) ticks) * this.getMaxValue());
                final LineMetrics lm = getLabelFont().getLineMetrics(label, frc);
                final double ascent = lm.getAscent();
                if (Math.abs(labelX - plotArea.getCenterX()) < THRESHOLD) {
                    labelX += valueLabelGap;
                    labelY += ascent / (float) 2;
                } else if (Math.abs(labelY - plotArea.getCenterY()) < THRESHOLD) {
                    labelY += valueLabelGap;
                } else if (labelX >= plotArea.getCenterX()) {
                    if (labelY < plotArea.getCenterY()) {
                        labelX += valueLabelGap;
                        labelY += valueLabelGap;
                    } else {
                        labelX -= valueLabelGap;
                        labelY += valueLabelGap;
                    }
                } else {
                    if (labelY > plotArea.getCenterY()) {
                        labelX -= valueLabelGap;
                        labelY -= valueLabelGap;
                    } else {
                        labelX += valueLabelGap;
                        labelY -= valueLabelGap;
                    }
                }
                g2.setPaint(getLabelPaint());
                g2.setFont(getLabelFont());
                g2.drawString(label, (float) labelX, (float) labelY);
            }
            g2.setComposite(saveComposite);
        }
    }
}


class MySpriderWebPlotTest {
    ArrayList<PeopleAttribute.Attribute> myAttributes = new ArrayList<>();
    ArrayList<PeopleAttribute.Attribute> otherAttribute = new ArrayList<>();
    //public MySpriderWebPlotTest

    public static void main(String[] args) {
        MySpriderWebPlotTest mySpriderWebPlotTest = new MySpriderWebPlotTest();
        mySpriderWebPlotTest.draw(null,null);
    }

    public void draw(ArrayList<PeopleAttribute.Attribute> myAttributes,ArrayList<PeopleAttribute.Attribute> otherAttributes) {
        //在SWING中显示
        //JFrame jf = new JFrame();
        //jf.add(erstelleSpinnenDiagramm());
        //jf.pack();
        //jf.setVisible(true);
        //将JFreeChart保存为图片存在文件路径中
        saveAsFile("D:/IdeaWorkSpace/ChatAnalyzer/picture/MySpiderWebPlot.png",500,400);
    }
    public JPanel erstelleSpinnenDiagramm() {
        JFreeChart jfreechart =createChart();
        ChartPanel chartpanel = new ChartPanel(jfreechart);
        return chartpanel;
    }


    public void saveAsFile(String outputPath,
                                  int weight, int height) {
        FileOutputStream out = null;
        try {
            File outFile = new File(outputPath);
            if (!outFile.getParentFile().exists()) {
                outFile.getParentFile().mkdirs();
            }
            out = new FileOutputStream(outputPath);

            // 保存为PNG
            ChartUtilities.writeChartAsPNG(out, createChart(),weight, height);
            // 保存为JPEG
            // ChartUtilities.writeChartAsJPEG(out, chart, 500, 400);
            out.flush();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (out != null) {
                try {
                    out.close();
                } catch (IOException e) {
                    // do nothing
                }
            }
        }
    }
    public JFreeChart createChart() {
        MySpiderWebPlot spiderwebplot = new MySpiderWebPlot(createDataset());
        JFreeChart jfreechart = new JFreeChart("Eugene聊天记录分析结果", TextTitle.DEFAULT_FONT,spiderwebplot, false);
        LegendTitle legendtitle = new LegendTitle(spiderwebplot);
        legendtitle.setPosition(RectangleEdge.BOTTOM);
        jfreechart.addSubtitle(legendtitle);
        return jfreechart;
    }
    public DefaultCategoryDataset createDataset() {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();

        String group1 = "苹果 ";
        dataset.addValue(50, group1, "一月份");
        dataset.addValue(60, group1, "二月份");
        dataset.addValue(40, group1, "三月份");
        dataset.addValue(20, group1, "四月份");
        dataset.addValue(50, group1, "五月份");
        dataset.addValue(50, group1, "六月份");
        dataset.addValue(50, group1, "七月份");
        //dataset.addValue(80, group1, "八月份");

        String group2 = "橙子";
        dataset.addValue(30, group2, "一月份");
        dataset.addValue(30, group2, "二月份");
        dataset.addValue(40, group2, "三月份");
        dataset.addValue(70, group2, "四月份");
        dataset.addValue(40, group2, "五月份");
        dataset.addValue(50, group2, "六月份");
        dataset.addValue(30, group2, "七月份");
        //dataset.addValue(30, group2, "八月份");

        /*String group3 = "香蕉";
        dataset.addValue(40, group3, "一月份");
        dataset.addValue(50, group3, "二月份");
        dataset.addValue(20, group3, "三月份");
        dataset.addValue(50, group3, "四月份");
        dataset.addValue(60, group3, "五月份");
        dataset.addValue(60, group3, "六月份");
        dataset.addValue(40, group3, "七月份");
        dataset.addValue(40, group3, "八月份");*/
        return dataset;
    }
}
