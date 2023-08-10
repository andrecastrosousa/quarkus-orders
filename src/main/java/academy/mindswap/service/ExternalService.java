package academy.mindswap.service;

import academy.mindswap.external.Extension;
import academy.mindswap.external.ExtensionsService;
import academy.mindswap.external.Pet;
import academy.mindswap.external.PetService;
import jakarta.enterprise.context.ApplicationScoped;
import org.eclipse.microprofile.rest.client.inject.RestClient;

import java.util.Set;

@ApplicationScoped
public class ExternalService {

    @RestClient
    ExtensionsService extensionsService;

    @RestClient
    PetService petService;


    public Set<Extension> getQuarkusExtension(String id) {
        return extensionsService.getById(id);
    }

    public Pet getPetById(Long id) {
        return petService.getById(id);
    }

}
