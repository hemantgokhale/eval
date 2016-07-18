import org.apache.http.client.fluent.Request;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.concurrent.ThreadLocalRandom;

@WebServlet("/")
public class DefaultServlet extends HttpServlet {
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        StringBuffer buffer = new StringBuffer("Request processing started\n");
        response.getWriter().println("Request processing started");

        // Parsing
        response.getWriter().print(Work.local(2));

        // 10% requests fail to parse
        if (ThreadLocalRandom.current().nextDouble(1) < 0.1) {
            buffer.append("Failed to parse\n");
            sendResponse(response, buffer);
            return;
        }

        // 10% requests are black listed
        if (ThreadLocalRandom.current().nextDouble(1) < 0.1) {
            buffer.append("black listed\n");
            sendResponse(response, buffer);
            return;
        }

        // Get user profile
        buffer.append(Request.Get(Work.url2).execute().returnContent().asString());

        // 30% requests dropped by early exit
        if (ThreadLocalRandom.current().nextDouble(1) < 0.3) {
            buffer.append("Eliminated by early exit\n");
            sendResponse(response, buffer);
            return;
        }

        // Get contextual info
        buffer.append(Request.Get(Work.url2).execute().returnContent().asString());

        // Prepare ad server request
        buffer.append(Work.local(4));
        
        // Call ad server
        buffer.append(Request.Get(Work.url10).execute().returnContent().asString());
        
        // Prepare response
        buffer.append(Work.local(2));
        
        // Send response
        sendResponse(response, buffer);
    }

    private void sendResponse(HttpServletResponse response, StringBuffer buffer) throws IOException {
        response.getWriter().println(buffer.toString());
    }
}
