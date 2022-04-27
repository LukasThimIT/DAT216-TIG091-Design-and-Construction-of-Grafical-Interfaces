
package recipesearch;
import javafx.application.Platform;
import javafx.scene.image.Image;
import javafx.util.Callback;
import recipesearch.RecipeListItem;

import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.FlowPane;
import se.chalmers.ait.dat215.lab2.Ingredient;
import se.chalmers.ait.dat215.lab2.Recipe;
import se.chalmers.ait.dat215.lab2.RecipeDatabase;
import recipesearch.RecipeBackendController;

public class RecipeSearchController implements Initializable {

    RecipeDatabase db = RecipeDatabase.getSharedInstance();
    RecipeBackendController RBC = new RecipeBackendController();
    //private RecipeSearchController parentController; //iffy


    
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
    @FXML private ImageView closeImageView;
    @FXML private SplitPane searchPane;
    @FXML private AnchorPane detailPane;
    @FXML private SplitPane recipeSearchSplitPane;
    @FXML private ImageView recipeDifficultyImg;
    @FXML private ImageView recipeTimeImg;
    @FXML private ImageView recipeIngredientImg;
    @FXML private ImageView recipeFlagImg;
    @FXML private Label recipeTime;
    @FXML private Label recipePrice;
    @FXML private Label recipeDescription;
    @FXML private Label recipeServings;
    @FXML private TextArea recipeInstructions;
    @FXML private TextArea recipeIngredients;

        @FXML
        public void closeRecipeView () {
            recipeSearchSplitPane.toFront();
        }

        public void openRecipeView (Recipe recipe){

            // System.out.println("openRecipeView");
            populateRecipeDetailView(recipe);
            detailPane.toFront();
        }

        @FXML
        public void closeButtonMouseEntered(){
            closeImageView.setImage(new Image(getClass().getClassLoader().getResourceAsStream(
            "RecipeSearch/resources/icon_close_hover.png")));
        }
        
        @FXML
        public void closeButtonMousePressed(){           
            closeImageView.setImage(new Image(getClass().getClassLoader().getResourceAsStream(
            "RecipeSearch/resources/icon_close_pressed.png")));
        }
        
        @FXML
        public void closeButtonMouseExited(){
            closeImageView.setImage(new Image(getClass().getClassLoader().getResourceAsStream(
            "RecipeSearch/resources/icon_close.png")));
        } 

        @FXML
        public void mouseTrap(Event event){
            event.consume();
        } 
    private Map<String, RecipeListItem> recipeListItemMap = new HashMap<String, RecipeListItem>();
    @Override
    public void initialize(URL url, ResourceBundle rb) {

        populateMainIngredientComboBox();
        populateCuisineComboBox();
        Platform.runLater(()->ingredientBox.requestFocus());

        for (Recipe recipe : RBC.getRecipes()) {
            RecipeListItem recipeListItem = new RecipeListItem(recipe, this);
            recipeListItemMap.put(recipe.getName(), recipeListItem);
        }

        updateRecipeList();
        
        //combobox main ingredient
        ingredientBox.getItems().addAll("Visa alla", "Kött", "Fisk", "Kyckling", "Vegetariskt");
        ingredientBox.getSelectionModel().select("Visa alla");
        ingredientBox.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {

                @Override
                public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                    //System.out.println(newValue);
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
                    //System.out.println(newValue);
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
        SpinnerValueFactory<Integer> valueFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 10000, 20, 5);
        priceBox.setValueFactory(valueFactory);
        priceBox.valueProperty().addListener(new ChangeListener<Integer>() {
            @Override
            public void changed(ObservableValue<? extends Integer> observable, Integer oldValue, Integer newValue) {
        
                if (priceBox.getValue() != null) {
                    //System.out.println(newValue);
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
                    if (value > 0) {
                        RBC.setMaxPrice(value);
                        updateRecipeList();
                    }
                }

            }
        });
        
        //time slider
        
        timeSlider.valueProperty().addListener(new ChangeListener<Number>() {

            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                //timeSliderLabel.setText(newValue.toString());
                if(newValue != null && !newValue.equals(oldValue) && !timeSlider.isValueChanging()) {
                    timeSliderLabel.setText(newValue.intValue() + " min");
                    RBC.setMaxTime(newValue.intValue());
                    updateRecipeList();
                }

            }
        });
    }

    private void populateMainIngredientComboBox () {
        Callback<ListView<String>, ListCell<String>> cellFactory = new Callback<ListView<String>, ListCell<String>>() {
            @Override
            public ListCell<String> call(ListView<String> p) {
                return new ListCell<String>() {

                    @Override
                    protected void updateItem(String item, boolean empty) {
                        super.updateItem(item, empty);
                        setText(item);
                        if (item == null || empty) {
                            setGraphic(null);
                        } else {
                            Image icon = null;
                            String iconPath;
                            try {
                                switch (item) {

                                    case "Kött":
                                        iconPath = "RecipeSearch/resources/icon_main_meat.png";
                                        icon = new Image(getClass().getClassLoader().getResourceAsStream(iconPath));
                                        break;
                                    case "Fisk":
                                        iconPath = "RecipeSearch/resources/icon_main_fish.png";
                                        icon = new Image(getClass().getClassLoader().getResourceAsStream(iconPath));
                                        break;
                                    case "Kyckling":
                                        iconPath = "RecipeSearch/resources/icon_main_chicken.png";
                                        icon = new Image(getClass().getClassLoader().getResourceAsStream(iconPath));
                                        break;
                                    case "Vegetariskt":
                                        iconPath = "RecipeSearch/resources/icon_main_veg.png";
                                        icon = new Image(getClass().getClassLoader().getResourceAsStream(iconPath));
                                        break;
                                }
                            } catch (NullPointerException ex) {
                                //This should never happen in this lab but could load a default image in case of a NullPointer
                            }
                            ImageView iconImageView = new ImageView(icon);
                            iconImageView.setFitHeight(12);
                            iconImageView.setPreserveRatio(true);
                            setGraphic(iconImageView);
                        }
                    }
                };
            }
        };
        ingredientBox.setButtonCell(cellFactory.call(null));
        ingredientBox.setCellFactory(cellFactory);
    }

    private void populateCuisineComboBox () {
        Callback<ListView<String>, ListCell<String>> cellFactory = new Callback<ListView<String>, ListCell<String>>() {
            @Override
            public ListCell<String> call(ListView<String> p) {
                return new ListCell<String>() {

                    @Override
                    protected void updateItem(String item, boolean empty) {
                        super.updateItem(item, empty);
                        setText(item);
                        if (item == null || empty) {
                            setGraphic(null);
                        } else {
                            Image icon = null;
                            String iconPath;
                            try {
                                switch (item) {

                                    case "Sverige":
                                        iconPath = "RecipeSearch/resources/icon_flag_sweden.png";
                                        icon = new Image(getClass().getClassLoader().getResourceAsStream(iconPath));
                                        break;
                                    case "Grekland":
                                        iconPath = "RecipeSearch/resources/icon_flag_greece.png";
                                        icon = new Image(getClass().getClassLoader().getResourceAsStream(iconPath));
                                        break;
                                    case "Indien":
                                        iconPath = "RecipeSearch/resources/icon_flag_india.png";
                                        icon = new Image(getClass().getClassLoader().getResourceAsStream(iconPath));
                                        break;
                                    case "Asien":
                                        iconPath = "RecipeSearch/resources/icon_flag_asia.png";
                                        icon = new Image(getClass().getClassLoader().getResourceAsStream(iconPath));
                                        break;
                                    case "Afrika":
                                        iconPath = "RecipeSearch/resources/icon_flag_africa.png";
                                        icon = new Image(getClass().getClassLoader().getResourceAsStream(iconPath));
                                        break;
                                    case "Frankrike":
                                        iconPath = "RecipeSearch/resources/icon_flag_france.png";
                                        icon = new Image(getClass().getClassLoader().getResourceAsStream(iconPath));
                                        break;
                                }
                            } catch (NullPointerException ex) {
                                //This should never happen in this lab but could load a default image in case of a NullPointer
                            }
                            ImageView iconImageView = new ImageView(icon);
                            iconImageView.setFitHeight(12);
                            iconImageView.setPreserveRatio(true);
                            setGraphic(iconImageView);
                        }
                    }
                };
            }
        };
        cuisineBox.setButtonCell(cellFactory.call(null));
        cuisineBox.setCellFactory(cellFactory);
    }
    
    public Image getCuisineFlag(String cuisine) {
        String iconPath;
        switch(cuisine){
            case "Sverige":
                iconPath = "RecipeSearch/resources/icon_flag_sweden.png";
                return new Image(getClass().getClassLoader().getResourceAsStream(iconPath));
            case "Grekland":
                iconPath = "RecipeSearch/resources/icon_flag_greece.png";
                return new Image(getClass().getClassLoader().getResourceAsStream(iconPath));
            case "Indien":
                iconPath = "RecipeSearch/resources/icon_flag_india.png";
                return new Image(getClass().getClassLoader().getResourceAsStream(iconPath));
            case "Asien":
                iconPath = "RecipeSearch/resources/icon_flag_asia.png";
                return new Image(getClass().getClassLoader().getResourceAsStream(iconPath));
            case "Afrika":
                iconPath = "RecipeSearch/resources/icon_flag_africa.png";
                return new Image(getClass().getClassLoader().getResourceAsStream(iconPath));
            case "Frankrike":
                iconPath = "RecipeSearch/resources/icon_flag_france.png";
                return new Image(getClass().getClassLoader().getResourceAsStream(iconPath));
            default:
                iconPath = "RecipeSearch/resources/icon_flag_sweden.png";
                return new Image(getClass().getClassLoader().getResourceAsStream(iconPath));
        }
    }

    public Image getDifficulty(String difficulty) {
        String iconPath;
        switch(difficulty){
            case "Lätt":
                iconPath = "RecipeSearch/resources/icon_difficulty_easy.png";
                return new Image(getClass().getClassLoader().getResourceAsStream(iconPath));
            case "Mellan":
                iconPath = "RecipeSearch/resources/icon_difficulty_medium.png";
                return new Image(getClass().getClassLoader().getResourceAsStream(iconPath));
            case "Svår":
                iconPath = "RecipeSearch/resources/icon_difficulty_hard.png";
                return new Image(getClass().getClassLoader().getResourceAsStream(iconPath));
            default:
                iconPath = "RecipeSearch/resources/icon_flag_india.png";
                return new Image(getClass().getClassLoader().getResourceAsStream(iconPath));
        }
    }
    public Image getIngredient(String difficulty) {
        String iconPath;
        switch(difficulty){
            case "Kött":
                iconPath = "RecipeSearch/resources/icon_main_meat.png";
                return new Image(getClass().getClassLoader().getResourceAsStream(iconPath));
            case "Fisk":
                iconPath = "RecipeSearch/resources/icon_main_fish.png";
                return new Image(getClass().getClassLoader().getResourceAsStream(iconPath));
            case "Kyckling":
                iconPath = "RecipeSearch/resources/icon_main_chicken.png";
                return new Image(getClass().getClassLoader().getResourceAsStream(iconPath));
            case "Vegetariskt":
                iconPath = "RecipeSearch/resources/icon_main_veg.png";
                return new Image(getClass().getClassLoader().getResourceAsStream(iconPath));
            default:
                iconPath = "RecipeSearch/resources/icon_flag_india.png";
                return new Image(getClass().getClassLoader().getResourceAsStream(iconPath));
        }
    }

    public Image getSquareImage(Image image){

        int x = 0;
        int y = 0;
        int width = 0;
        int height = 0;
    
        if(image.getWidth() > image.getHeight()){
            width = (int) image.getHeight();
            height = (int) image.getHeight();
            x = (int)(image.getWidth() - width)/2;
            y = 0;
        }
    
        else if(image.getHeight() > image.getWidth()){
            width = (int) image.getWidth();
            height = (int) image.getWidth();
            x = 0;
            y = (int) (image.getHeight() - height)/2;
        }
    
        else{
            //Width equals Height, return original image
            return image;
        }
        return new WritableImage(image.getPixelReader(), x, y, width, height);
    }

    private void populateRecipeDetailView(Recipe recipe){
        recipeDetailsImage.setImage(recipe.getFXImage());
        recipeDetailsLabel.setText(recipe.getName());
        recipeFlagImg.setImage(getCuisineFlag(recipe.getCuisine()));
        recipeDifficultyImg.setImage(getDifficulty(recipe.getDifficulty()));
        recipeIngredientImg.setImage(getIngredient(recipe.getMainIngredient()));
        //recipePrice.setText(Integer.toString(recipe.getPrice()));
        //recipeTime.setText(Integer.toString(recipe.getTime()));
        //recipeDescription.setText(recipe.getDescription());
        //recipeInstructions.setText(recipe.getInstruction());
        //recipeServings.setText(recipe.getServings() + " portioner");
        //recipeInstructions.setText(getIngredientsList(recipe));
    }

    private String getIngredientsList(Recipe recipe){
        List<Ingredient> ingredients = new ArrayList<>();
        String ingredientsString = "";
        for(Ingredient ingredient : recipe.getIngredients()){
            ingredientsString = ingredientsString + "\n" + ingredient.getAmount() + " " + ingredient.getUnit() + " " + ingredient.getName();
        }
        System.out.println(ingredientsString);
        return ingredientsString;
    }

    private void updateRecipeList(){
        recipeResults.getChildren().clear();
        List<Recipe> recipes = RBC.getRecipes();
        for (Recipe recipe : recipes) {
            //System.out.println(recipe);
            //RecipeListItem newItem = new RecipeListItem(recipe, this); //iffy
            recipeResults.getChildren().add(recipeListItemMap.get(recipe.getName()));
        }
        //recipeResults.getChildren().addAll(RBC.getRecipes());
    }
}