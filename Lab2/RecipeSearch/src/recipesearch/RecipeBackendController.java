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
        return db.search(new SearchFilter(diff, time, country, price, ingredient));
    }

    public void setCuisine(String cuisine){
        if(cuisine.equals("Visa alla") || cuisine.equals("Sverige") || cuisine.equals("Grekland") || cuisine.equals("Indien") || cuisine.equals("Asien") || cuisine.equals("Afrika") || cuisine.equals("Frankrike")){
            country = cuisine;
        }
        else{
            country = null;
        }
    }
    public void setMainIngredient(String mainIngredient){
        if(mainIngredient.equals("Visa alla") || mainIngredient.equals("Kött") || mainIngredient.equals("Fisk") || mainIngredient.equals("Kyckling") || mainIngredient.equals("Vegetariskt")){
            ingredient = mainIngredient;
        }
        else{
            ingredient = null;
        }
    }
    public void setDifficulty(String difficulty){
        if(difficulty.equals("Lätt") || difficulty.equals("Mellan") || difficulty.equals("Svår")){
            diff = difficulty;
        }
        else{
            diff = null;
        }
    }

    /*public void setCuisine(String cuisine){
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
    }*/
    public void setMaxPrice(int maxPrice){
        //System.out.println(maxPrice);
        if (maxPrice <= 1) {
            price = 0;
        }
        else {price = maxPrice;}
    }
    public void setMaxTime(int maxTime){
        //int temp = Integer.valueOf(maxTime);
        //System.out.println(temp);
        if(maxTime >= 10 && maxTime <= 150 && maxTime % 10 == 0) {
            time = maxTime;
        }
        else {time = 0;}
    }
}
