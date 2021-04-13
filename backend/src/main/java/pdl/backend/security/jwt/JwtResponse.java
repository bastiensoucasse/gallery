package pdl.backend.security.jwt;

import java.util.List;


public class JwtResponse {

    private String jwt;
    private Integer id;
    private String username;
    private String email;
    private List<String> roles;

    public JwtResponse(String jwt, Integer id, String username, String email, List<String> roles){
        this.jwt = jwt;
        this.id = id;
        this.username = username;
        this.email = email;
        this.roles = roles;
    }

    public String getEmail() {
        return email;
    }

    public Integer getId() {
        return id;
    }

    public String getJwt() {
        return jwt;
    }

    public List<String> getRoles() {
        return roles;
    }

    public String getUsername() {
        return username;
    }


    
}
