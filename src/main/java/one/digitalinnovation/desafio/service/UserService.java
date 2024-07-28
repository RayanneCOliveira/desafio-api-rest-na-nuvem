package one.digitalinnovation.desafio.service;

import jakarta.servlet.http.HttpServletResponse;
import one.digitalinnovation.desafio.model.User;

import java.io.IOException;

public interface UserService {

    User findById(Long id);
    User create(User userToCreate);
    User update(User userToUpdate);
    void delete(Long id);
    void exportToExcel(HttpServletResponse response) throws IOException;
    void exportUserByIdToExcel(Long id, HttpServletResponse response) throws IOException;
}
