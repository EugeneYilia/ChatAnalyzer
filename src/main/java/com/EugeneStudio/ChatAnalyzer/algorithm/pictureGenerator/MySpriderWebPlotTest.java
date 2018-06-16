package com.EugeneStudio.ChatAnalyzer.algorithm.pictureGenerator;


import com.EugeneStudio.ChatAnalyzer.algorithm.PeopleAttribute;
import com.EugeneStudio.ChatAnalyzer.algorithm.Utils;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.title.LegendTitle;
import org.jfree.chart.title.TextTitle;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.ui.RectangleEdge;

import javax.swing.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class MySpriderWebPlotTest {
    ArrayList<PeopleAttribute.Attribute> myAttributes = new ArrayList<>();
    ArrayList<PeopleAttribute.Attribute> otherAttributes = new ArrayList<>();
    public void draw(ArrayList<PeopleAttribute.Attribute> myAttributes,ArrayList<PeopleAttribute.Attribute> otherAttributes,String myName,String otherName) {
        this.myAttributes = myAttributes;
        this.otherAttributes = otherAttributes;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");
        String date = simpleDateFormat.format(System.currentTimeMillis());
        saveAsFile("D:/IdeaWorkSpace/ChatAnalyzer/picture/"+myName+"_"+otherName+date+".png",500,400);
        //传入自己的名字，对方的名字，自己的属性数组，对方的属性数组就可以画出对应的图像
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
        JFreeChart jfreechart = new JFreeChart("Eugene聊天记录分析", TextTitle.DEFAULT_FONT,spiderwebplot, false);
        LegendTitle legendtitle = new LegendTitle(spiderwebplot);
        legendtitle.setPosition(RectangleEdge.BOTTOM);
        jfreechart.addSubtitle(legendtitle);
        return jfreechart;
    }
    public DefaultCategoryDataset createDataset() {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();

        String group1 = "我 ";
        for(int i =0;i<myAttributes.size();i++){
            dataset.addValue(myAttributes.get(i).getLevel(),group1,myAttributes.get(i).getAttributeName());
        }

        String group2 = "对方";
        for(int i = 0;i<otherAttributes.size();i++){
            dataset.addValue(otherAttributes.get(i).getLevel(),group2,otherAttributes.get(i).getAttributeName());
        }
        return dataset;
    }
}
