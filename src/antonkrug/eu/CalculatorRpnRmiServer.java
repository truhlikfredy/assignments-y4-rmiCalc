package antonkrug.eu;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.Stack;

/**
 * The main part of the calculator doing the calculations in RPN style.
 * 
 * Inspired by:
 * http://www.meta-calculator.com/learning-lab/reverse-polish-notation-calculator.php
 * 
 * ON	 will clear display and the stack
 * AC 		 will clear just the display
 * DEL 	 will delete 1 decimal from the display
 * 
 * MOD     is modulos (will show remainder from division)
 * 
 * ENTER 		 is to enter the number from display to the top of stack
 * POP     is the other way around, it moves number from top of stack to the display
 * 
 * The server is kind of synchronized one so multiple clients share same calculator 
 * state at the same which is kinda interesting. 
 *
 * Other operator work as expected, they just take the top 2 items from stack, 
 * perform operation and push the result to the top.
 * 
 * This avoided worying about precedence and still have options to give priority 
 * to some and have full control on the order. This can achieve same results like
 * using brackets, but without using them at all.
 * 
 * Some people still swear by this notation, mostly HP calculator fans. 
 * 
 * See the Junit test to have some examples how it's working.
 * 
 * 
 * @author Anton Krug
 * @date 2016/12/01
 * @version 1.3
 */
public class CalculatorRpnRmiServer extends UnicastRemoteObject implements CalculatorInterface {

  private static final long     serialVersionUID = 4721489200606532753L;
  private int										displayValue;
	private Stack<Integer>				rpn;
	private static final boolean	VERBOSE	= true;

	
  public static void main(String args[]) {
    try {
   
      CalculatorRpnRmiServer obj = new CalculatorRpnRmiServer();     
      Registry registry = LocateRegistry.createRegistry( 1099 );
      registry.rebind( Config.getInstance().getString("class"), obj );
      //rmic and rmiregistry is depreaced, so this registering is actually prefered
      
      //default security manager will be good enough for simple calculator project running at localhost
      
      System.out.println(Config.getInstance().getString("class") + Messages.getString("SERVER_WORKING"));
      
      
    }
    catch (Exception e) {
      System.out.println(Messages.getString("SERVER_ERROR") + e.getMessage());
      e.printStackTrace();
      System.exit(1);
    }
  }
     
	/**
	 * All allowed operators
	 */
	public enum Operator {
		PLUS, MINUS, MULTIPLY, DIVIDE, MODULO;
	}

	/**
	 * Create a CalcEngine instance. Initialise its state so that it is ready for
	 * use.
	 */
	public CalculatorRpnRmiServer() throws RemoteException {
	  super();
		offOn();
	}

	/**
	 * Will print content of the stack into console
	 * @throws RemoteException 
	 */
  private void printStack() throws RemoteException {
    if (VERBOSE) {
      System.out.println(getStackContent());
    }    
  }
	
	/**
	 * Return the value that should currently be displayed on the calculator
	 * display.
	 */
	public int getDisplayValue() throws RemoteException {
		return (displayValue);
	}

	/**
	 * A number button was pressed. Do whatever you have to do to handle it. The
	 * number value of the button is given as a parameter.
	 */
	public void numberPressed(int number) throws RemoteException {
		displayValue = displayValue * 10 + number;
	}

	/**
	 * Will perform actions depending which operator will be given
	 * 
	 * @param operator
	 */
	private void evaluateStack(Operator operator) throws RemoteException {
		if (VERBOSE) System.out.println(operator);

		//if stack is not big enough do not do anything yet
		if (rpn.size()<2) return;
		
		switch (operator) {
			case PLUS:
				rpn.push(rpn.pop() + rpn.pop());
				break;

			case MINUS:
				rpn.push(-rpn.pop() + rpn.pop());
				break;

			case MULTIPLY:
				rpn.push(rpn.pop() * rpn.pop());
				break;

			case DIVIDE:
			  if (rpn.peek()>0) {
			    int numerator = rpn.pop();
			    rpn.push(rpn.pop() / numerator);
			  }
				break;

			case MODULO:
				int modNumerator = rpn.pop();
				rpn.push(rpn.pop() % modNumerator);
				break;

			default:
				System.out.println("Unsuported operator sent");
				break;
		}
		displayValue = rpn.peek();
    printStack();
	}

	@Override
	public void enter() throws RemoteException {
		rpn.push(displayValue);
		displayValue = 0;
    printStack();
	}

	/**
	 * The 'plus' button was pressed.
	 */
	@Override
	public void plus() throws RemoteException {
		evaluateStack(Operator.PLUS);
	}

	/**
	 * The 'minus' button was pressed.
	 */
	@Override
	public void minus() throws RemoteException {
		evaluateStack(Operator.MINUS);
	}

	/**
	 * The * button pressed
	 */
	@Override
	public void multiply() throws RemoteException {
		evaluateStack(Operator.MULTIPLY);
	}

	/**
	 * The / button pressed
	 */
	@Override
	public void divide() throws RemoteException {
		evaluateStack(Operator.DIVIDE);
	}

	/**
	 * The 'P' button was pressed.
	 */
	@Override
	public void pop() throws RemoteException {
		if (rpn.size() > 0) {
			displayValue = rpn.pop();
		}
    printStack();
	}

	/**
	 * The 'C' (clear) button was pressed.
	 */
	@Override
	public void clear() throws RemoteException {
		displayValue = 0;
    printStack();
	}

	/**
	 * Return the title of this calculation engine.
	 */
	@Override
	public String getTitle() throws RemoteException {
		return ("RPN RMI Calculator");
	}

	/**
	 * Return the author of this engine. This string is displayed as it is, so it
	 * should say something like "Written by H. Simpson".
	 */
	@Override
	public String getAuthor() throws RemoteException {
		return ("Anton Krug");
	}

	/**
	 * Return the version number of this engine. This string is displayed as it
	 * is, so it should say something like "Version 1.1".
	 */
	@Override
	public String getVersion() throws RemoteException {
		return ("RPN v1.3");
	}

	@Override
	public void equals() throws RemoteException {
		// not used by this implementation of calculator
	}

	@Override
	public void offOn() throws RemoteException {
		displayValue = 0;
		rpn = new Stack<>();
		printStack();
	}

	@Override
	public void del() throws RemoteException {
		displayValue = displayValue / 10;
	}

	@Override
	public void modulo() throws RemoteException {
		evaluateStack(Operator.MODULO);
	}

  @Override
  public String getStackContent() throws RemoteException {
    return Messages.getString("STACK_CONTENT") + rpn;
  }

}
