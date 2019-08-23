package sparksoniq;

import akka.NotUsed;
import akka.actor.ActorSystem;
import akka.http.javadsl.ConnectHttp;
import akka.http.javadsl.Http;
import akka.http.javadsl.ServerBinding;
import akka.http.javadsl.model.*;
import akka.http.javadsl.server.AllDirectives;
import akka.http.javadsl.server.Route;
import akka.stream.ActorMaterializer;
import akka.stream.javadsl.Flow;
import org.apache.spark.SparkConf;
import sparksoniq.config.SparksoniqRuntimeConfiguration;
import sparksoniq.spark.SparkSessionManager;

import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.util.concurrent.CompletionStage;

public class WebServer extends AllDirectives {
    public static void main(String[] args) throws Exception {
        // boot up server using the route as defined below
        ActorSystem system = ActorSystem.create("routes");

        final Http http = Http.get(system);
        final ActorMaterializer materializer = ActorMaterializer.create(system);

        // In order to access all directives we need an instance where the routes
        // are define.
        WebServer app = new WebServer();

        final Route routes = app.route(app.createRoute());

        final Flow<HttpRequest, HttpResponse, NotUsed> routeFlow = routes.flow(system, materializer);
        final CompletionStage<ServerBinding> binding = http.bindAndHandle(routeFlow, ConnectHttp.toHost("localhost", 8080), materializer);

        System.out.println("Server online at http://localhost:8080/\nPress RETURN to stop...");
        System.in.read(); // let it run until user presses return

        binding.thenCompose(ServerBinding::unbind) // trigger unbinding from the port
                .thenAccept(unbound -> system.terminate()); // and shutdown when done
    }

    public Route createRoute() {
        SparkConf conf = new SparkConf().setMaster("local[4]").setAppName("jsoniq-web-server");
        SparkSessionManager.getInstance().initializeConfigurationAndSession(conf,false);
        String filepath = "/Users/pj.fanning/code/rumble/src/main/resources/test_files/runtime-spark";
        String filename = "LetFlowr.jq";
        Path querypath = FileSystems.getDefault().getPath(filepath, filename);
        SparksoniqRuntimeConfiguration sparksoniqConf = new SparksoniqRuntimeConfiguration(new String[] { "--result-size", "1000" }); // simulate CLI parameters, you can also set higher to allow more objects in the output (but it is not recommended setting too high to avoid a crash)
        JsoniqQueryExecutor rumbleEngine = new JsoniqQueryExecutor(true, sparksoniqConf);
        String result;
        try {
            result = rumbleEngine.runInteractive(querypath);
        } catch (Exception e) {
            e.printStackTrace();
            result = e.toString();
        }
        HttpEntity.Strict entity = HttpEntities.create(ContentTypes.TEXT_HTML_UTF8, result);
        return path("hello", () -> get(() -> complete(entity)));
    }
}
