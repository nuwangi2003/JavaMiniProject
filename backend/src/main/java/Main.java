import com.mysql.cj.x.protobuf.MysqlxDatatypes;
import command.CommandRegistry;
import server.MultiServer;

public class Main {
    public static void main(MysqlxDatatypes.Scalar.String[] args) {
        CommandRegistry.init();

        MultiServer server = new MultiServer();
        server.start();
    }
}
