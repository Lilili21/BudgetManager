package budget;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class Purchase {
    HashMap<String, Double> categoryList = new HashMap<>();

    public Purchase(){
    }
    public Purchase(HashMap<String, Double> list) {
        this.categoryList = list;
    }
    public Purchase(String name, Double sum) {
        categoryList.put(name + " $" + sum, sum);
        System.out.println("Purchase was added!");
    }
    public Purchase(String line) {
        Double sum = Double.parseDouble(line.substring(line.lastIndexOf('$') + 1));
        categoryList.put(line, sum);
        //System.out.println("Purchase was added!");
    }

    public void add(String name, Double sum){
        categoryList.put(name + " $" + sum, sum);
        System.out.println("Purchase was added!");
    }
    public void add(String line){
        Double sum = Double.parseDouble(line.substring(line.lastIndexOf('$') + 1));
        categoryList.put(line, sum);
        //System.out.println("Purchase was added!");
    }
    public void add(HashMap<String, Double> list){
        for(Map.Entry<String, Double> elem : list.entrySet()) {
            categoryList.put(elem.getKey(), elem.getValue());
        }
    }

    public void showPurchase(boolean showSum){
        double sum = 0.0;
        for (Map.Entry<String, Double> elem : categoryList.entrySet()){
            System.out.println(elem.getKey());
            sum += (double)elem.getValue();
        }
        if (showSum) System.out.println("Total sum: $" + sum);
    }

    public double countTotalSum(){
        double sum = 0.0;
        for (Map.Entry<String, Double> elem : categoryList.entrySet()){
            sum += (double)elem.getValue();
        }
        return sum;
    }

    public HashMap<String, Double> sortPurchase(){
        return categoryList.entrySet()
                .stream()
                .sorted((Map.Entry.<String, Double>comparingByValue().reversed()))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e1, LinkedHashMap::new));

    }
}
