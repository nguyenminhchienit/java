package hutech.apicrud.controllers;

import hutech.apicrud.dto.request.ApiResponse;
import hutech.apicrud.dto.request.PermissionRequest;
import hutech.apicrud.dto.response.PermissionResponse;
import hutech.apicrud.entities.Permission;
import hutech.apicrud.services.PermissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/permissions")
public class PermissionController {
    @Autowired
    private PermissionService permissionService;

    @PostMapping
    ApiResponse<PermissionResponse> addPermission(@RequestBody PermissionRequest request) {
        ApiResponse<PermissionResponse> response = new ApiResponse<>();
        response.setMessage("Create permission successfully");
        response.setSuccess(true);
        response.setData(permissionService.create(request));

        return response;
    }

    @GetMapping
    ApiResponse<List<PermissionResponse>> getAll(){
        ApiResponse<List<PermissionResponse>> response = new ApiResponse<>();
        response.setSuccess(true);
        response.setData(permissionService.getAll());
        return response;
    }

    @DeleteMapping("/{name}")
    ApiResponse<Void> deletePermission(@PathVariable String name){
        ApiResponse<Void> response = new ApiResponse<>();
        response.setSuccess(true);
        response.setMessage("Delete permission successfully");
        permissionService.delete(name);
        return response;
    }
}
