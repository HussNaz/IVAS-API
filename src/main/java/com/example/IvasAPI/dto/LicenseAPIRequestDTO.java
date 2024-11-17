package com.example.IvasAPI.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class LicenseAPIRequestDTO {
    private String binNumber;
    private String nid;
}
