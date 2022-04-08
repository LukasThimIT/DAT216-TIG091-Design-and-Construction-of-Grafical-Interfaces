package recipesearch;

import se.chalmers.ait.dat215.lab2.Recipe;
import se.chalmers.ait.dat215.lab2.RecipeDatabase;
import se.chalmers.ait.dat215.lab2.SearchFilter;
import java.util.List;

public class RecipeBackendController {
    //getRecipies är filterfunktionen
    RecipeDatabase db = RecipeDatabase.getSharedInstance();
    String country;
    String ingredient;
    String diff;
    int price;
    int time;
    public List<Recipe> getRecipes() {
        return db.search(new SearchFilter(diff, time, country, price, ingredient));
    }
    public void setCuisine(String cuisine){
        String country = cuisine;
    }
    public void setMainIngredient(String mainIngredient){
        String ingredient = mainIngredient;
    }
    public void setDifficulty(String difficulty){
        String diff = difficulty;
    }
    public void setMaxPrice(int maxPrice){
        int price = maxPrice;
    }
    public void setMaxTime(int maxTime){
        int time = maxTime;
    }
}
