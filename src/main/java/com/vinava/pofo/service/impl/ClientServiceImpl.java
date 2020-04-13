package com.vinava.pofo.service.impl;

import com.vinava.pofo.dao.ClientRepository;
import com.vinava.pofo.dto.request.ClientRequest;
import com.vinava.pofo.dto.response.ClientResponse;
import com.vinava.pofo.exception.ProcessException;
import com.vinava.pofo.exception.ResourceNotFoundException;
import com.vinava.pofo.model.Client;
import com.vinava.pofo.service.ClientService;
import com.vinava.pofo.service.helper.SlugGenerationService;
import com.vinava.pofo.util.ConstantUtil;
import com.vinava.pofo.util.DateUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class ClientServiceImpl implements ClientService {

    @Autowired
    private SlugGenerationService slugGenerationService;

    @Autowired
    private ClientRepository clientRepository;

    @Override
    public ClientResponse createClient(ClientRequest clientRequest) {
        log.debug("Starting createClient with request: {}", clientRequest);
        Optional<Client> clientOptional = clientRepository.
                findByNameAndAddressPincodeAndClientType(clientRequest.getName(),
                                                        clientRequest.getAddress().getPincode(),
                                                        clientRequest.getClientType());
        if (clientOptional.isPresent()) {
            log.debug("Client already present with name : {} and pincode: {} and of clientType: {}",
                    clientRequest.getName(), clientRequest.getAddress().getPincode(), clientRequest.getClientType().name());
            return ClientResponse.from(clientOptional.get(), false);
        }
        Client client = clientRequest.from();
        if (client.isActive()) {
            client.setActivationDate(DateUtil.getCurrentDate());
        }
        client.setSlug(generateClientSlug(clientRequest.getName()));
        client = clientRepository.saveAndFlush(client);
        log.debug("Returning from createClient, response: {}", client);
        return ClientResponse.from(client, true);
    }

    @Override
    public ClientResponse updateClient(long id, ClientRequest clientRequest) throws ResourceNotFoundException, ProcessException {
        log.debug("Updating client with id: {}", id);
        Optional<Client> clientOptional = clientRepository.findById(id);
        if (!clientOptional.isPresent()) {
            log.error("No client found with id: {}", id);
            throw new ResourceNotFoundException("Client", "id", id);
        }
        Client client = clientOptional.get();
        boolean criticalUpdate = !(clientRequest.getName().equals(client.getName())
                                && clientRequest.getAddress().getPincode().equals(client.getAddress().getPincode())
                                && clientRequest.getClientType().equals(client.getClientType()));
        log.debug("Critical update: {}", criticalUpdate);
        client = clientRequest.updateClientFrom(client);
        if (criticalUpdate) {
            clientOptional = clientRepository.
                    findByNameAndAddressPincodeAndClientType(client.getName(),
                            client.getAddress().getPincode(), client.getClientType());
            if (clientOptional.isPresent()) {
                log.error("Client already present with name : {}, pincode: {}, clientType: {}",
                        clientRequest.getName(), clientRequest.getAddress().getPincode(), clientRequest.getClientType());
                String errorMessage = String.format("Client already present with name %s , pincode %s and clientType %s",
                        clientRequest.getName(), clientRequest.getAddress().getPincode(), clientRequest.getClientType().name());
                throw new ProcessException("Update client", errorMessage);
            }
        }
        client = clientRepository.save(client);
        return ClientResponse.from(client, false);
    }

    @Override
    public ResponseEntity<List<ClientResponse>> getClientsByName
            (String clientName, Integer pageNumber, Integer pageSize, String sortBy, String order) {
        log.debug("Starting getClients for clientName: {}, pageNumber: {}, pageSize: {}, sortBy: {} and order: {}",
                clientName, pageNumber, pageSize, sortBy, order);
        Sort.Direction direction = (order.equals("ASC")) ? Sort.Direction.ASC : Sort.Direction.DESC;
        Pageable pageable = PageRequest.of(pageNumber, pageSize, Sort.by(direction, sortBy));
        List<Client> clients = clientRepository.findAllByName(clientName, pageable);
        log.debug("Returning from getClientsByName with client response: {}", clients);
        return ClientResponse.getResponseEntityFrom(clients);
    }

    @Override
    public ResponseEntity<List<ClientResponse>> getAllClients(Integer pageNumber, Integer pageSize, String sortBy, String order) {
        log.debug("Starting getAllClients with pageNumber: {}, pageSize: {}, sortBy: {} and order: {}",
                pageNumber, pageSize, sortBy, order);
        Sort.Direction direction = (order.equals("ASC")) ? Sort.Direction.ASC : Sort.Direction.DESC;
        Pageable pageable = PageRequest.of(pageNumber, pageSize, Sort.by(direction, sortBy));
        Page<Client> clients = clientRepository.findAll(pageable);
        log.debug("Returning from getAllClients with client response: {}", clients);
        return ClientResponse.getResponseEntityFrom(clients.getContent());
    }

    @Override
    public ClientResponse getClientById(long id) {
        log.debug("Fetching client with id: {}", id);
        Optional<Client> clientOptional = clientRepository.findById(id);
        ClientResponse clientResponse = null;
        if (clientOptional.isPresent()) {
            clientResponse = ClientResponse.from(clientOptional.get(), false);
        }
        log.debug("Returning from getClientById with response: {}", clientResponse);
        return clientResponse;
    }

    @Override
    public ClientResponse getClientBySlug(String slug) {
        log.debug("Fetching client with slug: {}", slug);
        Optional<Client> clientOptional = clientRepository.findBySlug(slug);
        ClientResponse clientResponse = null;
        if (clientOptional.isPresent()) {
            clientResponse = ClientResponse.from(clientOptional.get(), false);
        }
        log.debug("Returning from getClientBySlug with response: {}", clientResponse);
        return clientResponse;
    }

    @Override
    public boolean deleteClientById(long id) {
        log.debug("Deleting client with id: {}", id);
        Optional<Client> optionalClient = clientRepository.findById(id);
        if (!optionalClient.isPresent()) {
            log.error("Client not present with id: {}", id);
            return false;
        }
        clientRepository.delete(optionalClient.get());
        return true;
    }

    @Override
    public boolean isClientActive(long id) {
        log.debug("Checking active status for client with id: {}", id);
        Optional<Client> clientOptional = clientRepository.findById(id);
        if (clientOptional.isPresent()) {
            Client client = clientOptional.get();
            log.debug("Client found : {}", client);
            if (client.isActive()) {
                return true;
            }
        }
        log.error("Active client not found with id: {}", id);
        return false;
    }

    @Override
    public boolean deleteClientBySlug(String slug) {
        log.debug("Deleting client with slug: {}", slug);
        Optional<Client> optionalClient = clientRepository.findBySlug(slug);
        if (!optionalClient.isPresent()) {
            log.error("Client not present with slug: {}", slug);
            return false;
        }
        clientRepository.delete(optionalClient.get());
        return true;
    }

    private String generateClientSlug(String clientName) throws ProcessException {
        log.debug("Creating slug for clientName: {}", clientName);
        int retryCount = 0;
        String slug = null;
        while (true) {
            slug = slugGenerationService.generateClientSlug(clientName, retryCount);
            Optional<Client> clientOptional = clientRepository.findBySlug(slug);
            if (!clientOptional.isPresent()) {
                break;
            }
            retryCount++;
            if (retryCount > ConstantUtil.DEFAULT_MAX_RETRY_COUNT) {
                log.error("Max retry count of {} exceeded", ConstantUtil.DEFAULT_MAX_RETRY_COUNT);
                String message = String.format("Max retry count of %s exceeded for the slug: %s",
                        ConstantUtil.DEFAULT_MAX_RETRY_COUNT, slug);
                throw new ProcessException("Generate Client Slug", message);
            }
        }
        log.debug("Returning from generateClientSlug with slug: {}, and try count: {}", slug, retryCount);
        return slug;
    }

}
