package com.vinava.pofo.controller;

import com.vinava.pofo.dto.request.ClientRequest;
import com.vinava.pofo.dto.response.ClientResponse;
import com.vinava.pofo.service.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

@RestController
@RequestMapping("/pofo/client")
public class ClientController {

    @Autowired
    private ClientService clientService;

    @GetMapping("test")
    private String clientTest() {
            return "POFO APPLICATION IS UP!!!";
    }

    @PostMapping()
    private ClientResponse createClient(@Valid @RequestBody ClientRequest request) {
        return clientService.createClient(request);
    }

    @PutMapping("{id}")
    private ClientResponse updateClient(@Valid @RequestBody ClientRequest request,
                                        @PathVariable(value = "id") long id) {
        return clientService.updateClient(id, request);
    }

    @DeleteMapping("{id}")
    private boolean deleteClientById(@NotNull @PathVariable(value = "id") long id) {
        return clientService.deleteClientById(id);
    }

    @DeleteMapping()
    private boolean deleteClientBySlug(@NotNull @RequestParam(value = "slug") String slug) {
        return clientService.deleteClientBySlug(slug);
    }

    @GetMapping("{id}")
    private ClientResponse getClientById(@PathVariable long id) {
        return clientService.getClientById(id);
    }

    @GetMapping("slug/{slug}")
    private ClientResponse getClientBySlug(@NotBlank @PathVariable(value = "slug") String slug) {
        return clientService.getClientBySlug(slug);
    }

    @GetMapping("name")
    public ResponseEntity<List<ClientResponse>> getClientsByName(@RequestParam(value = "name") String clientName,
                                                                 @RequestParam(value = "_page_number", defaultValue = "0") Integer pageNumber,
                                                                 @RequestParam(value = "_page_size", defaultValue = "10") Integer pageSize,
                                                                 @RequestParam(value = "_sort_by", defaultValue = "id") String sortBy,
                                                                 @RequestParam(value = "_order", defaultValue = "DESC") String order) {
        return clientService.getClientsByName(clientName, pageNumber, pageSize, sortBy, order);
    }

    @GetMapping()
    public ResponseEntity<List<ClientResponse>> getAllClients(@RequestParam(value = "_page_number", defaultValue = "0") Integer pageNumber,
                                                 @RequestParam(value = "_page_size", defaultValue = "10") Integer pageSize,
                                                 @RequestParam(value = "_sort_by", defaultValue = "id") String sortBy,
                                                 @RequestParam(value = "_order", defaultValue = "DESC") String order) {
        return clientService.getAllClients(pageNumber, pageSize, sortBy, order);
    }

}
