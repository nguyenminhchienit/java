package hutech.apicrud.services;

import hutech.apicrud.dto.request.RoleRequest;
import hutech.apicrud.dto.response.RoleResponse;
import hutech.apicrud.entities.Role;
import hutech.apicrud.mapper.RoleMapper;
import hutech.apicrud.mapper.UserMapper;
import hutech.apicrud.repository.PermissionRepository;
import hutech.apicrud.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;

@Service
public class RoleService {
    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PermissionRepository permissionRepository;

    @Autowired
    private RoleMapper roleMapper;


    public RoleResponse create(RoleRequest request){
        Role role = roleMapper.toRole(request);

        var permissions = permissionRepository.findAllById(request.getPermissions());
        role.setPermissions(new HashSet<>(permissions));
        role = roleRepository.save(role);
        return roleMapper.toRoleResponse(role);
    }

    public List<RoleResponse> getAll(){
        List<Role> roles = roleRepository.findAll();
        return roles
                .stream()
                .map(role -> roleMapper.toRoleResponse(role))
                .toList();
    }

    public void delete(String name){
        roleRepository.deleteById(name);
    }
}
