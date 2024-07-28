package one.digitalinnovation.desafio.controller;

import jakarta.servlet.http.HttpServletResponse;
import one.digitalinnovation.desafio.model.User;
import one.digitalinnovation.desafio.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.IOException;
import java.net.URI;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> findById(@PathVariable Long id) {
        var user = userService.findById(id);
        return ResponseEntity.ok(user);
    }

    @PostMapping
    public ResponseEntity<User> create(@RequestBody User userToCreate) {
        var userCreated = userService.create(userToCreate);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(userCreated.getId())
                .toUri();
        return ResponseEntity.created(location).body(userCreated);
    }

    @PutMapping("/{id}")
    public ResponseEntity<User> update(@PathVariable Long id, @RequestBody User userToUpdate) {
        userToUpdate.setId(id);
        var updatedUser = userService.update(userToUpdate);
        return ResponseEntity.ok(updatedUser);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        userService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/export")
    public void exportToExcel(HttpServletResponse response) throws IOException {
        userService.exportToExcel(response);
    }

    @GetMapping("/export/{id}")
    public void exportUserToExcel(@PathVariable Long id, HttpServletResponse response) throws IOException {
        userService.exportUserByIdToExcel(id, response);
    }
}
