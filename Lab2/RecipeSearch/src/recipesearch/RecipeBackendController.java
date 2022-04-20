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
    int price = 0;
    int time = 0;
    public List<Recipe> getRecipes() {
        //System.out.println(db.search(new SearchFilter(diff, 10, "Sverige", 100, "Kött")));
        return db.search(new SearchFilter(diff, time, "Sverige", price, ingredient));
    }
    public void setCuisine(String cuisine){
        country = cuisine;
    }
    public void setMainIngredient(String mainIngredient){
        ingredient = mainIngredient;
    }
    public void setDifficulty(String difficulty){
        if(difficulty == "Alla"){
            diff = null;
        }
        diff = difficulty;
    }
    public void setMaxPrice(int maxPrice){
        price = maxPrice;
    }
    public void setMaxTime(int maxTime){
        time = maxTime;
    }
}
