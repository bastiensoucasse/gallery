package pdl.backend.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/test")
public class TestController {
    
    @GetMapping("/all")
    public String allAcess(){
        return "Public Content";
    }

    @GetMapping("/user")
    @PreAuthorize("hasRole('ROLE_USER') or hasRole('ROLE_USER_PREMIUM') or hasRole('ROOT')")
    public String userAcess(){
        return "User Content";
    }

    @GetMapping("/premium")
    @PreAuthorize("hasRole('ROLE_USER_PREMIUM') or hasRole('ROOT')")
    public String premiumAcess(){
        return "Premium Content";
    }

    @GetMapping("/root")
    @PreAuthorize("hasRole('ROLE_ROOT')")
    public String rootAccess(){
        return "Root Content";
    }

}
