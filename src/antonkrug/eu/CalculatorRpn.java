package antonkrug.eu;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.Stack;

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
 * Other operator work as expected, they just take the top 2 items from stack,
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
 * @version 1.3
 */
public class CalculatorRpn extends UnicastRemoteObject implements CalculatorInterface {

  /**
   * All allowed operators
   */
  public enum Operator {
    PLUS, MINUS, MULTIPLY, DIVIDE, MODULO;
  }
  private static final long serialVersionUID = 4721489200606532753L;
  private int displayValue;
  private Stack<Integer> rpn;


  private static final boolean VERBOSE = true;

  /**
   * Create a Calculator instance. Initialise its state so that it is ready for
   * use.
   */
  public CalculatorRpn() throws RemoteException {
    super();
    offOn();
  }


  /**
   * The 'AC' (clear) button was pressed.
   */
  @Override
  public synchronized void clear() throws RemoteException {
    displayValue = 0;
    printStack();
  }


  /**
   * Remove one digit from the display
   */
  @Override
  public synchronized void del() throws RemoteException {
    displayValue = displayValue / 10;
  }


  /**
   * The / button pressed
   */
  @Override
  public synchronized void divide() throws RemoteException {
    evaluateStack(Operator.DIVIDE);
  }


  /**
   * Add to the top of the stack, when the Enter button is pressed
   */
  @Override
  public synchronized void enter() throws RemoteException {
    rpn.push(displayValue);
    displayValue = 0;
    printStack();
  }

  /**
   * Equals is not used in RPN calculators
   */
  @Override
  public synchronized void equals() throws RemoteException {
    // not used by this implementation of calculator
  }


  /**
   * Will perform actions depending which operator will be given
   * 
   * @param operator
   */
  private synchronized void evaluateStack(Operator operator) throws RemoteException {
    if (VERBOSE) System.out.println(operator);

    // if stack is not big enough do not do anything yet
    if (rpn.size() < 2) {
      System.out.println(Messages.getString("SERVER_STACK_SMALL"));
      return;
    }

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
      if (rpn.peek() != 0) {
        int numerator = rpn.pop();
        rpn.push(rpn.pop() / numerator);
      }
      else {
        System.out.println(Messages.getString("DIVIDE_ZERO"));
      }
      break;

    case MODULO:
      int modNumerator = rpn.pop();
      rpn.push(rpn.pop() % modNumerator);
      break;

    default:
      System.out.println(Messages.getString("SERVER_WRONG_OP"));
      break;
    }
    displayValue = rpn.peek();
    printStack();
  }


  /**
   * Return the author of this engine. This string is displayed as it is, so it
   * should say something like "Written by H. Simpson".
   */
  @Override
  public synchronized String getAuthor() throws RemoteException {
    return ("Anton Krug");
  }


  /**
   * Return the value that should currently be displayed on the calculator
   * display.
   */
  public synchronized int getDisplayValue() throws RemoteException {
    return (displayValue);
  }

  /**
   * Will return string with listed content of the stack
   */
  @Override
  public synchronized String getStackContent() throws RemoteException {
    return Messages.getString("STACK_CONTENT") + rpn;
  }


  /**
   * Return the title of this calculation engine.
   */
  @Override
  public synchronized String getTitle() throws RemoteException {
    return ("RPN RMI Calculator");
  }


  /**
   * Return the version number of this engine. This string is displayed as it
   * is, so it should say something like "Version 1.1".
   */
  @Override
  public synchronized String getVersion() throws RemoteException {
    return ("RPN v1.3");
  }


  /**
   * The 'minus' button was pressed.
   */
  @Override
  public synchronized void minus() throws RemoteException {
    evaluateStack(Operator.MINUS);
  }


  /**
   * The 'Modulo' button was pressed.
   */
  @Override
  public synchronized void modulo() throws RemoteException {
    evaluateStack(Operator.MODULO);
  }


  /**
   * The * button pressed
   */
  @Override
  public synchronized void multiply() throws RemoteException {
    evaluateStack(Operator.MULTIPLY);
  }


  /**
   * A number button was pressed. Do whatever you have to do to handle it. The
   * number value of the button is given as a parameter.
   */
  public synchronized void numberPressed(int number) throws RemoteException {
    displayValue = displayValue * 10 + number;
  }


  /**
   * The 'On' button was pressed.
   */
  @Override
  public synchronized void offOn() throws RemoteException {
    displayValue = 0;
    rpn = new Stack<>();
    printStack();
  }


  /**
   * The 'plus' button was pressed.
   */
  @Override
  public synchronized void plus() throws RemoteException {
    evaluateStack(Operator.PLUS);
  }


  /**
   * The 'Pop' button was pressed.
   */
  @Override
  public synchronized void pop() throws RemoteException {
    if (rpn.size() > 0) {
      displayValue = rpn.pop();
    }
    printStack();
  }


  /**
   * Will print content of the stack into console
   * 
   * @throws RemoteException
   */
  private synchronized void printStack() throws RemoteException {
    if (VERBOSE) {
      System.out.println(getStackContent());
    }
  }

  
}
