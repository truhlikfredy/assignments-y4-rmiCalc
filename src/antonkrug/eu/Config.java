package antonkrug.eu;

import java.util.Properties;

/**
 * Simplifies access to config values
 * 
 * @author Anton Krug
 * @date 01.12.2016
 * @version 1.1
 */
public class Config {

  private static final boolean    DEBUG = true;
  private static       Properties cfg   = null;


  public Config() {
    if (cfg == null) {
      // only instanciate the config access if it wasn't set before, it shoudn't
      // change between the instances so it can be static and kept the same for
      // all instances. And save a bit of work.
      cfg = new Properties();
      try {
        cfg.load(getClass().getClassLoader().getResourceAsStream("config.properties"));

      } catch (Exception e) {
        if (DEBUG) e.printStackTrace();
      }
    }

  }


  public static Config getInstance() {
    return new Config();
  }


  public String getString(String key) {
    return cfg.getProperty(key);
  }

  
  public Properties getProperties() {
    return cfg;
  }

  public Integer getInteger(String key) {
    final String ret = cfg.getProperty(key);

    Integer result = null;
    try {
      result = Integer.parseInt(ret);
    } catch (NumberFormatException e) {
      if (DEBUG) {
        System.out.println("Key :" + key + " value: " + ret);
        e.printStackTrace();
      }
      System.exit(1);
    }

    return result;
  }

  
}
