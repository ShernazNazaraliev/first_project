package com.example.AutoShop.Entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;


import javax.persistence.*;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

@Entity
@Table(name = "User")
public class User implements UserDetails {

    @Id
    @SequenceGenerator(name = "User_seq", sequenceName = "User_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "User_seq")
    @Column(name = "user_ID")
    private Long id;

    private String userName;
    private String password;
    interface Cylinder{
        void setRadius(double radius);
    }

    class SolidOfRev implements Cylinder{
        private double radius;
        @Override
        void setRadius(double radius){
            this.radius=radius;
        }
    }

    @ManyToMany (fetch = FetchType.EAGER)
    @JoinTable(
            name = "user_roles",
            joinColumns = {@JoinColumn(name = "user_ID")},
            inverseJoinColumns = {@JoinColumn(name = "role_ID")}
    )
    @Column(name = "roles")
    private Set<Role> roles= new HashSet<>();

    public User(String userName,String password) {
        this.userName = userName;
        this.password = password;
    }

    public void addRoles(Role role) {
        this.roles.add(role);
    }
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return getRoles();
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return userName;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
