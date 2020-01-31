package daggerok.data;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.springframework.data.cassandra.core.cql.PrimaryKeyType;
import org.springframework.data.cassandra.core.mapping.PrimaryKeyColumn;
import org.springframework.data.cassandra.core.mapping.Table;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

@Data
@Table
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class User implements Serializable, UserDetails {

  private static final long serialVersionUID = -4157105577394453113L;

  public static final List<GrantedAuthority> USER = AuthorityUtils.createAuthorityList("USER");
  public static final List<GrantedAuthority> ADMIN = AuthorityUtils.createAuthorityList("USER", "ADMIN");

  @PrimaryKeyColumn(type = PrimaryKeyType.PARTITIONED, ordinal = 1)
  String username;
  @PrimaryKeyColumn(type = PrimaryKeyType.CLUSTERED, ordinal = 2)
  String password;
  String name;
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
