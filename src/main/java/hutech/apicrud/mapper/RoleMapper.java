package hutech.apicrud.mapper;

import hutech.apicrud.dto.request.RoleRequest;

import hutech.apicrud.dto.request.UserUpdateRequest;
import hutech.apicrud.dto.response.RoleResponse;
import hutech.apicrud.entities.Role;
import hutech.apicrud.entities.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface RoleMapper {
    @Mapping(target = "permissions", ignore = true)
    Role toRole(RoleRequest request);
    RoleResponse toRoleResponse(Role role);
}
