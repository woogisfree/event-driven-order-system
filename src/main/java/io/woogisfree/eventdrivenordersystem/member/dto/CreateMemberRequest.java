package io.woogisfree.eventdrivenordersystem.member.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CreateMemberRequest {
    @NotBlank
    private String name;
    @NotBlank
    private String address;
}
