package daggerok.data;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

@Data
@Document
@NoArgsConstructor
@Accessors(chain = true)
public class User implements Serializable, UserDetails {

  private static final long serialVersionUID = -4157105577394453113L;

  public static final List<GrantedAuthority> USER = AuthorityUtils.createAuthorityList("USER");
  public static final List<GrantedAuthority> ADMIN = AuthorityUtils.createAuthorityList("USER", "ADMIN");

  @Id
  String username;
  String password, name;
  boolean enabled = false;
  Collection<? extends GrantedAuthority> authorities = USER;

  @Override
  public boolean isAccountNonExpired() {
    return enabled;
  }

  @Override
  public boolean isAccountNonLocked() {
    return enabled;
  }

  @Override
  public boolean isCredentialsNonExpired() {
    return enabled;
  }
}
