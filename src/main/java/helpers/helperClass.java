package helpers;

import org.openqa.selenium.WebElement;

import java.util.ArrayList;
import java.util.List;

public class helperClass {

    public static boolean isSortedAlphanum(List<WebElement> allProducts, String filter){

        List<String> allProductNames = new ArrayList<String>();
        for(WebElement x : allProducts) {
            allProductNames.add(x.getText());
        }

        boolean isSorted = true;
        String productName, nextProductName;


        switch (filter){
            case "a-z":
                for(int i = 0; i < allProductNames.size() - 1; i++) {
                    productName = allProductNames.get(i);
                    nextProductName = allProductNames.get(i + 1);
                    if(productName.charAt(0) > nextProductName.charAt(0))
                        isSorted = false;
                }
                break;

            case "z-a":
                for(int i = 0; i < allProductNames.size() - 1; i++) {
                    productName = allProductNames.get(i);
                    nextProductName = allProductNames.get(i + 1);
                    if(productName.charAt(0) < nextProductName.charAt(0))
                        isSorted = false;
                }
                break;
        }

        return isSorted;
    }

    public static boolean isSortedNum(List<WebElement> allProducts, String filter){

        List<Float> allProductPrices = new ArrayList<Float>();
        for(WebElement x : allProducts) {
            allProductPrices.add(Float.parseFloat(x.getText().substring(1)));
        }

        boolean isSorted = true;
        Float productPrice, nextProductPrice;


        switch (filter){
            case "l-h":
                for(int i = 0; i < allProductPrices.size() - 1; i++) {
                    productPrice = allProductPrices.get(i);
                    nextProductPrice = allProductPrices.get(i + 1);
                    if(productPrice > nextProductPrice)
                        isSorted = false;
                }
                break;

            case "h-l":
                for(int i = 0; i < allProductPrices.size() - 1; i++) {
                    productPrice = allProductPrices.get(i);
                    nextProductPrice = allProductPrices.get(i + 1);
                    if(productPrice < nextProductPrice)
                        isSorted = false;
                }
                break;
        }

        return isSorted;
    }
}
