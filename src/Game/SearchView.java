/**
 * Prathit Pannase: ppannase
 */

package Game;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.ComboBox;

import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.TableColumn;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.util.Callback;

import java.util.concurrent.atomic.AtomicBoolean;

public class SearchView{

	ComboBox<String> gameComboBox = new ComboBox<>(); //shows drop down for filtering the tableView data
	TextField searchTextField = new TextField();  //for entering search letters
	TableView<Score> searchTableView = new TableView<>();  //displays data from scores.csv
	Callback<TableColumn.CellDataFeatures<Score,String>, ObservableValue<String>> gameNameCallBack;
	Callback<TableColumn.CellDataFeatures<Score,String>, ObservableValue<String>> scoreCallBack;


	void setUpScene(ObservableList<Score> scores){

		// Sets up the Table for Search View Class
		searchTableView.setItems(scores);
		gameNameCallBack= scoreStringCellDataFeatures -> {
			if (scoreStringCellDataFeatures.getValue().getGameId() == 0)
				return new SimpleStringProperty("Hangman");
			else if (scoreStringCellDataFeatures.getValue().getGameId() == 1)
				return new SimpleStringProperty("Twister");
			return null;
		};

		scoreCallBack
				= scoreStringCellDataFeatures -> new SimpleStringProperty(String.format("%.2f", scoreStringCellDataFeatures.getValue().getScore()));

		TableColumn<Score,String> gameColumn = new TableColumn<>("Game");
		gameColumn.setCellValueFactory(gameNameCallBack);

		TableColumn<Score,String> wordColumn = new TableColumn<>("Word");
		wordColumn.setCellValueFactory(new PropertyValueFactory<>("PuzzleWord"));


		TableColumn<Score,Integer> timeColumn = new TableColumn<>("Time (sec)");
		timeColumn.setCellValueFactory(new PropertyValueFactory<>("TimeStamp"));

		TableColumn<Score,String> scoreColumn = new TableColumn<>("Score");
		scoreColumn.setCellValueFactory(scoreCallBack);

		//Sets all the columns for view
		searchTableView.getColumns().setAll(gameColumn,wordColumn,timeColumn,scoreColumn);
		searchTableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
	}

	/**setupView() sets up the GUI components
	 * for Search functionality
	 */
	void setupView() {
		
		VBox searchVBox = new VBox();  //searchVBox contains searchLabel and searchHBox
		Text searchLabel = new Text("Search");
		searchVBox.getChildren().add(searchLabel);

		HBox searchHBox = new HBox();  //searchHBox contain gameComboBox and searchTextField
		gameComboBox.getItems().add("All Games");
		gameComboBox.getItems().add("Hangman");
		gameComboBox.getItems().add("Twister");
		gameComboBox.getSelectionModel().selectFirst();

		searchHBox.getChildren().add(gameComboBox);
		searchHBox.getChildren().add(new Text("Search letters"));
		searchHBox.getChildren().add(searchTextField);
		searchVBox.getChildren().add(searchHBox);

		searchLabel.setStyle( "-fx-font: 30px Tahoma;" + 
				" -fx-fill: linear-gradient(from 0% 50% to 50% 100%, repeat, lightgreen 0%, lightblue 50%);" +
				" -fx-stroke: gray;" +
				" -fx-background-color: gray;" +
				" -fx-stroke-width: 1;") ;
		searchHBox.setPrefSize(WordNerd.GAME_SCENE_WIDTH, WordNerd.GAME_SCENE_HEIGHT / 3);
		gameComboBox.setPrefWidth(200);
		searchTextField.setPrefWidth(300);
		searchHBox.setAlignment(Pos.CENTER);
		searchVBox.setAlignment(Pos.CENTER);
		searchHBox.setSpacing(10);
		
		setupSearchTableView();
		
		WordNerd.root.setPadding(new Insets(10, 10, 10, 10));
		WordNerd.root.setTop(searchVBox);
		WordNerd.root.setCenter(searchTableView);
		WordNerd.root.setBottom(WordNerd.exitButton);
	}

	void setupSearchTableView() {
		//Binded Objects for updating table view

		AtomicBoolean hangmanAllow = new AtomicBoolean(true);
		AtomicBoolean twisterAllow = new AtomicBoolean(true);

		// Binding for gamecombobox
		gameComboBox.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {


			if(gameComboBox.getSelectionModel().getSelectedItem().equals("All Games")){
				hangmanAllow.set(true);
				twisterAllow.set(true);
			}
			else if(gameComboBox.getSelectionModel().getSelectedItem().equals("Hangman")){
				hangmanAllow.set(true);
				twisterAllow.set(false);
			}
			else if(gameComboBox.getSelectionModel().getSelectedItem().equals("Twister")){
				hangmanAllow.set(false);
				twisterAllow.set(true);
			}


			ObservableList<Score> temp = FXCollections.observableArrayList();
			for(Score score: SearchController.wordNerdModel.scoreList){

				char []checkChar = searchTextField.getText().toCharArray();
				boolean check= true;

				for(char c: checkChar)
					if (score.getPuzzleWord().indexOf(c)<0)
						check=false;

				// Add to list only if filters are satisfied
				if(check && hangmanAllow.get() &&  score.getGameId()==0)
					temp.add(score);

				if(check && twisterAllow.get() &&  score.getGameId()==1)
					temp.add(score);
			}
			setUpScene(temp); // Refresh Table View
		});

		// binding for searchtextfield
		searchTextField.textProperty().addListener((observable, oldValue, newValue) -> {

			ObservableList<Score> temp = FXCollections.observableArrayList();
			for(Score score: SearchController.wordNerdModel.scoreList){

				char []checkChar = searchTextField.getText().toCharArray();
				boolean check= true;

				for(char c: checkChar)
					if (score.getPuzzleWord().indexOf(c)<0)
						check=false;

				if(check && hangmanAllow.get() &&  score.getGameId()==0)
					temp.add(score);

				if(check && twisterAllow.get() &&  score.getGameId()==1)
					temp.add(score);
			}
			setUpScene(temp); // Refresh Table View
		});
	}
}