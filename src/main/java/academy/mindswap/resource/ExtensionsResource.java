package academy.mindswap.resource;


import academy.mindswap.external.Extension;
import academy.mindswap.external.Pet;
import academy.mindswap.service.ExternalService;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;

import java.util.Set;

@Path("/callrestapi")
public class ExtensionsResource {

    @Inject
    ExternalService externalService;
    @GET
    @Path("/id/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Set<Extension> get(@PathParam("id") String id) {
        return externalService.getQuarkusExtension(id);
    }


    @GET
    @Path("/pet/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Pet getById(@PathParam("id") Long id) {
        return externalService.getPetById(id);
    }
}
