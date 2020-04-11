package com.vinava.pofo.service;

import com.vinava.pofo.dto.request.ClientRequest;
import com.vinava.pofo.dto.response.ClientResponse;
import com.vinava.pofo.exception.ProcessException;
import com.vinava.pofo.exception.ResourceNotFoundException;

import java.util.List;

public interface ClientService {
    ClientResponse createClient(ClientRequest clientRequest);
    ClientResponse updateClient(ClientRequest clientRequest) throws ResourceNotFoundException, ProcessException;
    List<ClientResponse> getClientsByName(String clientName, Integer pageNumber, Integer pageSize, String sortBy, String order);
    ClientResponse getClientById(long id);
    ClientResponse getClientBySlug(String slug);
    List<ClientResponse> getAllClients(Integer pageNumber, Integer pageSize, String sortBy, String order);
    boolean deleteClient(long id);
    boolean isClientActive(long id);
}
