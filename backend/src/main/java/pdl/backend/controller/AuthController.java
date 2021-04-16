package pdl.backend.controller;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.jsonwebtoken.Jwt;

import javax.validation.Valid;

import pdl.backend.controller.requests.LoginRequest;
import pdl.backend.controller.requests.SignupRequest;
import pdl.backend.controller.responses.MessageResponse;
import pdl.backend.mysqldb.EnumRoles;
import pdl.backend.mysqldb.Image;
import pdl.backend.mysqldb.Roles;
import pdl.backend.mysqldb.User;
import pdl.backend.mysqldb.UserRepository;
import pdl.backend.mysqldb.RoleRepository;
import pdl.backend.mysqldb.ImageRepository;
import pdl.backend.security.jwt.JwtResponse;
import pdl.backend.security.jwt.JwtUtils;
import pdl.backend.security.services.CustomUserDetails;



@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/auth")
public class AuthController {
	@Autowired
	AuthenticationManager authenticationManager;

	@Autowired
	UserRepository userRepository;

	@Autowired
	RoleRepository roleRepository;

	@Autowired
	PasswordEncoder encoder;

	@Autowired
	JwtUtils jwtUtils;

	@Autowired
	ImageRepository imageRepository;

	@PostMapping("/signin")
	public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {

		try{
		Authentication authentication = authenticationManager.authenticate(
				new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

		SecurityContextHolder.getContext().setAuthentication(authentication);
		String jwt = jwtUtils.generateJwtToken(authentication);

		CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
		List<String> roles = userDetails.getAuthorities().stream()
				.map(item -> item.getAuthority())
				.collect(Collectors.toList());


		return ResponseEntity.ok(new JwtResponse(jwt, userDetails.getId(), userDetails.getUsername(), userDetails.getEmail(), roles));
		}catch(DisabledException e){
			return ResponseEntity.badRequest().body(new MessageResponse("Account has been disabled"));
		}catch(LockedException e){
			return ResponseEntity.badRequest().body(new MessageResponse("Account has been locked"));
		}catch(BadCredentialsException e){
			return ResponseEntity.badRequest().body(new MessageResponse("Wrong username or password"));
		}

	}

	@PostMapping("/signup")
	public ResponseEntity<?> registerUser(@Valid @RequestBody SignupRequest signUpRequest) {
		if (userRepository.existsByUsername(signUpRequest.getUsername())) {
			return ResponseEntity
					.badRequest()
					.body(new MessageResponse("Error: Username is already taken!"));
		}

		if (userRepository.existsByEmail(signUpRequest.getEmail())) {
			return ResponseEntity
					.badRequest()
					.body(new MessageResponse("Error: Email is already in use!"));
		}

		// Create new user's account
		User user = new User(signUpRequest.getFirstName(),
							 signUpRequest.getLastName(),
							 signUpRequest.getEmail(),
							 signUpRequest.getUsername(),
							 encoder.encode(signUpRequest.getPassword()));

		Set<String> strRoles = signUpRequest.getRole();
		Set<Roles> roles = new HashSet<>();



		if (strRoles == null) {
			Roles userRole = roleRepository.findByName(EnumRoles.ROLE_USER.toString())
					.orElseThrow(() -> new RuntimeException("Error: Role is not found."));
			roles.add(userRole);
		} else {
			strRoles.forEach(role -> {
				switch (role) {
				case "root":
					
					Roles adminRole = roleRepository.findByName(EnumRoles.ROLE_ROOT.toString())
							.orElseThrow(() -> new RuntimeException("Error: Role is not found."));
					roles.add(adminRole);

					break;
				case "premium":
					Roles modRole = roleRepository.findByName(EnumRoles.ROLE_USER_PREMIUM.toString())
							.orElseThrow(() -> new RuntimeException("Error: Role is not found."));
					roles.add(modRole);

					break;
				default:
					Roles userRole = roleRepository.findByName(EnumRoles.ROLE_USER.toString())
							.orElseThrow(() -> new RuntimeException("Error: Role is not found."));
					roles.add(userRole);
				}
			});
		}

		user.setRoles(roles);
		userRepository.save(user);

		List<Image> givenImages = imageRepository.findAllPublic();

		for(Image img : givenImages){
			imageRepository.save(new Image(img.getName(), img.getData(), img.getType(), img.getSize(), user, false));
		}

		return ResponseEntity.ok(new MessageResponse("User registered successfully!"));
	}
}