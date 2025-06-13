package com.allergydetection.controller;
import com.allergydetection.dto.UserDto;
import com.allergydetection.service.UserService;
import com.allergydetection.exception.UserNotFoundException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/users")
@Tag(name = "User Management", description = "APIs for managing users")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping
    @Operation(summary = "Create a new user")
    public ResponseEntity<Map<String, Object>> createUser(@Valid @RequestBody UserDto userDto) {
        UserDto createdUser = userService.createUser(userDto);
        
        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("user_id", createdUser.getId());
        response.put("message", "Utilisateur créé avec succès");
        response.put("user", createdUser);
        
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get user by ID")
    public ResponseEntity<UserDto> getUserById(@PathVariable Long id) {
        UserDto user = userService.getUserById(id);
        return ResponseEntity.ok(user);
    }

    @GetMapping
    @Operation(summary = "Get all users with pagination and search")
    public ResponseEntity<Map<String, Object>> getAllUsers(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int per_page,
            @RequestParam(required = false) String search) {
        
        Pageable pageable = PageRequest.of(page - 1, per_page);
        Page<UserDto> usersPage = userService.getAllUsers(pageable, search);
        
        Map<String, Object> response = new HashMap<>();
        response.put("users", usersPage.getContent());
        
        Map<String, Object> pagination = new HashMap<>();
        pagination.put("current_page", page);
        pagination.put("per_page", per_page);
        pagination.put("total_users", usersPage.getTotalElements());
        pagination.put("total_pages", usersPage.getTotalPages());
        pagination.put("has_next", usersPage.hasNext());
        pagination.put("has_prev", usersPage.hasPrevious());
        
        response.put("pagination", pagination);
        response.put("search", search);
        
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update user")
    public ResponseEntity<Map<String, Object>> updateUser(@PathVariable Long id, @Valid @RequestBody UserDto userDto) {
        UserDto updatedUser = userService.updateUser(id, userDto);
        
        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("message", "Utilisateur mis à jour avec succès");
        response.put("user", updatedUser);
        
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
@Operation(summary = "Delete user")
public ResponseEntity<Map<String, Object>> deleteUser(@PathVariable Long id, 
                                                     @RequestParam(defaultValue = "false") boolean confirm) {
    Map<String, Object> response = new HashMap<>();
    
    try {
        // Vérifier d'abord la confirmation
        if (!confirm) {
            response.put("error", "Suppression non confirmée");
            response.put("message", "Ajoutez ?confirm=true pour confirmer la suppression");
            response.put("warning", "Cette action supprimera définitivement toutes les données de l'utilisateur");
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
        // Récupérer l'utilisateur (peut lever une exception si introuvable)
        UserDto user = userService.getUserById(id);
        String username = user.getUsername(); // Sauvegarder juste le nom
        // Supprimer l'utilisateur
        userService.deleteUser(id);
        response.put("success", true);
        response.put("message", "Utilisateur " + username + " supprimé avec succès");
        response.put("deleted_user_id", id); // Retourner seulement l'ID
        return ResponseEntity.ok(response);
    } catch (UserNotFoundException e) {
        response.put("error", "Utilisateur introuvable");
        response.put("message", "Aucun utilisateur trouvé avec l'ID " + id);
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    } catch (Exception e) {
        response.put("error", "Erreur lors de la suppression");
        response.put("message", "Une erreur inattendue s'est produite");
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
}