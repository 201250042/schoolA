package com.example.a.utils;

import io.jsonwebtoken.*;

import java.util.Date;
import java.util.HashMap;

public class TokenUtil {
    public static String getToken(String content) {
        JwtBuilder jwtBuilder = Jwts.builder();
        HashMap<String, Object> map = new HashMap<>();
        map.put("content", content);
        String token = jwtBuilder.setClaims(map)
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 30))
                .signWith(SignatureAlgorithm.HS256, "SDFGjhdsfalshdfHFdsjkdsfds121232131afasdfac").compact();
        return token;
    }

    public static String parseToken(String token) {
        JwtParser jwtParser = Jwts.parser();
        jwtParser.setSigningKey("SDFGjhdsfalshdfHFdsjkdsfds121232131afasdfac");
        Jws<Claims> claimsJws = jwtParser.parseClaimsJws(token);
        Claims body = claimsJws.getBody();
        String content = body.get("content", String.class);
        System.out.println(content);
        return content;
    }
}
