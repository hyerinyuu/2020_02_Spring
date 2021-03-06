package com.biz.sec.service.auth;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.biz.sec.domain.AuthorityVO;
import com.biz.sec.domain.UserDetailsVO;
import com.biz.sec.persistance.AuthoritiesDao;
import com.biz.sec.persistance.UserDao;

import lombok.RequiredArgsConstructor;

/*
 * 사용자의 상세정보를 DB로부터 가져와서 
 * spring security 여러곳에서 사용할 수 있도록 VO에 연동하는 method
 * 
 * UserDetailService 인터페이스를 가져와서 Customizing 한다.
 * 
 * 만약 @Require...을 사용하지 않으려면
 * 클래스 생성자를 만들고 생성자에 @Autowired를 붙여서 초기화를 해주어야한다.
 */
@RequiredArgsConstructor
@Service("userDetailsService") // Dao와 implements한게 같아서 뒤에 정확히 명시해줘야함(안하면 오류)
public class UserDetailServiceImpl  implements UserDetailsService{

	/*
	 * 외부에서 주입받을 객체, 변수 선언
	 * 보통은 객체나 변수에 @Autowired를 사용하는데,
	 * 
	 * private final로 객체 변수를 선언하고,
	 * @RequiredArgsConstructor를 붙여서 변수를 생성(초기화) 하도록 한다.
	 */
	private final AuthoritiesDao authDao;
	private final UserDao userDao;
	
	
	/*
	 * DB로부터 데이터를 불러와서 UserDetailsVO에 데이터를 복사하여 연동하는 코드가 작성될 곳
	 */
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

		// DB로부터 사용자 정보 가져오기
		// UserVO userVO = userDao.findByUserName(username);
		
		// Spring security가 사용할 DetailVO 선언
		// 2020-04-14 직접 DB로부터 정보를 가져올 수 있도록 update
		UserDetailsVO userVO = userDao.findByUserName(username);
		if(userVO == null) {
			throw new UsernameNotFoundException("user name이 없습니다.");
		}
		
//		userVO.setUsername(userVO.getUsername());
//		userVO.setPassword(userVO.getPassword());
//		userVO.setEnabled(true);
		
		// 사용자 정보를 사용할 수 있는가 아닌가를 세밀하게 제어하기 위한 칼럼
		userVO.setAccountNonExpired(true);
		userVO.setAccountNonLocked(true);
		userVO.setCredentialsNonExpired(true);
		
		userVO.setAuthorities(this.getAuthorities(username));
		
//		userVO.setPhone("010-1111-1234");
//		userVO.setEmail("hyerin.you@gmail.com");
//		userVO.setAddress("광주광역시 북구");
		
		
		return userVO;
	}
	
	/**
	 * authorities 테이블에서 권한 리스트를 가져오기
	 */
	// 별도로 권한을 가져올 수 있는 method 생성
	private Collection<GrantedAuthority> getAuthorities(String username){
		
		List<AuthorityVO> authList = authDao.findByUserName(username);
		
		List<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();
		
		for(AuthorityVO vo : authList) {
			SimpleGrantedAuthority sAuth = new SimpleGrantedAuthority(vo.getAuthority());
			authorities.add(sAuth);
		}
		return authorities;
	}

}
