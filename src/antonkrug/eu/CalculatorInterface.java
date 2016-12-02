package antonkrug.eu;

import java.rmi.Remote;
import java.rmi.RemoteException;


/**
 * The interface for all calc engines to implement if they want be used with
 * Calculator class
 * 
 * @author Anton Krug 
 * @date 2016/12/01
 * @version 1.4
 */
public interface CalculatorInterface extends Remote {

	public int getDisplayValue() throws RemoteException;

	/**
	 * A number button was pressed. Do whatever you have to do to handle it. The
	 * number value of the button is given as a parameter.
	 */
	public void numberPressed(int number) throws RemoteException;
	
	/**
	 * Will return string listing the stack content of the calculator
	 * @return
	 * @throws RemoteException
	 */
	public String getStackContent() throws RemoteException;

	/**
	 * The 'plus' button was pressed.
	 */
	public void plus() throws RemoteException;

	/**
	 * The 'minus' button was pressed.
	 */
	public void minus() throws RemoteException;

	/**
	 * The * button pressed
	 */
	public void multiply() throws RemoteException;

	/**
	 * The / button pressed
	 */
	public void divide() throws RemoteException;

	/**
	 * The '=' button was pressed.
	 */
	public void equals() throws RemoteException;

	/**
	 * The 'C' (clear) button was pressed.
	 */
	public void clear() throws RemoteException;

	/**
	 * The 'E' (enter) button was pressed.
	 */
	public void enter() throws RemoteException;

	/**
	 * The 'P' (pop) button was pressed.
	 */
	public void pop() throws RemoteException;

	/**
	 * The 'I/O' (on / off) button was pressed.
	 */
	public void offOn() throws RemoteException;

	/**
	 * The '<-' (del) button was pressed.
	 */
	public void del() throws RemoteException;

	/**
	 * The '%' (modulo) button was pressed.
	 */
	public void modulo() throws RemoteException;

	/**
	 * Return the title of this calculation engine.
	 */
	public String getTitle() throws RemoteException;

	/**
	 * Return the author of this engine. This string is displayed as it is.
	 */
	public String getAuthor() throws RemoteException;

	/**
	 * Return the version number of this engine. This string is displayed as it
	 * is, so it should say something like "Version 1.1".
	 */
	public String getVersion() throws RemoteException;
}
