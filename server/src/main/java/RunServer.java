import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameter;

/**
 * Created by Michał Krzysztof Feiler on 01.04.17.
 */
public class RunServer {

    public static class CLOptions {
        @Parameter(names = "-u", description = "database username")
        public String u;

        @Parameter(names = "-p", description = "database password")
        public String p;
    }

    public static void main(String[] args) {
        CLOptions opt = new CLOptions();
        new JCommander(opt, args);
    }
}
