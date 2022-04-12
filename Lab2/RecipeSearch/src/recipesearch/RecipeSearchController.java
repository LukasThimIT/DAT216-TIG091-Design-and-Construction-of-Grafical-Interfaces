
package recipesearch;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.FlowPane;
import se.chalmers.ait.dat215.lab2.Recipe;
import se.chalmers.ait.dat215.lab2.RecipeDatabase;
import recipesearch.RecipeBackendController;

public class RecipeSearchController implements Initializable {

    RecipeDatabase db = RecipeDatabase.getSharedInstance();
    RecipeBackendController RBC = new RecipeBackendController();
    private RecipeSearchController parentController; //iffy
    
    @FXML private FlowPane recipeResults;
    @FXML private ComboBox ingredientBox;
    @FXML private ComboBox cuisineBox;
    @FXML private RadioButton diffButtonAll;
    @FXML private RadioButton diffButtonEasy;
    @FXML private RadioButton diffButtonMid;
    @FXML private RadioButton diffButtonHard;
    @FXML private Spinner priceBox;
    @FXML private Slider timeSlider;
    @FXML private Label timeSliderLabel;
    @FXML private Label recipeDetailsLabel;
    @FXML private ImageView recipeDetailsImage;
    @FXML private AnchorPane searchPane;
    @FXML private AnchorPane detailPane;
    @FXML private SplitPane recipeSearchSplitPane;

    public RecipeSearchController() {
    }

    @FXML
     public void closeRecipeView(){
        recipeSearchSplitPane.toFront();
    }

     public void openRecipeView(Recipe recipe){
        System.out.println("openRecipeView");
        populateRecipeDetailView(recipe);
        detailPane.toFront();
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        updateRecipeList();
        
        //combobox main ingredient
        ingredientBox.getItems().addAll("Visa alla", "Kött", "Fisk", "Kyckling", "Vegetariskt");
        ingredientBox.getSelectionModel().select("Visa alla");
        ingredientBox.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {

                @Override
                public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                    RBC.setMainIngredient(newValue);
                    updateRecipeList();
            }
        });

            //combobox Cuisine
            cuisineBox.getItems().addAll("Visa alla", "Sverige", "Grekland", "Indien", "Asien", "Afrika", "Frankrike");
            cuisineBox.getSelectionModel().select("Visa alla");
            cuisineBox.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {

                @Override
                public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                            RBC.setCuisine(newValue);
                            updateRecipeList();
            }
        });

        //radiobuttons
        ToggleGroup difficultyToggleGroup = new ToggleGroup();
        diffButtonAll.setToggleGroup(difficultyToggleGroup); 
        diffButtonEasy.setToggleGroup(difficultyToggleGroup); 
        diffButtonMid.setToggleGroup(difficultyToggleGroup); 
        diffButtonHard.setToggleGroup(difficultyToggleGroup); 

        diffButtonAll.setSelected(true); 
        difficultyToggleGroup.selectedToggleProperty().addListener(new ChangeListener<Toggle>() {

            @Override
            public void changed(ObservableValue<? extends Toggle> observable, Toggle oldValue, Toggle newValue) {
        
                if (difficultyToggleGroup.getSelectedToggle() != null) {
                    RadioButton selected = (RadioButton) difficultyToggleGroup.getSelectedToggle();
                    RBC.setDifficulty(selected.getText());
                    updateRecipeList();
                }
            }
        });

        //Spinner
        SpinnerValueFactory<Integer> valueFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 10000, 20, 5); 
        priceBox.setValueFactory(valueFactory);
        priceBox.valueProperty().addListener(new ChangeListener<Integer>() {
            @Override
            public void changed(ObservableValue<? extends Integer> observable, Integer oldValue, Integer newValue) {
        
                if (priceBox.getValue() != null) {
                    RBC.setMaxPrice(newValue);
                    updateRecipeList();
                }
            }
        });
        priceBox.focusedProperty().addListener(new ChangeListener<Boolean>() {

            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
        
                if(newValue){
                    //focusgained - do nothing
                }
                else{
                    Integer value = Integer.valueOf(priceBox.getEditor().getText());
                    RBC.setMaxPrice(value);
                    updateRecipeList();
                }
        
            }
        });
        
        //time slider
        
        timeSlider.valueProperty().addListener(new ChangeListener<Number>() {

            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                //timeSliderLabel.setText(newValue.toString());
                if(newValue.intValue() % 10 == 0) { //Detta är en idiotlösning
                    String newValStr = newValue.toString(); //Varför måste man flytta ut detta föt att ddä ska funka?
                    timeSliderLabel.setText(newValStr);
                }
                if(newValue != null && !newValue.equals(oldValue) && !timeSlider.isValueChanging()) {
                    RBC.setMaxTime(newValue.intValue());
                    updateRecipeList();
                }

            }
        });
    }

    private void populateRecipeDetailView(Recipe recipe){
        recipeDetailsLabel.setText(recipe.getName());
        recipeDetailsImage.setImage(recipe.getFXImage());
    }

    private void updateRecipeList(){
        recipeResults.getChildren().clear();
        List<Recipe> recipes = RBC.getRecipes();
        for (Recipe recipe : recipes) {
            //System.out.println(recipe);
            RecipeListItem newItem = new RecipeListItem(recipe, parentController); //iffy
            recipeResults.getChildren().add(newItem);
        }
        //recipeResults.getChildren().addAll(RBC.getRecipes());
    }

}