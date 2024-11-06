package dat;

import dat.config.ApplicationConfig;
import dat.utils.Populator;

public class Main {

    public static void main(String[] args) {
        ApplicationConfig.startServer(9009);
        Populator.populate();
    }
}