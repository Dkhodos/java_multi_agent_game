public class Logger {
    private final String name;

    public Logger(String name){
        this.name = name;
    }

    public static final String RESET = "\u001B[0m";
    public static final String INFO_BLUE = "\u001B[34m";
    public static final String TITLE_PURPLE = "\u001B[35m";
    public static final String WARNING_YELLOW = "\u001B[33m";
    public static final String ERROR_RED = "\u001B[31m";
    public static final String BOLD = "\u001B[1m";

    public void debug(String msg, LogType type){
        if(!Settings.DEBUG) return;
        printMessageByLogType(type, msg);
    }

    public void debug(String msg){
        debug(msg, LogType.Info);
    }

    public void info(String msg){
        printMessage(INFO_BLUE, msg);
    }

    public void title(String msg){
        System.out.println(TITLE_PURPLE + BOLD + "### " + name + ": " +msg + " ###" + RESET);
    }

    public  void warning(String msg){
        printMessage(WARNING_YELLOW, msg);
    }

    public  void error(String msg){
        printMessage(ERROR_RED, msg);
    }

    private void printMessageByLogType(LogType type, String msg){
        switch (type){
            case Info ->  info(msg);
            case Error -> error(msg);
            case Title -> title(msg);
            case Warning -> warning(msg);
        }
    }

    private void printMessage(String color, String msg){
        System.out.println(color + name + ": " + msg + RESET);
    }
}
