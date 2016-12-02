package antonkrug.eu;

import java.rmi.Naming;
import java.rmi.RemoteException;
import java.util.HashMap;
import java.util.Map;
import java.awt.*;
import java.awt.event.*;
import java.io.UncheckedIOException;

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
      System.out.println(Messages.getString("EXCEPTION") + e.getMessage());
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
           
    //do the exception catching and handling inside the lambda expressions and throw up on upper 
    //level to cascade the error up. create buttons starting from top left 
    for (Integer i=7; i<10; i++) addButton(i.toString(), null);
    addButton("ON",   ()->{try { calc.offOn();     } catch (RemoteException e) {throw new UncheckedIOException(e);}});
      
    for (Integer i=4; i<7;  i++) addButton(i.toString(), null);          
    addButton("AC",   ()->{try { calc.clear();     } catch (RemoteException e) {throw new UncheckedIOException(e);}});
      
    for (Integer i=1; i<4;  i++) addButton(i.toString(), null);          
    addButton("DEL",   ()->{try { calc.del();      } catch (RemoteException e) {throw new UncheckedIOException(e);}});
      
    addButton("0", null);
    addButton("+",     ()->{try { calc.plus();     } catch (RemoteException e) {throw new UncheckedIOException(e);}});
    addButton("-",     ()->{try { calc.minus();    } catch (RemoteException e) {throw new UncheckedIOException(e);}});
    addButton("*",     ()->{try { calc.multiply(); } catch (RemoteException e) {throw new UncheckedIOException(e);}});
      
    addButton("/",     ()->{try { calc.divide();   } catch (RemoteException e) {throw new UncheckedIOException(e);}});
    addButton("MOD",   ()->{try { calc.modulo();   } catch (RemoteException e) {throw new UncheckedIOException(e);}});
    addButton("POP",   ()->{try { calc.pop();      } catch (RemoteException e) {throw new UncheckedIOException(e);}});
    addButton("ENTER", ()->{try { calc.enter();    } catch (RemoteException e) {throw new UncheckedIOException(e);}});
    
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
  * An action has been performed. Find out what it was and handle it.
  */
  @Override
  public void actionPerformed(ActionEvent event) {
    String action = event.getActionCommand();
    try {
      //if it's number handle it as numberPress
      int number = Integer.parseInt(action);
      calc.numberPressed(number);
     
    } catch(Exception e) {
      //if it's not number just run the runnable
      if (actions.containsKey(action)) {
        actions.get(action).run();      
      }
      else {
        
        System.out.println(Messages.getString("UNSUPORTED_BUTTON"));
      }
    }
    redisplay();
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
