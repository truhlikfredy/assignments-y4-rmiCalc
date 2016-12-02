package antonkrug.eu.test;

import static org.junit.Assert.*;

import java.rmi.RemoteException;

import org.junit.Before;
import org.junit.Test;

import antonkrug.eu.CalculatorInterface;
import antonkrug.eu.CalculatorRpnRmiServer;

/**
 * Will validate all functions of the calculator to make sure it works as it should
 * 
 * @author Anton Krug
 * @date 2016/12/01
 * @version 1.1
 */
public class CalculatorRpnRmiServerTest {

  private CalculatorInterface calculator;

  @Before
  public void setUp() throws Exception {
    //populate stacks with some values first
    
    calculator = new CalculatorRpnRmiServer();
    calculator.numberPressed(5);
    calculator.enter();
    calculator.numberPressed(6);
    calculator.enter();
    calculator.numberPressed(2);
    calculator.enter();
  }
  

  @Test
  public void clearPopTest() throws RemoteException {
    calculator.pop();
    assertEquals(2, calculator.getDisplayValue());

    calculator.pop();
    assertEquals(6, calculator.getDisplayValue());

    calculator.pop();
    assertEquals(5, calculator.getDisplayValue());

    calculator.pop();
    calculator.pop();
    calculator.pop();
    calculator.pop();
    assertEquals(5, calculator.getDisplayValue());
  }
  
  
  /**
   * Operations need often 2 items in stack, if only 1 is pressent, do not mess up results nor crash
   * @throws RemoteException
   */
  @Test
  public void smallStack() throws RemoteException {
    //leave only 1 item in the stack
    calculator.pop();
    calculator.pop();
    
    calculator.plus();
    assertEquals(6,calculator.getDisplayValue());
  }


  @Test
  public void delTest() throws RemoteException {
    calculator.numberPressed(5);
    calculator.numberPressed(6);
    calculator.numberPressed(2);

    assertEquals(562, calculator.getDisplayValue());

    calculator.del();
    assertEquals(56, calculator.getDisplayValue());

    calculator.del();
    assertEquals(5, calculator.getDisplayValue());

    calculator.del();
    assertEquals(0, calculator.getDisplayValue());

    calculator.del();
    calculator.del();
    calculator.del();
    calculator.del();
    calculator.del();
    assertEquals(0, calculator.getDisplayValue());
  }


  @Test
  public void moduloTest() throws RemoteException {
    calculator.modulo();
    assertEquals(6 % 2, calculator.getDisplayValue());

    calculator.numberPressed(3);
    calculator.enter();
    calculator.plus();
    calculator.modulo();
    assertEquals(5 % 3, calculator.getDisplayValue());
  }


  @Test
  public void multAddTest() throws RemoteException {
    calculator.multiply();
    calculator.plus();
    assertEquals(5 + 6 * 2, calculator.getDisplayValue());
  }


  @Test
  public void subDivTest() throws RemoteException {
    calculator.divide();
    calculator.minus();
    assertEquals(5 - 6 / 2, calculator.getDisplayValue());
  }

}
