package com.likelion.market.jwt;

import com.likelion.market.dto.ResponseDto;
import lombok.Data;

@Data
public class JwtTokenDto extends ResponseDto {
    private String token;
}
