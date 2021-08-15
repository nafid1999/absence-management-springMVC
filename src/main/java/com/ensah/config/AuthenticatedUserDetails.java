package com.ensah.config;
import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.ensah.core.bo.Compte;

public class AuthenticatedUserDetails implements UserDetails {
	
	 public Compte getAccount() {
		return account;
	}

	public void setAccount(Compte account) {
		this.account = account;
	}

	private Compte account;
     
	    public AuthenticatedUserDetails(Compte account) {
	        this.account = account;
	    }

		@Override
		public Collection<? extends GrantedAuthority> getAuthorities() {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public String getPassword() {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public String getUsername() {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public boolean isAccountNonExpired() {
			// TODO Auto-generated method stub
			return false;
		}

		@Override
		public boolean isAccountNonLocked() {
			// TODO Auto-generated method stub
			return false;
		}

		@Override
		public boolean isCredentialsNonExpired() {
			// TODO Auto-generated method stub
			return false;
		}

		@Override
		public boolean isEnabled() {
			// TODO Auto-generated method stub
			return false;
		}

}
