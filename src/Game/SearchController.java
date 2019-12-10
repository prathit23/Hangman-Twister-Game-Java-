/**
 * Prathit Pannase: ppannase
 */
package Game;

public class SearchController extends WordNerdController {
	
	static WordNerdModel wordNerdModel;
	SearchView searchView;
	@Override
	void startController() {

		// Contoller to start SearchView Functionality
		wordNerdModel = new WordNerdModel();
		searchView = new SearchView();
		wordNerdModel.readScore();
		searchView.setUpScene(wordNerdModel.scoreList);
		searchView.setupView();

	}

	@Override
	void setupBindings() {
		// Mentioned in UML, not required
	}

}
