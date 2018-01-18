package app.HttpServer;

import app.common.Logger.Logger;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.util.stream.Stream;

public class LogHandler implements HttpHandler {

    @Override

    public void handle(HttpExchange he) throws IOException {
        StringBuilder sb = new StringBuilder();
        sb.append("<style>body{background-color:#5b6077;}</style>");
        sb.append("<script>setInterval(function(){location.reload();},1000);</script>");
        sb.append("<div style=\"max-width:800px;margin:0 auto;color:#00FF00;background-color:#242424;\">");

        try (Stream<String> stream = Files.lines(Logger.APPLICATION_LOG.toPath())) {
            stream.forEach(line -> {
                sb.append("<p>");
                sb.append(line);
                sb.append("</p>");
            });
        }

        sb.append("</div>");

        he.sendResponseHeaders(200, sb.toString().length());
        OutputStream os = he.getResponseBody();
        os.write(sb.toString().getBytes());

        os.close();
    }
}