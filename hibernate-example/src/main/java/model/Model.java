package model;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.xml.bind.annotation.XmlTransient;

@XmlTransient
public abstract class Model {

	@Id
	@GeneratedValue
	private int id;
	
	public abstract int getId();
}
