package one.digitalinnovation.desafio.service;

import one.digitalinnovation.desafio.model.User;

public interface UserService {

    User findById(Long id);
    User create(User userToCreate);
}
