RMI calculator - CA 3
========================
 by Anton Krug 20062210


![screenshot](https://raw.githubusercontent.com/truhlikfredy/assignments/master/images/app.jpg?token=ABC5iVnzhQtKyOekSlVzQUpt-I9ftVo7ks5X9nw6wA%3D%3D)

Features
--------

* RPN, reverse polish notation postfix type calculator
* All buttons on the calculator are RMI methods (not just add/subtract, but all numbers as well)
* Shared state of the calculator with all clients
* Add, Divide, Multiply, Modulus, Pop stack, Add to stack,
* Proper use of Java8. **lambda expressions** and **method referencing**, to attach GUI elements to runnables which will use the RMI methods and do Exception handling as well in a single line.
    `addButton("ENTER", ()->{try { calc.enter();    } catch (RemoteException e) {throw new UncheckedIOException(e);}});`

* Separation of concerns, configuration is removed from code and kept in separate **config.properties** file.

* Externalized Strings, all texts which comunicate with enduser are exported into **messagess.properties** allows faster proof reading, or easy multilangual support.

Documentation
-------------

Javadoc (only showing documentation for public methods) generated under the **doc/index.html**. There is bit more comments in the git repository as well which will be located under [github repository](https://github.com/truhlikfredy/assignments-y4-rmiCalc) when it will be made public (till then it will show 404).

Metrics
-------

Did static code analysis and was changing the code depending on the results. Got the cyclomatic complexity average to very low values. This should resort to very few possible bugs. And because this means very low branching it allows to simpler tests. When there are only 1 or 2 branches, then it's easier to cover fully in the tests all conditions and branches. 

Metric                           | Total  | Mean  | Std. Dev.  
:--------------------------------| ------:| -----:| ----------:
Cyclomatic Complexity            |        |   1.6 |        1.2
Nested Block Depth               |        |   1.1 |        0.7
Packages                         |      2 |       |            
Classes                          |      7 |       |            
Methods                          |     73 |       |            
Lines of code (without comments) |    694 |       |   


Testing
-------
Was done with JUnit tests and together with manual GUI testing on Debian Linux and Windows 7. 
  
