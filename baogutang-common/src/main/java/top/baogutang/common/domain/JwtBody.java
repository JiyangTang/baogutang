package top.baogutang.common.domain;

import io.jsonwebtoken.Claims;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * @description:
 * @author: nikooh
 * @date: 2023/06/15 : 11:47
 */
public class JwtBody {

    private String subject;

    private Long id;

    /**
     * 签发时间
     */
    private Long iat;

    public JwtBody(Claims claims) {
        if (Objects.nonNull(claims)) {
            this.subject = claims.getSubject();
            Map map = new HashMap<>(claims);
            this.iat = Long.valueOf(String.valueOf(map.get("iat")));
            this.id = Long.valueOf(String.valueOf(map.get("id")));
        }
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getIat() {
        return iat;
    }

    public void setIat(Long iat) {
        this.iat = iat;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    @Override
    public String toString() {
        return "JwtBody{" +
                "subject='" + subject + '\'' +
                ", id=" + id +
                ", iat=" + iat +
                '}';
    }
}
