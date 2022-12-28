package com.mokkoji.service.member;

import com.mokkoji.domain.dto.MemberDto;
import com.mokkoji.domain.dto.OauthDto;
import com.mokkoji.domain.entity.member.MemberEntity;
import com.mokkoji.domain.entity.member.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;


import javax.servlet.http.HttpServletRequest;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Service
public class MemberService  implements OAuth2UserService<OAuth2UserRequest , OAuth2User>  {

    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private HttpServletRequest request;
    // 회원 정보 가져오기 - 이름 mno -> memail 바꿈
    public String getloginMemail() {
        // 20221215 지웅 - 로그인 정보 제대로 리턴되지 않아 일부 수정

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Object principal = authentication.getPrincipal();

        if(principal.equals("anonymousUser")){ return ""; }
        MemberDto memberDto = (MemberDto) principal;
        return memberDto.getMemail();
    }

    // 유정 12/14 memail로 mno 찾기
    public int getmno(String memail){
        return memberRepository.findByMemail(memail).get().getMno();    // 만들어둔 findByMemail 을 용해 mno를 가져옴
    }

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        MemberDto memberDto = new MemberDto();
        // 1. 인증[ 로그인 ] 결과 정보 요청
        OAuth2UserService oAuth2UserService = new DefaultOAuth2UserService();
        OAuth2User oAuth2User = oAuth2UserService.loadUser(userRequest);
        // System.out.println("2. "+oAuth2User.toString() );
        // 2. oauth2 클라이언트 식별 지금은 카카오뿐이지만 나중엔 모르니까~
        String registrationId = userRequest.getClientRegistration().getRegistrationId();
        // System.out.println("3. oauth2 회사명 : "+registrationId);
        // 3. 회원정보 담는 객체 [ JSON 형태 ]
        String oauth2UserInfo = userRequest // 리퀘스트바디 같은.. 역할..
                .getClientRegistration()
                .getProviderDetails()
                .getUserInfoEndpoint()
                .getUserNameAttributeName();
        // System.out.println("회원정보 : "+oauth2UserInfo);
        // System.out.println("인증 결과"+oAuth2User.getAttributes());

        // * DB 처리
        OauthDto oauthDto = OauthDto.of( registrationId , oauth2UserInfo , oAuth2User.getAttributes() );

        Optional<MemberEntity> optional = memberRepository.findByMemail( oauthDto.getMemail() );
        MemberEntity memberEntity = null;
        if( !optional.isPresent() ){ // 가입한 적이 없으면? 저장시켜버림
            memberEntity = memberRepository.save( oauthDto.toEntity() );
        }else{
            memberEntity = optional.get();
        }

        // 권한 부여
        Set<GrantedAuthority> authorities = new HashSet<>();
        authorities.add( new SimpleGrantedAuthority( memberEntity.getMdomain() ) );
        // 5. 반환

        memberDto.setMemail( oauthDto.getMemail() );
        memberDto.setAuthorities( authorities ) ;
        memberDto.setAttributes( oAuth2User.getAttributes() );
        // System.out.println(memberDto.toString());
        return memberDto;
    }
}
