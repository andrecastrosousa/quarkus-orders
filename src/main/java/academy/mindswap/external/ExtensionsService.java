package academy.mindswap.external;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

import java.util.Set;

@Path("/extensions")
@RegisterRestClient
public interface ExtensionsService {

    @GET
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    Set<Extension> getById(@QueryParam("id") String id);
}
