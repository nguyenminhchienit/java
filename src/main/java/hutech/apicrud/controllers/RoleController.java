package hutech.apicrud.controllers;

import hutech.apicrud.dto.request.ApiResponse;
import hutech.apicrud.dto.request.RoleRequest;
import hutech.apicrud.dto.response.RoleResponse;
import hutech.apicrud.services.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/roles")
public class RoleController {
    @Autowired
    private RoleService roleService;

    @PostMapping
    ApiResponse<RoleResponse> addRole(@RequestBody RoleRequest request) {
        ApiResponse<RoleResponse> response = new ApiResponse<>();
        response.setMessage("Create role successfully");
        response.setSuccess(true);
        response.setData(roleService.create(request));

        return response;
    }

    @GetMapping
    ApiResponse<List<RoleResponse>> getAll(){
        ApiResponse<List<RoleResponse>> response = new ApiResponse<>();
        response.setSuccess(true);
        response.setData(roleService.getAll());
        return response;
    }

    @DeleteMapping("/{name}")
    ApiResponse<Void> deleteRole(@PathVariable String name){
        ApiResponse<Void> response = new ApiResponse<>();
        response.setSuccess(true);
        response.setMessage("Delete role successfully");
        roleService.delete(name);
        return response;
    }
}
