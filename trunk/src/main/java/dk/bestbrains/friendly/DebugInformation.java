package dk.bestbrains.friendly;

import freemarker.cache.FileTemplateLoader;
import freemarker.core.InvalidReferenceException;
import freemarker.core.ParseException;
import freemarker.template.Configuration;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.LineNumberReader;
import java.lang.reflect.InvocationTargetException;
import javax.servlet.ServletOutputStream;

public class DebugInformation {
    private Exception exception;
    private final String templatePath;
    private final Configuration configuration;

    DebugInformation(Exception e, String templatePath, Configuration config) {
        this.exception = e;
        this.templatePath = templatePath;
        this.configuration = config;
    }

    void print(ServletOutputStream output) throws IOException {
        try {
            if (exception.getClass().equals(InvalidReferenceException.class)) {
                InvalidReferenceException ex = (InvalidReferenceException) exception;
                String message = ex.getMessage();
                int indexOfLineNumber = message.indexOf("on line ") + "on line ".length();
                int indexOfColumnNumber = message.indexOf(", column ") + ", column ".length();
                int lineNumber = Integer.parseInt(message.substring(indexOfLineNumber, message.indexOf(",", indexOfLineNumber)));
                int columnNumber = Integer.parseInt(message.substring(indexOfColumnNumber, message.indexOf(" in", indexOfColumnNumber)));
                String source = configuration.getTemplate(templatePath).getSource(1, lineNumber > 1 ? lineNumber -1 : lineNumber, 0, lineNumber + 1);
                output.print("SOURCE: <br/><pre>" + source.replace("<", "&lt;").replace(">", "&gt;"));
                for (int i = 0; i < columnNumber; i++) {
                    output.print("-");
                }
                output.println("^</pre>");
            }
            else if(exception.getClass().equals(ParseException.class)) {
                ParseException ex = (ParseException) exception;
                String message = ex.getMessage();
                int indexOfLineNumber = message.indexOf("at line ") + "at line ".length();
                int indexOfColumnNumber = message.indexOf(", column ") + ", column ".length();
                int lineNumber = Integer.parseInt(message.substring(indexOfLineNumber, message.indexOf(",", indexOfLineNumber)));
                int columnNumber = Integer.parseInt(message.substring(indexOfColumnNumber, message.indexOf(" in", indexOfColumnNumber)));
                File file = (File)((FileTemplateLoader)configuration.getTemplateLoader()).findTemplateSource(templatePath);
                LineNumberReader reader = new LineNumberReader(new FileReader(file));

                for(int i=0;i<lineNumber-2;i++)
                    reader.readLine();

                String source = reader.readLine() + "\n";
                source += reader.readLine() + "\n";

                output.print("SOURCE: <br/><pre>" + source.replace("<", "&lt;").replace(">", "&gt;"));
                for (int i = 0; i < columnNumber; i++) {
                    output.print("-");
                }
                output.println("^</pre>");
            }
        }
        catch(Exception ex)
        {
            // Ignore that details can't be parsed and continue
        }
        
        if(exception.getClass().equals(InvocationTargetException.class)) {
            exception = (Exception) ((InvocationTargetException)exception).getTargetException();
        }

        output.println("Exception: " + exception.getClass().getName());
        output.println("<br/>Message: " + exception.getMessage());
        output.println("<br/>Cause: " + exception.getCause());
        output.println("<br/><br/>Stacktrace:<br/>");
        
        for (StackTraceElement frame : exception.getStackTrace()) {
            output.println(frame.toString() + "<br/>");
        }
    }

}
