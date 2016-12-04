package antonkrug.eu;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

/**
 * The main part of the calculator doing the calculations in RPN style.
 * 
 * Inspired by:
 * http://www.meta-calculator.com/learning-lab/reverse-polish-notation-calculator.php
 * 
 * ON will clear display and the stack AC will clear just the display DEL will
 * delete 1 decimal from the display
 * 
 * MOD is modulos (will show remainder from division)
 * 
 * ENTER is to enter the number from display to the top of stack POP is the
 * other way around, it moves number from top of stack to the display
 * 
 * The server behaves as synchronized one so multiple clients share same
 * calculator state at the same which is kinda interesting :-) .
 *
 * Other operator work as expected, they READMEjust take the top 2 items from stack,
 * perform operation and push the result to the top.
 * 
 * This avoided worying about precedence and still have options to give priority
 * to some and have full control on the order. This can achieve same results
 * like using brackets, but without using them at all.
 * 
 * Some people still swear by this notation, mostly HP calculator fans.
 * 
 * See the Junit test to have some examples how it's working.
 * 
 * 
 * @author Anton Krug
 * @date 2016/12/01
 * @version 1.4
 */
public class CalculatorRpnRmiServer {

  public static void main(String args[]) {
    try {

      CalculatorRpn obj = new CalculatorRpn();
      Registry registry = LocateRegistry.createRegistry(1099);
      registry.rebind(Config.getInstance().getString("class"), obj);
      // rmic and rmiregistry is depreaced, so this registering is actually
      // prefered

      // default security manager will be good enough for simple calculator
      // project running at localhost
     

      System.out.println(Config.getInstance().getString("class")
          + Messages.getString("SERVER_WORKING"));
      
      final String uri = 
          "rmi://" + Config.getInstance().getString("servername") + 
          ":" + Config.getInstance().getString("port") +
          "/" + Config.getInstance().getString("class");
      
      System.out.println(Messages.getString("CLIENTS_WILL_USE") + uri);

    } catch (Exception e) {
      System.out.println(Messages.getString("SERVER_ERROR"));
      System.out.println(e.getMessage());
      e.printStackTrace();
      System.exit(1);
    }
  }
  
}
