package com.pessoal.ToDoList.Repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.pessoal.ToDoList.Domain.User.Usuario;

public interface UsuarioRepository extends JpaRepository<Usuario,String>{

}
