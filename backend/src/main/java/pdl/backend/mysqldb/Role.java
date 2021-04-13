package pdl.backend.mysqldb;

import javax.persistence.*;


@Entity
@Table(name = "roles")
public class Role {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@Enumerated(EnumType.STRING)
	@Column(name = "name",length = 20, unique = true)
	private EnumRoles name;

	public Role() {

	}

	public Role(EnumRoles name) {
		this.name = name;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public EnumRoles getName() {
		return name;
	}

	public void setName(EnumRoles name) {
		this.name = name;
	}
}
