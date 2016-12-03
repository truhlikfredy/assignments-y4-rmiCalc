package antonkrug.eu;

import java.rmi.Naming;
import java.rmi.RemoteException;
import java.util.HashMap;
import java.util.Map;
import java.awt.*;
import java.awt.event.*;

import javax.swing.*;
import javax.swing.border.EmptyBorder;

/**
 * Simple GUI client
 * 
 * @author Anton Krug
 * @date 01.12.2016
 * @version 1.2
 */
public class CaclculatorClient extends JFrame implements ActionListener {
  
  private static final long                  serialVersionUID = 6743513674592292079L; 
  private static       CalculatorInterface            calc;
  private              JFrame                frame;
  private              JTextField            display;
  private              JLabel                status;
  private              Map<String, Runnable> actions;
  private              JPanel                buttonPanel;
  

  public static void main(String[] args) {
    final String uri = 
        "rmi://" + Config.getInstance().getString("servername") + 
        ":" + Config.getInstance().getString("port") +
        "/" + Config.getInstance().getString("class");
    
    try {   
      calc = (CalculatorInterface)Naming.lookup(uri);
      
      System.out.println(Messages.getString("CLIENT_CONNECTED"));
      new CaclculatorClient();
    }
    catch (Exception e) {
      System.out.println(Messages.getString("RMI_URI") + uri);
      System.out.println(Messages.getString("EXCEPTION_CONNECT"));
      System.out.println(e.getMessage());
      e.printStackTrace();
      System.exit(1);
    }
  }
  
  
  
  /**
   * Prepare the UI elements for display and attach runnables to buttons
   * @throws Exception
   */
  public CaclculatorClient() throws Exception {   
    actions = new HashMap<>();    
    
    frame = new JFrame(calc.getTitle());
    
    JPanel framePane = (JPanel) frame.getContentPane();
    
    JPanel contentPane = new JPanel();
    contentPane.setLayout(new BorderLayout(8, 8));
    contentPane.setBorder(new EmptyBorder(10, 10, 10, 10));

    display = new JTextField();
    contentPane.add(display, BorderLayout.NORTH);

    buttonPanel = new JPanel(new GridLayout(5, 4));
           
    //this will handle everything null pointer, RemoteException etc...
    //do the exception catching and handling inside the lambda expressions and throw up on upper
    //level to cascade the error up. create buttons starting from top left
    for (Integer i=7; i<10; i++) addButton(i.toString(), null);
    addButton("ON",   ()->{try { calc.offOn();     } catch (Exception e) {throw new RuntimeException(e);}});
      
    for (Integer i=4; i<7;  i++) addButton(i.toString(), null);          
    addButton("AC",   ()->{try { calc.clear();     } catch (Exception e) {throw new RuntimeException(e);}});
      
    for (Integer i=1; i<4;  i++) addButton(i.toString(), null);          
    addButton("DEL",   ()->{try { calc.del();      } catch (Exception e) {throw new RuntimeException(e);}});
      
    addButton("0", null);
    addButton("+",     ()->{try { calc.plus();     } catch (Exception e) {throw new RuntimeException(e);}});
    addButton("-",     ()->{try { calc.minus();    } catch (Exception e) {throw new RuntimeException(e);}});
    addButton("*",     ()->{try { calc.multiply(); } catch (Exception e) {throw new RuntimeException(e);}});
      
    addButton("/",     ()->{try { calc.divide();   } catch (Exception e) {throw new RuntimeException(e);}});
    addButton("MOD",   ()->{try { calc.modulo();   } catch (Exception e) {throw new RuntimeException(e);}});
    addButton("POP",   ()->{try { calc.pop();      } catch (Exception e) {throw new RuntimeException(e);}});
    addButton("ENTER", ()->{try { calc.enter();    } catch (Exception e) {throw new RuntimeException(e);}});

    contentPane.add(buttonPanel, BorderLayout.CENTER);

    status = new JLabel();
    
    contentPane.add(status, BorderLayout.SOUTH);
    
    framePane.setLayout(new BorderLayout(8, 8));
    framePane.add(new JLabel(new ImageIcon(getClass().getResource("/resources/logo.png"))),BorderLayout.NORTH);
    framePane.add(contentPane);


    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

    frame.pack();
    frame.setVisible(true);   
    
    //shutdown everything after closing the window
    frame.addWindowListener(new WindowAdapter() {
      public void windowClosing(WindowEvent e) {
        System.exit(0);
      }
    });
    
    redisplay();  
  }
 
  
  /**
   * Add a button to the button panel.
   */
  private void addButton(String buttonText, Runnable action) {
    actions.put(buttonText,action);
    JButton button = new JButton(buttonText);
    button.addActionListener(this);
    buttonPanel.add(button);
  }
  
  
  /**
   * If the button is number convert it into a number and handle it as number press
   * @return
   */
  private boolean handleNumbers(String actionText) {
    int number = -1;
    
    try {
      number = Integer.parseInt(actionText);     
    } catch(Exception e) {
      //any problem with parsing number means, not a good number
      return false;
    }
    
    try {
      calc.numberPressed(number);      
    } catch (Exception e) {
      System.out.println(Messages.getString("CLIENT_RMI_ERROR"));
      System.out.println(e.getMessage());
      System.exit(1);
    }
   
    //it was a number and was handled as such
    return true;
  }
  
  
  /**
   * Just run runnable for all the non-number events
   */
  private void handleOperators(String actionText) {
    
    //check if we have runnable for given key
    if (actions.containsKey(actionText)) {
      try {
        actions.get(actionText).run();
      }
      catch(Exception rmiE) {
        System.out.println(Messages.getString("CLIENT_RMI_ERROR"));
        System.out.println(rmiE.getMessage());
        System.exit(1);
      }
    }
    else {
      System.out.println(Messages.getString("UNSUPORTED_BUTTON"));
    }
    
  }
  
  
  /**
  * An action has been performed. Find out what it was and handle it.
  */
  @Override
  public void actionPerformed(ActionEvent event) {
    String actionText = event.getActionCommand();
    
    if (!handleNumbers(actionText)) {
      //if it's not number then it must be the operator, run the runnable associated with it
      handleOperators(actionText);
    }
    
    redisplay(); //update the calculator display
  }
  
  
  /**
   * Update the interface display to show the current value of the calculator.
   */
  private void redisplay() {
    try {
      display.setText(new Integer(calc.getDisplayValue()).toString());
      status.setText(calc.getStackContent());
    } catch (RemoteException e) {
      e.printStackTrace();
      System.exit(1);
    }
  }  
  
  
}
