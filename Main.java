package budget;
//проверить загрузку по категориям
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

public class Main {
    static Scanner sc = new Scanner(System.in);
    static double income = 0;
    static Map<Category, Purchase> purchases = new HashMap<Category, Purchase>();

    public static void addIncome(){
        double income;
        System.out.println("Enter income:");
        income = Double.parseDouble(sc.next());
        Main.income += income;
        System.out.println("Income was added!\n");
    }

    public static Category newCategory(String line, int num){
        if (!line.isEmpty()) {
            switch (line.substring(line.indexOf(":") + 2)) {
                case "Food":
                    return Category.Food;
                case "Clothes":
                    return Category.Clothes;
                case "Entertainment":
                    return Category.Entertainment;
                default:
                    return Category.Other;
            }
        } else {
            if (num == 1) {
                return Category.Food;
            } else if (num == 2) {
                return Category.Clothes;
            } else if (num == 3) {
                return Category.Entertainment;
            } else {
                return Category.Other;
            }
        }
    }

    public static void addPurchase(){
        int numMenu;
        String name1;
        double sum;
        Category newCategory;
        boolean showMenu = true;

        while (showMenu) {
            Menu.printAddPurchaseMenu();
            try {
                numMenu = Integer.parseInt(sc.next());
                System.out.println();
                if (numMenu == 5) {
                    showMenu = false;
                } else {
                    StringBuilder name = new StringBuilder();
                    System.out.println("Enter purchase name:");
                    name1 = sc.next();
                    name.append(name1);
                    if (sc.hasNextLine()) {
                        String name2 = sc.nextLine();
                        name.append(name2);
                    }
                    System.out.println("Enter its price:");
                    sum = Double.parseDouble(sc.next());
                    newCategory = newCategory("", numMenu);
                    if (purchases.containsKey(newCategory)) {
                        purchases.get(newCategory).add(name.toString(), sum);
                    } else {
                        purchases.put(newCategory, new Purchase(name.toString(), sum));
                    }
                }
            } catch (Exception e) {
                System.out.println("Wrong parameter.");
            }
            if (showMenu) System.out.println();
        }
    }

    public static void showPurchaseAll() {
        double allSum = 0.0;
        System.out.println("ALL:");
        for (Map.Entry<Category, Purchase> entry : purchases.entrySet()) {
            System.out.println("Category: " + entry.getKey());
            entry.getValue().showPurchase(false);
            allSum += entry.getValue().countTotalSum();
        }
        if (allSum == 0) {
            System.out.println("Purchase list is empty");
        } else {
            System.out.println("Total sum: $" + allSum);
        }
    }

    public static void showPurchase(){
        boolean showMenu = true;
        int numMenu;

        if (purchases.isEmpty()) {
            System.out.println("Purchase list is empty!");
        } else {
            while (showMenu) {
                Menu.printShowPurchaseMenu();
                try {
                    numMenu = Integer.parseInt(sc.next());
                    System.out.println();
                    if (numMenu == 5) {
                        showPurchaseAll();
                    } else if (numMenu == 6) {
                        showMenu = false;
                    } else if (numMenu > 0 && numMenu < 5) {
                        Category newCategory;
                        newCategory = newCategory("", numMenu);
                        if (purchases.containsKey(newCategory)) {
                            purchases.get(newCategory).showPurchase(true);
                        } else {
                            System.out.println("Purchase list is empty\n");
                        }
                    }
                } catch (Exception e) {
                    System.out.println("Wrong parameter.");
                }
                if (showMenu) System.out.println();
            }
        }
    }

    public static void showBalance(){
        double purchaseSum = 0.0;
        double balance;
        for (Map.Entry<Category, Purchase> entry : purchases.entrySet()) {
            purchaseSum += entry.getValue().countTotalSum();
        }
        balance = income - purchaseSum;
        balance = (balance < 0) ? 0 : balance;
        System.out.println("Balance: $" + balance + "\n");
    }

    public static void saveBuget() {
        try {
            File file = new File("purchases.txt");
            //boolean newFile = file.createNewFile();
            FileWriter writer = new FileWriter(file);
            String balanceToWrite = "Balance: $" + income + "\n";
            writer.write(balanceToWrite);
            writer.flush();
            writer.close();
            if (!purchases.isEmpty()){
                FileWriter writer1 = new FileWriter(file, true);
                for (Map.Entry<Category, Purchase> entry : purchases.entrySet()) {
                    writer1.write("Category: " + entry.getKey() + "\n");
                    for (Map.Entry<String, Double> elem : entry.getValue().categoryList.entrySet()) {
                        writer1.write(String.valueOf(elem.getKey()) + "\n");
                    }//writer1.write("sum: " + String.valueOf(entry.getValue().sum) + "\n");
                }
                writer1.close();
            }
            System.out.println("Purchases were saved!\n");
        } catch (Exception e) {
            System.out.println("Couldn't save to file");
        }
    }

    public static void loadBudget() {
        StringBuilder categoryName = new StringBuilder();
        Category newCategory;
        int i = 0;

        try {
            List<String> fileText = Files.readAllLines(Paths.get("purchases.txt"));
            while(i < fileText.size()) {
                if (fileText.get(i).contains("Balance:")) {
                    income = Double.parseDouble(fileText.get(i).substring(fileText.get(i).indexOf('$') + 1));
                    i++;
                } else if (fileText.get(i).contains("Category:")) {
                    newCategory = newCategory(fileText.get(i), -1);
                    i++;
                    while(i < fileText.size()) {
                        if (fileText.get(i).contains("Category") || fileText.get(i).contains("Total sum"))
                            break;
                        else if (purchases.containsKey(newCategory)) {
                            purchases.get(newCategory).add(fileText.get(i));
                        } else {
                            purchases.put(newCategory, new Purchase(fileText.get(i)));
                        }
                        i++;
                    }
                } else if (fileText.get(i).contains("Total sum")){
                    i++;
                } else {
                    System.out.println("Strange");
                    i++;
                }
            }
            System.out.println("Purchases were loaded!\n");
        }
        catch(IOException ex){
            System.out.println(ex.getMessage());
        }
    }

    public static void sortPurchase(){
        int numMenu;
        int numMenu2;
        Category newCategory;
        boolean showMenu = true;

        while (showMenu) {
            Menu.printSortPurchaseMenu();
            try {
                numMenu = Integer.parseInt(sc.next());
                System.out.println();
                switch (numMenu) {
                    case 4:
                        showMenu = false;
                        break;
                    case 1:
                        Purchase allPurchases = new Purchase();
                        for (Category elem : Category.values()) {
                            if (purchases.containsKey(elem)) {
                                allPurchases.add(purchases.get(elem).categoryList);
                            }
                        }
                        new Purchase(allPurchases.sortPurchase()).showPurchase(true);
                        break;
                    case 2:
                        HashMap<String, Double> types = new HashMap<>();
                        double sum;
                        for (Category elem : Category.values()) {
                            sum = (purchases.containsKey(elem)) ? purchases.get(elem).countTotalSum() : 0;
                                types.put(elem.toString() + " $" + sum, sum);
                            }
                        Purchase types2 = new Purchase(types);
                        new Purchase(types2.sortPurchase()).showPurchase(true);
                        break;
                    case 3:
                        Menu.printSortType();
                        numMenu2 = Integer.parseInt(sc.next());
                        newCategory = newCategory("", numMenu2);
                        new Purchase(purchases.get(newCategory).sortPurchase()).showPurchase(true);
                        break;
                    default:
                        System.out.println("Wrong parameter.");
                }
            } catch (Exception e) {
                System.out.println("Wrong parameter.");
            }
            if (showMenu) System.out.println();
        }
    }

    public static void work() {
        boolean showMenu = true;
        int numMenu;

        while (showMenu) {
            Menu.printBasicMenu();
            try {
                numMenu = Integer.parseInt(sc.next());
                System.out.println();
                switch (numMenu) {
                    case 1:
                        addIncome();
                        break;
                    case 2:
                        addPurchase();
                        break;
                    case 3:
                        showPurchase();
                        break;
                    case 4:
                        showBalance();
                        break;
                    case 5:
                        saveBuget();
                        break;
                    case 6:
                        loadBudget();
                        break;
                    case 7:
                        sortPurchase();
                        break;
                    case 0:
                        showMenu = false;
                        System.out.print("Bye!\n");
                        break;
                    default:
                        System.out.println("Wrong parameter.\n");
                }
            } catch (Exception e) {
                System.out.println("Wrong parameter.\n");
            }
        }
    }

    public static void main(String[] args) {
        work();
    }
}
