package com.paas.sms.tenantservice.document;

import org.springframework.security.core.GrantedAuthority;


//TODO: Auto-generated Javadoc
/**
* The Enum Authority.
*/
public enum Authority implements GrantedAuthority {
 
 /** The role user. */
 ROLE_USER,ROLE_CLIENT_MASTER,ROLE_SITE_ADMIN,ROLE_SITE_USER,
 ORG_ADMIN,ORG_TENANT,
 ROLE_ADMIN;

@Override
public String getAuthority() {
	// TODO Auto-generated method stub
	return this.name();
}
// ANONYMOUS;

 /**
  * Gets the authority.
  *
  * @return the authority
  */
	/*
	 * @Override public String getAuthority() { return this.name(); }
	 */
}

