package GUI;

import Agents.AI;
import Classes.InformAE;
import jade.core.AID;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.labels.ItemLabelAnchor;
import org.jfree.chart.labels.ItemLabelPosition;
import org.jfree.chart.labels.StandardCategoryItemLabelGenerator;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.CategoryItemRenderer;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.AbstractDataset;
import org.jfree.data.general.DefaultPieDataset;
import org.jfree.data.general.PieDataset;
import org.jfree.ui.TextAnchor;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.concurrent.ConcurrentMap;

public class Graficos extends JPanel {
    private ConcurrentMap<AID, InformAE> estacoes;
    private final ChartPanel bar_panel;
    private final ChartPanel bar_panel2;
    private final ChartPanel pie_chart1;
    private final ChartPanel bar_panel3;

    public Graficos() {
        super();
        bar_panel = new ChartPanel(null);
        bar_panel2 = new ChartPanel(null);
        pie_chart1 = new ChartPanel(null);
        bar_panel3 = new ChartPanel(null);
    }

    public void desenhaGraficos(ConcurrentMap<AID, InformAE> e) {
        this.estacoes = e;
        adicionaAoPanel();
    }
    /**
     * Cria todos os dataSets para gerar os graficos todos.
     */
    private LinkedList<AbstractDataset> createDatasets() {
        LinkedList<AbstractDataset> res = new LinkedList<>();
        DefaultCategoryDataset dataset1 = new DefaultCategoryDataset();
        DefaultCategoryDataset dataset2 = new DefaultCategoryDataset();
        DefaultPieDataset dataset3 = new DefaultPieDataset();
        DefaultCategoryDataset dataset4 = new DefaultCategoryDataset();


        ArrayList<InformAE> list = new ArrayList<>(this.estacoes.values());
        list.sort(Comparator.comparingInt(InformAE::getNum));

        for (InformAE e : list) {
            //numero de bikes disponiveis;
            dataset1.setValue(e.getBicicletas_disponiveis(), "Bicicletas disponiveis", Integer.toString(e.getNum()));
            dataset2.setValue(e.getChegadas(), "Nº de chegadas", Integer.toString(e.getNum()));
            dataset3.setValue(Integer.toString(e.getNum()), e.getEstado() * 100);
        }

        dataset4.setValue(AI.station_refused_empty.get(), "", "Alugueres\nrecusados\np/falta");
        dataset4.setValue(AI.station_refused_full.get(), "", "Alugueres\nrecusados\np/excesso");
        dataset4.setValue(AI.alugueres_feitos.get(), "", "Alugueres concluidos");
        dataset4.setValue(AI.user_new_dest.get(), "", "Mudanças de destino");

        res.add(dataset1);
        res.add(dataset2);
        res.add(dataset3);
        res.add(dataset4);

        return res;
    }

    /**
     * Cria um grafico de barras.
     *
     * @param dataset1 data do grafico
     * @param title    titulo do grafico
     * @param cats     categoria
     * @param ylabel   nome do eixo do x.
     * @return o objecto que contem um grafico.
     */
    private JFreeChart createbarChart(CategoryDataset dataset1, String title, String cats, String ylabel) {
        return ChartFactory.createBarChart(title, cats, ylabel, dataset1,
                PlotOrientation.VERTICAL, false, true, false);
    }

    /**
     * Cria um grafico de circular.
     * @param dataset3 dados para o grafico
     * @param title    titulo do grafico
     */
    public JFreeChart createPieChart(PieDataset dataset3, String title) {
        return ChartFactory.createPieChart(title, dataset3,
                true, false, false);
    }

    /**
     * Adiciona os graficos ao painel.
     */
    private void adicionaAoPanel() {
        LinkedList<AbstractDataset> data = this.createDatasets();
        CategoryDataset data_bc = (CategoryDataset) data.removeFirst();
        JFreeChart chart = createbarChart(data_bc, "Nº de Bicicletas disponiveis por estação", "Estações", "Bicicletas disponiveis");

        CategoryDataset data_chegadas = (CategoryDataset) data.removeFirst();
        JFreeChart chart2 = createbarChart(data_chegadas, "Nº de Bicicletas que tem a chegada programada para cada estação", "Estações", "Chegadas");

        PieDataset data_ocupacao = (PieDataset) data.removeFirst();
        JFreeChart chart3 = createPieChart(data_ocupacao, "Percentagem de Ocupação para cada estação");

        CategoryDataset mudar = (CategoryDataset) data.removeFirst();
        JFreeChart chart4 = createbarChart(mudar, "Diversos", "", "");
        CategoryPlot plotC4 = (CategoryPlot) chart4.getPlot();
        plotC4.getDomainAxis().setMaximumCategoryLabelLines(3);
        CategoryItemRenderer cir=  plotC4.getRenderer();
        cir.setBaseItemLabelGenerator(new StandardCategoryItemLabelGenerator());
        cir.setBaseItemLabelFont(new Font("default", Font.BOLD, 10));
        cir.setBaseItemLabelsVisible(true);
        cir.setBasePositiveItemLabelPosition(new ItemLabelPosition(ItemLabelAnchor.INSIDE3,
                    TextAnchor.CENTER_RIGHT, TextAnchor.CENTER, 0.0));
        //Mudar as posições das labels para vertical.
        //plotC4.getDomainAxis().setCategoryLabelPositions(
        //        CategoryLabelPositions.UP_90);
        plotC4.setDomainGridlinesVisible(true);
        plotC4.setRangeGridlinesVisible(false);

        // grafico de barras 1-> Bikes disponiveis.
        bar_panel.setChart(chart);
        bar_panel.setBorder(BorderFactory.createEmptyBorder(15, 15, 10, 10));
        bar_panel.setBackground(Color.black);
        //bar_panel.setLocation(10,10);

        // grafico de barras 2-> Chegadas.
        bar_panel2.setChart(chart2);
        bar_panel2.setBorder(BorderFactory.createEmptyBorder(15, 10, 10, 15));
        bar_panel2.setBackground(Color.black);
        //bar_panel.setLocation(10,10);

        // grafico de circular -> Ocupacao.
        pie_chart1.setChart(chart3);
        pie_chart1.setBorder(BorderFactory.createEmptyBorder(10, 15, 15, 10));
        pie_chart1.setBackground(Color.black);

        bar_panel3.setChart(chart4);
        bar_panel3.setBorder(BorderFactory.createEmptyBorder(10, 10, 15, 15));
        bar_panel3.setBackground(Color.black);

        this.add(bar_panel);
        this.add(bar_panel2);
        this.add(pie_chart1);
        this.add(bar_panel3);
        bar_panel.validate();
        bar_panel2.validate();
        pie_chart1.validate();
        bar_panel3.validate();

        this.setLayout(new GridLayout(0, 2));
    }
}
