package async_pkg;

import java.util.concurrent.TimeUnit;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.container.AsyncResponse;
import javax.ws.rs.container.Suspended;
import javax.ws.rs.container.TimeoutHandler;
import javax.ws.rs.core.Response;

@Path("/AsyncTimeout")
public class AsyncResourceWithTimeout {
    @GET
    @Path("/{id}")
    public void asyncGetWithTimeout(@Suspended final AsyncResponse asyncResponse, @PathParam(value = "id") final int value) {
    	
    	
    	asyncResponse.setTimeoutHandler(new TimeoutHandler() {

			@Override
			public void handleTimeout(AsyncResponse asyncResponse) {
				asyncResponse.resume(Response.status(Response.Status.SERVICE_UNAVAILABLE)
	                    .entity("Operation time out.").build());
			}
    	});
    	asyncResponse.setTimeout(2, TimeUnit.SECONDS);
    	
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
