package util;

public class Console {

    private ConsoleReader consoleReader = new ConsoleReader();
    private ConsoleWriter consoleWriter = new ConsoleWriter();


    public String vraagInput(){
        return consoleReader.read();
    }

    public void setConsoleReader(ConsoleReader consoleReader) {
        this.consoleReader = consoleReader;
    }

    public void setConsoleWriter(ConsoleWriter consoleWriter) {
        this.consoleWriter = consoleWriter;
    }
}
