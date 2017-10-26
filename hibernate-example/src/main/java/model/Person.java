package model;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="person")
public class Person extends Model{

	@Id
	@GeneratedValue
	private Integer person_id;
	
	@Column(name="first_name")
	private String first_name;
	
	Person(){}
	public Person(String name){this.first_name = name;}
	
	@Override
	public int getId() {
		return person_id;
	}
	
	public String getName(){return first_name;};
	public void setName(String name){first_name = name;}
	
}
