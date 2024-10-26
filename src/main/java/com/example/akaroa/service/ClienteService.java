package com.example.akaroa.service;

import com.example.akaroa.model.Cliente;
import com.example.akaroa.repository.ClienteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ClienteService {

    @Autowired
    private ClienteRepository clienteRepository;

    public List<Cliente> findAll() {
        return clienteRepository.findAll();
    }

    public Optional<Cliente> findById(Integer id) {
        return clienteRepository.findById(id);
    }

    public Cliente save(Cliente cliente) {
        return clienteRepository.save(cliente);
    }

    public void deleteById(Integer id) {
        clienteRepository.deleteById(id);
    }
}
