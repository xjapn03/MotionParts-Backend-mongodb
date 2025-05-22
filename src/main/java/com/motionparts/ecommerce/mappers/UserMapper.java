package com.motionparts.ecommerce.mappers;

import com.motionparts.ecommerce.models.User;
import com.motionparts.ecommerce.dto.UserDTO;
import com.motionparts.ecommerce.dto.UserInfoDTO;
import java.util.List; // Para List
import java.util.Collections; // Si usas Collections.singletonList
import java.util.stream.Collectors; // Para Collectors
import java.time.Instant;

public class UserMapper {

    public static User toUser(UserDTO userDTO, UserInfoDTO userInfoDTO, String encodedPassword) {
        User user = new User();
        user.setUsername(userDTO.getUsername());
        user.setEmail(userDTO.getEmail());
        user.setPassword(encodedPassword);
        user.setCreatedAt(Instant.now().toString());
        user.setUpdatedAt(Instant.now().toString());

        // Si el UserDTO tiene roles, los asignamos. Si no, asignamos un rol por defecto
        if (userDTO.getRoles() != null && !userDTO.getRoles().isEmpty()) {
            List<User.Role> roles = userDTO.getRoles().stream()
                .map(roleDTO -> {
                    User.Role role = new User.Role();
                    role.setId(roleDTO.getId());
                    role.setName(roleDTO.getName());
                    return role;
                })
                .collect(Collectors.toList());
            user.setRoles(roles);
        } else {
            // Asignar un rol por defecto ("USER")
            User.Role defaultRole = new User.Role();
            defaultRole.setId(1);  // O el id que corresponda
            defaultRole.setName("USER");
            user.setRoles(Collections.singletonList(defaultRole));
        }

        // Mapear la info del usuario si viene
        if (userInfoDTO != null) {
            user.setUserInfo(toUserInfo(userInfoDTO));
        }

        return user;
    }

    // Método auxiliar para mapear UserInfoDTO a UserInfo
    public static User.UserInfo toUserInfo(UserInfoDTO userInfoDTO) {
        User.UserInfo userInfo = new User.UserInfo();
        userInfo.setType(userInfoDTO.getType());
        userInfo.setDocumentType(userInfoDTO.getDocumentType());
        userInfo.setDocumentNumber(userInfoDTO.getDocumentNumber());
        userInfo.setDocumentExp(userInfoDTO.getDocumentExp());
        userInfo.setExpCountry(userInfoDTO.getExpCountry());
        userInfo.setExpRegion(userInfoDTO.getExpRegion());
        userInfo.setExpCity(userInfoDTO.getExpCity());
        userInfo.setFirstName(userInfoDTO.getFirstName());
        userInfo.setMiddleName(userInfoDTO.getMiddleName());
        userInfo.setLastName(userInfoDTO.getLastName());
        userInfo.setSecondLastName(userInfoDTO.getSecondLastName());
        userInfo.setOtherNames(userInfoDTO.getOtherNames());
        userInfo.setLegalName(userInfoDTO.getLegalName());
        userInfo.setEmail(userInfoDTO.getEmail());
        userInfo.setCountry(userInfoDTO.getCountry());
        userInfo.setRegion(userInfoDTO.getRegion());
        userInfo.setCity(userInfoDTO.getCity());
        userInfo.setAddress(userInfoDTO.getAddress());
        userInfo.setAddressDetail(userInfoDTO.getAddressDetail());
        userInfo.setPostalCode(userInfoDTO.getPostalCode());
        userInfo.setPhone(userInfoDTO.getPhone());
        userInfo.setPhone2(userInfoDTO.getPhone2());
        return userInfo;
    }

    public static void updateUserFromDTO(User user, UserDTO userDTO, UserInfoDTO userInfoDTO, String encodedPassword) {
        if (userDTO.getEmail() != null) user.setEmail(userDTO.getEmail());
        if (encodedPassword != null && !encodedPassword.isEmpty()) user.setPassword(encodedPassword);

        // Actualiza roles si vienen
        if (userDTO.getRoles() != null && !userDTO.getRoles().isEmpty()) {
            List<User.Role> roles = userDTO.getRoles().stream()
                .map(roleDTO -> {
                    User.Role role = new User.Role();
                    role.setId(roleDTO.getId());
                    role.setName(roleDTO.getName());
                    return role;
                })
                .collect(Collectors.toList());
            user.setRoles(roles);
        }

        // Actualiza info de usuario si viene
        if (userInfoDTO != null) {
            user.setUserInfo(toUserInfo(userInfoDTO));
        }
    }
}