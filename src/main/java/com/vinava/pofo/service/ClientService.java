package com.vinava.pofo.service;

import com.vinava.pofo.dto.request.ClientRequest;
import com.vinava.pofo.dto.response.ClientResponse;
import com.vinava.pofo.exception.ProcessException;
import com.vinava.pofo.exception.ResourceNotFoundException;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface ClientService {
    ClientResponse createClient(ClientRequest clientRequest);
    ClientResponse updateClient(long id, ClientRequest clientRequest) throws ResourceNotFoundException, ProcessException;
    ResponseEntity<List<ClientResponse>> getClientsByName(String clientName, Integer pageNumber, Integer pageSize, String sortBy, String order);
    ClientResponse getClientById(long id);
    ClientResponse getClientBySlug(String slug);
    ResponseEntity<List<ClientResponse>> getAllClients(Integer pageNumber, Integer pageSize, String sortBy, String order);
    boolean deleteClientById(long id);
    boolean deleteClientBySlug(String slug);
    boolean isClientActive(long id);
}
