import org.apache.http.client.fluent.Request;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/")
public class DefaultServlet extends HttpServlet {
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        
        response.getWriter().println("Request processing started");
        response.getWriter().print(Work.local(2));
        response.getWriter().print(Request.Get(Work.url2).execute().returnContent().asString());
        response.getWriter().print(Work.local(4));
        response.getWriter().print(Request.Get(Work.url10).execute().returnContent().asString());
        response.getWriter().print(Work.local(2));
        
//        Work.local(2);
//        response.getWriter().println("Local work completed");
        
//        if (ThreadLocalRandom.current().nextDouble(1) < 0.5) {  // only about 50% of the requests make the remote call
//            String message = Request.Get(Work.url2).execute().returnContent().asString();
//            response.getWriter().println(message);
//        }
    }

    /**
     * Simulate some non-blocking work that involves using local compute resources
     *
     * @param weight indicates the amount of work to be done. Minimum value is 1
     */
    static public void localWork(int weight) {
        if (weight < 1) {
            weight = 1;
        }

        for (int i = 0; i < weight; i++) {
            String a = "abc";
            for (int j = 0; j < 100; j++) {
                a = a + (j * j);
            }
        }
    }
}
