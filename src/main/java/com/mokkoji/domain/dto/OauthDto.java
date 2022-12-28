package com.mokkoji.domain.dto;

import com.mokkoji.domain.entity.member.MemberEntity;
import lombok.*;

import java.util.Map;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@Builder
public class OauthDto {

    private String memail;                   // 아이디 [이메일]
    private Map<String , Object> attributes; // 인증결과
    private String oauth2UserInfo;           // 회원정보 키
    private String registrationId;           // auth 회사명

    // 클라이언트 auth에 따른 객체 생성
    public static OauthDto of( String registrationId , // 1. 회사명
                               String oauth2UserInfo , // 2. 회원정보
                               Map<String , Object> attributes ){ // 3. 인증된 토큰

        if( registrationId.equals("kakao") ){ // 아직은 카카오 뿐이니..
            return ofkakao( registrationId , oauth2UserInfo , attributes );
        }else{ return null; }
    }

    // 1. 카카오 객체 생성 메소드
    public static OauthDto ofkakao( String registrationId , String oauth2UserInfo ,  Map<String , Object> attributes ){
        // 1. 회원정보 호출
        Map< String , Object > kakao_account = (Map<String, Object>) attributes.get(oauth2UserInfo); // kakao_account -> email , profile

        return OauthDto.builder()
                .memail((String) kakao_account.get("email"))
                .oauth2UserInfo( oauth2UserInfo )
                .registrationId( registrationId )
                .attributes( attributes )
                .build();
    }

    // 4. Dto -> ToEntity 이걸 하는 이유 : Dto는 DB에 못들어가기 때문에 엔티티화해서 그걸 디비에 넣어준다~
    public MemberEntity toEntity(){
        return MemberEntity
                .builder()
                .memail( this.memail )          // 회원 식별을 위한 이메일
                .mdomain( this.registrationId ) // 어떤 서비스로 로그인 했는지
                .build();
    }
}