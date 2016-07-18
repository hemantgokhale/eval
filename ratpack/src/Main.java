import ratpack.exec.Promise;
import ratpack.handling.Context;
import ratpack.http.client.HttpClient;
import ratpack.server.RatpackServer;

import java.net.URI;
import java.util.concurrent.ThreadLocalRandom;

public class Main {
    public static void main(String... args) throws Exception {
        final URI uri2 = new URI(Work.url2);
        final URI uri10 = new URI(Work.url10);
        
        RatpackServer.start(server -> server
                .handlers(chain -> chain
                        .get(ctx -> {

                            StringBuffer buffer = new StringBuffer("Request processing started\n");
                            HttpClient client = ctx.get(HttpClient.class);

                            // Parsing
                            Promise.sync(() -> Work.local(2)).then(buffer::append);
                            
                            // 10% requests fail to parse
                            if (ThreadLocalRandom.current().nextDouble(1) < 0.1) {
                                buffer.append("Failed to parse\n");
                                sendResponse(ctx, buffer);
                                return;
                            }

                            // 10% requests are black listed
                            if (ThreadLocalRandom.current().nextDouble(1) < 0.1) {
                                buffer.append("black listed\n");
                                sendResponse(ctx, buffer);
                                return;
                            }

                            // Get user profile
                            client.get(uri2).then(response -> buffer.append(response.getBody().getText()));

                            // 30% requests dropped by early exit
                            if (ThreadLocalRandom.current().nextDouble(1) < 0.3) {
                                buffer.append("Eliminated by early exit\n");
                                sendResponse(ctx, buffer);
                                return;
                            }

                            // Get contextual info
                            client.get(uri2).then(response -> buffer.append(response.getBody().getText()));
                            
                            // Prepare ad server request
                            Promise.sync(() -> Work.local(4)).then(buffer::append);
                            
                            // Call ad server
                            client.get(uri10).then(response -> buffer.append(response.getBody().getText()));
                            
                            // Prepare response
                            Promise.sync(() -> Work.local(2)).then(buffer::append);
                            
                            // Send response
                            sendResponse(ctx, buffer);
                        })
                )
        );
    }

    private static void sendResponse(Context ctx, StringBuffer buffer) {
        Promise.sync(buffer::toString).then(ctx::render);
    }
}