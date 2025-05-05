package com.shad.edulink.mapper;

import com.shad.edulink.dto.UserRegisterRequest;
import com.shad.edulink.dto.UserDetailsResponse;
import com.shad.edulink.entity.UserEntity;
import com.shad.edulink.entity.TeacherEntity;
import com.shad.edulink.entity.StudentEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface Mappers {
    Mappers INSTANCE = org.mapstruct.factory.Mappers.getMapper(Mappers.class);

    @Mapping(target = "id", ignore = true) // Ignore ID during creation
    UserEntity userRequestDtoToUserEntity(UserRegisterRequest userRequestDTO);

    // Map UserRegisterRequest to TeacherEntity
    @Mapping(target = "id", ignore = true)
    TeacherEntity userRequestDtoToTeacherEntity(UserRegisterRequest userRequestDTO);

    // Map UserRegisterRequest to StudentEntity
    @Mapping(target = "id", ignore = true)
    StudentEntity userRequestDtoToStudentEntity(UserRegisterRequest userRequestDTO);

    // Map TeacherEntity to UserDetailsResponse
    UserDetailsResponse teacherEntityToUserResponseDto(TeacherEntity teacher);

    // Map StudentEntity to UserDetailsResponse
    UserDetailsResponse studentEntityToUserResponseDto(StudentEntity student);

    // Map UserEntity to UserDetailsResponse
    UserDetailsResponse userEntityToUserResponseDto(UserEntity user);
}
