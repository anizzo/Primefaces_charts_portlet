package com.domande.beans;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;

import org.primefaces.event.ItemSelectEvent;
import org.primefaces.model.chart.Axis;
import org.primefaces.model.chart.AxisType;
import org.primefaces.model.chart.ChartSeries;
import org.primefaces.model.chart.HorizontalBarChartModel;
import org.primefaces.model.chart.PieChartModel;

public class RiepilogoDomandeBean implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 8557642471902622800L;

	static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
	static final String DB_URL = "< INSERIRE QUI L'HOST ED IL VERO DB DA CUI ESTRAPOLARE I DATI >";
	static final String USER = "< INSERIRE QUI L'USERNAME >";
	static final String PASS = "< INSERIRE QUI LA PASSWORD >";

	private Integer index = 0;
	
	private PieChartModel pieModel1;    
	private HorizontalBarChartModel horizontalBarModel;

	private List<DomandaBean> domande = new ArrayList<>();
	private List<DomandaBean> chiuse = new ArrayList<>();
	private List<DomandaBean> aperte = new ArrayList<>();
	
	private Connection c = null;
	private Statement stmt = null;
	
	@PostConstruct
	void init() {

		int maxSum = Integer.MIN_VALUE, minSum = Integer.MAX_VALUE; 
		pieModel1 = new PieChartModel();
		horizontalBarModel = new HorizontalBarChartModel();
		try{
			Class.forName("com.mysql.jdbc.Driver");
			c = DriverManager.getConnection(DB_URL, USER, PASS);
			stmt = c.createStatement();
			
			/**
			 *  QUERY D'ESEMPIO CON LA QUALE ESTRAGGO SOMMA TOTALE DI TUTTE LE DOMANDE ED IL LORO STATO, IN MODO DA
			 *  CREARE SUBITO DOPO I DUE GRAFICI; SOSTITUIRLA CON UNA VERA QUERY
			 *   
			 **/
			String query = "select sum(valore) as somma, stato from Domande group by stato";
			
			ResultSet rs = stmt.executeQuery(query);
			while(rs.next()){
		         int somma  = rs.getInt("somma");
		         if(somma > maxSum)
		        	 maxSum = somma;		         
		         if(somma < minSum)
		        	 minSum = somma;		         
		         String apertura = rs.getString("stato");
		         pieModel1.set(apertura, somma);
			}
			
			/* QUERY CON CUI ESTRAGGO TUTTE LE DOMANDE CON STATO 'APERTO' */
			query = "select * from Domande where stato = \'aperto\'";
			rs = stmt.executeQuery(query);
			while(rs.next()){
				aperte.add(new DomandaBean(rs.getInt("idDomanda"), rs.getString("stato"), rs.getInt("valore")));
			}
			
			/* QUERY CON CUI ESTRAGGO TUTTE LE DOMANDE CON STATO 'CHIUSO' */
			query = "select * from Domande where stato = \'chiuso\'";
			rs = stmt.executeQuery(query);
			while(rs.next()){
				chiuse.add(new DomandaBean(rs.getInt("idDomanda"), rs.getString("stato"), rs.getInt("valore")));
			}
			domande = aperte;
		}
		catch(Exception e){
			System.out.println(e.getStackTrace());
		}
		
		/* CREO IL GRAFICO DI DESTRA, EVIDENZIANDO LE DOMANDE APERTE */
        ChartSeries app1 = new ChartSeries();
        app1.set("aperte", (Number) pieModel1.getData().values().toArray()[0]);
        app1.set("chiuse", 0);
        app1.setLabel("aperte");
        
        /* CREO IL GRAFICO DI DESTRA, EVIDENZIANDO LE DOMANDE CHIUSE */
        ChartSeries app2 = new ChartSeries();
        app2.set("aperte", 0);
        app2.set("chiuse", (Number) pieModel1.getData().values().toArray()[1]);
        app2.setLabel("chiuse");
        
        horizontalBarModel.addSeries(app1);
        horizontalBarModel.addSeries(app2);
        
		pieModel1.setTitle("Riepilogo Domande");
		pieModel1.setLegendPosition("w");        
		
		horizontalBarModel.setTitle("Riepilogo Domande");
		horizontalBarModel.setLegendPosition("e");
        horizontalBarModel.setStacked(false);
        
        Axis xAxis = horizontalBarModel.getAxis(AxisType.X);
        xAxis.setLabel("Valori");
        xAxis.setMin(minSum > 0 ? 0 : minSum - 100);
        xAxis.setMax(maxSum + 100);
        xAxis.setTickFormat("%.0f");
        Axis yAxis = horizontalBarModel.getAxis(AxisType.Y);
        yAxis.setLabel("Stato"); 
        
        horizontalBarModel.setAnimate(true);
	}

	public PieChartModel getPieModel1() {
		return pieModel1;
	}

	public void setPieModel1(PieChartModel pieModel1) {
		this.pieModel1 = pieModel1;
	}

	
	public HorizontalBarChartModel getHorizontalBarModel() {
		return horizontalBarModel;
	}

	public void setHorizontalBarModel(HorizontalBarChartModel horizontalBarModel) {
		this.horizontalBarModel = horizontalBarModel;
	}

	public List<DomandaBean> getDomande() {
		return domande;
	}

	public void setDomande(List<DomandaBean> domande) {
		this.domande = domande;
	}
	
	/**
	 *  METODO DI CALLBACK PER IL CLICK SU UNA DELLE SEZIONI DEI DUE GRAFICI; A SECONDA DELLA SEZIONE CLICCATA 
	 *  POPOLA LA TABELLA CON TUTTE LE DOMANDE APERTE OPPURE CHIUSE
	 **/
	public void itemSelect(ItemSelectEvent event) {
		index = event.getItemIndex();

		domande = (index == 0 ? aperte : chiuse);
		
	}

}
