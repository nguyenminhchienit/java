package hutech.apicrud.mapper;

import hutech.apicrud.dto.request.PermissionRequest;

import hutech.apicrud.dto.response.PermissionResponse;

import hutech.apicrud.entities.Permission;

import org.mapstruct.Mapper;


@Mapper(componentModel = "spring")
public interface PermissionMapper {
    Permission toPermission(PermissionRequest request);
    PermissionResponse toPermissionResponse(Permission permission);
}
