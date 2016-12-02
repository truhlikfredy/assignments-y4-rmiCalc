RMI calculator - CA 3
========================
 by Anton Krug 20062210


![screenshot](https://raw.githubusercontent.com/truhlikfredy/assignments-y4-rmiCalc/master/images/shot.jpg?token=ABC5ibMQvOPnGcloC4eL2gnPqDZVDKhhks5YSou8wA%3D%3D)

Features
--------

* RPN, reverse polish notation postfix type calculator (first you need to populate stack and then apply the operator on it)
* All buttons on the calculator are RMI methods (not just add/subtract, but all numbers as well). Completely all processing is done on server.
* Shared syncrhonized state of the calculator with all clients
* Add, Divide, Multiply, Modulus, Pop stack, Add to stack, On, AC, Del.
* Proper use of Java8. **lambda expressions** and **method referencing**, to attach GUI elements to runnables which will use the RMI methods and do Exception handling as well in a single line.
```java
	addButton("ENTER", ()->{try { calc.enter();    } catch (RemoteException e) {throw new UncheckedIOException(e);}});
```
	Thanks to Java8 you can do more with less code, makes GUI event handling very simplistic:
```java
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
    redisplay();  //update display with new content
  }
```

* Separation of concerns, configuration is removed from code and kept in separate **config.properties** file.

* Externalized Strings, all texts which comunicate with enduser are exported into **messagess.properties** allows faster proof reading, or easy multilangual support.

Documentation
-------------

Javadoc (only showing documentation for public methods) generated under the **doc/index.html**. There is bit more comments in the git repository as well which will be located under [github repository](https://github.com/truhlikfredy/assignments-y4-rmiCalc) when it will be made public (till then it will show 404).

Metrics
-------

Did static code analysis. Got the cyclomatic complexity average to very low values thanks to Java8. This should resort to very few possible bugs. And because this means very low branching it allows to simpler tests. When there are only 1 or 2 branches, then it's easier to cover fully in the tests all conditions and branches. 

Metric                           | Total  | Mean  | Std. Dev.  
:--------------------------------| ------:| -----:| ----------:
Cyclomatic Complexity            |        |   1.4 |        1.2
Nested Block Depth               |        |   0.9 |        0.8
Packages                         |      2 |       |            
Classes                          |      7 |       |            
Methods                          |     53 |       |            
Lines of code (without comments) |    406 |       |   


Testing
-------
Was done with JUnit tests and together with manual GUI testing on Debian Linux and Windows 7. 
  
