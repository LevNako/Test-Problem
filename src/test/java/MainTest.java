import org.junit.Test;

import java.util.ArrayList;
import java.util.TreeMap;

import static org.junit.Assert.*;

public class MainTest {

    @Test
    public void addProductToList() {
        //Size 2  because 2017-04-25 - 2 products and add 2017-03-25 - 1 products ; 2 - dates;
        String command = "add 2017-04-25 93 USA pr1";
        String command1 = "add 2017-04-25 62 USA pr2";
        String command2 = "add 2017-03-25 93 USA pr1";
        Main.addProductToList(command);
        Main.addProductToList(command1);
        TreeMap<String, ArrayList<Product>> stringArrayListTreeMap = Main.addProductToList(command2);
        int size = stringArrayListTreeMap.size();
        assertEquals(size , 2);
    }

    @Test
    public void clearFromProductList(){
        String command1 = "add 2017-04-25 62 USA pr2";
        String command3 = "add 2017-04-25 62 USA pr3";
        String command4 = "add 2017-04-25 62 USA pr4";
        String command2 = "add 2017-03-25 93 USA pr2";
        String command21 = "add 2017-03-25 93 USA pr21";
        String command0 = "clear 2017-04-25";
        Main.addProductToList(command1);
        Main.addProductToList(command2);
        Main.addProductToList(command3);
        Main.addProductToList(command4);
        Main.addProductToList(command21);
        TreeMap<String, ArrayList<Product>> stringArrayListTreeMap2 = Main.clearFromProductList(command0);
        int size1 = stringArrayListTreeMap2.size();
        assertEquals(size1,1);
    }

    @Test
    public void totalEur(){
        String command1 = "add 2017-04-25 62 UAH pr1";
        String command2 = "add 2017-04-25 93 UAH pr2";
        String command3 = "add 2017-04-25 5 EUR pr3";
        Main.addProductToList(command1);
        Main.addProductToList(command2);
        Main.addProductToList(command3);
        double moneyEUR = 62/30.58176 + 93/30.58176 +5;
        double total_eur = Main.totalEur("total EUR");
        assertEquals(moneyEUR,total_eur,0.02);
    }
}