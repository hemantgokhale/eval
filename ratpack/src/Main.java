import ratpack.exec.Promise;
import ratpack.http.client.HttpClient;
import ratpack.server.RatpackServer;

import java.net.URI;

public class Main {
    public static void main(String... args) throws Exception {
        final URI uri2 = new URI(Work.url2);
        final URI uri10 = new URI(Work.url10);
        
        RatpackServer.start(server -> server
                .handlers(chain -> chain
                        .get(ctx -> {

                            StringBuffer buffer = new StringBuffer("Request processing started\n");
                            HttpClient client = ctx.get(HttpClient.class);

                            Promise.sync(() -> Work.local(2)).then(buffer::append);
                            client.get(uri2).then(response -> buffer.append(response.getBody().getText()));
                            Promise.sync(() -> Work.local(4)).then(buffer::append);
                            client.get(uri10).then(response -> buffer.append(response.getBody().getText()));
                            Promise.sync(() -> Work.local(2)).then(buffer::append);
                            Promise.sync(buffer::toString).then(ctx::render);
                            

//                            Work.local(2);
//                            buffer.append("\nLocal work completed");

//                            if (ThreadLocalRandom.current().nextDouble(1) < 0.5) {  // only about 50% of the requests make the remote call
//                                HttpClient httpClient = ctx.get(HttpClient.class);
//                                httpClient.get(uri).then(response -> buffer.append("\n").append(response.getBody().getText()));
//                            }
//                            Operation.of(() -> ctx.render(buffer.toString())).then();
                        })
                )
        );
    }
}