kill -9 `pgrep -f CalculatorRpnRmiServer.jar` >/dev/null  2>&1
sleep 1
java -jar CalculatorRpnRmiServer.jar 
