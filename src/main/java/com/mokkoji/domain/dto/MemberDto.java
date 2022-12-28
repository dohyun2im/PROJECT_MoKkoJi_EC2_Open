package com.mokkoji.domain.dto;

import com.mokkoji.domain.entity.member.MemberEntity;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.user.OAuth2User;

import javax.persistence.Column;
import java.util.Collection;
import java.util.Map;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class MemberDto implements OAuth2User {
    private int mno;
    private String memail;
    private String mdomain;
    private Set<GrantedAuthority> authorities; // 인증권한 토큰
    @Getter
    private Map<String , Object> attributes; // pauth2 인증 결과

    public void setAuthorities(Set<GrantedAuthority> authorities) {
        this.authorities = authorities;
    }

    public MemberEntity toEntity(){
        return MemberEntity
                .builder()
                .mno(this.mno)
                .memail(this.memail)
                .mdomain(this.mdomain)
                .build();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.authorities;
    }

    /* --------------OAuth2User----------------- */
    @Override
    public String getName() {
        return this.memail;
    }
}
