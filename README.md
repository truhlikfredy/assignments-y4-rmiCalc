Reverse polish notation calculator using remote method invocation- CA 3
========================
 by Anton Krug 20062210


![screenshot](/images/shot.jpg)

Starting
--------

* Windows - **run.bat** will start server and cliet as well.
* Linux   - **runServer.sh** for server and then **runClient.sh** for clients. You run server into background and can't find it or it's stuck then **stopServer.sh** should find the server process and kill it for you.
 

Features
--------

* RPN, reverse polish notation postfix type calculator (first you need to populate stack and then apply the operator on it)
* All buttons on the calculator are RMI methods (not just add/subtract, but all numbers as well). Completely all processing is done on server.
* Shared syncrhonized state of the calculator with all clients
* Add, Divide, Multiply, Modulus, Pop stack, Add to stack, On, AC, Del.
* Proper use of Java8. **lambda expressions** and **method referencing**, to attach GUI elements to runnables which will use the RMI methods and do Exception handling as well in a single line. It will handle all problems from null pointers to RemoteException.
```java
addButton("ENTER", ()->{try { calc.enter(); } catch (Exception e) {throw new RuntimeException(e);}});
```

Thanks to Java8 you can do more with less code, makes GUI event handling very simplistic. This will handle **EVERY** single button and **ALL** possible exceptions.

```java
  @Override
  public void actionPerformed(ActionEvent event) {
    String actionText = event.getActionCommand();
    
    if (!handleNumbers(actionText)) {
      //if it's not number then it must be the operator, run the runnable associated with it
      handleOperators(actionText);
    }
    
    redisplay(); //update the calculator display
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
Classes                          |      8 |       |            
Methods                          |     53 |       |            
Lines of code (without comments) |    406 |       |   


Testing
-------
Was done with JUnit tests and together with manual GUI testing on Debian Linux and Windows 7. 
  
