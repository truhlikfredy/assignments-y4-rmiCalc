kill -9 `pgrep -f CalculatorRpnRmiServer.jar` >/dev/null  2>&1
java -jar CalculatorRpnRmiServer.jar &
java -jar CalculatorClient.jar