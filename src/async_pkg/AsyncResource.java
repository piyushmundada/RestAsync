package async_pkg;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.container.AsyncResponse;
import javax.ws.rs.container.Suspended;

@Path("/resource")
public class AsyncResource {
    @GET
    @Path("/{id}")
    public void asyncGet(@Suspended final AsyncResponse asyncResponse, @PathParam(value = "id") final int value) {
 
        new Thread(new Runnable() {
            @Override
            public void run(){
            	String result = null;
                try {
					result = veryExpensiveOperation(value);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
               
                asyncResponse.resume(result);
            }
 
            private String veryExpensiveOperation(int value) throws InterruptedException {
				Thread.sleep(3000L);
				return value+"Waken after sleep of 3 sec";
            }
        }).start();
    }
}
