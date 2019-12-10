/**
 * Prathit Pannase: ppannase
 */
package Game;
import javafx.scene.chart.LineChart;


public class ScoreController extends WordNerdController{
	
	ScoreView scoreView;

	@Override
	void startController() {
		scoreView = new ScoreView();
		scoreView.setupView();

		ScoreChart scoreChart = new ScoreChart();
		wordNerdModel.readScore();
		LineChart lineChartH = scoreChart.drawChart(wordNerdModel.scoreList).get(0) ;
		LineChart lineChartT = scoreChart.drawChart(wordNerdModel.scoreList).get(1) ;


		scoreView.scoreGrid.add(lineChartH,1,1);
		scoreView.scoreGrid.add(lineChartT,1,2);
		WordNerd.root.setCenter(scoreView.scoreGrid);
	}

	@Override
	void setupBindings() {
		// Mentioned in UML but not required
	}
	
	

}
