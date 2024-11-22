package com.example.akaroa.service;

import com.example.akaroa.model.Rol;
import com.example.akaroa.repository.RolRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class RolService {

    @Autowired
    private RolRepository rolRepository;

    public List<Rol> findAll() {
        return rolRepository.findAll();
    }

//    public Optional<Rol> findById(Integer id) {
//        return rolRepository.findById(id);
//    }

    public Rol obtenerRolPorId(Integer idRol) {
        Optional<Rol> rol = rolRepository.findById(idRol);
        return rol.orElse(null);
    }

    public Rol save(Rol rol) {
        return rolRepository.save(rol);
    }

    public void deleteById(Integer id) {
        rolRepository.deleteById(id);
    }
}
