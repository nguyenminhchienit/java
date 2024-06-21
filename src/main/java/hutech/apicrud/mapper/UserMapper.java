package hutech.apicrud.mapper;

import hutech.apicrud.dto.request.UserCreateRequest;
import hutech.apicrud.dto.request.UserUpdateRequest;
import hutech.apicrud.dto.response.UserResponse;
import hutech.apicrud.entities.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface UserMapper {
    User toUser(UserCreateRequest request);

    @Mapping(target = "roles", ignore = true)
    UserResponse toUserResponse(User user);

    @Mapping(target = "roles", ignore = true)
    void updateUser(@MappingTarget User user, UserUpdateRequest request);
}
