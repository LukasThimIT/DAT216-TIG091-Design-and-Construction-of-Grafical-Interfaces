package recipesearch;

import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import se.chalmers.ait.dat215.lab2.Recipe;

import java.io.IOException;

public class RecipeListItem extends AnchorPane {
    private RecipeSearchController parentController;
    private Recipe recipe;

    @FXML private ImageView searchResultImg;
    @FXML private Label searchResultLabel;

    @FXML
    protected void onClick(Event event){
        //System.out.print("I onClick nu"); //Denna printas
        if (parentController == null) System.out.println("null");
        parentController.openRecipeView(recipe);
    }

    public RecipeListItem(Recipe recipe, RecipeSearchController recipeSearchController){
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("recipe_listitem.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);

        try {
            fxmlLoader.load();
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }

        //ÄR det nånting i denna som krånglar?

        this.recipe = recipe;
        this.parentController = recipeSearchController;

        searchResultImg.setImage(recipe.getFXImage());
        searchResultLabel.setText(recipe.getName());
    }
}
