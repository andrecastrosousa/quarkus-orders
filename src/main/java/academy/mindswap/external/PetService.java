package academy.mindswap.external;


import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

@RegisterRestClient
@Path("/pet")
public interface PetService {

    @GET
    @Path("/{id}")
    Pet getById(@PathParam("id") Long id);
}
