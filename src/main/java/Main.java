import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
import java.util.*;

public class Main {
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static TreeMap<String, ArrayList<Product>> product_list = new TreeMap<>();
    public static void main(String[] args){
        menu();
    }

        public static void menu(){
            System.out.println("If you want to add some product please enter command 'add 2017-04-25 12 USD Jogurt' - for example");
            System.out.println("If you want to see the list of all expenses sorted by date enter command 'list'");
            System.out.println("If you want to clear all products by date enter command 'clear 2017-04-25' - for example");
            System.out.println("If you want to see total EUR enter command 'total EUR' - any currency");
            System.out.println("If you want to exit enter command 'exit'");
            System.out.println();
            Scanner in = new Scanner(System.in);
            String command = in.nextLine();
            String[] commandMain = command.split(" ");
            if(commandMain[0].equals("list")){
                printProductList();
                menu();
            }
            else if(commandMain[0].equals("add")){
                addProductToList(command);
                menu();
            }
            else if(commandMain[0].equals("clear")){
                clearFromProductList(command);
                menu();
            }else if(commandMain[0].equals("total") && commandMain.length==2){
                totalEur(command);
                menu();
            }
            else if(commandMain[0].equals("exit")){
                System.out.println("Hope to see you again");
            }
            else{
                System.out.println("!You entered nonexistent command!");
                menu();
            }

        }

        public static void printProductList()
        {
            if (product_list.isEmpty()) {
                System.out.println("Sorry but your list of products is empty");
            } else {
                Set<Map.Entry<String, ArrayList<Product>>> entrySet = product_list.entrySet();
                Iterator<Map.Entry<String, ArrayList<Product>>> entryIterator = entrySet.iterator();
                while (entryIterator.hasNext()) {
                    Map.Entry<String, ArrayList<Product>> next = entryIterator.next();
                    System.out.println(next.getKey());
                    ArrayList<Product> products = next.getValue();
                    for (Product product : products) {
                        System.out.println(product.toString());
                    }
                    System.out.println();
                }
            }
        }

        public static TreeMap<String, ArrayList<Product>> addProductToList (String toadd)
        {
            int k = 0;
            String[] command = toadd.split(" ");
            char[] chars = command[1].toCharArray();
            if(chars[4]!='-' || chars[7]!='-'){
                System.out.println("You didn't enter date");
                return null;
            }
            String[] dateSplit = command[1].split("-");
            int month = Integer.parseInt(dateSplit[1]);
            int day = Integer.parseInt(dateSplit[2]);
            if(month > 12 || month < 0 || day >32 || day <0){
                System.out.println("You didn't enter date");
                return null;
            }

            if(command.length>4){
                for (int i = 5 ; i < command.length; i++){
                    command[4]+=" "+command[i];
                }
            }
            try {
                Set<Map.Entry<String, ArrayList<Product>>> entrySet = product_list.entrySet();
                Iterator<Map.Entry<String, ArrayList<Product>>> iterator = entrySet.iterator();
                while (iterator.hasNext()) {
                    Map.Entry<String, ArrayList<Product>> next = iterator.next();
                    if (next.getKey().equals(command[1])) {
                        k++;
                        ArrayList<Product> productArrayList = next.getValue();
                        productArrayList.add(new Product(Double.parseDouble(command[2]), command[3], command[4]));
                    }
                }
                if (k == 0) {
                    ArrayList<Product> pr_lst = new ArrayList<>();
                    pr_lst.add(new Product(Double.parseDouble(command[2]), command[3], command[4]));
                    product_list.put(command[1], pr_lst);
                }
                printProductList();
                return product_list;
            }
            catch (Exception e){
                System.out.println("Sorry something went wrong");
                System.out.println("Please reenter your command");
            }
            return null;
        }

        public static TreeMap<String, ArrayList<Product>> clearFromProductList(String toremove){
            String[] command = toremove.split(" ");
            if(command.length>2){
                System.out.println("You entered something wrong , please reenter");
                menu();
            }
            try{
                String data = command[1];
                Set<Map.Entry<String, ArrayList<Product>>> entrySet = product_list.entrySet();
                Iterator<Map.Entry<String, ArrayList<Product>>> iterator = entrySet.iterator();
                while(iterator.hasNext()){
                    Map.Entry<String, ArrayList<Product>> next = iterator.next();
                    if(next.getKey().equals(data)){
                        iterator.remove();
                    }
                }
                printProductList();
                return product_list;
            }
            catch (Exception e)
            {
                System.out.println("Sorry something went wrong");
                System.out.println("Please reenter your command");
            }
            return null;
        }

        public static Info getInfo() throws IOException {
        URLConnection connection = new URL("http://data.fixer.io/api/latest?access_key=6a2e02e88b710567a82afe14ff6a037d&format=1").openConnection();
        Scanner scanner = new Scanner(connection.getInputStream());
        scanner.useDelimiter("\\Z");
        String s = scanner.next();
        Info date1 = GSON.fromJson(s,Info.class);
        return date1;
        }

        public static double totalEur(String command){
        int i = 0;
            try {
                String[] split = command.split(" ");
                String curr = split[1];
                double sum = 0;
                Info info = getInfo();
                HashMap<String, Double> rates = info.getRates();
                Set<Map.Entry<String, ArrayList<Product>>> entrySet = product_list.entrySet();
                Iterator<Map.Entry<String, ArrayList<Product>>> iterator = entrySet.iterator();
                while (iterator.hasNext()){
                    Map.Entry<String, ArrayList<Product>> next = iterator.next();
                    ArrayList<Product> value = next.getValue();
                    for (Product product : value) {
                        String currency = product.getCurrency();
                        double price = product.getPrice();
                        Set<Map.Entry<String, Double>> entries = rates.entrySet();
                        Iterator<Map.Entry<String, Double>> iteratorRates = entries.iterator();
                        while (iteratorRates.hasNext()){
                            Map.Entry<String, Double> nextRates = iteratorRates.next();
                            if(currency.equals(nextRates.getKey())){
                                sum+=price/nextRates.getValue();
                            }
                        }
                    }
                }
                Set<Map.Entry<String, Double>> entriesCurr = rates.entrySet();
                Iterator<Map.Entry<String, Double>> iter = entriesCurr.iterator();
                while (iter.hasNext()){
                    Map.Entry<String, Double> next = iter.next();
                    if (next.getKey().equals(curr)){
                        i++;
                        sum*=next.getValue();
                        break;
                    }
                }
                if(i!=0){
                System.out.println();
                System.out.println(sum +" "+ curr);
                System.out.println();
                return sum;
                }
                else{
                    System.out.println("You entered nonexistent currency");
                    System.out.println();
                    return 0;
                }

            } catch (IOException e) {
                System.out.println("Sorry , error , please restart program");
            }
            return 0;
        }
}
