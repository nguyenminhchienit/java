package hutech.apicrud.services;

import hutech.apicrud.dto.request.PermissionRequest;
import hutech.apicrud.dto.response.PermissionResponse;
import hutech.apicrud.entities.Permission;
import hutech.apicrud.mapper.PermissionMapper;
import hutech.apicrud.repository.PermissionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PermissionService {
    @Autowired
    private PermissionRepository permissionRepository;
    @Autowired
    private PermissionMapper permissionMapper;

    public PermissionResponse create(PermissionRequest request){
        Permission permission = permissionMapper.toPermission(request);
        permissionRepository.save(permission);
        return permissionMapper.toPermissionResponse(permission);
    }

    public  List<PermissionResponse> getAll(){
        List<Permission> permissions = permissionRepository.findAll();
        return permissions
                .stream()
                .map(permission -> permissionMapper.toPermissionResponse(permission))
                .toList();
    }

    public void delete(String name){
        permissionRepository.deleteById(name);
    }

}
